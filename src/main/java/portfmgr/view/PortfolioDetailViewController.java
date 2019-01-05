package portfmgr.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.json.JSONException;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private JSONObject onlineDataJSON;
	private List<String> currencyList;
	private List<String> cryptocurrencyList;
	
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
	private TableColumn<?, String> overviewCurrencyColumn;
	@FXML
	private Label portfolioName;
	@FXML
	private Label portfolioCurrency;
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
	
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;
		setCurrencyList();
		checkAndSetPortfolioSettings();
		refreshPortfolio();
	}
	
	public void setCurrencyList() {
		currencyList = Arrays.asList("CHF", "EUR", "USD");
	}
	
	/*
	 * Checks if the choosen portolio has a proper name and currency set. If not opens updateView pop-up
	 */
	public void checkAndSetPortfolioSettings() {

		String tempPortfolioName = portfolio.getPortfolioName();
		String tempPortfolioCurrency = portfolio.getPortfolioCurrency();
		
		boolean currencyExists = currencyList.stream().anyMatch(str -> str.trim().equals(tempPortfolioCurrency));
		
		if (tempPortfolioName == "leeres Portfolio" || tempPortfolioName == "" || !currencyExists) {
			
			mainApp.openPortfolioUpdateView(portfolio, currencyList);
			
		}
		else {
			portfolioName.setText(portfolio.getPortfolioName());
			portfolioCurrency.setText(portfolio.getPortfolioCurrency());
			
		}
	}
	
	/*
	 * Refreshes the portfolio name and currency.
	 */
	public void refreshPortfolio() {

		portfolioName.setText(portfolio.getPortfolioName());
		portfolioCurrency.setText(portfolio.getPortfolioCurrency());
		
		// TO DO: UPDATE DATA
		profitOrLoss.setText("GEWINN VERLUST CHF");
		profitOrLossPercentage.setText("GEWINN VERLUST %");
		totalPortoflioValue.setText("WERT PORTFOLIO");
	}
	
	/*
	 * Method called if refresh button is clicked. It finds all symbols of crypto currencies in this portfolio
	 * and calculate the portfolio
	 */
	public void updatePortfolio() {
		 
		setCryptocurrencyList();
		calculatePortfolio();
		
	}
	
	public void setCryptocurrencyList() {
		/*
		 * TO DO:
		 * Finde alle CryptoCurrencies die in diesem Portfolio vorkommen
		 * aktuell eine Testliste mit verschiedenen Kryptos implementiert
		*/
		cryptocurrencyList = Arrays.asList("BTC", "ETH", "LTC", "XRP", "TRX", "IOT");
		
	}
	
	/*
	 * Extract JSON data from JSON Object and calculate the new portfolio value
	 */
	public void calculatePortfolio() {
		/*
		 * TO DO Portfolio berechnen
		 */
		onlineDataJSON = onlineCourseQuery();
				
		try {
			for (String symbol: cryptocurrencyList) {
				JSONObject values = onlineDataJSON.getJSONObject(symbol);
				System.out.println("Die Werte für " + symbol);
				
				
				//nur für CHF:
				//double result = values.getDouble("CHF");
				
				for (String currency: currencyList) {
					double result = values.getDouble(currency);
					System.out.print("Währung " + currency + " = " + result);
					System.out.println("");
				}
				System.out.println("");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Calls the Web API and query the data
	 */
	public JSONObject onlineCourseQuery() {
		
		OnlineCourseQuery query = new OnlineCourseQuery();
		JSONObject data;
		
		query.setSymbols(getCryptoCurrencyList());
		query.setCurrencies(getCurrencyList());
		
		try {
			data = query.getOnlineCourseData();
			return data;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	
	public List<String> getCryptoCurrencyList(){
		return cryptocurrencyList;
	}
	
	public List<String> getCurrencyList(){
		return currencyList;
	}
	
	
	/*
	 * Sends actual portfolio to the main app, which handles starts the UpdateViewController.
	 * If something has changed in the update view, the data will be saved directly to the database
	 */
	public void editPortfolio() {
		
		mainApp.openPortfolioUpdateView(portfolio, currencyList);
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
		
		// ToDo: Beim L�schen muss die Transation noch aus der Datenbank gel�scht werden!
		
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
		
		// vorerst werden nur mal BTC-Positionen angezeigt. Es k�nnen aktuell auch nur BTC Positionen erfasst werden.
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
	
					
	}
	
}


	
	


