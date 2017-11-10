package simulation.probdetsim;

import animal.SimpleOdontocete;
import propogation.Propogation;
import propogation.SimplePropogation;
import reciever.DefaultHydrophoneArrays;
import reciever.HydrophoneArray;
import reciever.SimpleHydrophoneArray;

/**
 * Probability of detection settings which can be serialized and saved 
 * @author Jamie Macaulay	
 *
 */
public class ProbDetSimSettings implements Cloneable {


	/**
	 * The number of runs
	 */
	public int nRuns=50000;

	/**
	 * The number of bootstraps 
	 */
	public int nBootStraps=10;


	/************Dimensions*************/

	/**
	 * The maximum detection range
	 */
	public double maxRange=700;

	/**
	 * The maximum possible depth
	 */
	public double minHeight=-200;

	/**
	 * The range bin
	 */
	public int rangeBin=25; 

	/**
	 * The depth bin
	 */
	public int depthBin=10;


	/********Recovers********/

	public HydrophoneArray recievers = new SimpleHydrophoneArray(DefaultHydrophoneArrays.PLABuoyLong); 

	/**
	 * The minimum number of receivers to detect a sound on. 
	 */
	public int minRecievers = 4; 


	/******Propagation********/

	public Propogation propogation = new SimplePropogation(20, 0.04);


	/****Noise to test****/

	public double noiseThreshold = 100; //dB


	/****The animal*****/

	/**
	 * The animal model
	 */
	public SimpleOdontocete simpleOdontocete =new SimpleOdontocete(); 

	/**
	 * Set up settings with default settings
	 */
	public ProbDetSimSettings(){

	}

	/**
	 * Create a settings class with intial settings values. Used primarily to call from MATLAB. 
	 * @param simpleAnimal - animal calss
	 * @param hydrophoneReceivers - list of hydrophone recievers
	 * @param nRuns - the number of runs
	 * @param nBootStraps - the number of bootstraps.
	 * @param maxRange - the maximum range
	 * @param maxDepth - the maximum depth
	 * @param rangeBin - the number of range bins
	 * @param depthBin - the number of depth bins
	 * @param noiseThreshold - the noisethreshold in dB 
	 * @param spreadingCoeff - the spreading coefficient using in (spreading coeff)*log10(range) + (absorption coeff)*R
	 * @param absorptionCoeff - the absorption coefficient using in (spreading coeff)*log10(range) + (absorption coeff)*R
	 */
	public ProbDetSimSettings(SimpleOdontocete simpleAnimal, double[][] hydrophoneReceivers, int nRuns, int nBootStraps,
			double maxRange, double maxDepth, int rangeBin, int depthBin, double noiseThreshold, double spreadingCoeff, double absorptionCoeff){
		this.simpleOdontocete=simpleAnimal;
		this.recievers=new SimpleHydrophoneArray(hydrophoneReceivers); 
		this.nRuns=nRuns; 
		this.nBootStraps=nBootStraps; 
		this.maxRange=maxRange; 
		this.minHeight=maxDepth; 
		this.rangeBin=rangeBin;
		this.depthBin=depthBin;
		this.noiseThreshold=noiseThreshold;
		this.propogation=new SimplePropogation(spreadingCoeff, absorptionCoeff);
	}
	
	/**
	 * Print the current settings
	 */
	public void printSettings() {
		System.out.println("Current Settings: ");
		System.out.println("---------------");
		System.out.println("Recievers: ");
		for (int i=0; i<this.recievers.getArrayXYZ().length; i++) {
			System.out.println(String.format("x: %.2f y: %.2f z: %.2f ", recievers.getArrayXYZ()[i][0], 
					recievers.getArrayXYZ()[i][1],  recievers.getArrayXYZ()[i][2]));
		}
		
		System.out.println("---------------");
		System.out.println("Animal: ");
		System.out.println(simpleOdontocete.toString()); 
		System.out.println("---------------");
		System.out.println("Propogation: ");
		System.out.println(propogation.toString()); 
		System.out.println("---------------");
		System.out.println("General: ");
		System.out.println("no. runs: "+ this.nRuns);
		System.out.println("no. bootstraps: "+ this.nBootStraps);
		System.out.println("max range: "+ this.maxRange);
		System.out.println("max depth: "+ this.minHeight);
		System.out.println("range bin: "+ this.rangeBin);
		System.out.println("depth bin: "+ this.depthBin);
		System.out.println("noise threshold: "+ this.noiseThreshold);



	}




}
