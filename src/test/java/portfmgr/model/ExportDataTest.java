package portfmgr.model;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Test class for ExportData
 * @author Marc Steiner
 *
 */
public class ExportDataTest {
	private ObservableList<Insight> insights = FXCollections.observableArrayList();
	
	
	@Before
	public void createListInsights() {

		Insight insightObject = new Insight();
		insightObject.setCryptoCurrency("BTC");
		insightObject.setSpotPrice(4500.0);
		insightObject.setNumberOfCoins(1.0);
		insightObject.setTotal(3000.0);
		insightObject.setAveragePrice(3000.0);
		insightObject.setChangePercent(50.0);
		insightObject.setChangeFiat(1500.0);

		this.insights.add(insightObject);
	}
	

	/**
	 * Test if extracting of data is working propperly.
	 * 
	 * Only these parameter will be extracted:
	 * 		getCryptoCurrency
	 * 		getNumberOfCoins
	 * 		getSpotPrice
	 * 		getTotal
	 * @throws IOException
	 */
	@Test
	public void extractDataTest() throws IOException {
		ExportData exportData = new ExportData(null, null);
		Map<String, Object[]> extractedData = new TreeMap<String, Object[]>();
		
		extractedData = exportData.extractData(this.insights);
		
		Set<String> keyid = extractedData.keySet();
		List<String> dataList = new ArrayList<String>();
        
        for (String key : keyid) {
        	Object[] objectArr = extractedData.get(key);
        	
        	for (Object obj : objectArr) {
        		dataList.add((String)obj);
            }  
        }
        
        assertEquals(insights.get(0).getCryptoCurrency(), dataList.get(0));
        assertEquals(insights.get(0).getNumberOfCoins().toString(), dataList.get(1));
        assertEquals(insights.get(0).getSpotPrice().toString(), dataList.get(2));
        assertEquals(insights.get(0).getTotal().toString(), dataList.get(3));
               
	}
}

