package simulation;

import animal.SimpleOdontocete;
import propogation.Propogation;
import propogation.SimplePropogation;
import reciever.DefaultHydrophoneArrays;

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
	public int nBootStraps=25;
	
	
	/************Dimensions*************/
	
	/**
	 * The maximum detection range
	 */
	public double maxRange=700;
	
	/**
	 * The maximum possible depth
	 */
	public double maxDepth=180;
	
	/**
	 * The range bin
	 */
	public int rangeBin=25; 
	
	/**
	 * The depth bin
	 */
	public int depthBin=10;
	
	
	/********Recovers********/
	
	double[][] recievers = DefaultHydrophoneArrays.PLABuoyLong; 
	
	/**
	 * The minimum number of receivers to detect a sound on. 
	 */
	public int minRecievers = 0; 
	
	
	/******Propagation********/
	
	public Propogation propogation = new SimplePropogation(20, 0.04);

	
	/****Noise to test****/
	
	public double noiseThreshold = 100; //dB
	
	
	/****The animal*****/
	
	/**
	 * The animal model
	 */
	public SimpleOdontocete simpleOdontocete =new SimpleOdontocete(); 
	

	
}
