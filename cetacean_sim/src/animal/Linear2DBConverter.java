package animal;


/**
 * Converts a linear to dB measurement
 * 
 * @author Jamie Macaulay 
 *
 */
public interface Linear2DBConverter {
	
	/**
	 * Convert from a linear to dB measurement for different instruments. 
	 * @param linearAmp - the linear amplitude from -1/1; 
	 * @return the dB amplitude in dB
	 */
	public double linear2dB(double linearAmp); 

}
