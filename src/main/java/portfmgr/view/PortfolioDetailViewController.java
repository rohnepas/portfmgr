package portfmgr.view;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
/**
 * This class represents one of the main controllers and manage the detail view about the portfolios.
 * 
 * @author Marc Steiner
 */

public class PortfolioDetailViewController {

	private portfmgrApplication mainApp;
	private Portfolio portfolio;
	private String portfolioName;
	private String portfolioCurrency;
	
	public PortfolioDetailViewController(Portfolio portfolio) {
		this.portfolio = portfolio;
		portfolioName = portfolio.getPortfolioName();
		portfolioCurrency = portfolio.getPortfolioCurrency();
		updatePortfolio();
	}
	
	@FXML
	private Button openDashboard;

	/**
	 * Opens the portfolioView within the rootLayout
	 */
	public void openDashboard() {
		try {
			// Loads the portfolioView
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(portfmgrApplication.class.getResource("view/PortfolioView.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();

			// Gives the controller class access to the mainApp in order to set the scene
			// within the rootLayout.
			PortfolioViewController controller = loader.getController();
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
	

	public void updatePortfolio() {
		editPortfolio(portfolioName, portfolioCurrency);
		// neue INstanz von OnlineCourseQuery, welche ein JSON Objekt zurück gibt
		/*
		 * TO DO
		 * claculte everything and update view
		 */
		
	}
	

	public void editPortfolio(String portfolioName, String portfolioCurrency) {
		portfolio.setPortfolioCurrency(portfolioCurrency);
		portfolio.setPortfolioName(portfolioName);
		
		/*
		 * TO DO
		 * Call Portfolio methods and set name and currency
		 */
		
	}

}
