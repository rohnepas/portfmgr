package portfmgr.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;
import portfmgr.model.Transaction;
import portfmgr.model.TransactionRepository;

/**
 * This class represents the link between the portfolio model and the portfolio
 * view.
 * 
 * @author pascal.rohner
 */

@Controller
public class PortfolioViewController implements Initializable {

	// Gets the repository instance injected and uses it.
	@Autowired
	PortfolioRepository portRepo;
	
	@Autowired
	TransactionRepository transRepo;

	// Reference to the main app (portfmgrApplication)
	private portfmgrApplication mainApp;

	@FXML
	private Button openPortfolio;
	
	@FXML
	private Button addPortfolio;
	
	@FXML
	private Button addTransaction;
	
	//@FXML
	//private Button addTransaction;

	/*
	 * Adds a portfolio to the database and prints a list of all portfolios with
	 * currency CHF
	 */
	public void addPortfolioToDatabase() {
		Portfolio portf = new Portfolio();
		portf.setPortfolioCurrency("CHF");
		portf.setPortfolioName("Portfolio1");
		portRepo.save(portf);

		List<Portfolio> portfolios = portRepo.findByPortfolioCurrency("CHF");
		for (Portfolio portfolio : portfolios) {
			// the toString method is overwritten
			System.out.println(portfolio);

		}
	}
	

	/**
	 * Opens the portfolioDetailView within the rootLayout
	 */
	public void openPortfolio() {
		try {
			// Loads the portfolioView
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(portfmgrApplication.class.getResource("view/PortfolioDetailView.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();

			// Gives the controller class access to the mainApp in order to set the scene within the rootLayout.
			PortfolioDetailViewController controller = loader.getController();
			controller.setMainApp(mainApp);

			// Opens the portfolioView within the rootLayout
			mainApp.getRootLayout().setCenter(overview);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the MainApp variable and is called by the MainApp itself.
	 * 
	 * @param portfmgrApplication
	 */
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;
	}

	@Override
    public void initialize(URL location, ResourceBundle resourceBundle) {}

}
