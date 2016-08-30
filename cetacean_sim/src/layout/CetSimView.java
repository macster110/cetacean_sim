package layout;

import java.util.ArrayList;

import cetaceanSim.CetSimControl;
import cetaceanSim.SimUnit;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.animal.AnimalPane;
import layout.bathymetry.BathymetryPane;

/**
 * The GUI for the CetSimView. 
 * @author Jamie Macaulay
 *
 */
public class CetSimView extends BorderPane {

	/**
	 * Reference to the control
	 */
	private CetSimControl cSControl;
	
	/**
	 * Pane which control abthymetry data. 
	 */
	private BathymetryPane bathyPane;
	
	/**
	 * Shows a 3D view of the MAP. 
	 */
	private MapPane3D mapPane;

	/**
	 * Pane which allows users to select animal models to add to simulation. 
	 */
	private AnimalPane animalPane;

	/**
	 * Hanldes groups/shapes etc. being to to the mpa from different partys of the simulation.
	 	 */
	private Map3DManager mapManager;

	/**
	 * Shows progress. 
	 */
	private CetStatusPane loadPane;
	
	

	public CetSimView(CetSimControl csControl) {
		this.cSControl= csControl;
		
		
		//initilize all the various panes. 
		bathyPane=new BathymetryPane(this);
		
		VBox holder=new VBox();
		holder.setSpacing(5);
		holder.setPadding(new Insets(10,0,0,10)); 
		holder.setPrefWidth(300);
		holder.setMinWidth(300);
	
		//Bathymetry
		Label bathyTitle=new Label("Bathymetry Data");
		bathyTitle.setFont(new Font("Ubuntu",16));
		holder.getChildren().addAll(bathyTitle,bathyPane= new BathymetryPane(this) ); 

		//Animals
		Label animalTitle=new Label("Animal Model");
		animalTitle.setFont(new Font("Ubuntu",16));
		holder.getChildren().addAll(animalTitle,animalPane= new AnimalPane(this) ); 
		
		
		//finally add the map pane. 
		StackPane stackPane=new StackPane(); 
		//stackPane.setMouseTransparent(true);
		stackPane.getChildren().add(mapPane=new MapPane3D(this));
		mapManager= new Map3DManager(this, mapPane); 
		
		
		/**Extra Panes**/
		//create load panel 
		addStausPane(stackPane);
		//add simulation buttons. 
		addProcessButtons(stackPane);

		
		
		SplitPane sp = new SplitPane();
		sp.getItems().add(holder);
		sp.getItems().add(stackPane);
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
	 * Get all the current simulation models. 
	 * @return the current simualtion models. 
	 */
	public ArrayList<SimUnit> getSimUnits(){
		return cSControl.getSimModels();
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
			mapManager.updateProviders();
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

	}
	
	/**
	 * Add a stus pane; 
	 * @param stackPane
	 */
	private void addStausPane(StackPane stackPane){
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

}
