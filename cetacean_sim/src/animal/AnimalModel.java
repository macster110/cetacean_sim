package animal;

import animal.AnimalManager.AnimalTypeEnum;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import layout.animal.BeamProfile;
import layout.utils.SettingsPane;

/**
 * Defines an animal in the simulation
 * 
 * @author Jamie Macaulay
 *
 */
public interface AnimalModel {

	/**
	 * The name for the animal type
	 * 
	 * @return the
	 */
	public StringProperty getAnimalName();

	/**
	 * The number of these animals in the simulation
	 * 
	 * @return the number property for the number of animals in the simulations
	 */
	public IntegerProperty getNumberOfAnimals();

	/**
	 * Get the track of the animal.
	 * 
	 * @return the track of an animal.
	 */
	public AnimalTrack getTrack(int animalN);

	/**
	 * Get the beam profile. This allows the calculations of transmission loss
	 * compared to on axis for different parts of an animals beam profile.
	 * 
	 * @return - the beam profile object.
	 */
	public BeamProfile getBeamProfileTL();

	/**
	 * Get a time series of on-axis vocalisations
	 * 
	 * @return a AnimalVocalsiations class containing an array of vocalisation
	 *         starts times and amplitude in dB re 1uPa
	 */
	public AnimalVocalisations getVocSeries(int animalN);

	/**
	 * The settings pane. Pane to allow settings for the animal to be changedd
	 * 
	 * @return the settings pane for the animal.
	 */
	public SettingsPane<AnimalModel> getSettingsPane();

	/**
	 * Get the type of animal this is
	 * 
	 * @return the type of animal.
	 */
	public AnimalTypeEnum getAnimalType();

}
