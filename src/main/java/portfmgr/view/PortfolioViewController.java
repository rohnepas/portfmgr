package portfmgr.view;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;

/**
 * This class represents the link between the portfolio model and the portfolio
 * view.
 * 
 * @author pascal.rohner
 */

@Controller
public class PortfolioViewController {

	// Gets the repository instance injected and uses it.
	@Autowired
	PortfolioRepository portRepo;

	// Reference to the main app (portfmgrApplication)
	private portfmgrApplication mainApp;

	@FXML
	private Button openPortfolio;

	/*
	 * Adds a portfolio to the database and prints a list of all portfolios with
	 * currency CHF
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

	/**
	 * Opens the portfolioDetailView
	 */
	public void openPortfolio() {
		try {
			// Loads the portfolioDetailView.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(portfmgrApplication.class.getResource("view/PortfolioDetailView.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();

			// Gets the RootLayout and sets the portfolioDetailView within the RootLayout
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

}
