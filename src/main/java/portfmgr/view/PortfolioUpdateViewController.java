package portfmgr.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;

/**
 * This class represents the controller for the update view which is used to change name and currency of the portfolio.
 * 
 * @author Marc Steiner
 */

@Controller
public class PortfolioUpdateViewController implements Initializable {
	
	private portfmgrApplication mainApp;
	private String portfolioName;
	private String portfolioCurrency;
	private Portfolio portfolio;
	ObservableList<String> currencyList = FXCollections.observableArrayList("CHF", "EUR", "USD");
	
	@Autowired
	PortfolioRepository portRepo;
	
	@FXML
	private Button submit;
	@FXML
	private Button cancel;
	@FXML
	private ComboBox<String> currencyBox;
	@FXML
	private TextField newPortfolioName;
	
	private Stage dialogStage;
	
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        //dialogStage.close();
    	System.out.println("CANCEL");
    }
    
    /**
     * Called when the user clicks submit. It writs the new portfolio name and currency direct into the database
     */
    @FXML
    private void handleSubmit() {
    	
        if (isInputValid()) {
        	System.out.println(newPortfolioName.getText());
        	System.out.println(currencyBox.getValue());
        	/*
        	 * Folgneder Code kann erst implementiert werden, wenn ein Portfolio vorhanden ist. sonst Exception
        	 * 
        	portfolio.setPortfolioName(newPortfolioName.getText()); 
        	portfolio.setPortfolioCurrency(currency.getValue());
        	portRepo.findById(portfolio.getId()). UPDATE???????;
        	
            dialogStage.close();
         */
        }
       
    }
    
    private boolean isInputValid() {
    	
    	if (newPortfolioName.getText() == null || newPortfolioName.getText().trim().isEmpty()) {
 
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText("Portfolioname darf nicht leer sein");
            alert.showAndWait();
            
    		return false;
    	}
    	
    	return true;
    }
    
    public void setPortfolio (Portfolio portfolio) {
    	/*
    	 * Zuerst muss Portoflio richtig implementiert werden. Bei der Auswahl des gewünschten Portfolios, muss
    	 * das Portfolio an den PortfolioDetailViewController mittels setActualPortoflio() übergeben werden.
    	 * Dieses Portfolio wird dann in ditPortfolio() im Code mainApp.openPortfolioUpdateView(portfolio); an den PortfolioUpdateViewController übergeben
    	 * Erst dann kann folgender Code ausgepfhrt werden, da sonst eine NULL POINTEr EXCEPTION geworfen wird
    	 * 
    	 * this.portfolio = portfolio;
    	 * portfolioName = portfolio.getPortfolioName();
    	 * portfolioCurrency = portfolio.getPortfolioCurrency();
    	 */
    	
    }
    
    public String getPortfolioName() {
    	return portfolioName;
    }
    
    public String getPortfolioCurrency() {
    	return portfolioCurrency;
    }
    
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		currencyBox.setItems(currencyList);
		
		// folgende zwei Werte "TEST" sollen mit portfolioName und portfolioCurrency ersetzt werden
		currencyBox.setValue("TEST");
		newPortfolioName.setText("TEST");
		
	}

}
