package portfmgr.view;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import portfmgr.portfmgrApplication;
import portfmgr.model.OnlineCourseQuery;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;
import portfmgr.model.Transaction;
import portfmgr.model.TransactionRepository;

/**
 * This class represents the controller for the transaction view which is used
 * to add, delete and edit transactions.
 * 
 * @author Pascal Rohner, Marc Steiner
 *
 */
@Controller
public class TransactionViewController implements Initializable {

	@Autowired
	TransactionRepository transRepo;

	@Autowired
	PortfolioRepository portRepo;

	private portfmgrApplication mainApp;
	private Transaction transaction;
	private Portfolio portfolio;
	private Stage dialogStage;
	private String coinlistPath;
	private JSONObject onlineDataJSON;
	private List<String> currencyList;
	private List<String> cryptocurrencyList;

	@FXML
	private ChoiceBox moneytary;

	@FXML
	private ChoiceBox type;

	@FXML
	private TextField price;

	@FXML
	private TextField numberOfCoins;

	@FXML
	private TextField fees;
	@FXML
	private Button save;

	@FXML
	private Button cancel;

	@FXML
	private TextField cryptoCurrency;

 
	/**
	 * Method is called when "save" is clicked in the transaction view. The method
	 * performs a validation and saves the portfolio to the database or updates the
	 * portfolio.
	 * 
	 * @author: Pascal Rohner, Marc Steiner
	 */
	public void handleAddition() {
		String tempMoneytary = moneytary.getValue().toString();
		String tempCurrency = cryptoCurrency.getText().toUpperCase();

		tempCurrency = validateCryptoInput(tempCurrency);

		// Simple validation if the entered Crypto Currency exists.
		if (tempCurrency == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Eingabefehler");
			alert.setHeaderText("Kryptowährung existiert nicht - bitte korrigieren");
			alert.showAndWait();
		}

		else {
			
			JSONObject cryptocurrencyData = getCryptoCurrencyData(tempCurrency);
			// Calls a method for calculating the total
			Double tempTotal = calculateTotal(type.getValue().toString(), Double.valueOf(price.getText()),
					Double.valueOf(numberOfCoins.getText()), Double.valueOf(fees.getText()));

			if (transaction == null) {
				saveTransaction(cryptocurrencyData, moneytary.getValue().toString(), tempCurrency, type.getValue().toString(),
						Double.valueOf(numberOfCoins.getText()),
						Double.valueOf(fees.getText()), tempTotal);
			} else {
				updateTransaction(transaction);
			}
			mainApp.openPortfolioDetailView();

			/*
			 * Sets the transaction to zero to know whether to add a new transaction or
			 * update an existing transaction when opening next time the dialog.
			 */
			this.transaction = null;

		}
	}
	
