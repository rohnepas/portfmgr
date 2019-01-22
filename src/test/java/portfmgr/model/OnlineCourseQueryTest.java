package portfmgr.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the query of the online API
 * @author Marc Steiner
 *
 */
public class OnlineCourseQueryTest {
	OnlineCourseQuery onlineCourseQuery;
	List<String> cryptoCurrencyList;
	List<String> fiatCurrencyList;
	
	/**
	 * Creates an instance of the OnlineCourseQuery and stores it in an instance 
	 * variable to be able to use this instance for all tests.
	 * 
	 */
	@Before
	public void createOnlineCourseQueryInstance() {
		OnlineCourseQuery onlineCourseQuery = new OnlineCourseQuery();
		this.onlineCourseQuery = onlineCourseQuery;
	}
	
	@Before
	public void setupList() {
		this.cryptoCurrencyList = Arrays.asList("BTC");
		this.fiatCurrencyList = Arrays.asList("CHF");
	}
	

	/**
	 * First it checks if the returned value of the method getOnlineCourseQuery is not null.
	 * Second it checks if the returned key of the JSON object is like expected.
	 * @throws IOException
	 */
	@Test
	public void getOnlineCourseDataTest() throws IOException {
		JSONObject obj = new JSONObject();
		obj = onlineCourseQuery.getOnlineCourseData(this.cryptoCurrencyList, this.fiatCurrencyList);
		assertNotNull(obj);

		for (String key : obj.keySet()) {
			String keyStr = (String)key;
			assertEquals(keyStr , this.cryptoCurrencyList.get(0));
		}	
	}

}
