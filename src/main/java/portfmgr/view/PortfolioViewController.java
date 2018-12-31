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
 * @author pascal.rohner
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
	private Label[] portfLabelArray;

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
	 * Sends the selected portfolio to the portfolioDetailView and 
	 * calls method from mainApp to open the portfolioView.
	 * 
	 * todo: send different portfolios. Currently the portfolio with the ID 1 is always sent.
	 */
	public void openPortfolioDetailView(MouseEvent event) {
		String source = event.getPickResult().getIntersectedNode().getId();
		System.out.println(source);
		Portfolio actualPortfolio = portfArray[0];
		portDetailViewController.setActualPortoflio(actualPortfolio);
		System.out.println(actualPortfolio);
		mainApp.openPortfolioDetailView();
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
	 * already exist. The names of the portfolios are also set.
	 * 
	 * @param portfmgrApplication
	 */
	@Override
	public void initialize(URL location, ResourceBundle resourceBundle) {

		int numberOfPortfolios = 4;

		if (portRepo.count() == 0) {
			portfArray = new Portfolio[4];

			for (int i = 0; i < numberOfPortfolios; i++) {
				portfArray[i] = new Portfolio();

				portRepo.save(portfArray[i]);
				System.out.println(portfArray[i]);
			}
		}
		
		Portfolio portfolio1 = portfArray[0];
		portfolio1.setPortfolioName("rohnestein");
		portfolio1.setPortfolioCurrency("CHF");
		portfArray[0] = portfolio1;
		
		if (portfLabelArray == null) {
			portfLabelArray = new Label[4];

			portfLabelArray[0] = labelPortfolio1;
			portfLabelArray[1] = labelPortfolio2;
			portfLabelArray[2] = labelPortfolio3;
			portfLabelArray[3] = labelPortfolio4;
		}

		for (Portfolio portfolio : portfArray) {
			for (int i = 0; i < portfLabelArray.length; i++) {
				if (portfolio.getPortfolioName() == null) {
					portfLabelArray[i].setText("leer");

				} else {
					portfLabelArray[i].setText(portfolio.getPortfolioName());
				}
			}

		}

	}

}
