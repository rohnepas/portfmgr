package portfmgr.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;

@Controller
public class PortfolioUpdateViewController implements Initializable {
	
	private portfmgrApplication mainApp;
	private String newPortfolioName;
	private String portfolioCurrency;
	private Portfolio portfolio;
	
	@Autowired
	PortfolioRepository portRepo;
	
	@FXML
	private Button submit;
	@FXML
	private Button cancel;
	@FXML
	private ChoiceBox<String> currency;
	@FXML
	private TextField portfolioName;
	
	private Stage dialogStage;
	
    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    /**
     * Called when the user clicks submit.
     */
    @FXML
    private void handleSubmit() {
        if (isInputValid()) {
        	portfolio.setPortfolioName(portfolioName.getText()); 
        	portfolio.setPortfolioCurrency(currency.getValue());
        	
            dialogStage.close();
            
           // portRepo.findById(portfolio.getId()). UPDATE???????;
        }
    }
    
    public void setPortfolio (Portfolio portfolio) {
    	// Aktueller Werte von Name und Währung schon anzeigen in Pop-Ip
    	
    }
    
    public String getNewPortfolioName() {
    	return newPortfolioName;
    }
    
    public String getPortfolioCurrency() {
    	return portfolioCurrency;
    }
    
    private boolean isInputValid() {
    	String errorMessage = "";
  
    	if (portfolioName.getText() == null || currency.getValue() == null) {
    		errorMessage = "Please provide a valid input";
    		
    	}
    	
    	if (errorMessage.length() == 0) {
            return true;
        } 
    	else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            
            alert.showAndWait();
            return false;
        }
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;

	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
