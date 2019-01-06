package portfmgr.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import portfmgr.portfmgrApplication;
import portfmgr.model.Transaction;
import portfmgr.model.TransactionRepository;


/**
 * 
 * @author pascal.rohner
 *
 */
@Controller
public class TransactionViewController implements Initializable {
	
	@Autowired
	TransactionRepository transRepo;
	
	private portfmgrApplication mainApp;
	private Stage dialogStage;
	
	@FXML
	private ChoiceBox moneytary;
	
	@FXML
	private ChoiceBox currency;
	
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
	
	
	
	 public void setDialogStage(Stage dialogStage) {
	        this.dialogStage = dialogStage;
	    }
	 
	 public void handleAddition() {
		 String tempMoneytary = moneytary.getValue().toString();
		 String tempCurrency = currency.getValue().toString();
		 String tempType = type.getValue().toString();
		 Double tempPrice = Double.valueOf(price.getText());
		 Double tempNumberOfCoins = Double.valueOf(numberOfCoins.getText());
		 Double tempFees = Double.valueOf(fees.getText());
		 Double tempTotal = 344.23;
		 
		 // es werden tempor�r immer die CHF-Werte gespeichert
		 
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
		 
		 // ist noch nicht ganz korrekt! Portfolio m�sste ja auch noch irgendwie mitgegeben werden!
		 mainApp.openPortfolioDetailView();
		
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
		
		currency.getItems().addAll("BTC", "ETH", "LTC");
		
		type.getItems().addAll("Kauf", "Verkauf");
		
	}

}
