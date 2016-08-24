package bathymetry;

import layout.utils.SettingsPane;

/**
 * The bathymetry type. 
 * @author Jamie Macaulay
 *
 */
public interface BathymetryModel {
	
	/**
	 * Called to chnage settings. May open a dialog. 
	 *  
	 */
	public void loadBathy(); 
	
	/**
	 * Get the bathymetry surface
	 * @return
	 */
	public float[][] getDepthSurface();
	
	/**
	 * Get a the depth at a certain point
	 * @param x the x point
	 * @param y the y point 
	 * @return the depth at x and y.
	 */
	public double getDepth(double x, double y);

	/**
	 * Get the name of the bathymetry type.  
	 * @return the name of the bathymetry type
	 */
	public String getName();

	/**
	 * The settings pane
	 * @return
	 */
	public SettingsPane getSettingsPane(); 

}
