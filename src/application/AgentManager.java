package application;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

public class AgentManager {

	//	List of Agents
	public static Map<String, MyAgent> agents 		= new HashMap<String, MyAgent>();
	public static ObservableList<MyAgent> myAgents 	= FXCollections.observableArrayList();
	//	List of Agents GUI
	public static ArrayList<AgentStage> agentsGUI 	= new ArrayList<AgentStage>();
	//	Number of Agents
	public static int agentsCount 					= 0;
	ContainerController container 					= null;
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public ContainerController createChatRoom(String name) {
		//	Creating container profile
		Profile profile = new ProfileImpl();
		//	Set container arguments
		profile.setParameter(Profile.MAIN_HOST, "localhost");
		profile.setParameter(Profile.CONTAINER_NAME, name);
		profile.setParameter(Profile.GUI, "true");
		//	Creating the container
		return Runtime.instance().createMainContainer(profile);
	}
	
	
	/**
	 * 
	 * @param agents
	 * @return
	 * @throws ControllerException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public ContainerController fillChatRoom(String... agents) throws ControllerException, IOException, InterruptedException {
		
		//	Creating container only once
		if(container == null)
			container = createChatRoom("Main-Container");
		
		//	
		AgentController agent;
		
		for(String agentName : agents) {
			agent = container.createNewAgent(agentName, "application.MyAgent", null);
			agent.start();
			//	GUI to the list
			AgentStage as = new AgentStage(agentName);
			AgentManager.agentsGUI.add(as);
		}
		
		return container;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws StaleProxyException
	 */
	public AgentController toggleSniffer() throws StaleProxyException {
		//	Creating container only once
		if(container == null)
			container = createChatRoom("Main-Container");
		AgentController sniffer = container.createNewAgent("Agents Sniffer", "application.MySniffer", null);
		sniffer.start();
		return sniffer;
	}
	
	
	/**
	 * Get Agent form created agents
	 * @param agentName
	 * @return
	 */
	public MyAgent getMyAgent(String agentName) {
		return agents.get(agentName);
	}
	
	
	/**
	 * Get Agent form created agents
	 * @param agentName
	 * @return
	 */
	public MyAgent getMyAgentObservable(String agentName) {
		for(MyAgent ag : myAgents)
			if(ag.getLocalName().equals(agentName))
				return ag;
		return null;
	}
	
	/**
	 * 
	 * @param agentName
	 * @return
	 * @throws ControllerException 
	 */
	public AgentController getAgentController(String agentName) throws ControllerException {
		return this.container.getAgent(agentName);
	}
	
	
	/**
	 * Printing on console 'Agent joined'
	 * @param agentName
	 */
	public static void loginAgent(String agentName) {
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date();
		Controller.console.appendText(">> [" + formatter.format(date) + "] Agent: <" + agentName + "> Joined Chat Room!\n");
	}
	
	
	/**
	 * Printing on console 'Agent left'
	 * @param agentName
	 */
	public static void logoutAgent(String agentName) {
		SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date();
		Controller.console.appendText(">> [" + formatter.format(date) + "] Agent: <" + agentName + "> Left Chat Room!\n");
	}
}
