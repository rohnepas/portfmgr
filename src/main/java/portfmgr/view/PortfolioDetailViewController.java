package portfmgr.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;
/**
 * This class represents one of the main controllers and manage the detail view about the portfolios.
 * 
 * @author Marc Steiner
 */

public class PortfolioDetailViewController {

	private portfmgrApplication mainApp;
	private Long Id;
	
	@Autowired
	PortfolioRepository portRepo;
	
	@FXML
	private Button openDashboard;

	
	
	// Konstruktor wegnehmen und eine mMEthode zur Verfügungstellen welche ID bekommt
	
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

	public void updatePortfolio() {
		// neue INstanz von OnlineCourseQuery, welche ein JSON Objekt zurück gibt
		/*
		 * TO DO
		 * claculte everything and update view
		 */
		
	}
	
	public void editPortfolio(String portfolioName, String portfolioCurrency, Long Id) {
		Portfolio portfolio = portRepo.findByPortfolioId(Id);
		portfolio.setPortfolioCurrency(portfolioCurrency);
		portfolio.setPortfolioName(portfolioName);

		
		/*
		 * TO DO
		 * Call Portfolio methods and set name and currency
		 */
		
	}
	
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;

	}
	
	public void setActualPortoflio(Long PortfolioId) {
		Id = PortfolioId;
		
	}

}
