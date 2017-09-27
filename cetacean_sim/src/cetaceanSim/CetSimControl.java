package cetaceanSim;

import java.util.ArrayList;

import animal.AnimalManager;
import animal.AnimalModel;
import bathymetry.BathymetryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import layout.CetSimView;
import layout.MapProjector3D;
import noise.NoiseModel;
import reciever.RecieverManager;
import reciever.RecieverModel;
import simulation.SimulationType;
import simulation.probdetsim.ProbDetSim;
import simulation.surveySim.SurveySim;
import tide.TideModel;

/**
 * Control class for the simulation. Holds all the data needed for a simulation. 
 * @author Jamie Macaulay 
 *
 */
public class CetSimControl {
	
	/**
	 * Bathymetry file has finished loading. 
	 */
	public static final int BATHY_LOADED = 0;

	/**
	 * Simulation data has chnaged. 
	 */
	public static final int SIM_DATA_CHANGED = 1;
	
	/**
	 * Flag that some data is being loaded. 
	 */
	public static final int LOAD_DATA_START = 2;
	
	/**
	 * Flag to show that data loading has ended. 
	 */
	public static final int LOAD_DATA_END = 3;
	
	/**
	 * Indicate a progress update.
	 */
	public static final int PROGRESS_UPDATE = 4;

	/**
	 * 
	 * Animal array has chnaged 
	 */
	public static final int ANIMAL_ARRAY_CHANGED = 5;

	
	/**
	 * Static reference to the ArrayModelControl. 
	 */
	private static CetSimControl cetSimControlInstance; 
	
	/**
	 * List of the possible types of simulation
	 */
	ArrayList<SimulationType> simulationTypes= new ArrayList<SimulationType>();
	
	/*
	 * The current index of the simulation. 
	 */
	int currentSimIndex=0; 

	/**
	 * reference to the view which handles the GUI
	 */
	private CetSimView cetSimView; 

	/**
	 * Constructor to initialise the simulation. 
	 */
	public CetSimControl(){
		simulationTypes.add(new SurveySim(this)); 
		simulationTypes.add(new ProbDetSim(this)); 
	}

	/**
	 * Create the controller. 
	 */
	public static void create() {
		if (cetSimControlInstance==null) {
			cetSimControlInstance= new CetSimControl(); 
		}
	}

	/**
	 * Get the current instance of the control 
	 * @return
	 */
	public static CetSimControl getInstance() {
		return cetSimControlInstance;
	}

	
	/**
	 * Set the current view. 
	 * @param view
	 */
	public void setView(CetSimView view) {
		this.cetSimView=view; 
		
	}
	

	public void setSimulation(int selectedIndex) {
		this.currentSimIndex=selectedIndex; 
		cetSimView.setSimulation(simulationTypes.get(selectedIndex)); 	
	}

	/**
	 * Start the simulation
	 */
	public void startSimulation() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Stop the simulation 
	 */
	public void stopSimulation() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Get the Cet sim view. 
	 * @return the cet sim view. 
	 */
	public CetSimView getCetSimView() {
		return cetSimView;
	}

	public String getCurrentMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public void notifyUpdate(int loadDataStart) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * List of the simulation types. 
	 * @return all possible simulations. 
	 */
	public ArrayList<SimulationType> getSimulationTypes() {
		return simulationTypes; 
	}

	/**
	 * Get the currently selected simulation. 
	 * @return the current simulation type. 
	 */
	public SimulationType getCurrentSimulation() {
		return simulationTypes.get(this.currentSimIndex);
	}
	


}
