package portfmgr;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import portfmgr.view.PortfolioViewController;

/**
 * This method sets the spring application context, creates a FXMLLoader and
 * start the JavaFx application.
 * 
 * The implementation of this class is based on the following guide:
 * https://better-coding.com/javafx-spring-boot-gradle-project-setup-guide-and-test/
 * 
 * @author pascal.rohner
 */
@SpringBootApplication
public class portfmgrApplication extends Application implements ApplicationContextAware {

	private ConfigurableApplicationContext springContext;
	private Stage primaryStage;
	private BorderPane rootLayout;

	/**
	 * Starts Spring context, creates FXMLLoader and ceeding the control of beans
	 * creation
	 */
	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(portfmgrApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/RootLayout.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		rootLayout = fxmlLoader.load();
	}

	/**
	 * Saves the primary stage and calls the two views (rootLayout and
	 * portfolioView)
	 * @param Stage
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		loadRootLayoutView();
		loadPortfolioView();
	}

	/**
	 * Opens the rootLayout within a new Scene and sets some stage settings
	 * 
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
	 * Loads the portfolioView within the rootLayout
	 */
	public void loadPortfolioView() {
		try {
			// Loads the portfolioView
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(portfmgrApplication.class.getResource("view/PortfolioView.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();

			// Gives the controller classe access to the mainApp in order to set the scene within the rootLayout.
			PortfolioViewController controller = loader.getController();
			controller.setMainApp(this);

			//Opens the portfolioView within the rootLayout
			rootLayout.setCenter(overview);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Method to return the rootLayout. Is use to open new scenes within the rootLayout
	 * @return rootLayout
	 */
	public BorderPane getRootLayout() {
		return rootLayout;
	}

	/**
	 * Stops the spring context when closing the JavaFx application
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