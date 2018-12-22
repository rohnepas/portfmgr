package portfmgr.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import portfmgr.portfmgrApplication;

public class PortfolioDetailViewController {

	// Reference to the main app (portfmgrApplication)
	private portfmgrApplication mainApp;
	
	@FXML
	private Button openDashboard;

	/**
	 * Opens the portfolioView within the rootLayout
	 */
	public void openDashboard() {
		try {
			// Loads the portfolioView
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(portfmgrApplication.class.getResource("view/PortfolioView.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();

			// Gives the controller class access to the mainApp in order to set the scene
			// within the rootLayout.
			PortfolioViewController controller = loader.getController();
			controller.setMainApp(mainApp);

			// Opens the portfolioView within the rootLayout
			mainApp.getRootLayout().setCenter(overview);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Initializes the MainApp variable and is called by the MainApp itself.
	 * 
	 * @param portfmgrApplication
	 */
	public void setMainApp(portfmgrApplication mainApp) {
		this.mainApp = mainApp;

	}

}
