package layout.animal;


import animal.AnimalModel;
import animal.ClickingOdontocete;
import cetaceanSim.CetSimControl;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class AnimalManager {

	/**
	 * Enum of array types
	 * RIGID_ARRAY-A rigid array 
	 * LINEAR_FILEXIBLE_ARRAY - a flexible linear array, e.g. towed array or vertical array. 
	 */
	public enum AnimalTypeEnum {
		CLICKING_ODONTOCETE
	}

	/**
	 * Holds a list of animals. 
	 */
	ObservableList<AnimalModel> arrays = FXCollections.observableArrayList();

	/**
	 * Reference to the array model control. 
	 */
	private CetSimControl arrayModelControl;

	/**
	 * Create the array manager.
	 */
	public AnimalManager(CetSimControl arrayModelControl){
		this.arrayModelControl=arrayModelControl;

		arrays.addListener((ListChangeListener.Change<? extends AnimalModel> c) ->{
			arrayModelControl.notifyUpdate(CetSimControl.ANIMAL_ARRAY_CHANGED); 
		});
	}

	/**
	 * Create a new sensor. 
	 * @return the new sensor. 
	 */
	public AnimalModel createNewArray(AnimalTypeEnum type){
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
		return arrays; 
	}

}
