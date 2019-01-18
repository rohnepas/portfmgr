package portfmgr.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PortfolioViewControllerTest {
	
	
	/**
	 * Checks whether the correct Id is returned when selecting a portfolio. 
	 * The first portfolio has the JavaFx-Id portfolio1. If portfolio 1 is selected, 
	 * the first portfolio is read from the list of portfolios. The one with the index 0.
	 * 
	 * @author Pascal Rohner
	 */
	@Test
	public void getSelectedPortfolioIdTest() {
		PortfolioViewController portViewController = new PortfolioViewController();
		int PortfolioIdTest = portViewController.getSelectedPortfolioId("Portfolio1");
		assertEquals(0, PortfolioIdTest);
		
	}
	
	
	/**
	 * Checks whether a portfolio can be created and whether it is stored correctly in the database.
	 * @author Pascal Rohner
	 * 
	 * TODO: Test cases still needs to be written!
	 */
	@Test
	public void createPortfolioTest() {
		
		
	}

}
