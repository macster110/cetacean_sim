package layout.animal;


import animal.AnimalManager;
import animal.AnimalManager.AnimalTypeEnum;
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

/**
 * Allows user to select an animal. 
 * @author Jamie Macaulay
 *
 */
public class AnimalSelectionDialog extends Dialog<AnimalModel>{
	
	/**
	 * Static reference ot the dialog. 
	 */
	private static AnimalSelectionDialog singleInstance;
	
	/**
	 * TextField to set name of sensor. 
	 */
	private TextField nameField; 

	/**
	 * The current movement sensor the dialog is showing. 
	 */
	private AnimalModel animalModel; 
	
	/**
	 * The pane shows custom controls for specific types of sensor. 
	 */
	private BorderPane customAnimalPane= new BorderPane();
	
	/**
	 * Reference to the sensor manager. 
	 */
	private AnimalManager animalManager=CetSimControl.getInstance().getAnimalManager();
	
	/**
	 * Combo box to allow users to change sensor type. 
	 */
	private ComboBox<AnimalTypeEnum> animalTypeBox;

	/**
	 * Reference to the main pane for the dialog. 
	 */
	private BorderPane mainPane; 

	
	public AnimalSelectionDialog(){
		//FIXME- this appears to cause an error in CSS? 
		//this.initOwner(HArrayModelControl.getInstance().getPrimaryStage());
		
		this.setTitle("Animal Dialog");
		this.getDialogPane().setContent(createDialogPane());
		this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		this.setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.OK) {
				return animalModel;
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

	public static Dialog<AnimalModel> createDialog(AnimalModel sensor){
		if (singleInstance==null) {
			singleInstance=new AnimalSelectionDialog();
		}
	
		singleInstance.setParams(sensor);
	
		return singleInstance; 
	}
	
	@SuppressWarnings("unchecked")
	private boolean getParams(){
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void setParams(AnimalModel movementSensor){
	
		
	}; 
	
	/**
	 * Create the dialog pane. 
	 * @return the main pane which sits inside dialog. 
	 */
	private Pane createDialogPane(){
				
		mainPane=new BorderPane();
		
		Label nameLabel=new Label("Animal Name"); 
		nameLabel.setPadding(new Insets(5,0,0,0));
		nameField=new TextField();
		
		
		Label sensorLabel=new Label("Select Animal");
		animalTypeBox=new ComboBox<AnimalTypeEnum>();
		
		animalTypeBox.setItems(FXCollections.observableArrayList(AnimalManager.AnimalTypeEnum.values()));
		
		
		/**
		 * Unlike arrays and hydrophones, animals are very different from each other and made
		 * up of multiple sub classes. Therefore when a new animal type is selected, that animaltype 
		 * requires a specific pane and a new instance of the subclass to be created. 
		 */
		animalTypeBox.valueProperty().addListener((obs, t, t1)->{
				this.animalModel=animalManager.createNewAnimal(obs.getValue());
				setParams(animalModel); 
				createAnimalPane(animalModel);
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
		
		
		animalTypeBox.setMaxWidth(Double.MAX_VALUE);
		HBox.setHgrow(animalTypeBox, Priority.ALWAYS);
		
		VBox selectSensor=new VBox();
		selectSensor.getChildren().addAll(nameLabel, nameField ,sensorLabel, animalTypeBox);
		
		mainPane.setTop(selectSensor);
		mainPane.setRight(customAnimalPane); 

		return mainPane; 
		
	}
	
	/**
	 * Create the specific pane in the dialog for the selected sensor. 
	 */
	public void createAnimalPane(AnimalModel animal){
		customAnimalPane.setCenter(null); 
		customAnimalPane.setCenter(animal.getSettingsPane().getContentNode());
		
		animal.getSettingsPane().setParams(animal, false);
//		if (sensor instanceof AbstractMovementSensor){
//			System.out.println("Set new sensor pane...");
//			customSensorPane.setCenter(	((AbstractMovementSensor) sensor).getSettingsPane());
//			((AbstractMovementSensor) sensor).getSettingsPane().setParams(sensor);
//		}
		//TODO- this is a bit CUMBERSOME and maybe fixed in new version of JavaFX
		//need to get stage and resize because new controls will have been added. 
		Stage stage = (Stage) this.getDialogPane().getScene().getWindow();
		stage.sizeToScene();
	}

	
}