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
	 * The pane which holds the probability simulation. 
	 */
	private BorderPane map;

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

	private Label progressLabel1;

	private Label progressLabel2;

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
		
		//progress bars for the simualtion.
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
	 * Center pane shows the current graph. 
	 */
	private void createCenterPane() {
		StackPane pane = new StackPane(); 
		
		map = new BorderPane(); 
		
		Pane controlPane = createSimControlPane(); 
		controlPane.setPadding(new Insets(10,10,10,10));
		controlPane.prefWidthProperty().bind(map.widthProperty());
		//controlPane.setPrefWidth(200);
		StackPane.setAlignment(controlPane, Pos.TOP_LEFT);
		
		pane.getChildren().addAll(map, controlPane);

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
			break;
		}

	}
	
	/**
	 * Set all paramter in the view. 
	 * @param settings - the parameter class to set.
	 */
	public void setParams(ProbDetSimSettings settings) {
		settingsPane.setParams(settings); 

	}
	
	/**
	 * Get all params in the view before the simulation is run. 
	 * @param settings - the paramter calss to change. 
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
		this.progressLabel2.setText("Sim. Progress: " + 100*simProg + "%");
		this.progressBar2.setProgress(simProg); ;

	}
	


}
