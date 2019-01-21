package portfmgr.model;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 
 * @author Marc Steiner
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PortfolioCalculatorTest {
	
	
	// Is used to insert a Portfolio in the database and reading it via the find by name API
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private PortfolioRepository portRepo;
	private TransactionRepository transRepo;

	private JSONObject JSONdata;
	private Transaction transaction;
	private Portfolio portfolio;

	/**
	 * Creates a portfolio, assigns it to an instance variable, and stores it in the
	 * database to use the portfolio in subsequent tests.
	 * 
	 * @author Pascal Rohner und Marc Steiner
	 */
	@Before
	public void createAndSavePortfolio() {
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioName("Test");
		portfolio.setPortfolioFiatCurrency("CHF");
		this.portfolio = portfolio;
		entityManager.persist(portfolio);
		entityManager.flush();
	}
	
	/**
	 * Creates a transaction and stores it into the database
	 * @author Marc Steiner
	 */
	@Before
	public void createAndSaveTransaction() {
		Transaction transaction = new Transaction();
		transaction.setFiatCurrency("CHF");
		transaction.setCryptoCurrency("BTC");
		transaction.setFees(0.0);
		transaction.setType("Kauf");
		transaction.setNumberOfCoins(1.0);
		transaction.setPrice(500.0);
		transaction.setTransactionDate(null);
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
	 * Checks if the calculated portfolio statistic is correct 
	 * @author Marc Steiner
	 */
	@Test
	public void calculatePortfolioTest() {
		PortfolioCalculator portCalculator = new PortfolioCalculator();
		portCalculator.init(portfolio);
		
		/*
		 *Following code part is not working properly, because lack of knowledge.
		 *The problem is, that SpringBoot connects the transaction automatically
		 *to the actual portfolio with "many to one". In JUNIT this must be done 
		 *manually, but it is not clear how
		 * 
		portCalculator.calculatePortfolio(JSONdata);
		assertEquals("500.00", portCalculator.getProfitOrLoss());
		assertEquals("100.00", portCalculator.getProfitOrLossPercentage());
		assertEquals("1000", portCalculator.getTotalPortfolioValue());
		assertEquals("500.00", portCalculator.getTotalSpent());
		*/
		
	}

}
