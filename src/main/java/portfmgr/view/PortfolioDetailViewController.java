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

	public void openDashboard() {
		// still open
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
