package application;
	
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	public static AgentManager agentManager = new AgentManager();
	public Controller controller = new Controller();
	public static Scene scene;
	
	/**
	 * 	MAIN
	 * @param argv
	 * @throws ControllerException
	 * @throws InterruptedException
	 */
	public static void main(String[] argv) throws ControllerException, InterruptedException  {
		launch(argv);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("ChatRoom.fxml"));
		scene = new Scene(root);
		scene.getStylesheets().add("application/application.css");
		
		primaryStage.setX(50);
		primaryStage.setY(7);
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Chat Area");
		primaryStage.setScene(scene);
		primaryStage.show();	
		Controller.console = (TextArea) Main.scene.lookup("#console");
		//	Closing operation for main GUI
		primaryStage.setOnCloseRequest(event -> {
			System.exit(0);
		});
	}
}