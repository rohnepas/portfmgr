package portfmgr.view;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import portfmgr.portfmgrApplication;
import portfmgr.model.Portfolio;
import portfmgr.model.PortfolioRepository;
import portfmgr.model.TransactionRepository;

/**
 * This class represents the link between the portfolio model and the portfolio
 * view.
 * 
 * @author Pascal Rohner
 */

@Controller
public class PortfolioViewController implements Initializable {

	private String defaultPortfolioname = "leeres Portfolio";
	private String defaultPortfolioFiatCurrency = "CHF";
	private String defaultProfitOrLoss = "-";
	private String defaultTotalPortfolioValue = "-";

	// Gets the repository instance injected and uses it.
	@Autowired
	PortfolioRepository portRepo;

	@Autowired
	TransactionRepository transRepo;

	@Autowired
	PortfolioDetailViewController portDetailViewController;

	// Reference to the main app (portfmgrApplication)
	private portfmgrApplication mainApp;
	private Portfolio[] portfArray;

	@FXML
	private Button openPortfolio;

	@FXML
	private Button setName;

	@FXML
	private Button addTransaction;

	@FXML
	private Button deletePortfolio;

	@FXML
	private Rectangle portfolio1;

	@FXML
	private Rectangle portfolio2;

	@FXML
	private Rectangle portfolio3;

	@FXML
	private Rectangle portfolio4;

	@FXML
	private Label labelPortfolio1;
	@FXML
	private Label labelPortfolio1TotValue;
	@FXML
	private Label labelPortfolio1profitOrLoss;

	@FXML
	private Label labelPortfolio2;
	@FXML
	private Label labelPortfolio2TotValue;
	@FXML
	private Label labelPortfolio2profitOrLoss;

	@FXML
	private Label labelPortfolio3;
	@FXML
	private Label labelPortfolio3TotValue;
	@FXML
	private Label labelPortfolio3profitOrLoss;

	@FXML
	private Label labelPortfolio4;
	@FXML
	private Label labelPortfolio4TotValue;
	@FXML
	private Label labelPortfolio4profitOrLoss;

	/**
	 * Delete all transactions and setback the portfolio name and fiat currency
	 * 
	 * @author Marc Steiner
	 */
	@FXML
	public void handleDeletePortfolio() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Portfolio loeschen");
		alert.setHeaderText("ALLE Portfolios wirklich loeschen?");
		alert.setContentText(null);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
		alert.getDialogPane().getStyleClass().add("dialog-pane");

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			int i = 0;
			Iterable<Portfolio> portfolioList = portRepo.findAll();

