package animal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class SimplePorpoise implements AnimalModel {

	@Override
	public double[][] getTrack(long timeStart, long longTimeEnd, double[][] bathySurface, double[][] tide) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getBeamProfileTL(double horzAngle, double vertAngle) {
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

}
