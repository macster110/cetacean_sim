package reciever;

import cetaceanSim.CetSimControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RecieverManager {
	
	/**
	 * Holds a list of animals. 
	 */
	ObservableList<RecieverModel> animals = FXCollections.observableArrayList();
	
	/**
	 * Reference ot the control 
	 */
	private CetSimControl cetSimControl;
	
	
	public RecieverManager(CetSimControl cetSimControl) {
		this.cetSimControl=cetSimControl; 
	}

	/**
	 * Enum of array types
	 * SIMPLE_CETACEAN-A very simplae animal. Swims in a straight line without any diving,, omni directional sounds etc.
	 * CLICKING_ODONTOCETE - a more complex animal with beam profile and complex diving behaviour. 
	 */
	public enum RecieverTypeEnum {
		SIMPLE_DRIFTER
	}
	
	/**
	 * Create a new sensor. 
	 * @return the new sensor. 
	 */
	public RecieverModel createNewAnimal(RecieverTypeEnum type){
		switch(type){
		case SIMPLE_DRIFTER:
			return new DriftingReciever(); 
		default:
			break;
		}
		return null;
	}

	/**
	 * Get the list of current arrays.
	 * @return list of current arrays. 
	 */
	public ObservableList<RecieverModel> getAnimalList(){
		return animals; 
	}

}
