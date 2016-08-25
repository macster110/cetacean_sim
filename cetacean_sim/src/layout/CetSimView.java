package layout;

import cetaceanSim.CetSimControl;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.bathymetry.BathymetryPane;
import utils.BathymetryUtils;
import utils.CSVReader;

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
	 * Updates from other panes are sent here. 
	 * @param bathyLoaded - the update flag. 
	 */
	public void notifyUpdate(int flag) {
		cSControl.notifyUpdate(flag);
		switch(flag){
		case CetSimControl.BATHY_LOADED:
			break; 
		
		}
		
	}

}
