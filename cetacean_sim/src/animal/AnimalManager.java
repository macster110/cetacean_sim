package animal;


import cetaceanSim.CetSimControl;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class AnimalManager {

	/**
	 * Enum of array types
	 * SIMPLE_CETACEAN-A very simplae animal. Swims in a straight line without any diving,, omni directional sounds etc.
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
	 * Reference to the array model control. 
	 */
	private CetSimControl csControl;

	/**
	 * Create the array manager.
	 */
	public AnimalManager(CetSimControl arrayModelControl){
		this.csControl=arrayModelControl;
		animals.addListener((ListChangeListener.Change<? extends AnimalModel> c) ->{
			arrayModelControl.notifyUpdate(CetSimControl.ANIMAL_ARRAY_CHANGED); 
		});
	}

	/**
	 * Create a new sensor. 
	 * @return the new sensor. 
	 */
	public AnimalModel createNewAnimal(AnimalTypeEnum type){
		switch(type){
		case CLICKING_ODONTOCETE:
			return new ClickingOdontocete(); 
		case SIMPLE_CETACEAN:
			return new SimpleCetacean(); 
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