	/**
	 * Calls the API for crypto currencies and give back the values only for one specific crypto currency
	 * @return values (JSON Object with fiat rate of defined crypto currency)
	 * @param tempCurrency  (crypto currency symbol for that the fiat rate will be called)
	 * @author Marc Steiner
	 * 
	 */
	public JSONObject getCryptoCurrencyData(String tempCurrency) {
		
		//Convert String to needed List>String>
		List<String> symbol = Arrays.asList(tempCurrency);
		OnlineCourseQuery query = new OnlineCourseQuery(symbol, currencyList);

		try {
			//Get the value for the specific crypto currency
			JSONObject values = query.getOnlineCourseData().getJSONObject(tempCurrency);
			return values;
		} catch (IOException e) {
			System.out.println("Problem within getOnlineCourseData()");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * This method validate the user input of the crypto currencies based on JSON
	 * list from http://www.cryptocompare.com
	 * 
	 * @author Marc Steiner
	 */
	public String validateCryptoInput(String input) {
		File file = new File(coinlistPath);
		String content;

		try {
			content = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
			JSONObject contentObj = new JSONObject(content);
			JSONObject cryptoCurrencyList = contentObj.getJSONObject("Data");
			JSONArray keys = cryptoCurrencyList.names();

			for (int i = 0; i < keys.length(); ++i) {
				String key = keys.getString(i);

				if (key.equalsIgnoreCase(input)) {
					return key;
				}
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Closes the dialog if the cancel button is clicked
	 * 
	 * @author Pascal Rohner
	 */
	public void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Calculates the total amount spent on a transaction.
	 * 
	 * @param type of the transaction, price paid, number of coins bought, fees spent
	 * @author Pascal Rohner
	 * 
	 */

	public Double calculateTotal(String type, Double price, Double numberOfCoins, Double fees) {
		if (type == "Kauf") {
			return (price * numberOfCoins) + fees;
		} else {
			return (price * numberOfCoins) - fees;
		}
	}

	/**
	 * Creates a new transaction and saves it in the database. It save not only the value for the specified currency
	 * it also saves for all other fiat currencies so that switching between different 
	 * portfolio currencies is possible.
	 * 
	 * @param moneytary (= Währung), currency bought, type of the transaction (Kauf
	 *                  oder Verkauf), price paid, number of coins bought, fees
	 *                  spent and total amout of money spent for the transaction
	 * @author Pascal Rohner und Marc Steiner
	 * 
	 */

	public void saveTransaction(JSONObject cryptocurrencyData, String moneytary, String currency, String type, Double numberOfCoins,
			Double fees, Double total) {
		
		Transaction transaction = new Transaction();
		transaction.setCurrency(currency);
		transaction.setMoneytary(moneytary);
		transaction.setType(type);
		transaction.setNumberOfCoins(numberOfCoins);
		transaction.setTransactionDate(LocalDate.now());
		transaction.setPortfolio(this.portfolio);
		transaction.setTotal(total);
		transaction.setPriceCHF(cryptocurrencyData.getDouble("CHF"));
		transaction.setPriceUSD(cryptocurrencyData.getDouble("USD"));
		transaction.setPriceEUR(cryptocurrencyData.getDouble("EUR"));
		

		if (moneytary == "CHF") {
			transaction.setPriceCHF(cryptocurrencyData.getDouble("CHF"));
			transaction.setFeesCHF(fees);
		}

		if (moneytary == "EUR") {
			transaction.setPriceCHF(cryptocurrencyData.getDouble("EUR"));
			transaction.setFeesEUR(fees);

		}

		if (moneytary == "USD") {
			transaction.setPriceCHF(cryptocurrencyData.getDouble("USD"));
			transaction.setFeesUSD(fees);

		}

		transRepo.save(transaction);
		dialogStage.close();
	}

	/**
	 * Saves the updates transaction into the database
	 * 
	 * @param transaction
	 * @author Pascal Rohner
	 */

	public void updateTransaction(Transaction transaction) {
		transRepo.save(transaction);
		dialogStage.close();
	}

	/**
	 * Fills in the transaction view if the transaction is not null. In case the
	 * transaction is null, a new transaction is added or the user has not selected
	 * a transaction to update.
	 *
	 * @param transaction
	 * @author Pascal Rohner
	 */

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;

		if (transaction != null) {
			moneytary.setValue(transaction.getMoneytary());
			cryptoCurrency.setText(transaction.getCurrency());
			type.setValue(transaction.getType());
			price.setText(String.valueOf(transaction.getPriceCHF()));
			numberOfCoins.setText(String.valueOf(transaction.getNumberOfCoins()));
			fees.setText(String.valueOf(transaction.getFeesCHF()));
		}

	}
	
	

	/**
	 * Sets the portfolio so that when a transaction is stored, it can be assigned
	 * to the portfolio.
	 * 
	 * @param portfolio
	 * @author Pascal Rohner
	 */

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;

	}

	/**
	 * 
	 * @author Marc Steiner
	 * 
	 */
	public void setCoinListPath(String coinlistPath) {
		this.coinlistPath = coinlistPath;
	}

	/**
	 * Sets the MainApp for calling a method.
	 * 
	 * @param mainApp
	 * @author Pascal Rohner und Marc Steiner
	 */
	public void setMainApp(portfmgrApplication mainApp, List<String> currencyList) {
		this.mainApp = mainApp;
		this.currencyList = currencyList;
		moneytary.getItems().addAll(currencyList);
		type.getItems().addAll("Kauf", "Verkauf");
	}

	/**
	 * Sets the dialog stage for calling methods on it.
	 * @author Pascal Rohner
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		

	}
}
