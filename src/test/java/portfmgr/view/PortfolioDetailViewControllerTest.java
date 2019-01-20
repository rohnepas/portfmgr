package portfmgr.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class PortfolioDetailViewControllerTest {

	PortfolioDetailViewController portDetailViewController;
	
	/**
	 * Creates an instance of the PortfolioDetailViewController and stores it in an instance 
	 * variable to be able to use this instance for all tests.
	 * 
	 * @author Pascal Rohner
	 */
	@Before
	public void createPortDetailViewControllerInstance() {
		PortfolioDetailViewController portDetailViewController = new PortfolioDetailViewController();
		this.portDetailViewController = portDetailViewController;
	}
	
	
	/**
	 * checks whether the percentage change of two values is calculated correctly.
	 * 
	 * @author Pascal Rohner
	 * 
	 */
	@Test
	public void getChangeinPercentageTest() {
		Double result = portDetailViewController.getChangeinPercentage(80.0, 100.0);
		assertEquals(-20.0, result.doubleValue(), 0);
		assertNotEquals(20, result, 0);
	}
	
	/**
	 * checks whether a Double is getting correctly rounded up. 
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void formatDoubleRoundingUpTest() {
		Double result = portDetailViewController.formatDouble(125.256885235);
		assertEquals(125.26, result, 0); 
	}
	
	/**
	 * checks whether a Double is getting correctly rounded down. 
	 * 
	 * @author Pascal Rohner
	 */
	
	@Test
	public void formatDoubleRoundingDownTest() {
		Double result = portDetailViewController.formatDouble(125.254885235);
		assertEquals(125.25, result, 0); 
	}

}
