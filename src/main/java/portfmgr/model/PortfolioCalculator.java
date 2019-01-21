package portfmgr.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
	
	private Portfolio portfolio;
	private String profitOrLoss = "-";
	private String profitOrLossPercentage = "-";
	private String totalPortfolioValue = "-";
	private String totalSpent;
	private boolean profit;

	@Autowired
	TransactionRepository transRepo;


	/*
	 * Funcation not implemented in constructor because of Autowired constructor would not get the correct portfolio
	 */
	public void init(Portfolio portfolio) {
		this.portfolio = portfolio;
		profit = true;
	}

	/**
	 * calculate all the portfolio statistics
	 *
	 */
	public void calculatePortfolio(JSONObject onlineDataJSON) {
		
		Double tempTotalPortfolioValue = 0.0;
		Double tempProfitOrLoss = 0.0;
		Double tempTotalSpent = 0.0;
				
		List<Map<String, Double>> dataList = new ArrayList<Map<String, Double>>();
		
		Map<String,Double> mapTotalPortfolioValue = convertData(transRepo.sumAndGroupTotalNumberOfCoins(this.portfolio.getId()));
		Map<String,Double> mapProfitOrLoss = convertData(transRepo.sumAndGroupTotalSpent(this.portfolio.getId()));
		
		dataList.add(mapTotalPortfolioValue);
		dataList.add(mapProfitOrLoss);
		
		// Calculate for every data set
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
						}
					}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		setStatisticValues(tempTotalSpent, tempTotalPortfolioValue, tempProfitOrLoss);		
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

	/**
	 * Set the statistic values of the portfolio
	 * 
	 * @param tempTotalSpent
	 * @param tempTotalPortfolioValue
	 * @param tempProfitOrLoss
	 */
	public void setStatisticValues(Double tempTotalSpent, Double tempTotalPortfolioValue, Double tempProfitOrLoss) {
		NumberFormat nf = NumberFormat.getNumberInstance(new Locale("de","CH"));
		nf.setMaximumFractionDigits(2);
		DecimalFormat df = (DecimalFormat)nf;

		
		if (transRepo.sumTotalSpent(this.portfolio.getId()) != null) {
			tempTotalSpent = transRepo.sumTotalSpent(this.portfolio.getId());
		} 

		totalSpent = df.format(tempTotalSpent);
		totalPortfolioValue = df.format(tempTotalPortfolioValue);
		profitOrLoss = df.format(tempProfitOrLoss);
		
		if(tempTotalSpent > 0) {
			profitOrLossPercentage = df.format(100 * (tempProfitOrLoss / tempTotalSpent));
			
		} else profitOrLossPercentage = df.format(0);
		
		
		if (tempProfitOrLoss < 0) {
			profit = false;
		} 
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
}
