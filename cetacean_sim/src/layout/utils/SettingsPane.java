package layout.utils;

import javafx.scene.Node;

/**
 * Settings pane. 
 * @author Jamie Macaulay
 *
 */
public interface SettingsPane {

	
	/**
	 * Get settings from the pane.
	 * @return settings class
	 */
	public void getParams(); 
	
	/**
	 * Called whenever the pane is first shown/open to set pane to show current settings.
	 * @param input- current settings class.
	 */
	public void setParams();

	/**
	 * Get the name of the pane.
	 * @return name of the pane
	 */
	public String getName();
	
	/**
	 * Get node for GUI chnage of settings. 
	*/
	public Node getContentNode(); 

	/**
	 * Called when settings pane is first initialised. This can be used if for example, a the size of a pane is needed for a param. 
	 * (Sizes are only initialised when Nodes are shown); 
	 */
	public void paneInitialized(); 

}
