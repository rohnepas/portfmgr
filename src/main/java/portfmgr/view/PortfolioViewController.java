package portfmgr.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private Label labelPortfolio2;

	@FXML
	private Label labelPortfolio3;

	@FXML
	private Label labelPortfolio4;

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
	 * @author: Pasccal Rohner, Marc Steiner
	 */
	@Override
	public void initialize(URL location, ResourceBundle resourceBundle) {

		int numberOfPortfolios = 4;

		if (portRepo.count() == 0) {
			portfArray = new Portfolio[4];

			for (int i = 0; i < numberOfPortfolios; i++) {
				portfArray[i] = new Portfolio();
				portfArray[i].setPortfolioName("leeres Portfolio");
				portfArray[i].setPortfolioFiatCurrency("CHF");

				portRepo.save(portfArray[i]);
			}

		}
		
		// Remark: When using a For-loop, the labels cannot be set correctly.
		//Therefore each label is called and set individually.

		labelPortfolio1.setText(portfArray[0].getPortfolioName());
		labelPortfolio2.setText(portfArray[1].getPortfolioName());
		labelPortfolio3.setText(portfArray[2].getPortfolioName());
		labelPortfolio4.setText(portfArray[3].getPortfolioName());

	}
}
