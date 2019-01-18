package portfmgr.view;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;
import portfmgr.model.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionViewControllerTest {
	
	@MockBean
	PortfolioRepository portRepo;
	
	/**
	 * checks whether the total is calculated correctly when a transaction is entered.
	 * @author Pascal Rohner
	 */
	@Test
	public void calculateTotalTest() {
		TransactionViewController transViewController = new TransactionViewController();
		Double result = transViewController.calculateTotal("Kauf", 1000.0, 2.0, 80.0);
		assertEquals(2080.0, result, 0);
	}
	
	
	/**
	 * Checks whether a transaction can be created and stored in the database.
	 * @author Pascal Rohner
	 * 
	 * TODO: Test cases still needs to be written!
	 * 
	 */
	
	@Test 
	public void saveTransactionTest(){
		Portfolio portfolio = new Portfolio();
		List<Portfolio> portfolioList = new ArrayList<>();
		portfolio.setPortfolioFiatCurrency("CHF");
		portfolio.setPortfolioName("Pascal Rohner");
		
		portRepo.save(portfolio);
		
		portfolioList.addAll(portRepo.findByPortfolioFiatCurrency("CHF"));

		
		// ToDo! the size of the portfolioList is 0, why?
	}
	
	
	/**
	 * Checks wheather a transaction can be updated and stored in the database
	 * @author Pascal Rohner
	 * 
	 * TODO: Test cases still needs to be written!
	 */
	
	@Test
	public void updateTransactionTest() {
		
	}
	

	
	
}
