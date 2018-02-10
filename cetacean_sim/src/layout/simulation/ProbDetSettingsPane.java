package layout.simulation;

import java.io.File;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import layout.CetSimView;
import layout.animal.SimpleOdontocetePane;
import propogation.SimplePropogation;
import simulation.probdetsim.ProbDetSim;
import simulation.probdetsim.ProbDetSimSettings;

/**
 * Settings pane for the probability of detection
 * @author Jamie Macaulay 
 *
 */
public class ProbDetSettingsPane extends BorderPane {
	
	/**
	 * Spinner to define the number of runs
	 */
	private Spinner<Integer> nRunnerSpinner;

	/**
	 * Spinner to define the number of bootstraps. 
	 */
	private Spinner<Integer> nBootSpinner;

	/**
	 * Spinner to define the maximum range
	 */
	private Spinner<Double> maxRange;

	/**
	 * Spinner to define the maximum depth/
	 */
	private Spinner<Double> maxDepth;


	private Spinner<Integer> rangeBin;


	private Spinner<Integer> depthBin;

	/**
	 * The side pane. 
	 */
	private VBox sidePane;


	private Spinner<Double> minNoise;


	private Spinner<Double> spreading;


	private Spinner<Double> absorbption;

	/**
	 * Reference to the porbability of detection view. 
	 */
	private ProbDetSim probDetSim;
	
	/**
	 * Border pane. 
	 */
	private BorderPane probDetTypeHolder;

	/**
	 * The minimum number of recievers to ensonify
	 */
	private Spinner<Integer> minRecievers; 
	
	/**
	 * Default width of the spinner. 
	 */
	public static int spinnerWidth=70; 

	/**
	 * Constructor for the main settings pane. 
	 * @param probDetSim - the probability of detection simulation
	 */
	public ProbDetSettingsPane(ProbDetSim probDetSim) {
		this.probDetSim=probDetSim; 
		this.setCenter(createSidePane()); 
	}
	
	/**
	 * Create the side pane. 
	 */
	private Pane createSidePane() {

		probDetTypeHolder = new BorderPane(); 

		int row=0; 

		GridPane  mainPane = new GridPane(); 
		mainPane.setHgap(5);
		mainPane.setVgap(5);

		Label simTypesLabel = new Label("Simulation Type"); 
		simTypesLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(simTypesLabel, 4);
		mainPane.add(simTypesLabel, 0, row);
		row++;
		
		ComboBox<String> simTypes = new ComboBox<String>(); 
		simTypes.prefWidthProperty().bind(this.widthProperty());
		for (int i=0; i<probDetSim.getProbDetSimTypes().size(); i++) {
			simTypes.getItems().add(probDetSim.getProbDetSimTypes().get(i).getName()); 
		} 
		simTypes.setOnAction((action)->{
			probDetSim.setSimIndex(simTypes.getSelectionModel().getSelectedIndex()); 
		
			probDetTypeHolder.setCenter(probDetSim.getCurrentSimType().getSettingsNode());
		}); 
		simTypes.getSelectionModel().select(probDetSim.getProbDetTypeIndex());
		row++;
		
		//import button
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("MATLAB Files", "*.mat"));
		
		Button button = new Button(); 
		button.setGraphic(GlyphsDude.createIcon(MaterialIcon.FILE_DOWNLOAD, "25"));
		button.setTooltip(new Tooltip("Import a .mat settings file"));
		button.setOnAction((action)->{
			File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
			probDetSim.importProbDetData(file);
		});
		
		GridPane.setColumnSpan(simTypes, 4);
		mainPane.add(simTypes, 0, row);		
		mainPane.add(button, 4, row);
		row++;
		
		GridPane.setColumnSpan(probDetTypeHolder, 6);
		mainPane.add(probDetTypeHolder, 0, row);
		row++;

		Label simLabel = new Label("Simulation Runs"); 
		simLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(simLabel, 4);
		mainPane.add(simLabel, 0, row);

