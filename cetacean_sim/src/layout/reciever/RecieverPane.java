package layout.reciever;

import animal.AnimalModel;
import animal.AnimalManager.AnimalTypeEnum;
import cetaceanSim.CetSimControl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import layout.CetSimView;
import layout.animal.AnimalSelectionDialog;
import layout.utils.TablePane;
import reciever.RecieverModel;

	
	public class RecieverPane extends BorderPane {
		
		/**
		 * Reeference to the view. 
		 */
		private CetSimView cetSimView;
		
		/**
		 * Table whihc holds all animal models in the simulation. 
		 */
		private RecieverTable recieverTable;

		/**
		 * Constructor for animal pane.
		 * @param cetSimView reference to the view
		 */
		public RecieverPane(CetSimView cetSimView){
			this.cetSimView=cetSimView;
			recieverTable = new RecieverTable(); 
			this.setCenter(recieverTable);
		}
		
		
		class RecieverTable extends TablePane<RecieverModel> {
			public RecieverTable() {
				super(cetSimView.getCetSimControl().getRecieverManager().getAnimalList());
				
				TableColumn<RecieverModel,String>  animalName = new TableColumn<RecieverModel,String>("Name");
				animalName.setCellValueFactory(cellData -> cellData.getValue().getStringName());
			
				TableColumn<RecieverModel,String>  animalModel = new TableColumn<RecieverModel,String>("Animal Type");
				animalModel.setCellValueFactory(cellData -> {
					return  new ReadOnlyObjectWrapper<String>(cellData.getValue().getReceiverType().toString());
				});

				TableColumn<RecieverModel,Number>  nAnimals = new TableColumn<RecieverModel,Number>("Number");
				nAnimals.setCellValueFactory(cellData -> cellData.getValue().getNReceivers());

				getTableView().getColumns().addAll(animalName, animalModel, nAnimals);
				
			}
			
			@Override
			public Dialog<RecieverModel> createSettingsDialog(RecieverModel data) {
				System.out.println("Animal data "+data); 
				if (data==null) {
					//create a new open tag sensor as a default. 
					AnimalModel movementSensor=CetSimControl.getInstance().getAnimalManager().createNewAnimal(AnimalTypeEnum.CLICKING_ODONTOCETE);
					return AnimalSelectionDialog.createDialog(movementSensor);
				}
				else return AnimalSelectionDialog.createDialog(data);			
			}

			@Override
			public void dialogClosed(RecieverModel data) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Dialog<RecieverModel> createSettingsDialog(RecieverModel data) {
				// TODO Auto-generated method stub
				return null;
			}
			
		}

}
