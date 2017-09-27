package layout.simulation;

import cetaceanSim.CetSimControl;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.CetSimView;
import layout.Map3DManager;
import layout.MapPane3D;
import layout.animal.AnimalPane;
import layout.bathymetry.BathymetryPane;
import layout.reciever.RecieverPane;
import simulation.surveySim.SurveySim;

/**
 * The P
 * @author Jamie Macaulay
 *
 */
public class SurveySimView implements SimulationView {
	
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

	private CetSimView cetSimView;
	
	/**
	 * The centger pane. 
	 */
	private BorderPane centerPane; 
	
	/**
	 *Holds settings for the side pane. 
	 */
	private VBox sidePane;

	/**
	 * Reference to the simulation. 
	 */
	private SurveySim surveySim;

	/**
	 * Pane which handles what recievers are used in the simulation. 
	 */
	private RecieverPane recieverPane;
	
	
	public SurveySimView(SurveySim surveySim, CetSimView cetSimView){
		this.surveySim=surveySim;
		this.cetSimView=cetSimView;
		createSurveySimView() ; 
	}
	
	/**
	 * Create the 
	 */
	private void createSurveySimView() {
		
		//this.setStyle("-fx-font: 12px Ubuntu;");
		//initilize all the various panes. 
		mapPane= new MapPane3D(cetSimView); 
		mapManager= new Map3DManager(cetSimView, mapPane); 
		centerPane= new BorderPane(); 
		centerPane.setCenter(mapPane);
		
		sidePane=new VBox();
		sidePane.setSpacing(5);
		sidePane.setPrefWidth(300);
		sidePane.setMinWidth(250);
	
		//Bathymetry
		Label bathyTitle=new Label("Bathymetry Data");
		bathyTitle.setFont(new Font("Ubuntu",CetSimView.titleFontSize));
		sidePane.getChildren().addAll(bathyTitle, bathyPane= new BathymetryPane(surveySim.getBathymetryManager(), cetSimView)); 

		//Animals
		Label animalTitle=new Label("Animal Model");
		animalTitle.setFont(new Font("Ubuntu",CetSimView.titleFontSize));
		sidePane.getChildren().addAll(animalTitle, animalPane= new AnimalPane(surveySim.getAnimalManager(), cetSimView)); 
		
		//Recievers
		Label recieverTitle=new Label("Reciever Model");
		animalTitle.setFont(new Font("Ubuntu",CetSimView.titleFontSize));
		sidePane.getChildren().addAll(recieverTitle, recieverPane= new RecieverPane(surveySim.getRecieverManager(), cetSimView)); 
		
		
		
	}


	@Override
	public Node getSidePane() {
		return sidePane;
	}


	@Override
	public Node getCenterPane() {
		return centerPane;
	}


	@Override
	public void notifyUpdate(int updateType) {
		switch(updateType){
		case CetSimControl.BATHY_LOADED:
			this.mapManager.updateProviders(surveySim.getSimModels());
			break; 
		case CetSimControl.SIM_DATA_CHANGED:
			break; 
		case CetSimControl.LOAD_DATA_START:
			break; 
		case CetSimControl.LOAD_DATA_END:
			break; 
		case CetSimControl.PROGRESS_UPDATE:
			break; 
		}
		
	}

}
