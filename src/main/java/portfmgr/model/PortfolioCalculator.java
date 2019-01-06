package portfmgr.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Calcualtes the whole portfolio with all the insights
 * @author Marc Steiner
 */
public class PortfolioCalculator {
	private JSONObject onlineDataJSON;
	private List<String> cryptocurrencyList;
	private List<String> currencyList;
	
	public PortfolioCalculator(JSONObject onlineDataJSON, List<String> cryptocurrencyList, List<String> currencyList) {
		this.onlineDataJSON = onlineDataJSON;
		this.cryptocurrencyList = cryptocurrencyList;
		this.currencyList = currencyList;
	}

	/*
	 * Extract JSON data from JSON Object and calculate the new portfolio value
	 */
	public void calculatePortfolio() {
		
		try {
			for (String symbol: cryptocurrencyList) {
				JSONObject values = onlineDataJSON.getJSONObject(symbol);
				System.out.println("Die Werte für " + symbol);
				
				
				//nur für CHF:
				//double result = values.getDouble("CHF");
				
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
	
}
