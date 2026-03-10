package animal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AnimalManager {

	/**
	 * Enum of array types
	 * SIMPLE_CETACEAN-A very simple animal. Swims in a straight line without any diving,, omni directional sounds etc.
	 * CLICKING_ODONTOCETE - a more complex animal with beam profile and complex diving behaviour. 
	 */
	public enum AnimalTypeEnum {
		SIMPLE_CETACEAN, CLICKING_ODONTOCETE
	}

	/**
	 * Holds a list of animals. 
	 */
	ObservableList<AnimalModel> animals = FXCollections.observableArrayList();

	/**
	 * Create the array manager.
	 */
	public AnimalManager(){
//		animals.addListener((ListChangeListener.Change<? extends AnimalModel> c) ->{
//			arrayModelControl.notifyUpdate(CetSimControl.ANIMAL_ARRAY_CHANGED); 
//		});
	}

	/**
	 * Create a new sensor. 
	 * @return the new sensor. 
	 */
	public static AnimalModel createNewAnimal(AnimalTypeEnum type){
		switch(type){
		case CLICKING_ODONTOCETE:
			return new ClickingOdontocete(); 
		default:
			break;
		}
		return null;
	}

	/**
	 * Get the list of current arrays.
	 * @return list of current arrays. 
	 */
	public ObservableList<AnimalModel> getAnimalList(){
		return animals; 
	}

}
