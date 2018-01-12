package layout.simulation;


import java.io.File;

import animal.SimpleOdontocete;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import layout.Pane3D;
import layout.animal.SimpleOdontocetePane;
import layout.reciever.ArrayPane;
import layout.utils.Utils3D;
import reciever.HydrophoneArray;
import simulation.StatusListener;
import simulation.probdetsim.ProbDetSim;
import simulation.probdetsim.ProbDetSimSettings;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;
import utils.Hist3;

/**
 * View for probability of detection simualtion views. 
 * @author Jamie Macaulay 
 *
 */
public class ProbDetSimView implements SimulationView {

	/**
	 * Main holder pane.
	 */
	private StackPane centerPane;

	/**
	 * Start the simulation
	 */
	private Button play;

	/**
	 * Export the simulation results to a file
	 */
	private Button export;


	/**
	 * Reference to the simulation. 
	 */
	private ProbDetSim probDetSim;

	/**
	 * Settings pane. 
	 */
	private ProbDetSettingsPane settingsPane;

	/**
	 * Progress bar 2
	 */
	private ProgressBar progressBar1;

	/**
	 * Progress bar 1
	 */
	private ProgressBar progressBar2;

	/**
	 * Pane which sits at the top of the center display and shows to progress bars. 
	 */
	private VBox progressVBox;

	/**
	 * Label which shows progress information on total number of bootstraps for the simualtion
	 */
	private Label customLabel;

	/**
	 * Label which shows progress information on total number of bootstraps for the simualtion
	 */
	private Label progressLabel1;

	/**
	 * Label which shows progress information on simulation
	 */
	private Label progressLabel2;

	/**
	 * 3D pane which shows graphs. 
	 */
	private Pane3D pane3D;

	/**
	 * The animal pane. 
	 */
	private SimpleOdontocetePane animalPane;

	/**
	 * Holds the hydrophone pane. 
	 */
	private ArrayPane recieverPane;

	/**
	 * Constructor for the probability of detection view. 
	 * @param probDetSim - reference to the ProbDetSim. 
	 */
	public ProbDetSimView(ProbDetSim probDetSim) {
		this.probDetSim=probDetSim; 
		this.settingsPane = new ProbDetSettingsPane(probDetSim); 
		createCenterPane(); 
		
		//the animal pane
		this.animalPane = new SimpleOdontocetePane(); 
		probDetSim.getProbDetSettings().simpleOdontocete.setUpAnimal(
				SimpleOdontocete.SIM_UNIFORM_DEPTH_HORZ, probDetSim.getProbDetSettings());
		animalPane.setParams(probDetSim.getProbDetSettings().simpleOdontocete, false);
		
		//the reciever pane. 
		this.recieverPane = new ArrayPane(); 
		recieverPane.setParams(probDetSim.getProbDetSettings().recievers, false);
	}
	
	
	/**
	 * Create pane to start and stop the simulation. 
	 * @return control pane. 
	 */
	private Pane createSimControlPane() {
		HBox hBox= new HBox();
		hBox.setSpacing(5); 

		//button to start thge simualtion 
		play = new Button(); 
		setPlayButtonGraphic(false);

		play.setOnAction((action)->{
			if (this.probDetSim.isRunning()) {
				probDetSim.runSim(false); 
			}
			else {
				probDetSim.runSim(true); 
			}
		});

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("MATLAB Files", "*.mat"));

		export = new Button(); 
		export.setGraphic(GlyphsDude.createIcon(MaterialIcon.SAVE, "25"));
		export.setOnAction((action)->{
			File selectedFile = fileChooser.showSaveDialog(hBox.getScene().getWindow());
			if (selectedFile != null) {
				probDetSim.saveProbDetData(selectedFile);
			}
		});

		//progress bars for the simulation.
		progressBar1= new ProgressBar(); 
		progressBar2= new ProgressBar(); 

		progressVBox = new VBox(); 
		progressVBox.setSpacing(5);
		progressVBox.getChildren().addAll(customLabel= new Label(), progressLabel1= new Label("No. Boostraps"),  progressBar1,
				progressLabel2= new Label("Sim. Progress"),  progressBar2); 
		HBox.setHgrow(progressVBox, Priority.ALWAYS);
		progressVBox.setVisible(false); 

