package reciever;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import layout.utils.SettingsPane;


/**
 * A receiver which drifts with the tide. 
 * @author Jamie Macaulay  
 *
 */
public class DriftingReciever implements RecieverModel {

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
		return null;
	}

	@Override
	public IntegerProperty getNReceivers() {
		// TODO Auto-generated method stub
		return null;
	}

}
