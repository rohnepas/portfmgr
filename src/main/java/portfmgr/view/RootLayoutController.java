package portfmgr.view;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

@Controller
public class RootLayoutController implements Initializable {

	private Stage primaryStage;
	

	
    @FXML private Button importBtn;
    @FXML private Button exportBtn;
    @FXML private Label label;
    
    
    public void openPortfolioModal() {
    	
    }

    
    
    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {}


}

	
	
	



