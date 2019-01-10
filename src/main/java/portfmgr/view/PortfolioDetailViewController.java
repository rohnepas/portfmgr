package portfmgr.view;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import portfmgr.portfmgrApplication;
import portfmgr.model.ExportData;
import portfmgr.model.Insight;
import portfmgr.model.OnlineCourseQuery;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioCalculator;
import portfmgr.model.PortfolioRepository;
import portfmgr.model.Transaction;
import portfmgr.model.TransactionRepository;

/**
 * This class represents one of the main controllers and manage the detail view
 * about the portfolios.
 * 
 * @author Marc Steiner & Pascal Rohner
 */

@Controller
public class PortfolioDetailViewController implements Initializable {

	private portfmgrApplication mainApp;
	private Portfolio portfolio;
	private JSONObject onlineDataJSON;
	private static List<String> currencyList = Arrays.asList("CHF", "EUR", "USD");
	private List<String> cryptocurrencyList;
	private static String coinlistPath = "src/main/java/coinlist/coinlist.json";

	@Autowired
	PortfolioRepository portRepo;

	@Autowired
	TransactionRepository transRepo;
	
	@Autowired
	TransactionViewController transViewController;
	
//	@Autowired
//	Insights insights;

	@FXML
	private TableView<Transaction> transactionTable;
	@FXML
	private TableView<Insight> insightTable;
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
	private TableColumn<Insight, String> insightCurrencyColumn;
	
	@FXML
	private TableColumn<Insight, Double> insightNumberOfCoinsColumn;
	
	@FXML
	private TableColumn<Insight, Double> insightTotalColumn;
	
	@FXML
	private TableColumn<Insight, Double> insightAveragePriceColumn;
	
	@FXML
	private TableColumn<Insight, Double> insightChangeColumn;
	
	
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
	private ImageView logo;
	@FXML
	private Button addTransaction;
	@FXML
	private Button editTransaction;
	@FXML
	private Button deleteTransaction;
	@FXML
	private Button testQuery;

	public void testQueryMethod() {
//		insights.setTotal(transRepo.sumUpTotals());
//		System.out.println("should print the total:");
//		System.out.println(insights.getTotal());
		
		// prints the distinct currencies
		System.out.println(transRepo.findDistinctCurrencies());
		
		
		
		
	}
	
	
	/**
	 * Calls method from mainApp to open the portfolioView
	 * 
	 * @author Pascal Rohner
	 */
	public void openPortfolioView() {
		mainApp.openPortfolioView();

	}

