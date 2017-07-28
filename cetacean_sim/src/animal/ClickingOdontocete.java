package animal;

import animal.AnimalManager.AnimalTypeEnum;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import layout.animal.BeamProfile;
import layout.animal.ClickingOdontocetesPane;
import layout.utils.SettingsPane;

/**
 * A clicking odontocete with a beam profile and diving behaviour. 
 * @author Jamie Macaulay
 *
 */
public class ClickingOdontocete implements AnimalModel, Cloneable {
	
	/**
	 * Settings whihc can be serialised. 
	 */
	private ClickingOdontocetesSettings settings=new ClickingOdontocetesSettings(); 
	
	/**
	 * The settings pane for a clicking odontocetes. 
	 */
	private ClickingOdontocetesPane settingsPane; 
	
	/**
	 * The beam profile of the animal. 
	 */
	private BeamProfile beamProfile= new BeamProfile(); 
	
	/**
	 * Inetger property for the number of animals in the simulation. 
	 */
	private IntegerProperty nAnimals= new SimpleIntegerProperty( 50); 
	
	
	/**
	 * String property for the name of the animal
	 */
	private StringProperty nameProperty= new SimpleStringProperty("My clicking odontocete"); 

	
	public ClickingOdontocete(){
		newSettings();
	}

	
	@Override
	public StringProperty getAnimalName() {
		return nameProperty;
	}
	
	
	/**
	 * Called wehenever settings are changed. 
	 */
	private void newSettings(){
		//create the beam profile. 
		beamProfile.createBeamProfile(settings.beamProfile); 
	}
	
	

	@Override
	public IntegerProperty getNumberOfAnimals() {
		return nAnimals;
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
	
	/**
	 * The settings pane. Pane to allow settings for the animal to be changedd
	 * @return the settings pane for the animal. 
	 */
	public SettingsPane getSettingsPane(){
		if (settingsPane==null) {
			settingsPane=new ClickingOdontocetesPane(this); 
		}
		return settingsPane; 
	}
	
	public ClickingOdontocetesSettings getSettings() {
		return settings;
	}


	public BeamProfile getBeamProfile() {
		return beamProfile;
	}
	
	@Override
	public ClickingOdontocete clone()  {

		try {
			return (ClickingOdontocete) super.clone();
		}
		catch (CloneNotSupportedException e) {
			return null;
		}
	}


	@Override
	public AnimalTypeEnum getAnimalType() {
		return AnimalTypeEnum.CLICKING_ODONTOCETE;
	}

}
