package layout.animal;

import java.util.ArrayList;

import animal.DefaultBeamProfiles;
import animal.SimpleOdontocete;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import layout.SimVariablePane;
import layout.SimVariablePane.DistributionType;
import layout.utils.SettingsPane;
import simulation.SimVariable;

/**
 * 
 * Simple pane for describing an animal. Usually used for prob det. simulation as doesn;t have detailed diving information. 
 * @author Jamie Macaulay 
 *
 */
public class SimpleOdontocetePane extends BorderPane implements SettingsPane<SimpleOdontocete> {
	
	/**
	 * The current settings ot be changed.
	 */
	private SimpleOdontocete simpleOdontocete;
	
	/**
	 * the vertical angle
	 */
	private SimVariablePane vertAngle;


	/**
	 * The horizontal angle. 
	 */
	private SimVariablePane horzAngle;

	/**
	 * Source level
	 */
	private SimVariablePane sourceAngle;

	/**
	 * The depth distribution
	 */
	private SimVariablePane depthDistribution;

	/**
	 * The beam profiles. 
	 */
	private ArrayList<BeamProfile> defaultBeamProfiles;

	/**
	 * Simple Odontocetes pane. 
	 */
	public SimpleOdontocetePane (){
		this.setTop(new Label("Hey, I''m a simple animal!"));
		this.setLeft(createSettingsPane());
		this.setCenter(createBeamPane()); 
	}
	
	
	/**
	 * Create the setttings pane.
	 * @return the pane for changing simulations variables.
	 */
	private Pane createSettingsPane() {
		
		GridPane gridPane = new GridPane(); 
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		
		vertAngle = new SimVariablePane("Vertical Angle", DistributionType.NORMAL, -90, 90, 0, 25);
		horzAngle = new SimVariablePane("Horizontal Angle", DistributionType.UNIFORM, -180, 180, 0, 90);
		sourceAngle = new SimVariablePane("Source Level", DistributionType.NORMAL, 0, 250, 180, 20);
		depthDistribution = new SimVariablePane("Source Level", DistributionType.UNIFORM, -180, 0, 0, 0);

		gridPane.add(vertAngle, 0, 0);
		gridPane.add(horzAngle, 0, 1);
		gridPane.add(sourceAngle, 0, 2);
		gridPane.add(depthDistribution, 0, 3);

		return gridPane; 
	}
	
	/**
	 * Create the beam pane. 
	 */
	private Pane createBeamPane() {
		
		BorderPane borderPane = new BorderPane(); 

		BeamProfile2D beamProfile = new BeamProfile2D();
		beamProfile.setPadding(new Insets(5,5,5,5)); 

		ComboBox<String> beamProfileBox = new ComboBox<String>();
		defaultBeamProfiles = DefaultBeamProfiles.getDefaultBeams();
		for (int i=0; i<defaultBeamProfiles.size(); i++) {
			beamProfileBox.getItems().add(defaultBeamProfiles.get(i).getName()); 
		}
		beamProfileBox.setOnAction((action)->{
			beamProfile.setSurface(defaultBeamProfiles.get(beamProfileBox.getSelectionModel().getSelectedIndex()));
		});
		beamProfileBox.getSelectionModel().select(0);
		beamProfile.setSurface(defaultBeamProfiles.get(0)); 
		
		VBox beamSelectorPane = new VBox();
		beamSelectorPane.getChildren().addAll(new Label("Select Beam Profile: "), beamProfileBox); 
		beamSelectorPane.setSpacing(5);
		beamSelectorPane.setPadding(new Insets(5,5,5,35));

		borderPane.setTop(beamSelectorPane);
		borderPane.setCenter(beamProfile);

		return borderPane; 
	
	}

	@Override
	public SimpleOdontocete getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(SimpleOdontocete settingsData, boolean clone) {
		//if (clone) this.simpleOdontocete=settingsData.clone();
		 this.simpleOdontocete=settingsData;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Simple Animal";
	}

	@Override
	public Node getContentNode() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void paneInitialized() {
		// TODO Auto-generated method stub
		
	}

}
