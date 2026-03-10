package layout.utils;

import javafx.scene.Node;

/**
 * Settings pane. 
 * @author Jamie Macaulay
 *
 */
public interface SettingsPane<T> {

	
	/**
	 * Get settings from the pane.
	 * @return settings class
	 */
	public T getParams(); 
	
	/**
	 * Called whenever the pane is first shown/open to set pane to show current settings.
	 * @param settingsData- current settings class.
	 * @parma clone - true to clone the settings class. This means any changes that take place do not effect main settings class. Use this with dialogs. 
	 */
	public void setParams(T settingsData, boolean clone);

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
