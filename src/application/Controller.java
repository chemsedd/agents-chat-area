package application;

import java.io.IOException;

import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	
	public static TextArea console;
	
	@FXML Button startConversation;
	@FXML Button toggleSniffer;
	@FXML Button exitConversation;
	@FXML Button clear;
	@FXML TextField agentName;

	private String typedName;
	
	
	/**
	 * Creating new Agent on the container
	 * @param event
	 * @throws ControllerException
	 * @throws IOException
	 */
	public void StartConversation(ActionEvent event) throws ControllerException, IOException {
		typedName = agentName.getText();
		if(!typedName.isEmpty() && !AgentManager.agents.containsKey(typedName)) {
			try {
				Main.agentManager.fillChatRoom(typedName);
			} catch(Exception e) {
				e.printStackTrace();
			}
			agentName.clear();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Agent creation error");
			alert.setContentText("Cannot create agent, Agent Name field is empty or Agent already exists on the ChatArea!!");
			alert.show();
		}
	}
	
	
	/**
	 * Call Agents Sniffer
	 * @throws IOException
	 * @throws StaleProxyException
	 */
	public void toggleAgentSinnifer() throws IOException, StaleProxyException {
		Main.agentManager.toggleSniffer();
	}
	
	
	/**
	 * 
	 * @param event
	 */
	public void clear(ActionEvent event) {
		console.clear();
	}
	
	
	/**
	 * 
	 * @param event
	 */
	public void Exit(ActionEvent event) {
		System.exit(0);
	}
}

