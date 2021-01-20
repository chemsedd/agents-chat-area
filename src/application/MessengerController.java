package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class MessengerController {
	
	@FXML AnchorPane st;
	@FXML TextArea messageToSend;
	@FXML TextArea receivedMessage;
	
	@FXML Button sendMessage;
	@FXML Button clear;
	@FXML Button exitRoom;
	
	@FXML ChoiceBox receivers;


}
