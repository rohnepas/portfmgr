package portfmgr.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class calls the API for online course data from https://min-api.cryptocompare.com.
 * It requests only the actual price from different symbols. The  API Key is a free API Key based on the account from Marc Steiner
 * 
 * @author Marc Steiner
 */

public class OnlineCourseQuery {
	
	private String APIKey = "3b46b9503250d561de3cfa120910d34bada6f4d0587b7e3db6cf15e02a509313";
	private String symbols;
	private String currencies;
	private List<String> cryptocurrencyList;
	private List<String> currencyList;
	
	
	public OnlineCourseQuery(List<String> cryptocurrencyList, List<String> currencyList) {
		this.cryptocurrencyList = cryptocurrencyList;
		this.currencyList = currencyList;
		setSymbols(cryptocurrencyList);
		setCurrencies (currencyList);
	}

	public JSONObject getOnlineCourseData() throws IOException {
		
		URL url = new URL("https://min-api.cryptocompare.com/data/pricemulti?fsyms=" + symbols + "&tsyms=" + currencies +"&api_key="+ APIKey);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		
		if(responseCode != 200) {
			throw new RuntimeException("Problem wirh HttpResponseCode: " + responseCode);
		}
		
		else
		{
			System.out.println("Response Code : " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer data = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				data.append(inputLine);
			}
			
			in.close();
			
			try {
				JSONObject obj = new JSONObject(data.toString());
				return obj;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public void setSymbols (List<String> listOfSymbols) {
		symbols = String.join(",", listOfSymbols);			
	}
	
	public void setCurrencies (List<String> listOfCurrencies) {
		currencies = String.join(",", listOfCurrencies);			
	}
	
}
