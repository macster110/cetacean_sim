package reciever;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import layout.utils.SettingsPane;

/**
 * The reciever
 * @author Jamie Macaulay
 *
 */
public interface RecieverModel {
	
	/**
	 * Get the name of the reciever. This is a custom name 
	 * @return
	 */
	public StringProperty getStringName(); 
	
	/**
	 * The number of recievers
	 * @return
	 */
	public IntegerProperty getNReceivers(); 


	
	/**
	 * Get the track of the reciever 
	 * @return the track of the reciever.
	 */
	double[][] getTrack(long timeStart, long longTimeEnd, double[][] bathySurface, double[][] tide );
	
	
	/**
	 * The settings pane. Pane to allow settings for the animal to be changed
	 * @return the settings pane for the animal. 
	 */
	public SettingsPane<RecieverModel> getSettingsPane();

}
