package application;

import java.io.IOException;
import java.util.ArrayList;

import jade.core.Agent;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AgentStage extends Stage {
	
	public MyAgent agent;
	Scene scene;
	Button sendMessage;
	Button killMe;
	Button clear;
	CheckBox isBroadcast;	
	TextArea message;
	TextArea receivedMessage;
	Text agentNameField;
	public ChoiceBox<MyAgent> receivers;
	
	public static int positionX = 50;
	public static int positionY = 240;
	static ArrayList<MyAgent> agentsList = null; 
	
	/**
	 * Constructor
	 * @param agentName
	 * @throws IOException
	 * @throws InterruptedException
	 */
	AgentStage(String agentName) throws IOException, InterruptedException {
		//	Initializing graphical components
		initComponent(agentName);
		//	Initializing functions
		initFunctions(agentName);
	}
	
	/**
	 * 
	 * @param agentName
	 * @throws IOException
	 * @throws InterruptedException
	 */
	void initComponent(String agentName) throws IOException, InterruptedException {
		 //	Waiting for JADE to load
		Thread.sleep(1000);
		// Initializing agent of this GUI
		this.agent = Main.agentManager.getMyAgent(agentName);
		//	Set GUI
		Parent root = FXMLLoader.load(getClass().getResource("Messenger.fxml"));
		scene 		= new Scene(root);
		
		scene.getStylesheets().add("application/application.css");
		
		super.initStyle(StageStyle.DECORATED);
		super.setResizable(false);
		super.setTitle("Chat Box (" + agentName + ")");
		super.setScene(scene);
		super.setX(positionX);
		positionX += 360;
		super.setY(positionY);
		super.show();
		
		if(agentsList == null)
			agentsList = new ArrayList<MyAgent>(AgentManager.agents.values());
		
		//	Set title (agent name)
		this.agentNameField = (Text) 	scene.lookup("#agentName");
		//	Received Message
		receivedMessage 	= (TextArea) scene.lookup("#receivedMessage");
		//	Message to send
		message 			= (TextArea) scene.lookup("#messageToSend");
		//	Send Button
		sendMessage 		= (Button) scene.lookup("#sendMessage");
		//	Clearing received messages
		clear 				= (Button) scene.lookup("#clear");
		//	Kill agent & close chat window Button
		killMe 				= (Button) scene.lookup("#killMe");
		//	Receivers choice box (choose to whom, message will be sent)
		receivers 			= (ChoiceBox) scene.lookup("#receivers");
		//	Type of message
		isBroadcast			= (CheckBox) scene.lookup("#broadcast");
	}
	
	
	//	
	void initFunctions(String agentName) {
		//
		this.agentNameField.setText(agentName);
		this.agent.receivedMessageArea = receivedMessage;
		//	Sending function
		sendMessage.setOnAction(new EventHandler<ActionEvent>() {
			//	Send message
			@Override
			public void handle(ActionEvent event) {
				if(!message.getText().isEmpty()) {
					if(isBroadcast.isSelected())
						for(MyAgent destination : AgentManager.agents.values())
							agent.SendMessage(message.getText(), destination);
					else
						try {
						agent.SendMessage(message.getText(), receivers.getValue());
						} catch(Exception e) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Message error");
							alert.setHeaderText("Cannot send message");
							alert.setContentText("You must at least chose an agent to send the message to!");
							alert.showAndWait();
						}
					message.clear();
				}
			}
		});
		//	Clearing received messages
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				receivedMessage.clear();
		}});
		//	Kill agent & close chat window Button
		killMe.setOnAction(new EventHandler<ActionEvent>() {
			//	Send message
			@Override
			public void handle(ActionEvent event) {
				try {
					killAgent(agentName);
				} catch (ControllerException e) {
					e.printStackTrace();
				}
		}});
		
		receivers.setItems(AgentManager.myAgents);
		
		//	Closing operation
		this.setOnCloseRequest(event -> {
			try {
				killAgent(agentName);
			} catch (ControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	
	/**
	 * Killing agent
	 * @param agentName
	 * @throws StaleProxyException
	 * @throws ControllerException
	 */
	void killAgent(String agentName) throws StaleProxyException, ControllerException {
		//	Remove agent from list 
		AgentManager.agents.remove(agentName);
		AgentManager.myAgents.remove(Main.agentManager.getMyAgentObservable(agentName));
		
		//	Kill agent
		Main.agentManager.getAgentController(agentNameField.getText()).kill();
		//	Close agent Messenger
		close();
		//	Reduce agents number
		AgentManager.agentsCount--;
		//	Print on console
		AgentManager.logoutAgent(agentName);
	}
}