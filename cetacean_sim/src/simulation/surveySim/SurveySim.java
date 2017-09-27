package simulation.surveySim;

import java.util.ArrayList;

import animal.AnimalManager;
import bathymetry.BathymetryManager;
import bathymetry.BathymetryModel;
import cetaceanSim.CetSimControl;
import cetaceanSim.SimUnit;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import layout.CetSimView;
import layout.simulation.SimulationView;
import layout.simulation.SurveySimView;
import noise.NoiseModel;
import reciever.RecieverManager;
import reciever.RecieverModel;
import simulation.SimulationType;
import tide.TideModel;

/**
 * Simulates a survey and acoustic data whihc would be recieved on hydrophones depending on noise, bathmetry and animal behaviour
 * @author Jamie Macaulay	
 *
 */
public class SurveySim implements SimulationType {

	/**
	 * Holds a list of recievers
	 */
	private ObservableList<RecieverModel> recievers;

	/**
	 * List of all simulation models. 
	 */
	private ArrayList<SimUnit> simModels;

	/**
	 * The current status message. 
	 */
	private String currentMessage;
	
	/**
	 * Animal manager
	 */
	private AnimalManager animals;

	/**
	 * The reciever manager
	 */
	private RecieverManager recieverManager;

	/**
	 * Manages the current bathyemtry 
	 */
	private BathymetryManager bathymetryManager;

	/**
	 * The GUI components for the simulation
	 */
	private SurveySimView surveySimView;

	/**
	 * Reference to the cet sim controller. 
	 */
	private CetSimControl cetSimControl; 

	/**
	 * Constructor to initialize the simulation. 
	 * @param cetSimControl 
	 */
	public SurveySim(CetSimControl cetSimControl){
		this.cetSimControl=cetSimControl; 
		//create managers for the different simualtion units. 
		animals=new AnimalManager();
		recieverManager=new RecieverManager();
		bathymetryManager=new BathymetryManager(); 		
		//create an array of simualtion components. 
		simModels=new ArrayList<SimUnit>(); 
	}
	
	
	@Override
	public SimulationView getSimView() {
		//must create after the control has created the simulations. Otherwise the view
		//will not have ben created yet. 
		if (surveySimView==null) {
			surveySimView= new SurveySimView(this, cetSimControl.getCetSimView()); 
		}
		return surveySimView;
	}


	@Override
	public String getName() {
		return "Survey Simulation"; 
	}
	

	/**
	 * Notification that an update has occured. 
	 * @param flag the update flag. 
	 */
	public void notifyUpdate(int flag) {
		cetSimControl.getCetSimView().notifyUpdate(flag);
	}
	
	public ArrayList<SimUnit> getSimModels(){
		simModels.clear();
		simModels.add(bathymetryManager.getBathymetryModel()); 
		//TODO- add animal and reciever models too. 
		return simModels;
	}

	/**
	 * Set the current progress messsage, 
	 * @param string - the prggreess message. 
	 */
	public void setSimMessage(String string) {
		this.currentMessage=string; 
		cetSimControl.getCetSimView().notifyUpdate(CetSimControl.PROGRESS_UPDATE);
		
	}

	public void startSimulation() {
		// TODO Auto-generated method stub
		
	}

	public void stopSimulation() {
		// TODO Auto-generated method stub
		
	}
	public String getCurrentMessage() {
		// TODO Auto-generated method stub
		return currentMessage;
	}

	/**
	 * Get the animal manager whihc handles animals in the simulation. 
	 * @return the animal manager
	 */
	public AnimalManager getAnimalManager() {
		return animals;
	}

	/**
	 * Get the reciever manager. Handles all acoustic recievers.
	 * @return the reciever manager 
	 */
	public RecieverManager getRecieverManager() {
		// TODO Auto-generated method stub
		return this.recieverManager;
	}

	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		return false;
	}


	
	public BathymetryManager getBathymetryManager() {
		return bathymetryManager;
	}

	public void setBathymetryManager(BathymetryManager bathymetryManager) {
		this.bathymetryManager = bathymetryManager;
	}


	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}
	

	


}
