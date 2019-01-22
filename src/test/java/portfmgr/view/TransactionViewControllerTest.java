package portfmgr.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TransactionViewControllerTest {

	/**
	 * checks whether the total is calculated correctly when a transaction is
	 * entered.
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void calculateTotalTest() {
		TransactionViewController transViewController = new TransactionViewController();
		Double result = transViewController.calculateTotal("Kauf", 1000.0, 2.0, 80.0);
		assertEquals(2080.0, result, 0);
	}
	
	/**
	 * checks whether the the fieldInput is correctly validated. 
	 * It should only be possible to enter numbers. The numbers can consist of one point and four decimal places.
	 * Checks the False case 
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void validateFieldInputFalseTest() {
		TransactionViewController transViewController = new TransactionViewController();
		boolean result = transViewController.validateFieldInput("ad 12321");
		assertTrue(result == false);
		
	}
	
	/**
	 * checks whether the the fieldInput is correctly validated. 
	 * It should only be possible to enter numbers. The numbers can consist of one point and four decimal places.
	 * Checks the True case 
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void validateFieldInputTrueTest() {
		TransactionViewController transViewController = new TransactionViewController();
		boolean result = transViewController.validateFieldInput("1235.2358");
		assertTrue(result == true);
		
	}
}
