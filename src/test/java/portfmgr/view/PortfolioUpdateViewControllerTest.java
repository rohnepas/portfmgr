package portfmgr.view;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Marc Steiner
 *
 */
public class PortfolioUpdateViewControllerTest {
	PortfolioUpdateViewController portUpdateViewController;
	
	/**
	 * Creates an instance of the PortfolioDetailViewController and stores it in an instance 
	 * variable to be able to use this instance for all tests.
	 * 
	 * @author Pascal Rohner
	 */
	@Before
	public void createPortfolioUpdateViewControllerInstance() {
		PortfolioUpdateViewController portUpdateViewController = new PortfolioUpdateViewController();
		this.portUpdateViewController = portUpdateViewController;
	}
	
	@Test
	public void isInputValidTest() {
		boolean isValid = portUpdateViewController.isInputValid("Test", "CHF");
		  
		assertTrue("Test, CHF", isValid);
		  
		boolean isNotValid1 = portUpdateViewController.isInputValid(null,"CHF");
		assertFalse("null, CHF", isNotValid1);
		  
		boolean isNotValid2 = portUpdateViewController.isInputValid("Test", null);
		assertFalse("Test, null", isNotValid2);
	 
	}
}
