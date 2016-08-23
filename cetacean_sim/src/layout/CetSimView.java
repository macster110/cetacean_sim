package layout;

import cetaceanSim.CetSimControl;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import utils.BathymetryUtils;
import utils.CSVReader;

public class CetSimView extends BorderPane {

	/**
	 * Reference to the control
	 */
	private CetSimControl csControl;
	
	/**
	 * Pane which control abthymetry data. 
	 */
	private BathymetryPane bathyPane;
	
	/**
	 * Shows a 3D view of the MAP. 
	 */
	private MapPane3D mapPane;

	public CetSimView(CetSimControl csControl) {
		this.csControl= csControl;
		
		//initilize all the various panes. 
		bathyPane=new BathymetryPane(this);
		
		VBox holder=new VBox();
		holder.setSpacing(5);
		holder.setPadding(new Insets(10,0,0,10)); 

		
		Label bathyTitle=new Label("Bathymetry Data");
		bathyTitle.setFont(new Font("Ubuntu",16));
		holder.getChildren().addAll(bathyTitle,bathyPane= new BathymetryPane(this) ); 
		holder.setPrefWidth(300);
		holder.setMinWidth(300);

				
		
		//finally add the map pane. 
		
		StackPane stackPane=new StackPane(); 
		//stackPane.setMouseTransparent(true);
		stackPane.getChildren().add(mapPane=new MapPane3D(this));
		
		
		
		SplitPane sp = new SplitPane();
		sp.getItems().add(holder);
		sp.getItems().add(stackPane);
		
		this.setCenter(sp);
		
		
	}
	
	


	/**
	 * Updates from other panes are sent here. 
	 * @param bathyLoaded - the update flag. 
	 */
	public void notifyUpdate(int flag) {
		csControl.notifyUpdate(flag);
		switch(flag){
		case CetSimControl.BATHY_LOADED:
			break; 
		
		}
		
	}

}
