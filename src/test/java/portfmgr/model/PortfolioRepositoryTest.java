package portfmgr.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PortfolioRepositoryTest {

	// Is used to insert a Portfolio in the database and reading it via the find by
	// name API
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private PortfolioRepository portRepo;

	private Portfolio portfolio;

	/**
	 * Creates a portfolio, assigns it to an instance variable, and stores it in the
	 * database to use the portfolio in subsequent tests.
	 * 
	 * @author Pascal Rohner
	 */
	@Before
	public void createAndSavePortfolio() {
		// given
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioName("Pascal Rohner");
		portfolio.setPortfolioFiatCurrency("CHF");
		this.portfolio = portfolio;
		entityManager.persist(portfolio);
		entityManager.flush();
	}

	/**
	 * checks whether a portfolio after the fiat currency is really delivered with
	 * the requested currency.
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void findByPortfolioFiatCurrency_thenReturnPortfolio() {
		// when
		List<Portfolio> found = portRepo.findByPortfolioFiatCurrency("CHF");

		// then
		assertEquals(this.portfolio.getPortfolioFiatCurrency(), found.get(0).getPortfolioFiatCurrency());
	}

	/**
	 * checks whether a portfolio name can be correctly changed. 
	 * The name change is immediately saved in the database.
	 * 
	 * @author Pascal Rohner
	 * 
	 */
	@Test
	public void changePortfolioName_thenReturnPortfolio() {
		// when
		portfolio.setPortfolioName("Pascal");
		
		List<Portfolio> found = portRepo.findByPortfolioFiatCurrency("CHF");

		// then
		assertEquals(this.portfolio.getPortfolioName(), found.get(0).getPortfolioName());
	}

}
