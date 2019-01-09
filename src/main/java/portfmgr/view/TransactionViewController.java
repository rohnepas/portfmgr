package portfmgr.view;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import portfmgr.portfmgrApplication;
import portfmgr.model.Transaction;
import portfmgr.model.TransactionRepository;

/**
 * 
 * @author pascal.rohner, Marc Steiner
 *
 */
@Controller
public class TransactionViewController implements Initializable {
	
	@Autowired
	TransactionRepository transRepo;
	
	private portfmgrApplication mainApp;
	private Stage dialogStage;
	private String coinlistPath;
	
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
	private Label total;
	
	@FXML
	private Button save;
	
	@FXML
	private Button cancel;
	
	@FXML
	private TextField cryptoCurrency;
	
	
	
	 public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
	
	 /*
	  * @author: pascal.rohner, Marc Steiner
	  */
	 public void handleAddition() {
		 String tempMoneytary = moneytary.getValue().toString();
		 String tempCurrency = cryptoCurrency.getText().toUpperCase();
		 
		 tempCurrency = validateCryptoInput(tempCurrency);
		 
		 if (tempCurrency == null) {
			 Alert alert = new Alert(AlertType.ERROR);
	         alert.initOwner(dialogStage);
	         alert.setTitle("Eingabefehler");
	         alert.setHeaderText("Kryptowährung existiert nicht - bitte korrigieren");
	         alert.showAndWait();
		 }
		 
		 else {
		 
		 String tempType = type.getValue().toString();
		 Double tempPrice = Double.valueOf(price.getText());
		 Double tempNumberOfCoins = Double.valueOf(numberOfCoins.getText());
		 Double tempFees = Double.valueOf(fees.getText());
		 Double tempTotal = 344.23;
		 
		 // es werden temporï¿½r immer die CHF-Werte gespeichert
		 
		 Transaction transaction = new Transaction();
		 transaction.setCurrency("BTC");
		 transaction.setType(tempType);
		 transaction.setPriceCHF(tempPrice);
		 transaction.setNumberOfCoins(tempNumberOfCoins);
		 transaction.setFeesCHF(tempFees);
		 transaction.setTransactionDate(LocalDate.now());
		 transaction.setTotal(tempTotal);
		 
		 // Portfolio muss noch gesetzt werden!
		 transRepo.save(transaction);
		 dialogStage.close();
		 
		 // ist noch nicht ganz korrekt! Portfolio mï¿½sste ja auch noch irgendwie mitgegeben werden!
		 mainApp.openPortfolioDetailView();
		 
		 }
		
	 }
	 
	 /*
	  * This method validate the user input of the crypto currencies based on JSON list from http://www.cryptocompare.com
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
	 
	 public void handleCancel() {
		 dialogStage.close();
	 }
	 
	 public void setMainApp(portfmgrApplication mainApp) {
			this.mainApp = mainApp;
		}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		moneytary.getItems().addAll("CHF", "USD", "EUR", "BTC");
		type.getItems().addAll("Kauf", "Verkauf");
		
	}

	public void setCoinListPath(String coinlistPath) {
		this.coinlistPath = coinlistPath;
	}

}