		progressBar1.prefWidthProperty().bind(progressVBox.widthProperty());
		progressBar2.prefWidthProperty().bind(progressVBox.widthProperty());

		hBox.getChildren().addAll(play, export, progressVBox);

		return hBox; 
	}

	
	/**
	 * Centre pane shows the current graph. 
	 */
	private void createCenterPane() {
		StackPane pane = new StackPane(); 

		BorderPane plotPane = new BorderPane(); 

		pane3D = new Pane3D(); 
		pane3D.setCache(true);
		pane3D.setCacheHint(CacheHint.QUALITY);

		plotPane.setCenter(pane3D);

		//		////TEMP////
		//		pane3D.getDynamicGroup().getChildren().add(Utils3D.buildAxes(100, Color.RED, "x", "y", "z", Color.WHITE)); 
		//		ColouredSurfacePlot surfacePlot = new ColouredSurfacePlot(null, null, ColouredSurfacePlot.createTestData(400, 100.)); 
		//		pane3D.getDynamicGroup().getChildren().add(surfacePlot);
		//		Line line = new Line(0,0,100,100); 
		//		pane3D.getRootGroup().getChildren().add(line); 
		//		//pane3D.getRootGroup().getChildren().add(Utils3D.buildAxes(100, Color.CYAN, "x", "y", "z", Color.WHITE)); 
		//		///////////

		Pane controlPane = createSimControlPane(); 
		controlPane.setPadding(new Insets(10,10,10,10));
		controlPane.prefWidthProperty().bind(plotPane.widthProperty());
		//controlPane.setPrefWidth(200);
		StackPane.setAlignment(controlPane, Pos.TOP_LEFT);
		controlPane.setMaxHeight(50);
		//controlPane.setStyle("-fx-background-color: cornsilk;");

		pane.getChildren().addAll(plotPane, controlPane);

		this.centerPane = pane;
	}

	@Override
	public Node getSidePane() {
		return settingsPane;
	}

	@Override
	public Node getCenterPane() {
		return centerPane;
	}


	/**
	 * Set the play button graphic.
	 */
	private void setPlayButtonGraphic(boolean running) {
		Platform.runLater(()->{
			if (running) {
				play.setGraphic(GlyphsDude.createIcon(MaterialIcon.PAUSE, "25"));
			}
			else {
				play.setGraphic(GlyphsDude.createIcon(MaterialIcon.PLAY_ARROW, "25"));
			}
		});
	}


	/**
	 * Called whenever the simualtion starts or starts running.
	 */
	private void setSimControls(boolean running) {
		setPlayButtonGraphic(running);  
		progressVBox.setVisible(running);
	}

	@Override
	public void notifyUpdate(int updateType) {
		Platform.runLater(()->{
			switch (updateType) {
			case StatusListener.SIM_STARTED:
				//reset the custom label. 
				setSimControls(true); 
				break;
			case StatusListener.SIM_FINIHSED:
				setSimControls(false); 
				displaySimResults(probDetSim.getProbDetResults()); 
				//customLabel.setText(""); 
				break;
			}
		});
	}


	/**
	 * Display the results of the simualtion
	 * @param probDetResults - the results for the simulation
	 */
	private void displaySimResults(ProbDetResult probDetResults) {
		pane3D.getDynamicGroup().getChildren().clear();

		//ProbDetMonteCarlo.printResult(probDetResults.probSurfaceMean.getHistogram()); 
		
		if (probDetResults==null || probDetResults.probSurfaceMean==null) return; 

		float[][] surface = Utils3D.double2float(probDetResults.probSurfaceMean.getHistogram()); 
		float[][] Xq=Hist3.getXYSurface(probDetResults.probSurfaceMean.getXbinEdges(), probDetResults.probSurfaceMean.getYbinEdges(), true); 
		float[][] Yq=Hist3.getXYSurface(probDetResults.probSurfaceMean.getXbinEdges(), probDetResults.probSurfaceMean.getYbinEdges(), false); 

		ColouredSurfacePlot surfacePlot = new ColouredSurfacePlot(Xq, Yq, surface); 
		surfacePlot.setAxisNames("range (m)", "depth(m)", "p detection"); 

		pane3D.getDynamicGroup().getChildren().add(surfacePlot); 

	}


	/**
	 * Set all parameter in the view. 
	 * @param settings - the parameter class to set.
	 */
	public void setParams(ProbDetSimSettings settings) {
		settingsPane.setParams(settings); 

	}

	/**
	 * Get all parameters in the view before the simulation is run. 
	 * @param settings - the parameter class to change. 
	 */
	public ProbDetSimSettings getParams(ProbDetSimSettings settings) {
		
		settings=this.settingsPane.getParams(settings);
		settings.simpleOdontocete=this.animalPane.getParams();
		settings.recievers=this.recieverPane.getParams();

		return settings; 
	}

	/**
	 * Set the progress of the progress bar.s
	 * @param bootstraps - the number of bootstraps completed
	 * @param simProb - the progress of the current simulation. 
	 */
	public void setProgress(int bootstraps, double simProg) {
		this.progressLabel1.setText("No. Bootstraps: " + bootstraps + " of " +  probDetSim.getNBootstraps());
		this.progressBar1.setProgress( bootstraps/(double) this.probDetSim.getNBootstraps());
		this.progressLabel2.setText("Sim. Progress: " + String.format("%.1f",100*simProg) + "%");
		this.progressBar2.setProgress(simProg); ;

	}

	/**
	 * Get the prob det sim. 
	 * @return the prob det sim. 
	 */
	public ProbDetSim getProbDetSim() {
		return this.probDetSim;
	}

	/**
	 * Set custom message in the progress pane. 
	 * @param text - the custom progress message
	 */
	public void setCustomProgressText(String text) {
		this.customLabel.setText(text);
	}

	/**
	 * Enable all the controls on the GUI
	 * @param  true to enable the controls. False to disbale all controls. 
	 */
	public void enableControls(boolean enable) {
		this.settingsPane.enableControls(enable);
		
	}
	
	/**
	 * Open the reciever dialog.
	 */
	public void openRecieverDialog() {
		
		Dialog<HydrophoneArray> recieverDialog = new Dialog<>();
		recieverDialog.setTitle("Reciever Settings");
		recieverDialog.setResizable(true);
		DialogPane dPane = new DialogPane(); 
		dPane.setContent(this.recieverPane);
		recieverPane.setPrefSize(300, 300);
		recieverDialog.setDialogPane(dPane);
		
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		recieverDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		
		
		recieverDialog.setResultConverter(new Callback<ButtonType, HydrophoneArray>() {
			@Override
			public HydrophoneArray call(ButtonType b) {

				if (b == buttonTypeOk) {
					return recieverPane.getParams();
				}
				return null;
			}
		});

		recieverDialog.showAndWait().ifPresent(response -> {
			if (response!=null) {
				
			}
		});
		
	} 

	/**
	 * Open the animal dialog.
	 */
	public void openAnimalDialog() {
		
		Dialog<SimpleOdontocete> animalDialog = new Dialog<>();
		animalDialog.setTitle("Animal Settings");
		DialogPane dPane = new DialogPane(); 
		dPane.setContent(this.animalPane);
		animalPane.setPrefSize(900, 800);
		animalPane.setParams(probDetSim.getProbDetSettings().simpleOdontocete, false);
		animalDialog.setDialogPane(dPane);
		
		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		animalDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		
		
		animalDialog.setResultConverter(new Callback<ButtonType, SimpleOdontocete>() {
			@Override
			public SimpleOdontocete call(ButtonType b) {

				if (b == buttonTypeOk) {
					return animalPane.getParams();
				}
				return null;
			}
		});

		animalDialog.showAndWait().ifPresent(response -> {
			if (response!=null) {
				this.probDetSim.getProbDetSettings().simpleOdontocete=response;
			}
		});
		
	} 

}