		row++; 
		mainPane.add(new Label("No. Runs"), 0, row);
		nRunnerSpinner= new Spinner<Integer>(10,50000000,50000,10000); 
		GridPane.setColumnSpan(nRunnerSpinner, 2);
		styleSpinner(nRunnerSpinner);
		nRunnerSpinner.setPrefWidth(100);
		mainPane.add(nRunnerSpinner, 1, row);

		row++; 
		mainPane.add(new Label("No. Boot"), 0, row);
		nBootSpinner= new Spinner<Integer>(10,50000000,10,25); 
		GridPane.setColumnSpan(nBootSpinner, 2);
		styleSpinner(nBootSpinner);
		nBootSpinner.setPrefWidth(100);
		mainPane.add(nBootSpinner, 1, row);
		
		/******Sim Dimensions*****/

		row++; 
		Label coOrds = new Label("Dimensions"); 
		coOrds.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(coOrds, 4);
		mainPane.add(coOrds, 0, row);

		row++; 
		mainPane.add(new Label("Range: Max"), 0, row);
		maxRange = new Spinner<Double>(0.,50000000.,700.,10.); 
		styleSpinner(maxRange);
		mainPane.add(maxRange, 1, row);
		mainPane.add(new Label("No. bins"), 2, row);
		rangeBin = new Spinner<Integer>(0,50000000,25,2); 
		styleSpinner(rangeBin);
		mainPane.add(rangeBin, 3, row);

		row++;
		mainPane.add(new Label("Depth: Max"), 0, row);
		maxDepth= new Spinner<Double>(-50000000.,50000000.,-200.,10.); 
		styleSpinner(maxDepth);
		mainPane.add(maxDepth, 1, row);
		mainPane.add(new Label("No. bins"), 2, row);
		depthBin= new Spinner<Integer>(0,50000000,10,2); 
		styleSpinner(depthBin);
		mainPane.add(depthBin, 3, row);
		
		/****Receiver****/

		row++;
		Label recieverLabel = new Label("Receivers"); 
		recieverLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(coOrds, 6);
		mainPane.add(recieverLabel, 0, row); 
		
		row++; 
		Button recievers = new Button("Recievers"); 	
		recievers.prefWidthProperty().bind(mainPane.widthProperty());
		recievers.setMaxWidth(500);
		recievers.setGraphic(GlyphsDude.createIcon(MaterialIcon.SETTINGS, "25")); 
		recievers.setOnAction((action)->{
			probDetSim.getProbDetSimView().openRecieverDialog();
		});
	
		
		GridPane.setColumnSpan(recievers, 5);
		mainPane.add(recievers, 0, row);
		
		row++;
		
		minRecievers = new Spinner<Integer>(0,128,1,1); 
		styleSpinner(minRecievers);
		minRecievers.setPrefWidth(90);
		
		Label minLabel=new Label("no. of recievers to ensonify");
		GridPane.setColumnSpan(minLabel, 4);

		mainPane.add(minLabel, 1, row);
		mainPane.add(minRecievers, 0, row);


		/******Propagation*****/
		row++;		
		Label propogationLabel = new Label("Propogation"); 
		propogationLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(propogationLabel, 4);
		mainPane.add(propogationLabel, 0, row);

		row++;
		HBox propogationHolder = new HBox();
		propogationHolder.setAlignment(Pos.CENTER);
		propogationHolder.setSpacing(5); 
//		mainPane.add(new Label(" Spreading = "), 0, row);
		spreading = new Spinner<Double>(0.,20.,20.,0.5); 
		styleSpinner(spreading);
//		mainPane.add(spreading, 1, row);
//		mainPane.add(new Label("*log10(R) +"), 2, row);
		absorbption= new Spinner<Double>(0.000000001,300.,0.04,0.01); 
		styleSpinner(absorbption);
//		mainPane.add(absorbption, 3, row);
//		mainPane.add(new Label("*R"), 4, row);
		
		propogationHolder.getChildren().addAll(new Label(" Spreading = "), spreading, new Label("*log10(R) +"),
				absorbption, new Label("*R")); 
		
