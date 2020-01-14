package layout;

import java.io.File;

import cetaceanSim.CetSimControl;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
	public CustomSimPane(){
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
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Text/CSV Files", "*.txt", "*.csv"),
				new ExtensionFilter("MAT", "*.mat"));
		File selectedFile = fileChooser.showOpenDialog(CetSimControl.getInstance().getCetSimView().getMainStage());
		if (selectedFile != null) {
			loadProbData(selectedFile); 
		}

	}

	/**
	 * Load the probability of detection data and the max/min value. 
	 * @param selectedFile
	 * @return
	 */
	private boolean loadProbData(File selectedFile) {
		String extension=  getFileExtension(selectedFile); 
		if (extension=="txt" || extension=="csv") {
			
		}
		else if (extension=="mat") {
			
		}
		return false; 
	}
	
	private void showAlertDialog() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Feature not yet supported in the GUI");
		alert.setHeaderText("Information Alert");
		String s ="This feature is not yet supported. On the TODO list";
		alert.setContentText(s);
		alert.show();
	}
	
	
	private boolean loadCSV(File selectedFile) {
		//TODO - need to implement importing of data. 
		showAlertDialog();
		return false; 
	}
	
	
	private boolean loadMAT(File selectedFile) {
		//TODO - need to implement importing of data. 
		showAlertDialog();
		return false; 
	}



	private String getFileExtension(File file) {
		String name = file.getName();
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}



	@Override
	public void setSimVariable(SimVariable simVar) {
		CustomSimVar customSimVar=(CustomSimVar) simVar; 
		
//		System.out.println("Set Sim Variable: Custom sim variable: " + simVar + "  " + this );
		if (customSimVar==null) {
			customSimVar = new CustomSimVar(); 
		}
//		System.out.println("Set Sim Variable: Custom sim variable: " + simVar +  " min: " +customSimVar.getMin() +" max: " + customSimVar.getMax()+ "  " + this );

		this.minSpinner.getValueFactory().setValue(customSimVar.getMin());
		this.maxSpinner.getValueFactory().setValue(customSimVar.getMax());
		this.probData=customSimVar.getProbData(); 
		this.name= customSimVar.getName();
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
		if (probData==null) {

			return new CustomSimVar(minSpinner.getValue(), maxSpinner.getValue()); 
		}
		System.out.println("HH: Create prob data: " + probData.length + "   " + probData[0]); 
		return new CustomSimVar(this.name, probData, minSpinner.getValue(), maxSpinner.getValue()); 
	}


}