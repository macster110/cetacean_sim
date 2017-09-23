package layout.simulation;

import animal.SimpleOdontocete;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.CetSimView;
import simulation.ProbDetSimSettings;

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


	private Spinner<Double> rangeBin;


	private Spinner<Double> depthBin;

	/**
	 * The side pane. 
	 */
	private VBox sidePane;


	private Spinner<Double> minNoise;


	private Spinner<Double> noiseBin;


	private Spinner<Double> maxNoise;


	private Spinner<Double> spreading;


	private Spinner<Double> absorbption;
	
	/**
	 * Default width of the spinner. 
	 */
	public static int spinnerWidth=60; 

	
	public ProbDetSettingsPane() {
		this.setCenter(createSidePane()); 
	}
	
	/**
	 * Create the side pane. 
	 */
	private Pane createSidePane() {


		int row=0; 

		GridPane  mainPane = new GridPane(); 
		mainPane.setHgap(5);
		mainPane.setVgap(5);


		Label simLabel = new Label("Simulation Runs"); 
		simLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(simLabel, 4);
		mainPane.add(simLabel, 0, row);

		row++; 
		mainPane.add(new Label("No. Runs"), 0, row);
		nRunnerSpinner= new Spinner<Integer>(10,50000000,50000,10000); 
		GridPane.setColumnSpan(nRunnerSpinner, 2);
		styleSpinner(nRunnerSpinner);
		nRunnerSpinner.setPrefWidth(170);
		mainPane.add(nRunnerSpinner, 1, row);

		row++; 
		mainPane.add(new Label("No. Boot"), 0, row);
		nBootSpinner= new Spinner<Integer>(10,50000000,100,25); 
		GridPane.setColumnSpan(nBootSpinner, 2);
		styleSpinner(nBootSpinner);
		nBootSpinner.setPrefWidth(170);
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
		mainPane.add(new Label("bin"), 2, row);
		rangeBin = new Spinner<Double>(0.,50000000.,25.,2.); 
		styleSpinner(rangeBin);
		mainPane.add(rangeBin, 3, row);

		row++;
		mainPane.add(new Label("Depth: Max"), 0, row);
		maxDepth= new Spinner<Double>(-50000000.,50000000.,-200.,10.); 
		styleSpinner(maxDepth);
		mainPane.add(maxDepth, 1, row);
		mainPane.add(new Label("bin"), 2, row);
		depthBin= new Spinner<Double>(0.,50000000.,10.,2.); 
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
		GridPane.setColumnSpan(recievers, 6);
		mainPane.add(recievers, 0, row);


		/******Propagation*****/
		row++;		
		Label propogationLabel = new Label("Propogation"); 
		propogationLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(propogationLabel, 4);
		mainPane.add(propogationLabel, 0, row);

		row++;
		HBox propogationHolder = new HBox();
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
		
		GridPane.setColumnSpan(propogationHolder, 6);
		mainPane.add(propogationHolder, 0, row);

		/*******Noise*******/
		row++; 
		Label noiseLabel = new Label("Noise"); 
		noiseLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(noiseLabel, 4);
		mainPane.add(noiseLabel, 0, row);

		row++; 
		mainPane.add(new Label("Noise: min"), 0, row);

		minNoise = new Spinner<Double>(0.,300.,85.,1.); 
		mainPane.add(minNoise, 1, row);
		styleSpinner(minNoise);

		mainPane.add(new Label("bin"), 2, row);
		noiseBin= new Spinner<Double>(0.0001,300.,1.,0.5); 
		styleSpinner(noiseBin);
		mainPane.add(noiseBin, 3, row);

		mainPane.add(new Label("max"), 4, row);
		maxNoise = new Spinner<Double>(0.,300,150.,1.); 
		styleSpinner(maxNoise);
		mainPane.add(maxNoise, 5, row);

		/*******Animal*******/
		row++; 
		Label animalLabel = new Label("Animal"); 
		animalLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(animalLabel, 6);
		mainPane.add(animalLabel, 0, row);

		row++; 
		Button animals = new Button("Animal"); 		
		animals.setOnAction((action)->{
			//open the animal pane. 
			
		});
		animals.setGraphic(GlyphsDude.createIcon(MaterialIcon.SETTINGS, "25")); 
		animals.prefWidthProperty().bind(mainPane.widthProperty());
		animals.setMaxWidth(500);
		GridPane.setColumnSpan(animals, 6);
		mainPane.add(animals, 0, row);

		VBox sidePane = new VBox(); 
		sidePane.setSpacing(5);
		sidePane.getChildren().add(mainPane); 
		
		return sidePane; 

	}
	
	/**
	 * Set default spinner look and size. 
	 * @param spinner - the spinner to set look and feel for. 
	 */
	private void styleSpinner(Spinner spinner) {
		spinner.setEditable(true);
		spinner.setPrefWidth(spinnerWidth);
	}
	
	/**
	 * Set all paramter in the view. 
	 * @param settings - the parameter class to set.
	 */
	public void setParams(ProbDetSimSettings settings) {


	}
	
	/**
	 * Get all params from the settings pane . 
	 * @param settings - the paramter class to setting in
	 * @return the changed parameter class. 
	 */
	public ProbDetSimSettings getParams(ProbDetSimSettings settings) {
		
		settings.simpleOdontocete.setUpAnimal(SimpleOdontocete.SIM_UNIFORM_DEPTH_HORZ, settings);

		return settings; 

	}

}