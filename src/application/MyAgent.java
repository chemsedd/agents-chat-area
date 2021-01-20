package application;

import java.text.SimpleDateFormat;
import java.util.Date;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import javafx.scene.control.TextArea;

public class MyAgent extends Agent {

	//	Warning suppression 
	private static final long serialVersionUID = 1L;
	//	His text area
	public TextArea receivedMessageArea;
	
	/**
	 * 
	 */
	@SuppressWarnings("serial")
	protected void setup() {
		
		//	Adding agent to the list
		AgentManager.agents.put(this.getLocalName(), this);
		AgentManager.myAgents.add(this);
		
		//	Adding Agent's behaviour (send & receive messages)
		this.addBehaviour(new CyclicBehaviour(this) {	
			@Override
			public void action() {
				//	Receiving the message
				ACLMessage receivedMsg;
				receivedMsg = receive();
				//	Printing received message
				if(receivedMsg != null) {
					SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss");					
					receivedMessageArea.appendText("\n>> [" + date.format(new Date()) + "] " + receivedMsg.getSender().getLocalName() + ": " + receivedMsg.getContent());
				}
			}
		});
		//	Printing action on console
		AgentManager.loginAgent(getLocalName());
		//	Updating number of agents
		AgentManager.agentsCount++;
	}

	
	/**
	 * Send message to another agent
	 * @param message
	 * @param receiver
	 */
	public void SendMessage(String message, Agent receiver) {
		//	Agent's Message (send)
		ACLMessage sentMessage = new ACLMessage(ACLMessage.INFORM);
		//	Setting message content
		sentMessage.setContent(message);
		//	Setting message receiver
		sentMessage.addReceiver(receiver.getAID());
		//	Sending message
		send(sentMessage);
		//	Clear message area
	}
	
	@Override
	public String toString() {
		return this.getLocalName();
	}
	
}