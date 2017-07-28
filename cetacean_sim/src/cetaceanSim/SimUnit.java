package cetaceanSim;

import layout.MapShapeProvider;

/**
 * All simulation components must satisfy this mode. 
 * @author Jamie Macaulay
 *
 */
public interface SimUnit {
	
	/**
	 * Get the map shape provider.
	 * @return the map shape provider. Can be null if no provider is present. 
	 */
	public MapShapeProvider getMapShapeProvider(); 

}
