package layout;

import animal.AnimalModel;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
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
			super(cetSimView.getCetSimControl().getAnimals());
		
			TableColumn<AnimalModel,String>  animalName = new TableColumn<AnimalModel,String>("Animal Type");
			animalName.setCellValueFactory(cellData -> cellData.getValue().sensorNameProperty());
			
			TableColumn<AnimalModel,Number>  nAnimals = new TableColumn<AnimalModel,Number>("Number");
			nAnimals.setCellValueFactory(cellData -> cellData.getValue().numberAnimalsameProperty());

			getTableView().getColumns().addAll(animalName, nAnimals);
			
		}

		@Override
		public void dialogClosed(AnimalModel data) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Dialog<AnimalModel> createSettingsDialog(AnimalModel data) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	

}
