package portfmgr.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import portfmgr.portfmgrApplication;
import portfmgr.model.OnlineCourseQuery;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;
import portfmgr.model.Transaction;
import portfmgr.model.TransactionRepository;

/**
 * This class represents one of the main controllers and manage the detail view about the portfolios.
 * 
 * @author Marc Steiner & pascal.rohner
 */

@Controller
public class PortfolioDetailViewController  implements Initializable {

	private portfmgrApplication mainApp;
	private Portfolio portfolio;
	private JSONObject onlineData;
	
	@Autowired
	PortfolioRepository portRepo;
	
	@Autowired
	TransactionRepository transRepo;
	
	@FXML
	private TableView<Transaction> transactionTable;
	
	@FXML
	private TableColumn<Transaction, LocalDate> transactionDateColumn;
	
	@FXML
	private TableColumn<Transaction, String> transactionTypeColumn;
	
	@FXML
	private TableColumn<Transaction, String> transactionCurrencyColumn;
	
	@FXML
	private TableColumn<Transaction, Double> transactionPriceColumn;
	
	@FXML
	private TableColumn<Transaction, Double> transactionAmountColumn;
	
	@FXML
	private TableColumn<Transaction, Double> transactionFeesColumn;
	
	@FXML
	private TableColumn<Transaction, Double> transactionTotalColumn;
	
	
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
	
	@FXML
	private Button addTransaction;
	
	@FXML
	private Button deleteTransaction;
	
	
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
		 * JSON FILE auflÃ¶sen und in TEXTArea als erstes anzeigen 
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
	
	/**
	 * Opens the transaction dialog to add a transaction
	 * 
	 * @author pascal.rohner
	 */
	
	public void addTransaction() {
		mainApp.openTransactionViewAdd();
		
	}
	
	
	/**
	 * Deletes a transaction from the transaction table
	 * 
	 * @author pascal.rohner
	 */
	public void deleteTransaction() {
		
		// Creation of a observable list for selected transaction and all transaction
		ObservableList<Transaction> selectedTransactions, allTransactions;
		
		allTransactions = transactionTable.getItems();
		
		// gives the selected rows
		selectedTransactions = transactionTable.getSelectionModel().getSelectedItems();
		
		// loops over the selected rows and removes them from the transaction table
		for (Transaction transaction : selectedTransactions) {
			allTransactions.remove(transaction);
		}
		
		// ToDo: Beim Löschen muss die Transation noch aus der Datenbank gelöscht werden!
		
	}
	
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;

	}
	
	public void setActualPortoflio(Portfolio portfolio) {
		this.portfolio = portfolio;	
	}
	
	/**
	 * Returns an ObservableList of Transaction objects
	 * @return ObversableList of transactions
	 * @author pascal.rohner 
	 */
	public ObservableList<Transaction> getTransaction(){	
		ObservableList<Transaction> transaction = FXCollections.observableArrayList();
		
		// vorerst werden nur mal BTC-Positionen angezeigt. Es können aktuell auch nur BTC Positionen erfasst werden.
		transaction.addAll(transRepo.findByCurrency("BTC"));
		
		return transaction;
		}
		
		
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**
		 * Sets up the columns for the transaction table
		 * @author pascal.rohner
		 */
		transactionDateColumn.setCellValueFactory(new PropertyValueFactory<Transaction, LocalDate>("transactionDate"));
		transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("type"));
		transactionCurrencyColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("currency"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceUSD"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceBTC"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceEUR"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("priceCHF"));
		transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("numberOfCoins"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesUSD"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesBTC"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesEUR"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("feesCHF"));
		transactionTotalColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("total"));
		
		/**
		 * Gets all the transactions which need to been shown in the transaction table
		 * 
		 * @author pascal.rohner
		 */
				
		transactionTable.setItems(getTransaction());
		
		/**
		 * Allows the user to just select a single rows in the transaction table
		 * 
		 * @author pascal.rohner
		 */
		
		transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		
		portfolioName.setText(portfolio.getPortfolioName());
		profitOrLoss.setText("GEWINN VERLUST CHF");
		profitOrLossPercentage.setText("GEWINN VERLUST %");
		totalPortoflioValue.setText("WERT PORTFOLIO");

		}
		

	
		
	
		
		
		
		
		
		
		
	}


	
	


