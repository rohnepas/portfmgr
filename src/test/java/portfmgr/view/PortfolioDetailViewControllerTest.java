package portfmgr.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import portfmgr.model.PortfolioRepository;

public class PortfolioDetailViewControllerTest {
	
	@Autowired
	PortfolioRepository portRepo;
	PortfolioDetailViewController portDetailController = new PortfolioDetailViewController();
	
	/**
	 * checks whether the percentage change of two values is calculated correctly.
	 * @author Pascal Rohner
	 * 
	 */
	@Test
	public void getChangeinPercentageTest() {
		Double result = portDetailController.getChangeinPercentage(80.0, 100.0);
		assertEquals(-20.0, result.doubleValue(), 0);
		assertNotEquals(20, result, 0);
	}
	
	
	/**
	 * Checks whether a transaction is correctly deleted from the database.
	 * @author Pascal Rohner
	 * 
	 * TODO: Test cases still needs to be written!
	 */
	@Test
	public void deleteTransactionTest() {
		
	}
	
	
	/**
	 * Checks whether a transaction can be correctly retrieved from the database. 
	 * @author Pascal Rohner
	 * 
	 * TODO: Test cases still needs to be written!
	 */
	@Test
	public void getTransactionTest() {
		
	}
	
	
	

}