	/**
	 * Setup the main app and initalize portfolio values
	 * 
	 * @param mainApp
	 * @throws IOException
	 * @author Marc Steiner
	 */
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;
		checkAndSetPortfolioSettings();
		updatePortfolio();
	}

	/**
	 * Checks if the choosen portfolio has a proper name and currency set. If not
	 * opens updateView pop-up
	 * 
	 * @author Marc Steiner
	 */
	public void checkAndSetPortfolioSettings() {

		String tempPortfolioName = portfolio.getPortfolioName();
		String tempPortfolioCurrency = portfolio.getPortfolioCurrency();

		boolean currencyExists = currencyList.stream().anyMatch(str -> str.trim().equals(tempPortfolioCurrency));

		if (tempPortfolioName == "leeres Portfolio" || tempPortfolioName == "" || !currencyExists) {

			mainApp.openPortfolioUpdateView(portfolio, currencyList);

		} else {
			portfolioName.setText(portfolio.getPortfolioName());
			portfolioCurrency.setText(portfolio.getPortfolioCurrency());

		}
	}

	/**
	 * Refreshe the portfolio name and currency.
	 * 
	 * @author Marc Steiner
	 */
	public void refreshPortfolio() {

		portfolioName.setText(portfolio.getPortfolioName());
		portfolioCurrency.setText(portfolio.getPortfolioCurrency());

		// TO DO: UPDATE DATA
		profitOrLoss.setText("GEWINN VERLUST CHF");
		profitOrLossPercentage.setText("GEWINN VERLUST %");
		totalPortoflioValue.setText("WERT PORTFOLIO");
	}

	/**
	 * Method called if refresh button is clicked. It finds all symbols of crypto
	 * currencies in this portfolio and calls the portfolio calculate class
	 * 
	 * @throws IOException
	 * @author Marc Steiner
	 */
	public void updatePortfolio() {

		setCryptocurrencyList();
		OnlineCourseQuery query = new OnlineCourseQuery(cryptocurrencyList, currencyList);

		try {
			onlineDataJSON = query.getOnlineCourseData();
		} catch (IOException e) {
			System.out.println("Problem within getOnlineCourseData()");
			e.printStackTrace();
		}

		System.out.println("ONLINE DATEN SIND:  " + onlineDataJSON);

		PortfolioCalculator calculator = new PortfolioCalculator(portfolio, onlineDataJSON, cryptocurrencyList,
				currencyList, coinlistPath);
		calculator.calculatePortfolio();
		refreshPortfolio();
	}

	/**
	 * Extract all the crypto currencies which are within the portfolio and add them
	 * to a list
	 * @author Marc Steiner
	 */
	public void setCryptocurrencyList() {
		/*
		 * TO DO: Finde alle CryptoCurrencies die in diesem Portfolio vorkommen aktuell
		 * eine Testliste mit verschiedenen Kryptos implementiert
		 */
		cryptocurrencyList = Arrays.asList("BTC", "ETH", "LTC", "XRP", "TRX", "IOT");
	}

	/**
	 * 
	 * @return
	 * @author Marc Steiner
	 */
	public List<String> getCryptoCurrencyList() {
		return cryptocurrencyList;
	}

	/**
	 * 
	 * @return
	 * @author Marc Steiner
	 */
	public List<String> getCurrencyList() {
		return currencyList;
	}

	/**
	 * Sends actual portfolio to the main app, which handles and starts the
	 * UpdateViewController. If the name or the currency has been changed in the
	 * update view, the data will be saved directly to the database by the
	 * UpdateViewController
	 * 
	 * @author Marc Steiner
	 */
	public void editPortfolio() {

		mainApp.openPortfolioUpdateView(portfolio, currencyList);
		refreshPortfolio();
	}

	/**
	 * Export the portfolio into a Excel Sheet with Apache POI
	 * 
	 * @author Marc Steiner
	 */
	public void exportPortfolio() {
		try {
			new ExportData(portfolio);
		} catch (IOException e) {
			System.out.println("Problem with writing or closing of EXCEL sheet");
			e.printStackTrace();
		}
		System.out.println("Datei erfolgreich exportiert");
	}

	public void deletePortfolio() {
		System.out.println("Portfolio DELETE");
		/*
		 * TO DO: Delete Portfolio
		 */
	}

	/**
	 * Opens the transaction dialog to add a transaction. Because there is no 
	 * transaction at this point, the parameter null is passed.
	 * 
	 * @author Pascal Rohner
	 */
	public void addTransaction() {
		mainApp.openTransactionViewAdd(portfolio, null, coinlistPath);
	}

	/**
	 * Deletes a transaction from the transaction table and from the database
	 * 
	 * @author Pascal Rohner
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
			transRepo.delete(transaction);
		}
	}

	/**
	 * Opens the transaction view to edit a given transaction
	 * 
	 * @author Pascal Rohner
	 */

	public void editTransaction() {
		Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();		
		mainApp.openTransactionViewAdd(portfolio, selectedTransaction, coinlistPath);	
	}

	/**
	 * Sets the actual portfolio. Is needed to assign a transaction to the portfolio when it is created.
	 * 
	 * @author Pascal Rohner
	 */
	public void setActualPortoflio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	
	/**
	 * 
	 * @param list
	 * @author Marc Steiner
	 */
	public void setCurrencyList(List<String> list) {
		currencyList = list;
	}

	/**
	 * Returns an ObservableList of Transaction objects
	 * 
	 * @return ObversableList of transactions
	 * @author Pascal Rohner
	 */
	public ObservableList<Transaction> getTransaction() {
		ObservableList<Transaction> transaction = FXCollections.observableArrayList();

		
		transaction.addAll(transRepo.findByPortfolio(portfolio));

		return transaction;
	}
	
	/**
	 * Manually added insight object
	 * 
	 */
	
	public ObservableList<Insight> getInsight() {
		ObservableList<Insight> insight = FXCollections.observableArrayList();
		
		// an manual an insight
		Insight insightObject = new Insight();
		insightObject.setCurrency("BTC");
		insightObject.setNumberOfCoins(2.3);
		insightObject.setAveragePrice(3232.0);
		insightObject.setTotal(8988.23);
		insightObject.setChange(79.9);
		
		insight.add(insightObject);
		return insight;
		

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**
		 * Sets up the columns for the transaction table
		 * 
		 * @author Pascal Rohner
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
		 * @author Pascal Rohner
		 */

		transactionTable.setItems(getTransaction());

		/**
		 * Allows the user to just select a single rows in the transaction table
		 * 
		 * @author Pascal Rohner
		 */

		transactionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		/**
		 * Sets up the columns for the insight table
		 * 
		 * @author Pascal Rohner
		 */
		


	}

}
