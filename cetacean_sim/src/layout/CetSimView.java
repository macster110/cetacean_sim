package layout;

import cetaceanSim.CetSimControl;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import utils.CSVReader;

public class CetSimView extends BorderPane {

	/**
	 * Reference to the control
	 */
	private CetSimControl csControl;
	
	/**
	 * Shows a 3D view of the MAP. 
	 */
	private MapPane3D mapPane;

	public CetSimView(CetSimControl csControl) {
		this.csControl= csControl;
		this.setCenter(mapPane=new MapPane3D(this));
		
		double[][] bathyData=CSVReader.readCSV(); 
		System.out.println("Bathy data: "+ bathyData.length);
	}

}
