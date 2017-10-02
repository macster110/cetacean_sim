package reciever;

/**
 *A hydrophone array 
 */
public interface HydrophoneArray {
	
	/**
	 * Name of the Hydrophone array, 
	 * @return the name of the hydrophone array,. 
	 */
	public String getName(); 
	
	/**
	 * Gety the position of the array in cartesian co-ordinates
	 * @return
	 */
	public double[][] getArrayXYZ();
	
	
	/**
	 * Get the sensitivity offset of the array. e.g. 1dB would mean that the array is 1dB more sensitive than 
	 * the standard sensitivity. 
	 */
	public double[] getSensOffset();

}
