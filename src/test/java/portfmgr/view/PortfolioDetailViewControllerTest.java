package portfmgr.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class PortfolioDetailViewControllerTest {

	/**
	 * checks whether the percentage change of two values is calculated correctly.
	 * 
	 * @author Pascal Rohner
	 * 
	 */
	@Test
	public void getChangeinPercentageTest() {
		PortfolioDetailViewController portDetailController = new PortfolioDetailViewController();
		Double result = portDetailController.getChangeinPercentage(80.0, 100.0);
		assertEquals(-20.0, result.doubleValue(), 0);
		assertNotEquals(20, result, 0);
	}

}
