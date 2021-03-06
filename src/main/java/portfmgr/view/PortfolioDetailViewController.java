package portfmgr.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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
	private static List<String> fiatCurrencyList = Arrays.asList("CHF", "EUR", "USD");
	private List<String> cryptoCurrencyList;
	private String defaultPortfolioname = "leeres Portfolio";
	private String defaultPortfolioFiatCurrency = "CHF";
	private String defaultProfitOrLoss = "-";
	private String defaultTotalPortfolioValue = "-";

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
	@FXML
	private ImageView imageDashboard;
	@FXML
	private ImageView imageUpdate;
	@FXML
	private ImageView imageExport;
	@FXML
	private ImageView imageEdit;
	@FXML
	private ImageView imageDelete;


	/**
	 * Calls method from mainApp to open the portfolioView
	 *
	 * @author Pascal Rohner
	 */
	public void openPortfolioView() {
		mainApp.openPortfolioView();

	}

	/**
	 * Setup the main app and initialize portfolio values
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
	 * Checks if the chosen portfolio has a proper name and currency set. 
	 * If not opens updateView pop-up
	 *
	 * @author Marc Steiner
	 */
	public void checkAndSetPortfolioSettings() {

		String tempPortfolioName = portfolio.getPortfolioName();
		String tempPortfolioCurrency = portfolio.getPortfolioFiatCurrency();

		boolean currencyExists = fiatCurrencyList.stream().anyMatch(str -> str.trim().equals(tempPortfolioCurrency));

	
		if (tempPortfolioName.equals("leeres Portfolio") || tempPortfolioName.equals("") || !currencyExists) {
	
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

		OnlineCourseQuery query = new OnlineCourseQuery();
		
		setCryptoCurrencyList();

		try {
			this.onlineDataJSON = query.getOnlineCourseData(this.cryptoCurrencyList, fiatCurrencyList);
			
		} catch (IOException e) {
			//System.out.println("Problem within getOnlineCourseData()");
			e.printStackTrace();
		}

		// do not instance portfolioCalulator because of @Autowired
		portfolioCalculator.init(this.portfolio);
		portfolioCalculator.calculatePortfolio(this.onlineDataJSON);

		refreshPortfolioData();
	}

	/**
	 * Refresh the portfolio without calculation.
	 * Do not instance portfolioCalulator because of @Autowired
	 *
	 * @author Marc Steiner
	 */
	public void refreshPortfolioData() {

		portfolioName.setText(this.portfolio.getPortfolioName());
		portfolioFiatCurrency.setText(this.portfolio.getPortfolioFiatCurrency());
		totalSpent.setText(portfolioCalculator.getTotalSpent() + " " + this.portfolio.getPortfolioFiatCurrency());
		totalPortoflioValue.setText(portfolioCalculator.getTotalPortfolioValue() + " " + this.portfolio.getPortfolioFiatCurrency());
		profitOrLoss.setText(String.valueOf(portfolioCalculator.getProfitOrLoss()) + " " + this.portfolio.getPortfolioFiatCurrency());
		profitOrLossPercentage.setText(String.valueOf(portfolioCalculator.getProfitOrLossPercentage()) + " %");

		// change color of the statistic labels if profit or loss
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
		cryptoCurrencyList = transRepo.findDistinctCryptoCurrency(this.portfolio.getId());
	}

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

		mainApp.openPortfolioUpdateView(this.portfolio, Arrays.asList(this.portfolio.getPortfolioFiatCurrency()));
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
				ExportData exportData = new ExportData(this.portfolio, file);
				exportData.setup();
				exportData.exportData(getInsight());
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText(null);
				alert.getDialogPane().getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
		        alert.getDialogPane().getStyleClass().add("dialog-pane");
				alert.setContentText("Excel File wurde erfolgreich erstellt");
				alert.showAndWait();		

			} catch (IOException e) {
				//System.out.println("Problem with writing or closing of EXCEL sheet");
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
		  alert.setTitle("Portfolio loeschen");
		  alert.setHeaderText("Portfolio wirklich loeschen?");
		  alert.setContentText(null); 
		  alert.getDialogPane().getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
          alert.getDialogPane().getStyleClass().add("dialog-pane");
		  
		  Optional<ButtonType> result = alert.showAndWait();

		  if (result.get() == ButtonType.OK) {
		  transRepo.deleteAllTransactions(this.portfolio.getId());
		  updatePortfolio();
		  this.portfolio.setPortfolioFiatCurrency(defaultPortfolioFiatCurrency);
		  this.portfolio.setPortfolioName(defaultPortfolioname);
		  this.portfolio.setProfitOrLoss(defaultProfitOrLoss);
		  this.portfolio.setTotalPortfolioValue(defaultTotalPortfolioValue);
		  
		  portRepo.save(this.portfolio); 
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
		mainApp.openTransactionViewAdd(this.portfolio, null, fiatCurrencyList);

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
		
		updatePortfolio();
		
		insightTable.setItems(getInsight());
		
	}

	/**
	 * Opens the transaction view to edit a given transaction
	 *
	 * @author Pascal Rohner
	 */

	public void editTransaction() {
		Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
		mainApp.openTransactionViewAdd(this.portfolio, selectedTransaction, fiatCurrencyList);

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

		transaction.addAll(transRepo.findByPortfolio(this.portfolio));
		
		// formats all the total values withing the transactionList
		for (Transaction singleTransaction : transaction) {
			singleTransaction.setTotal(formatDouble(singleTransaction.getTotal()));
		}
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
			
			JSONObject unwrappedCryptoCurrencyPrices = this.onlineDataJSON.getJSONObject(cryptoCurrency);
			
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
			insightObject.setTotal(formatDouble(tempTotalAmount));

			// avarage price paied for a specific currency
			Double tempAvaragePrice = transRepo.sumUpTotalForCryptoCurrency(this.portfolio.getId(), cryptoCurrency) / tempSumNrbOfCoinsKauf;
			insightObject.setAveragePrice(formatDouble(tempAvaragePrice));

			// change in percent. calls a separate method to calculate the change
			insightObject.setChangePercent(formatDouble(getChangeinPercentage(tempTotalAmount, tempAvaragePrice * tempSumNbrOfCoins)));

			// change in absolute numbers (fiat)
			insightObject.setChangeFiat(formatDouble(tempTotalAmount - (tempAvaragePrice * tempSumNbrOfCoins)));


			insight.add(insightObject);

		}

		return insight;

	}
	
	/**
	 * Formats a double value by converting the double to a string and then parsing it as a double again.
	 * The formatter is used to format the values in the transaction and insight tables.
	 * 
	 * @param Double -> value to be formatted
	 * @return formatted value as double
	 * @author Pascal Rohner
	 */
	
	public Double formatDouble(Double value) {
		DecimalFormat df = new DecimalFormat("####0.00");
		String valueAsString = df.format(value);
		return Double.parseDouble(valueAsString);
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
		if(newValue == 0 && oldValue == 0) {
			return 0.0;
		}else {
			// New value - old value to get the absolute amount
			Double absolutAmount = newValue - oldValue;
			// Divide old value from the absolut amount to get the change in decimal
			Double changeInDecimal = absolutAmount / oldValue;
			// multiply change in decimal with 100 to get the change in percente
			Double changeInPercent = changeInDecimal * 100.0;
			return changeInPercent;
		}
		
	}

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		/**
		 * Set all pictures
		 * 
		 * @author Marc Steiner
		 */
		Image image1 = new Image("/images/dashboard_white.png");
		imageDashboard.setImage(image1); 
		
		Image image2 = new Image("/images/refresh_white.png");
		imageUpdate.setImage(image2);
		
		Image image3 = new Image("/images/export_white.png");
		imageExport.setImage(image3);
		
		Image image4 = new Image("/images/pen_white.png");
		imageEdit.setImage(image4);
		
		Image image5 = new Image("/images/trash_white.png");
		imageDelete.setImage(image5);
		
		
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
