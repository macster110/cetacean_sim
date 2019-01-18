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
	private CustomSimVar simVar = new CustomSimVar("Detection probability", defaultDetProb, 0, 100); 

	
	@Override
	public double getProbClassified(double SNR) {
		if (perfectDetector) return 1.; //it's a perfect detector, p = 1. 
		return simVar.getNextRandom(); //get probability from distribution. 
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
	public CustomSimVar getDetectoDistribution() {
		return this.simVar;
	}
	
}


