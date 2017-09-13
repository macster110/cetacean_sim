package simulation;

import java.util.ArrayList;

import animal.AnimalModel;
import animal.SimpleOdontocete;
import reciever.RecieverModel;

/**
 * Probability of detection settings. 
 * @author Jamie Macaulay	
 *
 */
public class ProbDetSimSettings {
	
	/**
	 * The number of runs
	 */
	public int nRuns=50000;
	 
	/**
	 * The number of bootstraps 
	 */
	public int nBootStraps=100;
	
	
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
	public double rangeBin=25; 
	
	/**
	 * The depth bin
	 */
	public double depthBin=10;
	
	
	/********Recovers********/
	
	double[][] hydrophonePositions; 
	
	
	/******Propagation********/
	
	public double spreading=20;
	
	
	public double absorption=0.04; 
	
	
	/****Noise to test****/
	
	public double minNoise=85; 
	
	public double noiseBin=1; 
	
	public double noiseMax=150; //dB re 1uPa pp. 
	
	
	/****The animal*****/
	
	/**
	 * The animal model
	 */
	public SimpleOdontocete simpleOdontocete =new SimpleOdontocete(); 
	
	/**
	 * Random horizontal angle
	 */
	public boolean randomHorz=true;
	
	/****Depth Distribution****/
	
	/**
	 * Use the a depth distribution in the simulation. 
	 */
	public boolean useDepthDist= false; 
	
	/**
	 * The depth distribution
	 */
	public double[][] depthDistribution; 
	
	


	
}
