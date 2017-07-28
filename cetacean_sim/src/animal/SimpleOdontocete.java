package animal;

import animal.AnimalManager.AnimalTypeEnum;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import layout.utils.SettingsPane;

/**
 * Simple Odontocete is used for calculating the probability of detection. 
 * @author Jamie Macaulay 
 *
 */
public class SimpleOdontocete implements AnimalModel, Cloneable {
	
	
	/**
	 * Settings. 
	 */
	SimpleOdontoceteSettings simpleOdontoceteSettings = new SimpleOdontoceteSettings(); 
	
	public SimpleOdontocete() {
		
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

	@Override
	public SettingsPane<AnimalModel> getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnimalTypeEnum getAnimalType() {
		return AnimalTypeEnum.SIMPLE_ODONTOCETE;
	}

}
