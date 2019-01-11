package portfmgr.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Calculates profit and loss values and the total portfolio value.
 * This class must be marked as @Service because of Spring Data and in the controller class, 
 * it must be marked @Autowired. Documentation: https://www.moreofless.co.uk/spring-mvc-java-autowired-component-null-repository-service/
 * 
 * @param portfolio (whole JSON online data file, cryptoCurrencyList and the fiatCurrencyList)
 * @author Marc Steiner
 *
 */
@Service
public class PortfolioCalculator {
	private JSONObject onlineDataJSON;
	private Portfolio portfolio;
	private List<String> cryptoCurrencyList;
	private List<String> fiatCurrencyList;
	private List<Transaction> transactionList;
	private String coinlistPath;
	private static String BaseLinkUrl = "https://www.cryptocompare.com";
	private double profitOrLoss;
	private double profitOrLossPercentage;
	private double totalPortfolioValue;
	
	
	@Autowired
	TransactionRepository transRepo;
	
	public void init(Portfolio portfolio, JSONObject onlineDataJSON, List<String> cryptoCurrencyList, List<String> fiatCurrencyList, String coinlistPath) {
		this.portfolio = portfolio;
		this.coinlistPath = coinlistPath;
		this.onlineDataJSON = onlineDataJSON;
		this.cryptoCurrencyList = cryptoCurrencyList;
		this.fiatCurrencyList = fiatCurrencyList;
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
		
		try {
			
			for (String symbol: cryptoCurrencyList) {
								
				/*
				 * Extract the underlying JSONObject from the main JSONObject
				 * and get the values (CHF, USD, EUR) for the specific symbol (e.g BTC).
				 */
				JSONObject values = onlineDataJSON.getJSONObject(symbol);
								
				/*
				 * Extract the value of the specific crypto currency (e.g BTC)
				 * in the specified portfolio currency (e.g CHF)
				 */
				double result = values.getDouble(portfolio.getPortfolioCurrency());
				totalPortfolioValue = totalPortfolioValue + result;
				
				System.out.println("'"+ symbol + "': Wert: " + result);
			}
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Calculate the total portfolio value based on data from database 
	 * 
	 */
	public void calculateTotalPortfolioValue() {
		
		// Get a list of total number of coins per crypto currency
		//List<?> list = transRepo.sumAndGroupTotalNumberOfCoins();
		totalPortfolioValue = 0;
		
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
	
	public Double getProfitOrLoss() {
		return profitOrLoss;
	}
	
	public Double getProfitOrLossPercentage() {
		return profitOrLossPercentage;
	}
	
	public Double getTotalPortfolioValue() {
		return totalPortfolioValue;
	}

}
