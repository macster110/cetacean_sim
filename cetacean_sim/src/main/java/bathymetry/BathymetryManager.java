package bathymetry;

import layout.MapShapeProvider;
import layout.utils.SettingsPane;

/**
 * Simple class which can be used to select what type of bathymetry is set. 
 * @author Jamie Macaulay
 *
 */
public class BathymetryManager {
	
	BathymetryModel bathymetryModel;
	
	/**
	 * Get the bathymetry model
	 * @return
	 */
	public BathymetryModel getBathymetryModel() {
		return bathymetryModel; 
	}
	
	/**
	 * Set the bathymetry model. 
	 * @param bathymetryModel
	 */
	public void setBathymetryModel(BathymetryModel bathymetryModel) {
		this.bathymetryModel=bathymetryModel; 
	}
	
	
	
	
	
}