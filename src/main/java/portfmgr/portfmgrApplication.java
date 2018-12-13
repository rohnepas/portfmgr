package portfmgr;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Example:
 * https://better-coding.com/javafx-spring-boot-gradle-project-setup-guide-and-test/
 * https://github.com/gerardoprada/javafx-gradle-hello-world
 * 
 * 
 */
@SpringBootApplication
public class portfmgrApplication extends Application implements ApplicationContextAware {

	private ConfigurableApplicationContext springContext;
	private Parent rootNode;
	private Stage primaryStage;

	@Override
	public void init() throws Exception {
		springContext = SpringApplication.run(portfmgrApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource("view/RootLayout.fxml"));
		fxmlLoader.setControllerFactory(springContext::getBean);
		rootNode = fxmlLoader.load();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Portfolio Manager");
		primaryStage.setScene(new Scene(rootNode));

		primaryStage.centerOnScreen();
		primaryStage.show();
	}
	
	
	/**
     * Gibt die Primary Stage zurück.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }


	@Override
	public void stop() {
		springContext.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}

}