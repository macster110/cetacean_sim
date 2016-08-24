package cetaceanSim;

import animal.AnimalModel;
import bathymetry.BathymetryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import noise.NoiseModel;
import reciver.RecieverModel;
import tide.TideModel;

/**
 * Control class for the simulation. Holds all the data needed for a simulation. 
 * @author Jamie Macaulay 
 *
 */
public class CetSimControl {

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

	
	public static final int BATHY_LOADED = 0;
	
	/**
	 * Constructor to initialize the simulation. 
	 */
	public CetSimControl(){
		//create defualt instances of each model
		animals=FXCollections.observableArrayList();
		
		recievers=FXCollections.observableArrayList();

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
		this.bathymetry = bathymetry;
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
	 * Get the current list of recievers for the simulation. 
	 * @return  an observable list of recievers. 
	 */
	public ObservableList<RecieverModel> getRecievers() {
		return recievers;
	}



	public void setRecievers(ObservableList<RecieverModel> recievers) {
		this.recievers = recievers;
	}

	

	/**
	 * Notification that an update has occured. 
	 * @param flag the update flag. 
	 */
	public void notifyUpdate(int flag) {
		// TODO Auto-generated method stub
		
	}

}