			for (Portfolio portfolio : portfolioList) {
				portfArray[i] = portfolio;
				portfArray[i].setPortfolioFiatCurrency(defaultPortfolioFiatCurrency);
				portfArray[i].setPortfolioName(defaultPortfolioname);
				portfArray[i].setProfitOrLoss(defaultProfitOrLoss);
				portfArray[i].setTotalPortfolioValue(defaultTotalPortfolioValue);
				portRepo.save(portfArray[i]);
				transRepo.deleteAllTransactions(portfArray[i].getId());
				i++;
			}
			mainApp.openPortfolioView();
		}
	}

	/**
	 * Sends the selected portfolio to the portfolioDetailView and calls method from
	 * mainApp to open the portfolioView.
	 * 
	 * @param MouseEvent todo: send different portfolios. Currently the portfolio
	 *                   with the ID 1 is always sent.
	 */
	public void openPortfolioDetailView(MouseEvent event) {
		String source = event.getPickResult().getIntersectedNode().getId();
		Portfolio actualPortfolio = portfArray[getSelectedPortfolioId(source)];
		portDetailViewController.setActualPortoflio(actualPortfolio);
		mainApp.openPortfolioDetailView();
	}

	/**
	 * Returns the index of the portfolio that must be passed when ordering the
	 * Portfolio Detail View.
	 * 
	 * @param portfolioLabeId as a string
	 * @return index of the portfolio in the list of portfolios (portfArray) as an
	 *         int
	 */
	public int getSelectedPortfolioId(String portfolioLabeId) {
		String tempString = portfolioLabeId;
		// takes the last character from the string
		int tempInt = Integer.parseInt(tempString.substring(tempString.length() - 1));
		// subtracts minus 1 to get the index.
		int listIndex = tempInt - 1;
		return listIndex;
	}

	/**
	 * Initializes the MainApp variable and is called by the MainApp itself.
	 * 
	 * @param portfmgrApplication
	 */
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Creates four portfolios and stores them in the database if they do not
	 * already exist, the names of the portfolios are set and the loadPortfolio
	 * Method is called
	 * 
	 * @author: Pasccal Rohner, Marc Steiner
	 */
	@Override
	public void initialize(URL location, ResourceBundle resourceBundle) {

		int numberOfPortfolios = 4;

		if (portRepo.count() == 0) {
			// Makes an Array of size 4 with type of content portfolio
			portfArray = new Portfolio[4];

			for (int i = 0; i < numberOfPortfolios; i++) {
				portfArray[i] = new Portfolio();
				portfArray[i].setPortfolioName(defaultPortfolioname);
				portfArray[i].setPortfolioFiatCurrency(defaultPortfolioFiatCurrency);
				portfArray[i].setProfitOrLoss(defaultProfitOrLoss);
				portfArray[i].setTotalPortfolioValue(defaultTotalPortfolioValue);
				// portfArray[i].setProfit(true);

				portRepo.save(portfArray[i]);
			}

		} else {
			// Makes an Array of size 4 with type of content portfolio
			int i = 0;
			portfArray = new Portfolio[4];
			Iterable<Portfolio> portfolioList = portRepo.findAll();

			for (Portfolio portfolio : portfolioList) {
				portfArray[i] = portfolio;
				i++;
			}

			// Sort portfArray by portfolio ID

			Arrays.sort(portfArray, new Comparator<Portfolio>() {
				@Override
				public int compare(Portfolio p1, Portfolio p2) {
					return p1.getId().compareTo(p2.getId());
				}
			});

		}

		// Remark: When using a For-loop, the labels cannot be set correctly.
		// Therefore each label is called and set individually.

		
		  labelPortfolio1.setText(portfArray[0].getPortfolioName());
		  labelPortfolio1TotValue.setText(portfArray[0].getTotalPortfolioValue() + " " + portfArray[0].getPortfolioFiatCurrency());
		  labelPortfolio1profitOrLoss.setText(portfArray[0].getProfitOrLoss() + " " + portfArray[0].getPortfolioFiatCurrency());
		
		  if (portfArray[0].getProfitOrLoss().indexOf('-') >=0) {
		  
		  labelPortfolio1profitOrLoss.setStyle("-fx-background-color: red");
		  
		  } else { 
			  labelPortfolio1profitOrLoss.setStyle("-fx-background-color:green");
			}
		 
		  labelPortfolio2.setText(portfArray[1].getPortfolioName());
		  labelPortfolio2TotValue.setText(portfArray[1].getTotalPortfolioValue() + " " + portfArray[1].getPortfolioFiatCurrency());
		  labelPortfolio2profitOrLoss.setText(portfArray[1].getProfitOrLoss() + " " + portfArray[1].getPortfolioFiatCurrency());

		  if (portfArray[1].getProfitOrLoss().indexOf('-') >=0) {
		  
		  labelPortfolio2profitOrLoss.setStyle("-fx-background-color: red");
		  
		  } else { 
			  labelPortfolio2profitOrLoss.setStyle("-fx-background-color:green");
			}
		  
		  labelPortfolio3.setText(portfArray[2].getPortfolioName());
		  labelPortfolio3TotValue.setText(portfArray[2].getTotalPortfolioValue() + " " + portfArray[2].getPortfolioFiatCurrency());
		  labelPortfolio3profitOrLoss.setText(portfArray[2].getProfitOrLoss() + " " + portfArray[2].getPortfolioFiatCurrency());

		  if (portfArray[2].getProfitOrLoss().indexOf('-') >=0) {
		  
		  labelPortfolio3profitOrLoss.setStyle("-fx-background-color: red");
		  
		  } else { 
			  labelPortfolio3profitOrLoss.setStyle("-fx-background-color:green");
			}
		  
		  labelPortfolio4.setText(portfArray[3].getPortfolioName());
		  labelPortfolio4TotValue.setText(portfArray[3].getTotalPortfolioValue() + " " + portfArray[3].getPortfolioFiatCurrency());
		  labelPortfolio4profitOrLoss.setText(portfArray[3].getProfitOrLoss() + " " + portfArray[3].getPortfolioFiatCurrency());

		  if (portfArray[3].getProfitOrLoss().indexOf('-') >=0) {
		  
		  labelPortfolio4profitOrLoss.setStyle("-fx-background-color: red");
		  
		  } else { 
			  labelPortfolio4profitOrLoss.setStyle("-fx-background-color:green");
			}
		 
	}
}
