package simulation.probdettrack;

import animal.AnimalModel;
import propogation.SimplePropogation;
import reciever.HydrophoneArray;
import simulation.probdetsim.ProbDetSimSettings;

/**
 * Settings for the probability track simualtion
 * @author Jamie Macaulay
 *
 */
public class ProbDetTrackSettings extends ProbDetSimSettings {
	
	

	/**
	 * The animal model. This holds the tracks and vocalisation of all animals 
	 * in the simualtion. 
	 */
	public AnimalModel animal; 
	
	
	/**
	 * True to consider roll in the calculations. 
	 */
	public boolean useRoll = true; 
	
	/**
	 * The length of the snapshot in seconds. Note that this will only be used in the snapshot method. 
	 */
	public double snapTime = 10.; 
	
	
	/**
	 * The start and end of the simulation. Note that this will only be used in the snapshot method. 
	 * (seocnds from tag start.)
	 */
	public double[] snapTimeLims = null; 
	
	/**
	 * Save a time series of clicks on all devices. 
	 */
	public boolean saveTimeSeries = false;
	
	
	/**
	 * Create a settings class with initial settings values. Used primarily to call from MATLAB. 
	 * @param simpleAnimal - the animal model. This holds all info on animal movements, number of animals etc. 
	 * @param hydrophoneArray
	 * @param animalTracks
	 * @param noise
	 * @param snrThresh
	 * @param spreadingCoeff
	 * @param absorptionCoeff
	 */
	public ProbDetTrackSettings(AnimalModel simpleAnimal, HydrophoneArray hydrophoneArray, double noise, double snrThresh, double spreadingCoeff, 
			double absorptionCoeff){
		super(0); 
		this.recievers=hydrophoneArray; 
		this.noise=noise;
		this.snrThreshold=snrThresh;
		this.animal = simpleAnimal; 
		this.propogation=new SimplePropogation(spreadingCoeff, absorptionCoeff);
	}
	
	
	/**
	 * Print the current settings
	 */
	@Override
	public void printSettings() {
		System.out.println("Current Settings: ");
		System.out.println("---------------");
		System.out.println("No. Recievers: ");
		System.out.println(recievers.getArrayXYZ().length);
		System.out.println("---------------");
		System.out.println("Animal: ");
		System.out.println(animal); 
		System.out.println("---------------");
		System.out.println("Propogation: ");
		System.out.println(propogation.toString()); 
		System.out.println("---------------");
		System.out.println("Detection Efficiency: ");
		System.out.println(detector.toString()); 
		System.out.println("---------------");
		System.out.println("max range: "+ this.maxRange);
		System.out.println("noise threshold: "+ this.noise);
		System.out.println("snrThreshold threshold: "+ this.snrThreshold);

	}
	
	


}
