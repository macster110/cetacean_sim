package layout.animal;

import animal.AnimalManager.AnimalTypeEnum;
import animal.AnimalModel;
import cetaceanSim.CetSimControl;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import layout.CetSimView;
import layout.utils.TablePane;

public class AnimalPane extends BorderPane {
	
	/**
	 * REeference to the view. 
	 */
	private CetSimView cetSimView;
	
	/**
	 * Table whihc holds all animal models in the simulation. 
	 */
	private AnimalTable animalTable;

	/**
	 * Constructor for animal pane.
	 * @param cetSimView reference to the view
	 */
	public AnimalPane(CetSimView cetSimView){
		this.cetSimView=cetSimView;
		animalTable = new AnimalTable(); 
		this.setCenter(animalTable);
	}
	
	
	class AnimalTable extends TablePane<AnimalModel> {
		public AnimalTable() {
			super(cetSimView.getCetSimControl().getAnimalManager().getAnimalList());
		
			TableColumn<AnimalModel,String>  animalName = new TableColumn<AnimalModel,String>("Animal Type");
			animalName.setCellValueFactory(cellData -> cellData.getValue().sensorNameProperty());
			
			TableColumn<AnimalModel,Number>  nAnimals = new TableColumn<AnimalModel,Number>("Number");
			nAnimals.setCellValueFactory(cellData -> cellData.getValue().numberAnimalsameProperty());

			getTableView().getColumns().addAll(animalName, nAnimals);
			
		}

		@Override
		public void dialogClosed(AnimalModel data) {
			//TODO- add some notifications. 
			
		}

		@Override
		public Dialog<AnimalModel> createSettingsDialog(AnimalModel data) {
			System.out.println("Animal data "+data); 
			if (data==null) {
				//create a new open tag sensor as a default. 
				AnimalModel movementSensor=CetSimControl.getInstance().getAnimalManager().createNewAnimal(AnimalTypeEnum.CLICKING_ODONTOCETE);
				return AnimalSelectionDialog.createDialog(movementSensor);
			}
			else return AnimalSelectionDialog.createDialog(data);			
		}
		
	}
	
	

}