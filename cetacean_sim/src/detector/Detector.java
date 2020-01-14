package detector;

/**
 * The detector is the performance of the detector in detecting and classifying recieved 
 * sounds which are above the required noise threshold. Usually this will be a custom distribution
 * of detector performance verses SNR. 
 * 
 * @author Jamie Macaulay 
 *
 */
public interface Detector {
	
	/**
	 * Get the probability that a received true detection was classified as belong to target species. 
	 * @param SNR - the SNR in dB
	 * @return - the probability the recieved true detection was classiifed from 0 to 1. 
	 */
	public double getProbClassified(double SNR); 
	
	

}
