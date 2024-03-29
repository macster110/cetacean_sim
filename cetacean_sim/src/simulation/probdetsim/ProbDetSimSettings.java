package simulation.probdetsim;

import animal.SimpleOdontocete;
import detector.Detector;
import detector.SimpleDetector;
import propogation.Propogation;
import propogation.SimplePropogation;
import reciever.DefaultHydrophoneArrays;
import reciever.HydrophoneArray;
import reciever.SimpleHydrophoneArray;

/**
 * 
 * Probability of detection settings which can be serialised and saved. 
 * 
 * @author Jamie Macaulay	
 *
 */
public class ProbDetSimSettings implements Cloneable {
	
	/**
	 * Flag used by evenXy to distribute simulation points evenly in X and Y
	 */
	public final static int UNIFORM_XY = 0 ;
	
	/**
	 * Flag used by evenXY to distribute simulation points so there is a uniform range distribution. 
	 */
	public final static int UNIFORM_HORZ_RANGE = 1; 

	/**
	 * The number of runs
	 */
	public int nRuns=50000;

	/**
	 * The number of bootstraps 
	 */
	public int nBootStraps=10;
	
	/**
	 * True to use evenly distributed x y points. If false then the horizontal 
	 * range is even. If set to true then there is no need to multiply the
	 * probability of detection in 2D by the triangular step function. 
	 */
	public int evenXY=UNIFORM_XY;


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
	 * The number of range bins between the minimum and maximum bin. 
	 */
	public int numRangeBins=25; 

	/**
	 * The of number of depth bins between the minimum and maximum bin. 
	 */
	public int numDepthBins=10;


	/********Recovers********/

	public HydrophoneArray recievers = new SimpleHydrophoneArray(DefaultHydrophoneArrays.PLABuoyLong); 

	/**
	 * The minimum number of receivers to detect a sound on. 
	 */
	public int minRecievers = 4; 


	/******Propagation********/

	public Propogation propogation = new SimplePropogation(20, 0.04);


	/****Noise to test****/

	/**
	 * The noise level 
	 */
	public double noise = 85; //dB re 1uPa
	
	/**
	 * the SNR threshold for a click
	 */
	public double snrThreshold = 16; //dB 
	
	/****The Classifier***/
	
	/**
	 * The detector
	 */
	public Detector detector = new SimpleDetector();  

	/****The Animal*****/

	/**
	 * The animal model
	 */
	public SimpleOdontocete simpleOdontocete;

	/**
	 * Set up settings with default settings
	 */
	public ProbDetSimSettings(){
		simpleOdontocete =new SimpleOdontocete(); 
	}
	
	/**
	 * Hack so that subclasses don't have to create a simpleOdontocete class...Messy
	 */
	public ProbDetSimSettings(int nadda){
//		simpleOdontocete =new SimpleOdontocete(); 
	}


	/**
	 * Create a settings class with initial settings values. Used primarily to call from MATLAB. 
	 * @param simpleAnimal - animal class
	 * @param hydrophoneReceivers - list of hydrophone receivers.
	 * @param nRuns - the number of runs
	 * @param nBootStraps - the number of bootstraps.
	 * @param maxRange - the maximum range
	 * @param maxDepth - the maximum depth
	 * @param rangeBin - the number of range bins
	 * @param depthBin - the number of depth bins
	 * @param noise - the noise threshold in dB re 1uPa
	 * @param snr - the minimum signal to noise ratio required in dB
	 * @param spreadingCoeff - the spreading coefficient using in (spreading coefficient)*log10(range) + (absorption coefficient)*R
	 * @param absorptionCoeff - the absorption coefficient using in (spreading coefficient)*log10(range) + (absorption coefficient)*R
	 */
	public ProbDetSimSettings(SimpleOdontocete simpleAnimal, double[][] hydrophoneReceivers, int nRuns, int nBootStraps,
			double maxRange, double maxDepth, int rangeBin, int depthBin, double noise, double snrThresh, double spreadingCoeff, 
			double absorptionCoeff){
		this.simpleOdontocete=simpleAnimal;
		this.recievers=new SimpleHydrophoneArray(hydrophoneReceivers); 
		this.nRuns=nRuns; 
		this.nBootStraps=nBootStraps; 
		this.maxRange=maxRange; 
		this.minHeight=maxDepth; 
		this.numRangeBins=rangeBin;
		this.numDepthBins=depthBin;
		this.noise=noise;
		this.snrThreshold=snrThresh;
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
		System.out.println("Detection Efficiency: ");
		System.out.println(detector.toString()); 
		System.out.println("---------------");
		System.out.println("General: ");
		System.out.println("no. runs: "+ this.nRuns);
		System.out.println("no. bootstraps: "+ this.nBootStraps);
		System.out.println("even x y: "+ this.evenXY);
		System.out.println("max range: "+ this.maxRange);
		System.out.println("max depth: "+ this.minHeight);
		System.out.println("range bin: "+ this.numRangeBins);
		System.out.println("depth bin: "+ this.numDepthBins);
		System.out.println("noise threshold: "+ this.noise);

	}
	
	@Override 
	public ProbDetSimSettings clone() {
		try {
			return (ProbDetSimSettings) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
		}
		return null;
	}




}