		GridPane.setColumnSpan(propogationHolder, 5);
		mainPane.add(propogationHolder, 0, row);

		/*******Noise*******/
		row++; 
		Label noiseLabel = new Label("Noise"); 
		noiseLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(noiseLabel, 4);
		mainPane.add(noiseLabel, 0, row);

		row++; 
		mainPane.add(new Label("Noise Threshold"), 0, row);

		minNoise = new Spinner<Double>(0.,300.,100.,1.); 
		mainPane.add(minNoise, 1, row);
		styleSpinner(minNoise);
		
		mainPane.add(new Label(SimpleOdontocetePane.dB), 2, row);

		
		/*******Animal*******/
		row++; 
		Label animalLabel = new Label("Animal"); 
		animalLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(animalLabel, 5);
		mainPane.add(animalLabel, 0, row);

		row++; 
		Button animals = new Button("Animal"); 		
		animals.setOnAction((action)->{
			//open the animal pane. 
			
		});
		animals.setGraphic(GlyphsDude.createIcon(MaterialIcon.SETTINGS, "25")); 
		animals.prefWidthProperty().bind(mainPane.widthProperty());
		animals.setMaxWidth(500);
		GridPane.setColumnSpan(animals, 5);
		mainPane.add(animals, 0, row);
		animals.setOnAction((action)->{
			probDetSim.getProbDetSimView().openAnimalDialog(); 
		});

		VBox sidePane = new VBox(); 
		sidePane.setSpacing(5);
		sidePane.getChildren().add(mainPane); 
		
		return sidePane; 

	}
	
	/**
	 * Set default spinner look and size. 
	 * @param spinner - the spinner to set look and feel for. 
	 */
	public static void styleSpinner(Spinner spinner) {
		spinner.setEditable(true);
		spinner.setPrefWidth(spinnerWidth);
		//HACK: spinner don;t change value when typed in. This forces change
		spinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				spinner.increment(0); // won't change value, but will commit editor
			}
		});
	}
	
	/**
	 * Set all parameter in the view. 
	 * @param settings - the parameter class to set.
	 */
	public void setParams(ProbDetSimSettings settings) {
		
		nBootSpinner.getValueFactory().setValue(settings.nBootStraps);
		nRunnerSpinner.getValueFactory().setValue(settings.nRuns);

		depthBin.getValueFactory().setValue(settings.depthBin);
		maxDepth.getValueFactory().setValue(settings.minHeight);
		
		rangeBin.getValueFactory().setValue(settings.rangeBin);
		maxRange.getValueFactory().setValue(settings.maxRange);
		
		minNoise.getValueFactory().setValue(settings.noiseThreshold);
		minRecievers.getValueFactory().setValue(settings.minRecievers);
		
		//TODO- eventually this will have it's own pane. 
		spreading.getValueFactory().setValue(((SimplePropogation) settings.propogation).spreading);
		absorbption.getValueFactory().setValue(((SimplePropogation) settings.propogation).absorption);
		
	}
	
	/**
	 * Get all params from the settings pane . 
	 * @param settings - the paramter class to setting in
	 * @return the changed parameter class. 
	 */
	public ProbDetSimSettings getParams(ProbDetSimSettings settings) {
		
		settings.nBootStraps=nBootSpinner.getValue(); 
		settings.nRuns=this.nRunnerSpinner.getValue(); 
		
		settings.depthBin=this.depthBin.getValue();
		settings.minHeight=this.maxDepth.getValue();

		settings.rangeBin=this.rangeBin.getValue();
		settings.maxRange=this.maxRange.getValue();
		
		settings.noiseThreshold=this.minNoise.getValue(); 
		
		settings.minRecievers=this.minRecievers.getValue();
		
		//TODO - eventually propogation will have it's own pane etc. 
		settings.propogation = new SimplePropogation(this.spreading.getValue(), this.absorbption.getValue());
		
		//TODO

		return settings; 
	}

	/**
	 * 
	 * @param enable
	 */
	public void enableControls(boolean enable) {
		// TODO Auto-generated method stub
	}

}
