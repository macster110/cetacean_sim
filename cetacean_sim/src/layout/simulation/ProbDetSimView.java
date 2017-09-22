package layout.simulation;


import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import layout.Pane3D;
import layout.utils.Utils3D;
import simulation.ProbDetMonteCarlo.ProbDetResult;
import simulation.ProbDetSim;
import simulation.ProbDetSimSettings;
import simulation.StatusListener;

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
	 * Constructor for the probability of detection view. 
	 * @param probDetSim - reference to the ProbDetSim. 
	 */
	public ProbDetSimView(ProbDetSim probDetSim) {
		this.probDetSim=probDetSim; 
		this.settingsPane = new ProbDetSettingsPane(); 
		createCenterPane(); 
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
				probDetSim.run(false); 
			}
			else {
				probDetSim.run(true); 
			}
		});
	
		export = new Button(); 
		export.setGraphic(GlyphsDude.createIcon(MaterialIcon.SAVE, "25"));
		export.setOnAction((action)->{
			
		});
		
		//progress bars for the simulation.
		progressBar1= new ProgressBar(); 
		progressBar2= new ProgressBar(); 
		
		progressVBox = new VBox(); 
		progressVBox.setSpacing(5);
		progressVBox.getChildren().addAll(progressLabel1= new Label("No. Boostraps"),  progressBar1,
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
		plotPane.setCenter(pane3D);
		
		////TEMP////
		pane3D.getDynamicGroup().getChildren().add(Utils3D.buildAxes(100, Color.RED, "x", "y", "z", Color.WHITE)); 
		ColouredSurfacePlot surfacePlot = new ColouredSurfacePlot(null, null, ColouredSurfacePlot.createTestData(400, 100.)); 
		pane3D.getDynamicGroup().getChildren().add(surfacePlot);
		Line line = new Line(0,0,100,100); 
		pane3D.getRootGroup().getChildren().add(line); 
		//pane3D.getRootGroup().getChildren().add(Utils3D.buildAxes(100, Color.CYAN, "x", "y", "z", Color.WHITE)); 
		///////////
		
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
		switch (updateType) {
		case StatusListener.SIM_STARTED:
			setSimControls(true); 
			break;
		case StatusListener.SIM_FINIHSED:
			setSimControls(false); 
			displaySimResults(probDetSim.getProbDetResults()); 
			break;
		}

	}
	
	
	/**
	 * Display the results of the simualtion
	 * @param probDetResults - the results for the simulation
	 */
	private void displaySimResults(ProbDetResult probDetResults) {
		pane3D.getDynamicGroup().getChildren().clear();
		
		float[][] surface = Utils3D.double2float(probDetResults.probSurfaceMean.getHistogram()); 
		
		ColouredSurfacePlot surfacePlot = new ColouredSurfacePlot(null, null, surface); 
		
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
		
		return settingsPane.getParams(settings); 

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
	


}
