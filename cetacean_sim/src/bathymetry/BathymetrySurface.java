package bathymetry;

import cetaceanSim.SimUnit;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import layout.MapProjector3D;
import layout.MapShapeProvider;
import layout.utils.SurfacePlot;

/**
 * Class for creating a bathymetry surface. 
 * @author Jamie Macaulay 
 *
 */
public class BathymetrySurface extends Group  implements MapShapeProvider {
	
	/**
	 * The main group for all bathymetry related shapes. 
	 */
	private Group bathySurface;
	
	/**
	 * The actual bathymetry surface
	 */
	private SurfacePlot surfacePlot;

	/**
	 * Reference to the sim unit. 
	 */
	private BathymetryModel bathyModel; 

	/**
	 * Create a bathymetry surface for a bathymetry model. 
	 * @param bathyModel - the bathymetry model whihc uses the map provider
	 */
	public BathymetrySurface(BathymetryModel bathyModel){
		this.bathyModel=bathyModel; 
		bathySurface=new Group(); 
	}

	/**
	 * New bathymetry surface. 
	 * @param data - the new bathymetry data
	 * @param mapProjector - the map projector to convert between co-ordinates. 
	 */
	public void newBathySurface(BathyData data){
		bathySurface.getChildren().clear(); //clear all current children. 
		surfacePlot =new SurfacePlot(data.getBathySurface(), 10, Color.BLACK, Color.GREY, false, false);
		bathySurface.getChildren().add(surfacePlot); 
	}
	

	@Override
	public boolean shapeUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Group getMapShapes() {
		return bathySurface;
	}

	@Override
	public SimUnit getSimModel() {
		return bathyModel;
	}
	
	

}
