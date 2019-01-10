package portfmgr.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Calculates profit and loss values and the total portfolio value
 * 
 * @param portfolio (whole JSON online data file, cryptocurrencyList and the currencyList)
 * @author Marc Steiner
 *
 */
public class PortfolioCalculator {
	private JSONObject onlineDataJSON;
	private Portfolio portfolio;
	private List<String> cryptocurrencyList;
	private List<String> currencyList;
	private String coinlistPath;
	private static String BaseLinkUrl = "https://www.cryptocompare.com";
	private double profitOrLoss;
	private double profitOrLossPercentage;
	private double totalPortfolioValue;
	
	
	@Autowired
	TransactionRepository transRepo;
	
	public PortfolioCalculator(Portfolio portfolio, JSONObject onlineDataJSON, List<String> cryptocurrencyList, List<String> currencyList, String coinlistPath) {
		this.portfolio = portfolio;
		this.coinlistPath = coinlistPath;
		this.onlineDataJSON = onlineDataJSON;
		this.cryptocurrencyList = cryptocurrencyList;
		this.currencyList = currencyList;
	}
	
	/**
	 * Extract JSON data from JSON Object and calculate the new portfolio value
	 * Display everything in the insights
	 * 
	 * TO DO: basierend auf dem Portfolio alles berechnen
	 * 
	 */
	public void calculatePortfolio() {
		
		try {
			for (String symbol: cryptocurrencyList) {
				getImageOfCryptoCurrency(symbol);
				
				/*
				 * Extract the underlying JSONObject from the main JSONObject
				 * and get the values (CHF, USD, EUR) for the specific symbol (e.g BTC).
				 */
				JSONObject values = onlineDataJSON.getJSONObject(symbol);
				System.out.println("Die Werte für " + symbol);
				
				//nur für CHF:
				//double result = values.getDouble("CHF");
				
				// print for each currency (CHF, USD, EUR) the specific value 
				for (String currency: currencyList) {
					double result = values.getDouble(currency);
					System.out.print("Währung " + currency + " = " + result);
					System.out.println("");
				}
				System.out.println("");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
