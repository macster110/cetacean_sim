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
	 * Create a settings class with initial settings values. Used primarily to call from MATLAB. 
	 * @param simpleAnimal - a an animal 
	 * @param hydrophoneArray
	 * @param animalTracks
	 * @param noise
	 * @param snrThresh
	 * @param spreadingCoeff
	 * @param absorptionCoeff
	 */
	public ProbDetTrackSettings(AnimalModel simpleAnimal, HydrophoneArray hydrophoneArray, double noise, double snrThresh, double spreadingCoeff, 
			double absorptionCoeff){
		this.recievers=hydrophoneArray; 
		this.noise=noise;
		this.snrThreshold=snrThresh;
		this.animal = simpleAnimal; 
		this.propogation=new SimplePropogation(spreadingCoeff, absorptionCoeff);
	}
	
	


}
