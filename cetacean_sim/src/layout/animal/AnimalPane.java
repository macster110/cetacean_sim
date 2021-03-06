package layout.animal;

import animal.AnimalManager;
import animal.AnimalManager.AnimalTypeEnum;
import animal.AnimalModel;
import cetaceanSim.CetSimControl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import layout.CetSimView;
import layout.utils.TablePane;

public class AnimalPane extends BorderPane {
	
	/**
	 * Reeference to the view. 
	 */
	private CetSimView cetSimView;
	
	/**
	 * Table whihc holds all animal models in the simulation. 
	 */
	private AnimalTable animalTable;

	/**
	 * The animal manager. 
	 */
	private AnimalManager animalManager;

	/**
	 * Constructor for animal pane.
	 * @param cetSimView reference to the view
	 */
	public AnimalPane(AnimalManager animalManager, CetSimView cetSimView){
		this.animalManager=animalManager;
		this.cetSimView=cetSimView;
		animalTable = new AnimalTable(); 
		this.setCenter(animalTable);
	}
	
	
	class AnimalTable extends TablePane<AnimalModel> {
		public AnimalTable() {
			super(animalManager.getAnimalList());
			
			TableColumn<AnimalModel,String>  animalName = new TableColumn<AnimalModel,String>("Name");
			animalName.setCellValueFactory(cellData -> cellData.getValue().getAnimalName());
		
			TableColumn<AnimalModel,String>  animalModel = new TableColumn<AnimalModel,String>("Animal Type");
			animalModel.setCellValueFactory(cellData -> {
				return  new ReadOnlyObjectWrapper<String>(cellData.getValue().getAnimalType().toString());
			});

			TableColumn<AnimalModel,Number>  nAnimals = new TableColumn<AnimalModel,Number>("Number");
			nAnimals.setCellValueFactory(cellData -> cellData.getValue().getNumberOfAnimals());

			getTableView().getColumns().addAll(animalName, animalModel, nAnimals);
			
		}

		@Override
		public void dialogClosed(AnimalModel data) {
			
		}

		
		@Override
		public Dialog<AnimalModel> createSettingsDialog(AnimalModel data) {
			System.out.println("Animal data "+data); 
			if (data==null) {
				//create a new open tag sensor as a default. 
				AnimalModel animalModel=AnimalManager.createNewAnimal(AnimalTypeEnum.CLICKING_ODONTOCETE);
				return AnimalSelectionDialog.createDialog(animalModel);
			}
			else return AnimalSelectionDialog.createDialog(data);			
		}
		
	}
	
	

}
