package layout;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import layout.SimVariablePane.SimTypePane;
import layout.simulation.ProbDetSettingsPane;
import simulation.CustomSimVar;
import simulation.SimVariable;
import simulation.SimVariable.DistributionType;

/**
 * SimPane for custom distribution
 * @author Jamie Macaulay 
 *
 */
public class CustomSimPane  implements SimTypePane {
	
	
	private String name=""; 
	
	/**
	 * Custom pane 
	 */
	private Pane customPane;

	/**
	 * The sim variable, 
	 */
	private CustomSimVar customSimVar;

	/**
	 * The min value spinner
	 */
	private Spinner<Double> minSpinner;

	/**
	 * The max value spinner. 
	 */
	private Spinner<Double> maxSpinner;

	private double[] probData;

	/**
	 * Constructor for the custom sim pane
	 */
	CustomSimPane(){
		//this.name=name;
	}

	@Override
	public String getSimVarName() {
		return name;
	}
	
	private Pane createCustomPane() {	
		
		HBox mainPane = new HBox(); 
		mainPane.setAlignment(Pos.CENTER_LEFT);

		mainPane.setSpacing(5);

		minSpinner= new Spinner<Double>(-50000000.,50000000.,-100, 5.); 
		minSpinner.setEditable(true);
		ProbDetSettingsPane.styleSpinner(minSpinner);

		maxSpinner= new Spinner<Double>(-50000000.,50000000.,100,5.); 
		maxSpinner.setEditable(true);
		ProbDetSettingsPane.styleSpinner(maxSpinner);
		
		Button importButton = new Button(); 
		importButton.setTooltip(new Tooltip("Import data for custom distribution. This is 1D data of probabilities from a .CSV or .MAT file"));
		importButton.setGraphic(GlyphsDude.createIcon(MaterialIcon.FILE_DOWNLOAD, "18"));
		importButton.setOnAction((action)->{
			importCustomVarData(); 
		}); 

		mainPane.getChildren().addAll(new Label("Min"), minSpinner, new Label("Max"), maxSpinner, importButton); 
		
		return mainPane;
	}

	/**
	 * Import data into the custom distribution. 
	 */
	private void importCustomVarData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSimVariable(SimVariable simVar) {
		CustomSimVar customSimVar=(CustomSimVar) simVar; 
		if (customSimVar==null) {
			customSimVar = new CustomSimVar(); 
		}
		this.minSpinner.getValueFactory().setValue(customSimVar.getMin());
		this.minSpinner.getValueFactory().setValue(customSimVar.getMax());
		this.probData=customSimVar.getProbData(); 
	}

	@Override
	public DistributionType getSimVarType() {
		return DistributionType.CUSTOM;
	}

	@Override
	public Region getPane() {
		if (customPane==null) customPane=createCustomPane(); 
		return  customPane; 
	}

	@Override
	public SimVariable getSimVariable() {
		if (probData==null) return new CustomSimVar(minSpinner.getValue(), maxSpinner.getValue()); 
		return new CustomSimVar(probData, minSpinner.getValue(), maxSpinner.getValue()); 
	}
	
	
}