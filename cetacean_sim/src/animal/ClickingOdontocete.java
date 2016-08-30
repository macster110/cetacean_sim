package animal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * A clicking odontocete with a beam profile and diving behaviour. 
 * @author Jamie Macaulay
 *
 */
public class ClickingOdontocete implements AnimalModel {

	@Override
	public StringProperty sensorNameProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegerProperty numberAnimalsameProperty() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[][] getTrack(long timeStart, long longTimeEnd, double[][] bathySurface, double[][] tide) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getBeamProfileTL(double horzAngle, double vertAngle) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double[] getVocWav(double horzAngle, double vertAngle, int sR) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double[][] getVocSeries(long timeStart, long longTimeEnd) {
		// TODO Auto-generated method stub
		return null;
	}

}
