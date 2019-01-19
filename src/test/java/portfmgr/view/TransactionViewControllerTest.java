package portfmgr.view;

import static org.junit.Assert.assertEquals;

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

}
