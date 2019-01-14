package portfmgr.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Calculates profit and loss values and the total portfolio value.
 * This class must be marked as @Service because of Spring Data and in the controller class,
 * it must be marked @Autowired. Documentation: https://www.moreofless.co.uk/spring-mvc-java-autowired-component-null-repository-service/
 *
 * @author Marc Steiner
 *
 */
@Service
public class PortfolioCalculator {
	private JSONObject onlineDataJSON;
	private Portfolio portfolio;
	private List<Transaction> transactionList;
	private String coinlistPath;
	private static String BaseLinkUrl = "https://www.cryptocompare.com";
	private String profitOrLoss = "-";
	private String profitOrLossPercentage = "-";
	private String totalPortfolioValue = "-";
	private String totalSpent = "0.00";
	private boolean profit;


	@Autowired
	TransactionRepository transRepo;

	public void init(Portfolio portfolio, JSONObject onlineDataJSON, String coinlistPath) {
		this.portfolio = portfolio;
		this.coinlistPath = coinlistPath;
		this.onlineDataJSON = onlineDataJSON;
		profit = true;
		setTransactionList();
	}

	/**
	 * Find all transactions whithin this portfolio
	 */
	public void setTransactionList() {
		transactionList = transRepo.findByPortfolio(portfolio);
	}

	/**
	 * Extract JSON data from JSON Object and calculate the portfolio statistics
	 *
	 * TO DO: basierend auf dem Portfolio alles berechnen
	 *
	 */
public void calculatePortfolio() {
		
		Double tempTotalPortfolioValue = 0.0;
		Double tempProfitOrLoss = 0.0;
		Double tempProfitOrLossPercentage = 0.0;
		DecimalFormat df = new DecimalFormat("####0.00");
		
		if (transRepo.sumTotalSpent() != null) {
			totalSpent = df.format(transRepo.sumTotalSpent());
		} 
		
		List<Map<String, Double>> dataList = new ArrayList<Map<String, Double>>();
		
		Map<String,Double> mapTotalPortfolioValue = convertData(transRepo.sumAndGroupTotalNumberOfCoins());
		Map<String,Double> mapProfitOrLoss = convertData(transRepo.sumAndGroupTotalSpent());
	
		dataList.add(mapTotalPortfolioValue);
		dataList.add(mapProfitOrLoss);
		
		for (Map<String, Double> map: dataList) {
			
			if (map != null) {
								
				try {
					
					// Loop over every element in the Map
					for (Map.Entry<String, Double> symbol: map.entrySet()) {
										
						/*
						 * Extract the underlying JSONObject from the main JSONObject
						 * and get the fiat values for the specific symbol (e.g BTC).
						 * Example: {"BTC":{"CHF": 1000, "USD: 1100, "EUR"; 800}}
						 */
						JSONObject values = onlineDataJSON.getJSONObject(symbol.getKey());
						
						/*
						 * Extract the value of the specific crypto currency (e.g BTC)
						 * in the specified portfolio currency (e.g CHF)
						 * Example: cryptoCurrencyPrice = 1000 (CHF for 1 BTC) 
						 */
						double cryptoCurrencyPrice = values.getDouble(portfolio.getPortfolioFiatCurrency());
						
						// symbol.getValue() = 
						if (map == mapTotalPortfolioValue) {
							tempTotalPortfolioValue = tempTotalPortfolioValue + cryptoCurrencyPrice * symbol.getValue();
						}
						
						// symbol.getValue() = total (price + fees) spent to buy this crypto currency
						if (map == mapProfitOrLoss) {
							Double totalNumberOfCoins = dataList.get(0).get(symbol.getKey());							
							tempProfitOrLoss = tempProfitOrLoss + (cryptoCurrencyPrice * totalNumberOfCoins - symbol.getValue());
							tempProfitOrLossPercentage = tempProfitOrLossPercentage + (((cryptoCurrencyPrice * totalNumberOfCoins) - symbol.getValue()) / symbol.getValue());
						}
						
					}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
		totalPortfolioValue = df.format(tempTotalPortfolioValue);
		profitOrLoss = df.format(tempProfitOrLoss);
		profitOrLossPercentage = df.format(100 * tempProfitOrLossPercentage);
		
		if (tempProfitOrLossPercentage < 0) {
			profit = false;
		} 
	}
	
	/**
	 * Convert a list into a map with key and value. Code in separate method 
	 * to avoid code duplication
	 * 
	 * @param list
	 * @return map of list
	 */
	public Map<String, Double> convertData(List<Object[]> list) {
		Map<String,Double> map = null;
		if(list != null && !list.isEmpty()) {
			map = new HashMap<String,Double>();
			for (Object[] object : list) {
				map.put(((String)object[1]),(Double)object[0]);
			}
			return map;
		}
		return null;
	}	

	public String getProfitOrLoss() {
		return profitOrLoss;
	}

	public String getProfitOrLossPercentage() {
		return profitOrLossPercentage;
	}

	public String getTotalPortfolioValue() {
		return totalPortfolioValue;
	}
	
	public String getTotalSpent() {
		return totalSpent;
	}
	
	public boolean getProfit() {
		return profit;
	}
	
	/**
	 * reads the file in coinlistPath and extracts the URL for the picture and name of the specific crypto currency
	 * "Data" is the key in the JSON file for the symbol of the currency.
	 * "ImageURL" is the value in the JSON file for the URL of the specific image
	 * "CoinName" is the value in the JSON file for the specific symbol
	 *
	 *@param symbol (crypto currency symbol (e.g. "BTC" for Bitcoin)
	 *@return String imageURL
	 *
	 */
	public String getImageOfCryptoCurrency(String symbol) {
		File file = new File(coinlistPath);
		String content;

		try {
			content = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
			JSONObject obj = new JSONObject(content);

			// stores all symbols with the values in a new JSON object
			JSONObject data = obj.getJSONObject("Data");

			String imageURL = BaseLinkUrl + data.getJSONObject(symbol).get("ImageUrl");

			System.out.println(imageURL);
			System.out.println("COIN NAME :" + data.getJSONObject(symbol).get("CoinName"));

			return imageURL;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
