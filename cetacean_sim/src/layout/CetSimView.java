package layout;

import cetaceanSim.CetSimControl;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import simulation.SimulationType;

/**
 * The GUI for the CetSimView. 
 * @author Jamie Macaulay
 *
 */
public class CetSimView extends BorderPane {
	
	/**
	 * Shows progress. 
	 */
	private CetStatusPane loadPane;

	private CetSimControl cSControl;

	private VBox sidePane;

	private StackPane centerHolder;

	private VBox sideHolder;
	
	/**
	 * Default font size ofr titles. 
	 */
	public final static int titleFontSize=16;   


	public CetSimView(CetSimControl csControl) {
		
		this.cSControl= csControl;
		
		//finally add the map pane. 
		centerHolder=new StackPane(); 
		
		//holds the side panes
		sidePane= new VBox(); 
		sidePane.setPadding(new Insets(10,0,0,10)); 
		sidePane.setSpacing(5);
		
		Label simSelectLabel = new Label("Select Simultation");
		simSelectLabel.setFont(new Font(titleFontSize));; 
		
		ComboBox<String> simulationSelect= new ComboBox<String>(); 
		for (int i=0; i<csControl.getSimulationTypes().size(); i++) {
			simulationSelect.getItems().add(csControl.getSimulationTypes().get(i).getName()); 
		}
		simulationSelect.setOnAction((action)->{
			csControl.setSimulation(simulationSelect.getSelectionModel().getSelectedIndex()); 
		});
		
		//holds the GUI from the simulation
		sideHolder= new VBox();
		
		sidePane.getChildren().addAll(simSelectLabel, simulationSelect, new Separator(Orientation.HORIZONTAL), sideHolder); 

	
		SplitPane sp = new SplitPane();
		sp.getItems().add(sidePane);
		sp.getItems().add(centerHolder);
		sp.setDividerPositions(0.2f, 0.8f);
		 
		this.setCenter(sp);
		
	}
	
	
	/**
	 * Get CetSimControl. Controls the actual simulation. 
	 * @return the CetSimControl for this view. 
	 */
	public CetSimControl getCetSimControl() {
		return cSControl;
	}

	/**
	 * Updates from other panes are sent here. 
	 * @param bathyLoaded - the update flag. 
	 */
	public void notifyUpdate(int flag) {
		switch(flag){
		case CetSimControl.BATHY_LOADED:
			break; 
		case CetSimControl.SIM_DATA_CHANGED:
			break; 
		case CetSimControl.LOAD_DATA_START:
			loadPane.setProgress(-1);
			loadPane.setMessage(cSControl.getCurrentMessage());
			loadPane.setVisible(true);
			break; 
		case CetSimControl.LOAD_DATA_END:
			loadPane.setMessage(cSControl.getCurrentMessage());
			loadPane.setVisible(false);
			break; 

		case CetSimControl.PROGRESS_UPDATE:
			loadPane.setMessage(cSControl.getCurrentMessage());
			break; 
		}
		this.cSControl.getCurrentSimulation().getSimView().notifyUpdate(flag); 
	}
	
	/**
	 * Add a stus pane; 
	 * @param stackPane
	 */
	private void addStatusPane(StackPane stackPane){
		loadPane =new CetStatusPane();
		StackPane.setAlignment(loadPane, Pos.TOP_RIGHT);
	    StackPane.setMargin(loadPane, new Insets(10,10,0,0));
		stackPane.getChildren().addAll(loadPane);
		loadPane.setVisible(false);
	}
	
	/**
	 * Add a play and stop button.s 
	 * @param stackPane
	 */
	private void addProcessButtons(StackPane stackPane){
//		
		final Button batchProcess=new Button("",GlyphsDude.createIcon(MaterialIcon.PLAY_ARROW, "25")); 
		batchProcess.setOnAction((action)->{
			cSControl.startSimulation(); 
		});
		StackPane.setAlignment(batchProcess, Pos.TOP_LEFT);
	    StackPane.setMargin(batchProcess, new Insets(10,0,0,10));

		final Button stopProcess=new Button("", GlyphsDude.createIcon(MaterialIcon.STOP,"25")); 
		stopProcess.setOnAction((action)->{
			cSControl.stopSimulation(); 
		});
		stopProcess.prefHeightProperty().bind(batchProcess.heightProperty());
		StackPane.setAlignment(stopProcess, Pos.TOP_LEFT);
	    StackPane.setMargin(stopProcess, new Insets(10,0,0,60));
	

		stackPane.getChildren().addAll(batchProcess, stopProcess);
		
	}


	public void setSimMessage(String string) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Set the current simulation. This changes the GUI to the current simulation
	 * @param simulationType - the current simulation to set. 
	 */
	public void setSimulation(SimulationType simulationType) {
		this.centerHolder.getChildren().clear();
		this.sideHolder.getChildren().clear();

		if (simulationType.getSimView()!=null) {
			this.centerHolder.getChildren().add(simulationType.getSimView().getCenterPane());
			this.sideHolder.getChildren().add(simulationType.getSimView().getSidePane());

		}
		
	}

}
