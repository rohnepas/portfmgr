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
	
	private String APIKey = "PASTE YOUR API KEY HERE";
	private String cryptoCurrencies;
	private String fiatCurrencies;

	/**
	 * Calls the online API and get the actual data
	 * 
	 * @param fiatCurrencyList 
	 * @param cryptoCurrencyList 
	 * @return obj (JSON Object
	 * @throws IOException
	 */
	public JSONObject getOnlineCourseData(List<String> cryptoCurrencyList, List<String> fiatCurrencyList) throws IOException {
		
		cryptoCurrencies = String.join(",", cryptoCurrencyList);
		fiatCurrencies = String.join(",", fiatCurrencyList);
		
		URL url = new URL("https://min-api.cryptocompare.com/data/pricemulti?fsyms=" + cryptoCurrencies + "&tsyms=" + fiatCurrencies +"&api_key="+ this.APIKey);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);

		int responseCode = con.getResponseCode();
		
		if(responseCode != 200) {
			throw new RuntimeException("Problem with HttpResponseCode: " + responseCode);
		}
		
		else
		{
			
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
								
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
}
