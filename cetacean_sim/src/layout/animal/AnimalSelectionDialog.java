package layout.animal;


import animal.AnimalModel;
import cetaceanSim.CetSimControl;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AnimalSelectionDialog extends Dialog<AnimalModel>{
	
	private static AnimalSelectionDialog singleInstance;
	
	/**
	 * TextField to set name of sensor. 
	 */
	TextField nameField; 

	/**
	 * The current movement sensor the dialog is showing. 
	 */
	private AnimalModel movementSensor; 
	
	/**
	 * The pane shows custom controls for specific types of sensor. 
	 */
	BorderPane customSensorPane= new BorderPane();
	
	/**
	 * Reference to the sensor manager. 
	 */
	AnimalManager sensorManager=CetSimControl.getInstance().getSensorManager();

	/**
	 * Combo box to xhoose the parent array the sensor belongs to. 
	 */
	private ParentArrayComboBox parentArrayComboBox;
	
	/**
	 * Combo box to allow users to change sensor type. 
	 */
	private ComboBox<AnimalType> sensorBox;

	/**
	 * Reference to the main pane for the dialog. 
	 */
	private BorderPane mainPane; 

	
	public SensorDialog(){
		//FIXME- this appears to cause an error in CSS? 
		//this.initOwner(HArrayModelControl.getInstance().getPrimaryStage());
		
		this.setTitle("Sensor Dialog");
		this.getDialogPane().setContent(createDialogPane());
		this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		this.setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.OK) {
				return movementSensor;
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
			
		//this.setResizable(true);
		
	}

	public static Dialog<MovementSensor> createDialog(MovementSensor sensor){
		if (singleInstance==null) {
			singleInstance=new SensorDialog();
		}
	
		singleInstance.setParams(sensor);
	
		return singleInstance; 
	}
	
	@SuppressWarnings("unchecked")
	private boolean getParams(){
	 
		movementSensor.sensorNameProperty().setValue(nameField.getText());
		movementSensor.parentArrayProperty().setValue(parentArrayComboBox.getValue());
		if (movementSensor instanceof AbstractMovementSensor){
			int error=((AbstractMovementSensor) movementSensor).getSettingsPane().getParams(movementSensor);
			if (error==0) return true; 	
			else {
				return ((AbstractMovementSensor) movementSensor).getSettingsPane().showErrorWarning(error);
			}
			
		}
		return true; 
		
	}
	
	@SuppressWarnings("unchecked")
	private void setParams(MovementSensor movementSensor){
		
		this.movementSensor=movementSensor;		
		
		nameField.setText(movementSensor.sensorNameProperty().get());
		parentArrayComboBox.setValue(movementSensor.parentArrayProperty().getValue());
		sensorBox.setValue(movementSensor.sensorTypeProperty().get());
		
		if (movementSensor instanceof AbstractMovementSensor){
			createSensorPane(movementSensor);
			((AbstractMovementSensor) movementSensor).getSettingsPane().setParams(movementSensor);
		}
		
	}; 
	
	/**
	 * Create the dialog pane. 
	 * @return the main pane which sits inside dialog. 
	 */
	private Pane createDialogPane(){
		
		double sectionPadding=10;
		
		 mainPane=new BorderPane();
		
		Label nameLabel=new Label("Sensor Name"); 
		nameLabel.setPadding(new Insets(5,0,0,0));
		nameField=new TextField();
		
		Label parentArrayLabel=new Label("Parent Array");
		parentArrayLabel.setPadding(new Insets(sectionPadding,0,0,0));
		parentArrayComboBox = new ParentArrayComboBox();
		//FIXME - weird- only by adding a listener does the combo box update properly when arrays are renamed? Mayb actually be a bug in JavaFX source code? 
		parentArrayComboBox.valueProperty().addListener((obs, t, t1)->{
		}); 
		
		Label sensorLabel=new Label("Select Sensor");
		sensorBox=new ComboBox<SensorType>();
		
		sensorBox.setItems(FXCollections.observableArrayList(SensorType.values()));
		/**
		 * Unlike arrays and hydrophones, sensors are very different from each other and made
		 * up of multiple sub classes. Therefore when a new sensor type is selected, that sensor 
		 * requires a specific pane and a new instance of the subclass to be created. 
		 */
		sensorBox.valueProperty().addListener((obs, t, t1)->{
			if (this.movementSensor.sensorTypeProperty().get()!=t1){
				this.movementSensor=sensorManager.createNewSensor(obs.getValue());
				setParams(movementSensor); 
			}
		}); 
		

		
//		sensorBox.setConverter(new StringConverter<SensorType>() {
//            @Override
//            public String toString(SensorType user) {
//              if (user == null){
//                return null;
//              } else {
//                return user.sensorNameProperty().get();
//              }
//            }
//
//          @Override
//          public MovementSensor fromString(String userId) {
//              return null;
//          }
//		});
		
		
		sensorBox.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(sensorBox, Priority.ALWAYS);
		
		VBox selectSensor=new VBox();
		selectSensor.getChildren().addAll(nameLabel, nameField, parentArrayLabel, parentArrayComboBox,sensorLabel, sensorBox);
		
		mainPane.setTop(selectSensor);
		mainPane.setBottom(customSensorPane); 

		return mainPane; 
		
	}
	
	/**
	 * Create the specific pane in the dialog for the selected sensor. 
	 */
	public void createSensorPane(MovementSensor sensor){
		customSensorPane.setCenter(null); 
		if (sensor instanceof AbstractMovementSensor){
			System.out.println("Set new sensor pane...");
			customSensorPane.setCenter(	((AbstractMovementSensor) sensor).getSettingsPane());
			((AbstractMovementSensor) sensor).getSettingsPane().setParams(sensor);
		}
		//TODO- this is a bit CUMBERSOME and maybe fixed in new version of JavaFX
		//need to get stage and resize because new controls will have been added. 
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.sizeToScene();
	}

	
}