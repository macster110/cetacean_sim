package layout;

import cetaceanSim.SimUnit;
import javafx.scene.Group;

public interface MapShapeProvider {
	
	/**
	 * Determine whether shapes are available to be displayed on the map/do the current shapes need updated. 
	 * @return true to add or update shapes on the map. 
	 */
	public boolean shapeUpdate(); 
	
	/**
	 * The group of shpaes to display on the map. 
	 * @return the shapes to display on the map. 
	 */
	public Group getMapShapes();
	
	/**
	 * Get the simulation componenet that the map provider belongs ot. 
	 * @return the cet sim model the map provider belongs to. 
	 */
	public SimUnit getSimModel();
		
	

}
