package layout.reciever;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import layout.CetSimView;
import layout.utils.TablePane;
import reciever.RecieverManager;
import reciever.RecieverManager.RecieverTypeEnum;
import reciever.RecieverModel;

/**
 * 
 * Pane which shows a list 
 * @author macst
 *
 */
public class RecieverPane extends BorderPane {

	/**
	 * Reference to the view. 
	 */
	private CetSimView cetSimView;

	/**
	 * Table whihc holds all animal models in the simulation. 
	 */
	private RecieverTable recieverTable;

	private RecieverManager recieverManager;

	/**
	 * Constructor for animal pane.
	 * @param cetSimView reference to the view
	 */
	public RecieverPane(RecieverManager recieverManager, CetSimView cetSimView){
		this.cetSimView=cetSimView;
		this.recieverManager=recieverManager; 
		recieverTable = new RecieverTable(); 
		this.setCenter(recieverTable);
	}


	class RecieverTable extends TablePane<RecieverModel> {
		public RecieverTable() {
			super(recieverManager.getRecieverList());
			
			TableColumn<RecieverModel,String>  animalName = new TableColumn<RecieverModel,String>("Name");
			animalName.setCellValueFactory(cellData -> cellData.getValue().getStringName());

			TableColumn<RecieverModel,String>  recieverModel = new TableColumn<RecieverModel,String>("Reciever Type");
			recieverModel.setCellValueFactory(cellData -> {
				return  new ReadOnlyObjectWrapper<String>(cellData.getValue().getReceiverType().toString());
			});

			TableColumn<RecieverModel,Number>  nAnimals = new TableColumn<RecieverModel,Number>("Number");
			nAnimals.setCellValueFactory(cellData -> cellData.getValue().getNReceivers());

			getTableView().getColumns().addAll(animalName, recieverModel, nAnimals);

		}

		@Override
		public Dialog<RecieverModel> createSettingsDialog(RecieverModel data) {
			System.out.println("Animal data "+data); 
			if (data==null) {
				//create a new open tag sensor as a default. 
				RecieverModel movementSensor=RecieverManager.createNewReciever(RecieverTypeEnum.SIMPLE_DRIFTER);
				return ReciverSelectionDialog.createDialog(movementSensor);
			}
			else return ReciverSelectionDialog.createDialog(data);			
		}

		@Override
		public void dialogClosed(RecieverModel data) {
			// TODO Auto-generated method stub

		}
	}

}
