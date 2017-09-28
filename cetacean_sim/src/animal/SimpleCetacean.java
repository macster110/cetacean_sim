package animal;

import animal.AnimalManager.AnimalTypeEnum;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import layout.animal.ClickingOdontocetesPane;
import layout.animal.SimpleOdontocetePane;
import layout.utils.SettingsPane;


public class SimpleCetacean implements AnimalModel, Cloneable {

	private SimpleOdontocetePane settingsPane;

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
	public StringProperty getAnimalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IntegerProperty getNumberOfAnimals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SettingsPane getSettingsPane() {
//		if (settingsPane==null) {
//			settingsPane=new SimpleOdontocetePane(this); 
//		}
//		return settingsPane; 
		return null; 
	}

	@Override
	public AnimalTypeEnum getAnimalType() {
		return AnimalTypeEnum.SIMPLE_CETACEAN;
	}
	
	@Override
	public SimpleCetacean clone()  {

		try {
			return (SimpleCetacean) super.clone();
		}
		catch (CloneNotSupportedException e) {
			return null;
		}
	}

}
