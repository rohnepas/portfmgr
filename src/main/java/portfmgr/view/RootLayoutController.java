package portfmgr.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;

@Controller
public class RootLayoutController implements Initializable {

	
	// Get the repository instance injected and use it.
	@Autowired
	PortfolioRepository portRepo;
	
    @FXML private Label label;
    
    /*
     * Adds a portfolio to the database and prints a list of all portfolios
     * with currency CHF
     */
    public void addPortfolioToDatabase() {
    	Portfolio portf = new Portfolio();
    	portf.setPortfolioName("Portfolio 1");
    	portf.setPortfolioCurrency("CHF");
    	portRepo.save(portf);
    	
    	List<Portfolio> portfolios = portRepo.findByPortfolioCurrency("CHF");
    	for (Portfolio portfolio : portfolios) {
    		// the toString method is overwritten
    		System.out.println(portfolio);
			
		}
    	
    	
    	
    	
    }

    
    
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {}


}

	
	
	



