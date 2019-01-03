package portfmgr.view;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import portfmgr.portfmgrApplication;
import portfmgr.model.OnlineCourseQuery;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;

/**
 * This class represents one of the main controllers and manage the detail view about the portfolios.
 * 
 * @author Marc Steiner
 */

@Controller
public class PortfolioDetailViewController  implements Initializable {

	private portfmgrApplication mainApp;
	private Portfolio portfolio;
	private JSONObject onlineData;
	
	@Autowired
	PortfolioRepository portRepo;
	
	@FXML
	private Button openDashboard;
	@FXML
	private Button updatePortfolio;
	@FXML
	private Button exportPortfolio;
	@FXML
	private Button editPortfolio;
	@FXML
	private Button deletePortfolio;
	@FXML
	private Label portfolioName;
	@FXML
	private Label profitOrLoss;
	@FXML
	private Label profitOrLossPercentage;
	@FXML
	private Label totalPortoflioValue;
	
	
	/**
	 * Calls method from mainApp to open the portfolioView
	 */
	public void openPortfolioView() {
		mainApp.openPortfolioView();

	}	

	
	public void onlineCourseQuery() {
		
		OnlineCourseQuery query = new OnlineCourseQuery();
		
		List<String> testListe = Arrays.asList("BTC", "ETH", "LTC");
		query.setSymbols(testListe);
		
		List<String> testListeCurrency = Arrays.asList("CHF", "USD", "EUR");
		
		query.setCurrencies(testListeCurrency);
		
		
		try {
			onlineData = query.getOnlineCourseData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(onlineData);
		
	}
	
	
	public void updatePortfolio() {
		
		/*
		 * TO DO:
		 * JSON FILE auflösen und in TEXTArea als erstes anzeigen 
		 * Alle Daten  neu Berechnen und die Daten des Portoflio updaten
		 */
		onlineCourseQuery();
		
	}
	
	public void refreshPortfolio() {
		/*
		 * Holt das aktuelle Portfolio nochmasl aus der Datenbank ohne Online Daten und ohne Datenberechnung
		 */
		
		//Portfolio portfolio = portRepo.findById(portfolio.getId());
		//portfolio.getPortfolioName();
		// portfolio.getPortfolioCurrency();
		// NAME und Currency anzeigen
		
	}
	
	
	/*
	 * Sends actual portfolio to the main app, which handles starts the UpdateViewController.
	 * If something has changed in the update view, the data will be saved directly to the database
	 */
	public void editPortfolio() {
		
		mainApp.openPortfolioUpdateView(portfolio);
		refreshPortfolio();
	
	}
	
	public void deletePortfolio() {
		System.out.println("Portfolio DELETE");
		/*
		 * TO DO: Delete Portfolio
		 */
	}
	
	public void exportPortfolio() {
		System.out.println("Portfolio EXPORT");
		/*
		 * TO DO: Delete Portfolio
		 */
	}
	
	public void addTransaction() {
		System.out.println("Transaction ADDED");
		/*
		 * TO DO: Delete Portfolio
		 */
	}
	
	public void deleteTransaction() {
		System.out.println("Transaction DELETET");
		/*
		 * TO DO: Delete Portfolio
		 */
	}
	
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;

	}
	
	public void setActualPortoflio(Portfolio portfolio) {
		this.portfolio = portfolio;	
	}
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		portfolioName.setText(portfolio.getPortfolioName());
		profitOrLoss.setText("GEWINN VERLUST CHF");
		profitOrLossPercentage.setText("GEWINN VERLUST %");
		totalPortoflioValue.setText("WERT PORTFOLIO");
	}

}
