package cetaceanSim;

import java.util.ArrayList;

import animal.AnimalModel;
import bathymetry.BathymetryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import layout.CetSimView;
import layout.MapProjector3D;
import noise.NoiseModel;
import reciever.RecieverModel;
import tide.TideModel;

/**
 * Control class for the simulation. Holds all the data needed for a simulation. 
 * @author Jamie Macaulay 
 *
 */
public class CetSimControl {
	
	/**
	 * Static reference to the ArrayModelControl. 
	 */
	private static CetSimControl arrayControlInstance; 

	/**
	 * The bathymetry
	 */
	private BathymetryModel bathymetry; 
	
	/**
	 * A list of animal types. 
	 */
	private ObservableList<AnimalModel> animals;  
	
	/**
	 * The current tide model for the simulation
	 */
	private TideModel tide; 
	
	/**
	 * The current noise model for the simulation
	 */
	private NoiseModel noise; 

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
	 * Reference ot the view. Can be null if there is no view. 
	 */
	private CetSimView cetSimView;

	
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

	public static final int ANIMAL_ARRAY_CHANGED = 5;

	/**
	 * Constructor to initialize the simulation. 
	 */
	public CetSimControl(){
		//create defualt instances of each model
		animals=FXCollections.observableArrayList();
		recievers=FXCollections.observableArrayList();
		
		//create an array of simualtion components. 
		simModels=new ArrayList<SimUnit>(); 
		//some model refernces always exist...others have to be dynamically added to the list. 
		simModels.add(bathymetry);
		simModels.add(tide);
		simModels.add(noise);
	}
	
	/**
	 * Get the bathymtry model for the simualtion
	 * @return the bathymetry model 
	 */
	public BathymetryModel getBathymetry() {
		return bathymetry;
	}


	/**
	 * Set the bathymetry model 
	 * @param bathymetry the bathymtry model
	 */
	public void setBathymetry(BathymetryModel bathymetry) {
		simModels.remove(this.bathymetry);
		this.bathymetry = bathymetry;
		simModels.add(bathymetry);
	}


	/**
	 * Get the animals in the simulation. 
	 * @return the animals in the simulation
	 */
	public ObservableList<AnimalModel> getAnimals() {
		return animals;
	}


	/**
	 * Set a list of animals for the simulation. 
	 * @param animals
	 */
	public void setAnimals(ObservableList<AnimalModel> animals) {
		this.animals = animals;
	}


	/**
	 * Get the tide model for the simualtion. 
	 * @return the tide model. 
	 */
	public TideModel getTide() {
		return tide;
	}


	/**
	 * Set the tide for the simualtion
	 * @param tide the tide ofr the simualtion. 
	 */
	public void setTide(TideModel tide) {
		this.tide = tide;
	}


	/**
	 * Get the noise model for the simulation
	 * @return the noise model
	 */
	public NoiseModel getNoise() {
		return noise;
	}


	/**
	 * Set the noise model for the simulation
	 * @param noise the noise model to set for the simulation.
	 */
	public void setNoise(NoiseModel noise) {
		this.noise = noise;
	}
	

	/**
	 * Notification that an update has occured. 
	 * @param flag the update flag. 
	 */
	public void notifyUpdate(int flag) {
		cetSimView.notifyUpdate(flag);
	}
	
	public ArrayList<SimUnit> getSimModels(){
		return simModels;
	}

	/**
	 * Set the current progress messsage, 
	 * @param string - the prggreess message. 
	 */
	public void setSimMessage(String string) {
		this.currentMessage=string; 
		cetSimView.notifyUpdate(PROGRESS_UPDATE);
		
	}

	public void startSimulation() {
		// TODO Auto-generated method stub
		
	}

	public void stopSimulation() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Set the view in the control. 
	 * @param view - the view. 
	 */
	public void setView(CetSimView view) {
		this.cetSimView=view; 
		
	}


	public String getCurrentMessage() {
		// TODO Auto-generated method stub
		return currentMessage;
	}
	
	public static CetSimControl getInstance(){
		return arrayControlInstance; 
	}

	public static void create() {
		if (arrayControlInstance==null){
			new  CetSimControl(); 
		}
		
	}


}
