package layout.animal;

import java.util.ArrayList;

import animal.DefaultBeamProfiles;
import animal.SimpleOdontocete;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import layout.ResultConverter;
import layout.SimVariablePane;
import layout.SimVariablePaneMulti;
import layout.utils.SettingsPane;
import simulation.SimVariable.DistributionType;

/**
 * 
 * Simple pane for describing an animal. Usually used for prob. det. simulation as does not have detailed diving information. 
 * @author Jamie Macaulay 
 *
 */
public class SimpleOdontocetePane extends BorderPane implements SettingsPane<SimpleOdontocete> {
	
	/**
	 * The current settings ot be changed.
	 */
	private SimpleOdontocete simpleOdontocete;
	
	/**
	 * The vertical angle.
	 */
	private SimVariablePaneMulti vertAngle;

	/**
	 * The horizontal angle. 
	 */
	private SimVariablePane horzAngle;

	/**
	 * Source level
	 */
	private SimVariablePane sourceLevel;

	/**
	 * The depth distribution
	 */
	private SimVariablePane depthDistribution;

	/**
	 * The beam profiles. 
	 */
	private ArrayList<BeamProfile> defaultBeamProfiles;
	
	/**
	 * The current beam profile. 
	 */
	private int currentBeamProfile;
	
	/**
	 * Symbol for degrees
	 */
	public static final String  degreeSymbol =  "" +(char) 0x00B0 ; 

	public static final String dB = "dB re 1" + "\u00B5"+"Pa";
	/**
	 * Simple Odontocetes pane. 
	 */
	public SimpleOdontocetePane (){
		this.setLeft(createSettingsPane());
		this.setCenter(createBeamPane()); 
	}
	
	
	/**
	 * Create the settings pane.
	 * @return the pane for changing simulations variables.
	 */
	private Pane createSettingsPane() {
		
		GridPane gridPane = new GridPane(); 
		gridPane.setVgap(5);
		gridPane.setHgap(5);
		gridPane.setAlignment(Pos.TOP_LEFT);

		vertAngle 	= new SimVariablePaneMulti("Vertical Angle", degreeSymbol, "Depth", "m");
		horzAngle = new SimVariablePane("Horizontal Angle", degreeSymbol, DistributionType.UNIFORM, -180, 180, 0, 90);
		sourceLevel = new SimVariablePane("Source Level", dB, DistributionType.NORMAL, 0, 250, 180, 20);
		depthDistribution = new SimVariablePane("Depth Distribution", "m", DistributionType.UNIFORM, -180, 0, 0, 0);
		
		horzAngle.setResultConverter(new Radians2Degrees());
		vertAngle.setResultConverter(new Radians2Degrees());
		
		gridPane.add(vertAngle, 0, 0);
		gridPane.add(horzAngle, 0, 1);
		gridPane.add(sourceLevel, 0, 2);
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
		defaultBeamProfiles = new DefaultBeamProfiles().getDefaultBeams();
		for (int i=0; i<defaultBeamProfiles.size(); i++) {
			beamProfileBox.getItems().add(defaultBeamProfiles.get(i).getName()); 
		}
		
		currentBeamProfile=0; 
		beamProfileBox.setOnAction((action)->{
			this.currentBeamProfile= beamProfileBox.getSelectionModel().getSelectedIndex();
			beamProfile.setSurface(defaultBeamProfiles.get(currentBeamProfile));
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
				
		simpleOdontocete.setBeamProfile(this.defaultBeamProfiles.get(currentBeamProfile));
		
		System.out.println("SimpleOdontocetesPane.Beam index: " + currentBeamProfile); 
		
		simpleOdontocete.horzAngle=this.horzAngle.getSimVariable(); 
		simpleOdontocete.vertAngles=this.vertAngle.getSimVariable(); 
		simpleOdontocete.depthDistribution=this.depthDistribution.getSimVariable(); 
		simpleOdontocete.sourceLevel=this.sourceLevel.getSimVariable();
		
		return simpleOdontocete;
	}

	@Override
	public void setParams(SimpleOdontocete settingsData, boolean clone) {
		
		this.simpleOdontocete=settingsData;

		//if (clone) this.simpleOdontocete=settingsData.clone();		
		//set the controls. 
		
		System.out.println("SimpleOdontocetesPane.Settings the params: "+ settingsData.vertAngles.size() + "  SRC: " + settingsData.sourceLevel);
		
		this.horzAngle.setSimVariable(settingsData.horzAngle); 
		this.vertAngle.setSimVariables(settingsData.vertAngles); 
		this.sourceLevel.setSimVariable(settingsData.sourceLevel); 
		this.depthDistribution.setSimVariable(settingsData.depthDistribution); 
		
		//TODO- set the beam profile. 

	}

	@Override
	public String getName() {
		return "Simple Animal";
	}

	@Override
	public Node getContentNode() {
		return this;
	}

	@Override
	public void paneInitialized() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Convert to degrees
	 * @author Jamie Macaulay 
	 *
	 */
	public class Radians2Degrees extends ResultConverter {
		
		public double convert2Control(double value) {
			return Math.toDegrees(value); 
		}
		
	
		public double convert2Value(double value) {
			return Math.toRadians(value); 
		}
	}

}
