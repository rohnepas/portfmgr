package portfmgr.view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import portfmgr.portfmgrApplication;
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

	@FXML
	private Label fiatCurrency;

	@FXML
	private TextField cryptoCurrency;

	@FXML
	private ComboBox<String> type;

	@FXML
	private Label date;
	
	@FXML
	private DatePicker datePicker;
	
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

	/**
	 * Method is called when "save" is clicked in the transaction view. The method
	 * performs a validation and saves the portfolio to the database or updates the
	 * portfolio.
	 *
	 * @author: Pascal Rohner, Marc Steiner 
	 */
	public void handleAddition() {
		String tempCryptoCurrency = cryptoCurrency.getText().toUpperCase();
		tempCryptoCurrency = validateCryptoInput(tempCryptoCurrency);
		
		// is set to false in case a textField is empty 
		boolean inputComplete = true;
		List<TextField> labelList = new ArrayList<>();
		
		// list of textFields which should be validate for input
		labelList.add(price);
		labelList.add(numberOfCoins);
		labelList.add(fees);
		
		// validates if the textField input is empty
		for (TextField textField : labelList) {
			
			if (textField.getText().trim().isEmpty()) {
				inputComplete = false;
			}
			
			if (validateFieldInput(textField.getText()) == false){
				inputComplete = false;
			};
		}
		
		if (inputComplete == false) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Eingabefehler");
			alert.getDialogPane().getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
			alert.getDialogPane().getStyleClass().add("dialog-pane");
			alert.setHeaderText("Eingabe ist unvollständig oder ungültig");
			alert.showAndWait();
		} else if (tempCryptoCurrency == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Eingabefehler");
			alert.getDialogPane().getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
	        alert.getDialogPane().getStyleClass().add("dialog-pane");
			alert.setHeaderText("Symbol für Coin existiert nicht");
			alert.showAndWait();
		}
		else {
			// if no type is entered, "Kauf" is automatically set
			if (type.getValue() == null){
				type.setValue("Kauf");
			}
			
			if (datePicker.getValue() == null) {
				datePicker.setValue(LocalDate.now());
			}

			// Calls a method for calculating the total
			Double tempTotal = calculateTotal(type.getValue().toString(), Double.valueOf(price.getText()),
					Double.valueOf(numberOfCoins.getText()), Double.valueOf(fees.getText()));

			Double tempNbrOfCoins = Double.valueOf(numberOfCoins.getText());

			
			if (type.getValue().toString() == "Verkauf") {
				tempNbrOfCoins = tempNbrOfCoins * -1;
			}

			if (transaction == null) {
				saveTransaction(Double.valueOf(price.getText()), fiatCurrency.getText(), datePicker.getValue(), tempCryptoCurrency,
						type.getValue().toString(), tempNbrOfCoins, Double.valueOf(fees.getText()), tempTotal);
			} else {
				updateTransaction(transaction, Double.valueOf(price.getText()), fiatCurrency.getText(), datePicker.getValue(),
						tempCryptoCurrency, type.getValue().toString(), tempNbrOfCoins, Double.valueOf(fees.getText()),
						tempTotal);
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
	 * Checks whether the input in the text fields is a number. 
	 * The number can have one comma and four decimal places.
	 * @param input from the textField as String
	 * @return boolean true if input is valid
	 * 
	 * @author Pascal Rohner
	 */
	public Boolean validateFieldInput(String input) {
		Boolean valid = false;
	    if (input.matches("^[0-9]{1,8}([.][0-9]{1,4})?$")) {
	    	
	    	valid = true;
	    }
	      
	    return valid;
	}
	
	
	/*
	 * This method validate the user input of the crypto currencies based on JSON
	 * list from http://www.cryptocompare.com. This JSON list is saved in project folder
	 * src/main/java/coinlist/coinlist.json = coinlistPath
	 *
	 * @author Marc Steiner
	 */
	public String validateCryptoInput(String input) {
		File file = new File(this.coinlistPath);
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
		} catch (IOException e) {
			String nameofCurrMethod = new Object(){}.getClass().getEnclosingMethod().getName(); 
			System.out.println("Problem with Files.readAllBytes in method " + nameofCurrMethod);
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
	 * Calculates the total amount spent on a transaction. In case of a selling the
	 * amount is converted to a negative double.
	 *
	 * @param type of the transaction, price paid, number of coins bought, fees
	 *             spent
	 * @author Pascal Rohner
	 *
	 */

	public Double calculateTotal(String type, Double price, Double numberOfCoins, Double fees) {
		if (type == "Kauf") {
			return (price * numberOfCoins) + fees;
		} else {
			return ((price * numberOfCoins) + fees) * -1;
		}
	}

	/**
	 * Creates a new transaction and saves it in the database.
	 *
	 * @param moneytary (= Wï¿½hrung), currency bought, type of the transaction
	 *                  (Kauf oder Verkauf), price paid, number of coins bought,
	 *                  fees spent and total amout of money spent for the
	 *                  transaction
	 * @author Pascal Rohner und Marc Steiner
	 *
	 */

	public void saveTransaction(Double price, String fiatCurrency, LocalDate date, String cryptoCurrency, String type,
			Double numberOfCoins, Double fees, Double total) {

		Transaction transaction = new Transaction();
		transaction.setCryptoCurrency(cryptoCurrency);
		transaction.setFiatCurrency(fiatCurrency);
		transaction.setNumberOfCoins(numberOfCoins);
		transaction.setPrice(price);
		transaction.setTransactionDate(date);
		transaction.setType(type);
		transaction.setFees(fees);
		transaction.setTotal(total);
		transaction.setPortfolio(this.portfolio);

		transRepo.save(transaction);

		dialogStage.close();
	}

	/**
	 * Saves the updates transaction into the database
	 *
	 * @param transaction
	 * @author Pascal Rohner
	 */

	public void updateTransaction(Transaction transaction, Double price, String fiatCurrency, LocalDate date, String cryptoCurrency,
			String type, Double numberOfCoins, Double fees, Double total) {
		transaction.setPrice(price);
		transaction.setFiatCurrency(fiatCurrency);
		transaction.setCryptoCurrency(cryptoCurrency);
		transaction.setType(type);
		transaction.setTransactionDate(date);
		transaction.setNumberOfCoins(numberOfCoins);
		transaction.setFees(fees);
		transaction.setTotal(total);
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
			fiatCurrency.setText(transaction.getFiatCurrency());
			cryptoCurrency.setText(transaction.getCryptoCurrency());
			type.setValue(transaction.getType());
			datePicker.setValue(transaction.getTransactionDate());
			price.setText(String.valueOf(transaction.getPrice()));
			numberOfCoins.setText(String.valueOf(transaction.getNumberOfCoins()));
			fees.setText(String.valueOf(transaction.getFees()));
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
		fiatCurrency.setText(portfolio.getPortfolioFiatCurrency());

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
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;
		type.setItems(FXCollections.observableArrayList(Arrays.asList("Kauf", "Verkauf")));
	}

	/**
	 * Sets the dialog stage for calling methods on it.
	 * 
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
