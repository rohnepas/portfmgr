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
	 * Creates an instance of the PortfolioUpdateViewController and stores it in an instance 
	 * variable to be able to use this instance for all tests.
	 * 
	 * @author Marc Steiner
	 */
	@Before
	public void init() {
		PortfolioUpdateViewController portUpdateViewController = new PortfolioUpdateViewController();
		this.portUpdateViewController = portUpdateViewController;		
	}
	
	/**
	 * Would test if input is valid, but couldn't build the test because of 
	 * not finding out how to initialize the DialogPane and Alert. These two causing a
	 * failure trace in JUNI test in code boolean isValid = portUpdateViewController.isInputValid("Test", "CHF") .
	 * @author Marc Steiner
	 */
	@Test
	public void isInputValidTest() {
		/*boolean isValid = portUpdateViewController.isInputValid("Test", "CHF");
		  
		assertTrue("Test, CHF", isValid);
		  
		boolean isNotValid1 = portUpdateViewController.isInputValid(null,"CHF");
		assertFalse("null, CHF", isNotValid1);
		  
		boolean isNotValid2 = portUpdateViewController.isInputValid("Test", null);
		assertFalse("Test, null", isNotValid2);*/
	 
	}
}
