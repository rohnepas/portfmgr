package portfmgr.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.json.JSONObject;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private static List<String> fiatCurrencyList = Arrays.asList("CHF", "EUR", "USD");
	private List<String> cryptoCurrencyList;
	private static String coinlistPath = "src/main/java/coinlist/coinlist.json";

	@Autowired
	PortfolioRepository portRepo;

	@Autowired
	TransactionRepository transRepo;

	@Autowired
	TransactionViewController transViewController;

	@Autowired
	PortfolioCalculator portfolioCalculator;

	@FXML
	private TableView<Transaction> transactionTable;
	@FXML
	private TableView<Insight> insightTable;
	@FXML
	private TableColumn<Transaction, LocalDate> transactionDateColumn;
	@FXML
	private TableColumn<Transaction, String> transactionTypeColumn;
	@FXML
	private TableColumn<Transaction, String> transactionCryptoCurrencyColumn;

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
	private TableColumn<Insight, String> insightCryptoCurrencyColumn;

	@FXML
	private TableColumn<Insight, Double> insightSpotPriceColumn;

	@FXML
	private TableColumn<Insight, Double> insightNumberOfCoinsColumn;

	@FXML
	private TableColumn<Insight, Double> insightTotalColumn;

	@FXML
	private TableColumn<Insight, Double> insightAveragePriceColumn;

	@FXML
	private TableColumn<Insight, Double> insightChangePercentColumn;

	@FXML
	private TableColumn<Insight, Double> insightChanageFiatColumn;

	@FXML
	private Label portfolioName;
	@FXML
	private Label portfolioFiatCurrency;
	@FXML
	private Label profitOrLoss;
	@FXML
	private Label profitOrLossPercentage;
	@FXML
	private Label totalPortoflioValue;
	@FXML
	private Label totalSpent;
	@FXML
	private Button addTransaction;
	@FXML
	private Button editTransaction;
	@FXML
	private Button deleteTransaction;


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
		String tempPortfolioCurrency = portfolio.getPortfolioFiatCurrency();

		boolean currencyExists = fiatCurrencyList.stream().anyMatch(str -> str.trim().equals(tempPortfolioCurrency));

		if (tempPortfolioName == "leeres Portfolio" || tempPortfolioName == "" || !currencyExists) {

			mainApp.openPortfolioUpdateView(portfolio, fiatCurrencyList);

		} else {
			portfolioName.setText(portfolio.getPortfolioName());
			portfolioFiatCurrency.setText(portfolio.getPortfolioFiatCurrency());

		}
	}

	/**
	 * Method called if refresh button is clicked. It finds all symbols of crypto
	 * currencies in this portfolio, let calculate the portfolio value and
	 * statistics and display the results
	 *
	 * @author Marc Steiner
	 */

	public void updatePortfolio() {

		setCryptoCurrencyList();
		OnlineCourseQuery query = new OnlineCourseQuery(cryptoCurrencyList, fiatCurrencyList);

		try {
			onlineDataJSON = query.getOnlineCourseData();
			// saves the json object in an instance variable so that it can be used by other methods
			this.onlineDataJSON = onlineDataJSON;
		} catch (IOException e) {
			System.out.println("Problem within getOnlineCourseData()");
			e.printStackTrace();
		}

		portfolioCalculator.init(portfolio, onlineDataJSON, coinlistPath);
		portfolioCalculator.calculatePortfolio();

		refreshPortfolioData();
	}

	/**
	 * Refresh the portfolio without calculation.
	 *
	 * @author Marc Steiner
	 */
	public void refreshPortfolioData() {

		portfolioName.setText(portfolio.getPortfolioName());
		portfolioFiatCurrency.setText(portfolio.getPortfolioFiatCurrency());
		totalSpent.setText(portfolioCalculator.getTotalSpent() + " " + portfolio.getPortfolioFiatCurrency());
		totalPortoflioValue
				.setText(portfolioCalculator.getTotalPortfolioValue() + " " + portfolio.getPortfolioFiatCurrency());
		profitOrLoss.setText(
				String.valueOf(portfolioCalculator.getProfitOrLoss()) + " " + portfolio.getPortfolioFiatCurrency());
		profitOrLossPercentage.setText(String.valueOf(portfolioCalculator.getProfitOrLossPercentage()) + " %");

		if (portfolioCalculator.getProfit()) {

			profitOrLoss.setStyle("-fx-text-fill:green");
			profitOrLossPercentage.setStyle("-fx-text-fill:green");

		} else {
			profitOrLoss.setStyle("-fx-text-fill:red");
			profitOrLossPercentage.setStyle("-fx-text-fill:red");
		}
	}

	/**
	 * Extract all the crypto currencies which are within the portfolio and add them
	 * to a list
	 *
	 * @author Marc Steiner
	 */
	public void setCryptoCurrencyList() {
		cryptoCurrencyList = transRepo.findDistinctCryptoCurrency(portfolio.getId());
	}

	/**
	 *
	 * @return
	 * @author Marc Steiner
	 */
	public List<String> getCryptoCurrencyList() {
		return cryptoCurrencyList;
	}

	public List<String> getfiatCurrencyList() {
		return fiatCurrencyList;
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

		// change fiatCurrency of portfolio is not allowed. List<String> = actual
		// portfolio currency
		mainApp.openPortfolioUpdateView(portfolio, Arrays.asList(portfolio.getPortfolioFiatCurrency()));
		refreshPortfolioData();
	}

	/**
	 * Export the portfolio into a Excel Sheet with Apache POI
	 *
	 * @author Marc Steiner
	 */
	public void exportPortfolio() {
		// Opens the file chooser function and give back the destination file
		File file = mainApp.openFileExportView();

		if (file != null) {
			try {
				new ExportData(portfolio, file, getInsight());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Excel File wurde erfolgreich erstellt");
				alert.showAndWait();

			} catch (IOException e) {
				System.out.println("Problem with writing or closing of EXCEL sheet");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Set back the portfolio to default name and currency and delete all data from
	 * that portfolio
	 *
	 * @author Marc Steiner
	 */
	public void deletePortfolio() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Portfolio löschen");
		alert.setHeaderText("Portfolio wirklich löschen?");
		alert.setContentText(null);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			transRepo.deleteAllTransactions(portfolio.getId());
			portfolio.setPortfolioFiatCurrency("CHF");
			portfolio.setPortfolioName("leeres Portfolio");
			mainApp.openPortfolioView();
		}
	}

	/**
	 * Opens the transaction dialog to add a transaction. Because there is no
	 * transaction at this point, the parameter null is passed.
	 *
	 * @author Pascal Rohner und Marc Steiner
	 */
	public void addTransaction() {
		mainApp.openTransactionViewAdd(portfolio, null, coinlistPath, fiatCurrencyList);

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
		mainApp.openTransactionViewAdd(portfolio, selectedTransaction, coinlistPath, fiatCurrencyList);

	}

	/**
	 * Sets the actual portfolio. Is needed to assign a transaction to the portfolio
	 * when it is created.
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
	public void setFiatCurrencyList(List<String> list) {
		fiatCurrencyList = list;
	}

	/**
	 * Returns an ObservableList of Transaction objects to fill the transaction table
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
	 * Returns an ObservableList of Insight objects to fill the insights table
	 *
	 * @return ObversableList of insights
	 * @author Pascal Rohner
	 *
	 */

	public ObservableList<Insight> getInsight() {
		ObservableList<Insight> insight = FXCollections.observableArrayList();

		// create for each crypto currency on row in the table
		List<String> cryptoCurrencySymbols = transRepo.findDistinctCryptoCurrency(this.portfolio.getId());		
		
		for (String cryptoCurrency : cryptoCurrencySymbols) {
			Insight insightObject = new Insight();
			
			insightObject.setCryptoCurrency(cryptoCurrency);
			
			/*
			 *  Switches the desired cryptoCurrencie out of the JSON object
			 *  (contains all currencies according to the transactions) and saves it in a new JSON object. 
			 */
			
			JSONObject unwrappedCryptoCurrencyPrices = onlineDataJSON.getJSONObject(cryptoCurrency);
			
			/*
			 * The fiatCurrencie of the portfolio is read from the new JSON object and stored in a local
			 * variable
			 */
			
			Double tempSpotPrice = (Double) unwrappedCryptoCurrencyPrices.get(this.portfolio.getPortfolioFiatCurrency());
			insightObject.setSpotPrice((Double) unwrappedCryptoCurrencyPrices.get(this.portfolio.getPortfolioFiatCurrency()));

			// total number of coins for a specific currency taking into account the types Kauf and Verkauf
			Double tempSumNbrOfCoins = transRepo.sumUpNumberOfCoinsForCryptoCurrency(this.portfolio.getId(), cryptoCurrency);
			insightObject.setNumberOfCoins(tempSumNbrOfCoins);

			Double tempSumNrbOfCoinsKauf = transRepo.sumUpNumberOfCoinsForCryptoCurrencyTypeKauf(this.portfolio.getId(), cryptoCurrency);

			// total amout spent of a specific currency
			Double tempTotalAmount = tempSpotPrice * tempSumNbrOfCoins;
			insightObject.setTotal(tempTotalAmount);

			// avarage price paied for a specific currency
			Double tempAvaragePrice = transRepo.sumUpTotalForCryptoCurrency(this.portfolio.getId(), cryptoCurrency) / tempSumNrbOfCoinsKauf;
			insightObject.setAveragePrice(tempAvaragePrice);

			// change in percent. calls a separate method to calculate the change
			insightObject.setChangePercent(getChangeinPercentage(tempTotalAmount, tempAvaragePrice * tempSumNbrOfCoins));

			// change in absolute numbers (fiat)
			insightObject.setChangeFiat(tempTotalAmount - (tempAvaragePrice * tempSumNbrOfCoins));


			insight.add(insightObject);

		}

		return insight;

	}

	/**
	 * Calculates the percentage change of two values
	 *
	 * @param newValue
	 * @param oldValue
	 * @return changen in percent as Double
	 * @author Pascal Rohner
	 */
	public Double getChangeinPercentage(Double newValue, Double oldValue) {
		// New value - old value to get the absolute amount
		Double absolutAmount = newValue - oldValue;
		// Divide old value from the absolut amount to get the change in decimal
		Double changeInDecimal = absolutAmount / oldValue;
		// multiply change in decimal with 100 to get the change in percente
		Double changeInPercent = changeInDecimal * 100.0;
		return changeInPercent;
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
		transactionCryptoCurrencyColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("cryptoCurrency"));
		transactionPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("price"));
		transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("numberOfCoins"));
		transactionFeesColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Double>("fees"));
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

		/**
		 * Sets up the columns for the insights table
		 *
		 * @author Pascal Rohner
		 */

		insightCryptoCurrencyColumn.setCellValueFactory(new PropertyValueFactory<Insight, String>("cryptoCurrency"));
		insightSpotPriceColumn.setCellValueFactory(new PropertyValueFactory<Insight, Double>("spotPrice"));
		insightNumberOfCoinsColumn.setCellValueFactory(new PropertyValueFactory<Insight, Double>("numberOfCoins"));
		insightTotalColumn.setCellValueFactory(new PropertyValueFactory<Insight, Double>("total"));
		insightAveragePriceColumn.setCellValueFactory(new PropertyValueFactory<Insight, Double>("averagePrice"));
		insightChangePercentColumn.setCellValueFactory(new PropertyValueFactory<Insight, Double>("changePercent"));
		insightChanageFiatColumn.setCellValueFactory(new PropertyValueFactory<Insight, Double>("changeFiat"));

		/**
		 * Gets all the insights which need to been shown in the insight table
		 *
		 * @author Pascal Rohner
		 */
		
		
		// updates the portfolio before loading the table to get the spot prices of the crypto currencies
		updatePortfolio();

		insightTable.setItems(getInsight());

	}

}
