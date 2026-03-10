package layout.reciever;

import animal.AnimalManager;
import animal.AnimalModel;
import animal.AnimalManager.AnimalTypeEnum;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.CetSimView;
import reciever.RecieverManager;
import reciever.RecieverManager.RecieverTypeEnum;
import reciever.RecieverModel;

public class ReciverSelectionDialog extends Dialog<RecieverModel> {
	
	private static ReciverSelectionDialog singleInstance;
	
	/**
	 * The current movement sensor the dialog is showing. 
	 */
	private RecieverModel recieverModel;

	private BorderPane mainPane;

	private TextField nameField;

	private ComboBox<RecieverTypeEnum> recieverTypeBox; 
	
	/**
	 * The pane shows custom controls for specific types of sensor. 
	 */
	private BorderPane customRecieverPane= new BorderPane();

	public ReciverSelectionDialog() {
		
		//FIXME- this appears to cause an error in CSS? 
		//this.initOwner(HArrayModelControl.getInstance().getPrimaryStage());
		
		this.setTitle("Reciver Dialog");
		this.getDialogPane().setContent(createDialogPane());
		this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		
		this.setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.OK) {
				return recieverModel;
			}
			return null;
		});

		final Button btOk = (Button) this.getDialogPane().lookupButton(ButtonType.OK); 
		btOk.addEventFilter(ActionEvent.ACTION, (event) -> { 
			if (getParams());
			else {
				//do not close (error dialogs are handled in getParams class)
				event.consume();
			}
		}); 
		
	}
	
	
	private boolean getParams() {
		if (nameField.getText()==null || nameField.getText()==""){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText("No reciver name");
			alert.setContentText("You need to name the animal(s) for the simulation!");
			alert.showAndWait();
			return false;
		}
		else recieverModel.getStringName().set(nameField.getText());
		
	
		if (recieverModel!=null){
			if (recieverModel.getSettingsPane().getParams()!=null) return true;
			else return false; 
		}
		else return false;
	}
	
	@SuppressWarnings("unchecked")
	private void setParams(RecieverModel recieverModel){
		recieverTypeBox.setValue(recieverModel.getReceiverType());
		//create the pane
		createRecieverPane(recieverModel);
	}
	
	
	public static Dialog<RecieverModel> createDialog(RecieverModel sensor){
		if (singleInstance==null) {
			singleInstance=new ReciverSelectionDialog();
		}
	
		singleInstance.setParams(sensor);
	
		return singleInstance; 
	}
	
	
	/**
	 * Create the dialog pane. 
	 * @return the main pane which sits inside dialog. 
	 */
	private Pane createDialogPane(){
				
		mainPane=new BorderPane();
		
		double maxWidth=300; 

		Label titleLabel=new Label("Basic Info"); 
		titleLabel.setFont(new Font("Ubuntu", CetSimView.titleFontSize));
		
		Label nameLabel=new Label("Reciever Name"); 
		nameLabel.setPadding(new Insets(5,0,0,0));
		nameField=new TextField();
		nameField.setMaxWidth(maxWidth);

		
		Label sensorLabel=new Label("Select Reciever");
		recieverTypeBox=new ComboBox<RecieverTypeEnum>();
		recieverTypeBox.setItems(FXCollections.observableArrayList(RecieverManager.RecieverTypeEnum.values()));
		recieverTypeBox.setMaxWidth(maxWidth);

		/**
		 * Different recievers require different pane.s 
		 */
		recieverTypeBox.valueProperty().addListener((obs, t, t1)->{
				this.recieverModel=RecieverManager.createNewReciever(obs.getValue());
				setParams(recieverModel); 
				createRecieverPane(recieverModel);
		}); 
		
		recieverTypeBox.setMaxWidth(maxWidth);
		HBox.setHgrow(recieverTypeBox, Priority.ALWAYS);
		
		VBox selectSensor=new VBox();
		selectSensor.getChildren().addAll(titleLabel, nameLabel, nameField ,sensorLabel, recieverTypeBox);
		
		mainPane.setTop(selectSensor);
		mainPane.setRight(customRecieverPane); 

		return mainPane; 
		
	}
	
	/**
	 * Create the specific pane in the dialog for the selected sensor. 
	 */
	public void createRecieverPane(RecieverModel reciever){
		customRecieverPane.setCenter(null); 
		customRecieverPane.setCenter(reciever.getSettingsPane().getContentNode());
		
		reciever.getSettingsPane().setParams(reciever, false);
	}

}
