package layout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import layout.utils.SurfacePlot;
import layout.utils.Utils3D;

/**
 * Shows a 3D map. Bathymetry etc can be added.  
 * @author Jamie Macaulay 
 *
 */
public class MapPane3D extends Pane3D {
	
	public MapPane3D(CetSimView simControl) {
		super(); 
		createAxes(getDynamicGroup());
	}

	/**
	 * Create the axis for the Map. Shows x, y and z directions. 
	 * @param sceneRoot
	 */
	private void createAxes(Group sceneRoot) {
		 Group axis = Utils3D.buildAxes(300.,Color.DARKRED, Color.RED,
				 Color.DARKGREEN, Color.GREEN,
				 Color.CYAN, Color.BLUE,
				 Color.WHITE); 
		 sceneRoot.getChildren().add(axis); 
	}
	

	/**
	 * Create a surface for the map from set of scatterred points
	 * @param surface - list of 3D points.
	 * @return - surface fitted to 3D points. 
	 */
	public SurfacePlot createSurface(float[][] surface){
		return new SurfacePlot(surface, 20, Color.BLACK, Color.DARKGRAY, true, false);
	}
}
