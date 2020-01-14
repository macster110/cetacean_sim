package detector;

import simulation.CustomSimVar;
import simulation.SimVariable;

/**
 * 
 * Simple detector with a custom probability curve. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class SimpleDetector implements Detector {
	
	/**
	 * Use a perfect detector. 
	 */
	private boolean perfectDetector = false; 
	
	/**
	 * Default detection probability.
	 */
	private static double[] defaultDetProb = 
		{0.3, 0.4, 0.49, 0.58, 0.66, 0.73, 0.79, 0.85, 0.89, 0.91, 0.92, 
		0.93, 0.93, 0.945, 0.95, 0.96, 0.965, 0.97, 0.975, 0.98, 0.985, 0.99, 1.0};
	
	/**
	 * Custom simulation variable. 
	 */
	private CustomSimVar simVar = new CustomSimVar("SNR", defaultDetProb, 0, 110); 
	
	/**
	 * Constructor for a simple detector
	 * @param probDet - the detection efficiency with equal sized bins between minSNR and maxSNR
	 * @param minSNR - the minimum signal to noise ratio ofg probDet
	 * @param maxSNR - the maximum signal to noise ratio ofg probDet
	 * @param perfDet - true for a perfect detector (note if true negates the previous variables)
	 */
	public SimpleDetector(double[] probDet, double minSNR, double maxSNR, boolean perfDet) {
		simVar = new CustomSimVar("SNR", probDet, minSNR, maxSNR); 
		this.perfectDetector=perfDet;
	}

	
	public SimpleDetector() {
		//nothing to set. 
	}
	
	/**
	 * Constructor for the a SImple detector
	 * @param customSimVar - the detection efficiency versus SNR
	 * @param perfectDetector - true to be a perfect detector (negating customSimVar).
	 */
	public SimpleDetector(SimVariable customSimVar, boolean perfectDetector) {
		setDetectorDistribution(customSimVar);
		setPerfectDetector(perfectDetector);
	}

	
	@Override
	public double getProbClassified(double SNR) {
		if (perfectDetector) return 1.; //it's a perfect detector, p = 1. 
		return simVar.getProbability(SNR); //get probability from distribution. 
	}

	/**
	 * Set the perfect detector. 
	 * @param perfectDetector
	 */
	public void setPerfectDetector(boolean perfectDetector) {
		this.perfectDetector=perfectDetector;
	}
	
	/**
	 * Set the detector distribution. 
	 * @param customSimVar - the sim variable 
	 */
	public void setDetectorDistribution(SimVariable customSimVar) {
		this.simVar=(CustomSimVar) customSimVar;
	}
	
	/**
	 * Set the perfect detector. 
	 * @return perfectDetector - true for perfect detector.
	 */
	public boolean isPerfectDetector() {
		return this.perfectDetector;
	}
	
	/**
	 * Set the detector distribution. 
	 * @return customSimVar - the simulation variable. 
	 */
	public CustomSimVar getDetectionDistribution() {
		return this.simVar;
	}
	
	@Override 
	public String toString() {
		if (perfectDetector) return "Perfect detector!"; 
		else return "Simple detector: prob. det. from  "+  simVar.getProbData()[0] 
				+ " to "+ simVar.getProbData()[simVar.getProbData().length-1];
	}
	
}


