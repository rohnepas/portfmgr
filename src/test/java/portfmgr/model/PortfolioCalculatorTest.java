package portfmgr.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test class for portfolio calculation.
 * @author Marc Steiner
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PortfolioCalculatorTest {
	PortfolioCalculator portCalculator;
	
	
	// Is used to insert a Portfolio in the database and reading it via the find by name API
	@Autowired
	private TestEntityManager entityManager;
		
	@Autowired
	private TransactionRepository transRepo;

	private JSONObject JSONdata;
	private Transaction transaction;
	private Portfolio portfolio;

	
	@Before
	public void init() {
		PortfolioCalculator portCalculator = new PortfolioCalculator();
		this.portCalculator = portCalculator;		
	}
	
	/**
	 * Creates a transaction and stores it into the database
	 * @author Marc Steiner
	 */
	@Before
	public void setupPortfolioAndTransaction() {
		
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioFiatCurrency("CHF");
		portfolio.setPortfolioName("Test");
		this.portfolio = portfolio;
		entityManager.persist(portfolio);
		entityManager.flush();
		
		portCalculator.init(this.portfolio);
		
		Transaction transaction = new Transaction();
		transaction.setFiatCurrency("CHF");
		transaction.setCryptoCurrency("BTC");
		transaction.setFees(0.0);
		transaction.setType("Kauf");
		transaction.setNumberOfCoins(1.0);
		transaction.setPrice(500.0);
		transaction.setTransactionDate(null);
		transaction.setPortfolio(portfolio);
		this.transaction = transaction;
		entityManager.persist(transaction);
		entityManager.flush();
	}

	/**
	 * Create a JSOBObject:  {"BTC":{"CHF": 1000, "USD: 1100, "EUR"; 800}}
	 * @author Marc Steiner
	 */
	@Before
	public void createJSONObject() {
		JSONObject jObject = new JSONObject();
		jObject.put("CHF", 1000);
		jObject.put("USD", 1100);
		jObject.put("EUR", 800);
		
		JSONObject jDataObject = new JSONObject();
		jDataObject.put("BTC", jObject);
		this.JSONdata = jDataObject;
	}
	
	/**
	 * Test if the convertion from List<Object> into Map works by checking
	 * one value (total spent) of the transaction
	 * @author Marc Steiner
	 * 
	 */
	@Test
	public void convertDataTest() {		
		List<Object[]> found = transRepo.sumAndGroupTotalSpent(this.portfolio.getId());
		Map<String,Double> tempMap = portCalculator.convertData(found);			
		
		//Map<k,v> result example: {BTC=1.0, LTC=10.0}
		assertEquals(this.transaction.getTotal(), tempMap.get("BTC"));
	}
	
}
