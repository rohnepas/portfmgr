package portfmgr;

import java.io.File;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import portfmgr.model.Portfolio;
import portfmgr.model.Transaction;
import portfmgr.view.PortfolioDetailViewController;
import portfmgr.view.PortfolioUpdateViewController;
import portfmgr.view.PortfolioViewController;
import portfmgr.view.TransactionViewController;

/**
 * This method sets the spring application context, creates a FXMLLoader and
 * start the JavaFx application.
 * 
 * The implementation of this class is based on the following guide:
 * https://better-coding.com/javafx-spring-boot-gradle-project-setup-guide-and-test/
 * 
 * @author Pascal Rohner & Marc Steiner
 */
@SpringBootApplication
public class portfmgrApplication extends Application implements ApplicationContextAware {

	private ConfigurableApplicationContext springContext;
	private Stage primaryStage;
	private BorderPane rootLayout;
 
	/**
	 * Sets the spring Context for the whole application and calls the
	 * setupLoader-Method and sets as rootLayout.
	 * 
	 * @author Pascal Rohner
	 * 
	 */
	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(portfmgrApplication.class);
		rootLayout = setupLoader("view/RootLayout.fxml").load();
	}

	/**
	 * Saves the primary stage and calls the two views (rootLayout and
	 * portfolioView)
	 * 
	 * @param Stage
	 * @author Pascal Rohner
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		loadRootLayoutView();
		openPortfolioView();
	}

	/**
	 * Sets up the loader and the spring context which is used in different parts of
	 * the mainApp.
	 * 
	 * @return FXMLLoader as loader
	 * @author Pascal Rohner
	 */
	public FXMLLoader setupLoader(String view) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(portfmgrApplication.class.getResource(view));
		loader.setControllerFactory(springContext::getBean);
		return loader;
	}

	/**
	 * Opens the rootLayout within a new Scene and sets some stage settings
	 * @author Pascal Rohner
	 */
	public void loadRootLayoutView() {
		try {
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Portfolio Manager");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets up and loads the PortfolioView
	 * @author Pascal Rohner
	 */
	public void openPortfolioView() {
		try {
			// Loads the portfolioView
			FXMLLoader loader = setupLoader("view/PortfolioView.fxml");
			AnchorPane portfolioView = (AnchorPane) loader.load();

			// Gives the controller classe access to the mainApp in order to set the scene
			// within the rootLayout.
			PortfolioViewController controller = loader.getController();
			controller.setMainApp(this);

			// Opens the portfolioView within the rootLayout
			rootLayout.setCenter(portfolioView);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets up and loads the DetailView
	 * @author Marc Steiner
	 */
	
	public void openPortfolioDetailView() {
		try {
			// Loads the portfolioDetailView
			FXMLLoader loader = setupLoader("view/PortfolioDetailView.fxml");
			AnchorPane portfolioDetailView = (AnchorPane) loader.load();

			// Gives the controller class access to the mainApp in order to set the scene
			// within the rootLayout.
			PortfolioDetailViewController controller = loader.getController();
			controller.setMainApp(this);

			// Opens the portfolioDetailView within the rootLayout
			rootLayout.setCenter(portfolioDetailView);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function is called by PortfolioDetailViewController and Opens a pop-up window to change portoflio name
	 * @param portfolio:  The portfolio which should be edit is passed from the PortfolioDetailViewController
	 * @author Marc Steiner
	 */
	public void openPortfolioUpdateView(Portfolio portfolio, List<String> fiatCurrencyList) {

		try {
			// Loads the portfolioDetailView
			FXMLLoader loader = setupLoader("view/PortfolioUpdateView.fxml");
			AnchorPane portfolioUpdateView = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Update Portfolio");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(portfolioUpdateView);
			dialogStage.setScene(scene);

			// Gives the controller class access to the mainApp in order to set the scene
			// within the rootLayout.
			PortfolioUpdateViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.setPortfolio(portfolio, fiatCurrencyList);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens a window which let the user decide where to save the export Excel file
	 * @author Marc Steiner
	 * @return file ("path" where to save the export file)
	 */
	public File openFileExportView() {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(primaryStage);
        return file;
	}
	
	
	/**
	 * Function is called by PortfolioDetailViewController and Opens a pop-up window to add/edit a transaction.
	 * @param portfolio:  The portfolio which should be edit is passed from the PortfolioDetailViewController
	 * @param transaction: The transaction which should be edit is passed from the PortfolioDetailViewController
	 * @param coinlistPath: Path to the coinlist is getting passed 
	 * @author Pascal Rohner und Marc Steiner
	 * @param currencyList 
	 * @param cryptocurrencyList 
	 */ 
	public void openTransactionViewAdd(Portfolio portfolio, Transaction transaction, String coinlistPath, List<String> currencyList) {
		try {
			FXMLLoader loader = setupLoader("view/TransactionView.fxml");
			AnchorPane transactionView = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Update Transaction");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(transactionView);
			dialogStage.setScene(scene);

			// Gives the controller class access to the mainApp in order to set the scene
			// within the rootLayout.
			TransactionViewController controller = loader.getController();
			controller.setMainApp(this, currencyList);
			controller.setDialogStage(dialogStage);
			controller.setPortfolio(portfolio);
			controller.setTransaction(transaction);
			controller.setCoinListPath(coinlistPath);
			

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Stops the spring context when closing the JavaFx application
	 *
	 *@author Pascal Rohner
	 */
	@Override
	public void stop() {
		springContext.stop();
	}

	/**
	 * Starts the JavaFx application
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}

}