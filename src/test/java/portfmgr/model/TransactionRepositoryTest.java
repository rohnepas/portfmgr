package portfmgr.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {

	// Is used to insert a Portfolio in the database and reading it via the find by name API
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private PortfolioRepository portRepo;
	
	@Autowired
	private TransactionRepository transRepo;
	
	private Portfolio portfolio;
	private Transaction firstTransaction;
	private Transaction secondTransaction;
	
	
	/**
	 * Creates a portfolio and a transaction, assigns it to instance variable variable, and stores it in the
	 * database to use the portfolio and transaction in subsequent tests.
	 * 
	 * @author Pascal Rohner
	 */
	@Before
	public void setUpPortfolioAndTransaction() {
		// given
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioFiatCurrency("CHF");
		portfolio.setPortfolioName("Pascal Rohner");
		this.portfolio = portfolio;
		entityManager.persist(portfolio);
		entityManager.flush();
		
		Transaction firstTransaction = new Transaction();
		firstTransaction.setCryptoCurrency("BTC");
		firstTransaction.setNumberOfCoins(5.0);
		firstTransaction.setTotal(1235.25);
		firstTransaction.setType("Kauf");
		firstTransaction.setPortfolio(portfolio);
		this.firstTransaction = firstTransaction;
		
		entityManager.persist(firstTransaction);
		entityManager.flush();
		
		Transaction secondTransaction = new Transaction();
		secondTransaction.setCryptoCurrency("BTC");
		secondTransaction.setNumberOfCoins(2.25);
		secondTransaction.setTotal(222.85);
		secondTransaction.setType("Verkauf");
		secondTransaction.setPortfolio(portfolio);
		this.secondTransaction = secondTransaction;
		
		entityManager.persist(secondTransaction);
		entityManager.flush();
		
	}
	
	
	/**
	 * Checks whether the query for adding the number of coins of a given currency is correct.
	 * Two different types of transactions are intentionally used. "Kauf" and "Verkauf".
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void sumUpNumberOfCoinsForCryptoCurrency_thenReturnNbr() {
		// when
		Double result = transRepo.sumUpNumberOfCoinsForCryptoCurrency(this.portfolio.getId(), "BTC");
		
		// then
		assertEquals(7.25, result, 0);
	}
	
	/**
	 * Checks whether the query for summing up the total of a given currency is correct.
	 * Two different types of transactions are intentionally used. "Kauf" and "Verkauf".
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void sumUpTotalForCryptoCurrency_thenReturnSum() {
		// when
		Double result = transRepo.sumUpTotalForCryptoCurrency(this.portfolio.getId(), "BTC");
		
		//then
		assertEquals(1235.25, result, 0);
		
		
		
	}

}
