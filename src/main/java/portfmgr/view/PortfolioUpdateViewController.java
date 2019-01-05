package portfmgr.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;

/**
 * This class represents the controller for the update view which is used to set name and currency of the actual portfolio.
 * 
 * @author Marc Steiner
 */

@Controller
public class PortfolioUpdateViewController implements Initializable {
	
	@SuppressWarnings("unused")
	private portfmgrApplication mainApp;
	
	private String portfolioName;
	private String portfolioCurrency;
	private Portfolio portfolio;
	
	@Autowired
	PortfolioRepository portRepo;
	
	@FXML
	private ComboBox<String> currencyBox;
	@FXML
	private TextField newPortfolioName;
	
	private Stage dialogStage;
	
    /**
     * Called when the user clicks cancel button.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
    
    /**
     * Called when the user clicks submit button. It writes the new portfolio name and currency direct into the database
     */
    @FXML
    private void handleSubmit() {
    	
        if (isInputValid()) {
        	
        	portfolio.setPortfolioName(newPortfolioName.getText());
        	portfolio.setPortfolioCurrency(currencyBox.getValue());        	
        	portRepo.save(portfolio);
        	dialogStage.close();
        	
        }
       
    }
    
    /*
     * Checks if the input for name and currency is correct, otherwise prompt alert
     */
    private boolean isInputValid() {
    	
    	if (newPortfolioName.getText() == null || newPortfolioName.getText().trim().isEmpty() || currencyBox.getValue() == null) {
 
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Eingabefehler");
            alert.setHeaderText("Portfolioname und Währung darf nicht leer sein");
            alert.showAndWait();
            
    		return false;
    	}
    	
    	return true;
    }
    
    /*
     * Set the possible currency list for the choiceBox and displays the actual name and currency from the portfolio.
     * @param currencyList: Is the possible selection of currencies (CHF, EUR...)
     * @param portfolio: the actual portfolio
     */
    public void setPortfolio (Portfolio portfolio, List<String> currencyList) {

    	this.portfolio = portfolio;
    	
    	currencyBox.setItems(FXCollections.observableArrayList(currencyList));
    	currencyBox.setValue(portfolio.getPortfolioCurrency());
    	newPortfolioName.setText(portfolio.getPortfolioName());
    	
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
		
	}

}
