package reciever;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import layout.utils.SettingsPane;
import reciever.RecieverManager.RecieverTypeEnum;


/**
 * A receiver which drifts with the tide. 
 * @author Jamie Macaulay  
 *
 */
public class DriftingReciever implements RecieverModel {
	
	/**
	 * The name property of the reciever 
	 */
	StringProperty recieverName= new SimpleStringProperty("Drifting reciever"); 

	/**
	 * Number of recievers property
	 */
	IntegerProperty nRecievers= new SimpleIntegerProperty(1); 

	@Override
	public double[][] getTrack(long timeStart, long longTimeEnd, double[][] bathySurface, double[][] tide) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SettingsPane<RecieverModel> getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringProperty getStringName() {
		// TODO Auto-generated method stub
		return recieverName;
	}

	@Override
	public IntegerProperty getNReceivers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RecieverTypeEnum getReceiverType() {
		// TODO Auto-generated method stub
		return RecieverTypeEnum.SIMPLE_DRIFTER;
	}

	@Override
	public double[][] getRecieverPositions() {
		// TODO Auto-generated method stub
		return null;
	}

}
