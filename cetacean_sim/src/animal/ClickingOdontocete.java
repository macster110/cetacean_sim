package animal;

import java.util.Arrays;
import java.util.List;

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
	//private ClickingOdontocetesSettings settings=new ClickingOdontocetesSettings(); 
	
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
	private IntegerProperty nAnimals= new SimpleIntegerProperty(50); 
	
	/**
	 * A list of animal tracks. 
	 */
	public List<AnimalTrack> animalTrack; 
	
	/**
	 * A list of the animal vocalisations.
	 */
	public List<AnimalVocalisations> animalVoc; 
	
	
	/**
	 * String property for the name of the animal
	 */
	private StringProperty nameProperty= new SimpleStringProperty("My clicking odontocete"); 

	
	public ClickingOdontocete(){
		beamProfile.createBeamProfile(DefaultBeamProfiles.porpBeam1); 
	}
	

	/**
	 * Create a model with just one track. 
	 * @param trackxyz - the x y and z cartesian co-ordintaes of the track
	 * @param trackang - the horizontal and vertical angles of each trackxyz 
	 * @param vocalsiations
	 * @param beamProfileRaw
	 */
	public ClickingOdontocete(double[] tracktimes, double[][] trackxyz, double[][] trackang, double[] vocTimes, double[] vocAmp, double[][] beamProfileRaw) {
		beamProfile = new BeamProfile("Custom Beam", beamProfileRaw); 
		
		//the animal tracks
		animalTrack = Arrays.asList(new AnimalTrack(tracktimes, trackxyz, trackang)); 
		
		//animal voclaisations
		animalVoc = Arrays.asList(new ClickingAnimalVoc(vocTimes, vocAmp)); 
		
		//the beam profile. 
		beamProfile = new BeamProfile("Custom Beam", beamProfileRaw); 

		nAnimals.set(1);
		
	}

	@Override
	public StringProperty getAnimalName() {
		return nameProperty;
	}
	

	@Override
	public IntegerProperty getNumberOfAnimals() {
		return nAnimals;
	}


	@Override
	public BeamProfile getBeamProfileTL() {
		return beamProfile;
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
	
//	public ClickingOdontocetesSettings getSettings() {
//		return settings;
//	}


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


	@Override
	public AnimalTrack getTrack(int animalN) {
		return animalTrack.get(animalN);
	}


	@Override
	public AnimalVocalisations getVocSeries(int animalN) {
		return animalVoc.get(animalN);
	}
	
	@Override
	public String toString() {
		String outString = ""; 
		for (int i =0; i<this.nAnimals.intValue(); i++) {
			outString += "---Clicking Odontocete---" + i; 
			outString += "\n"; 

			outString +=  "No. clicks: " + animalVoc.get(i).getVocAmplitudes().length; 
			outString += "\n"; 

			outString +=  "No. track points: " + animalTrack.get(i).getTrackTimes().length; 
			outString += "\n"; 
		}
		return outString;
	}

}
