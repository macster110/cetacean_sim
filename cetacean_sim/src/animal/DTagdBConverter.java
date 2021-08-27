package animal;


/**
 * Converts a received tag measurement to a dB measurment. 
 * @author Jamie Macaulay
 *
 */
public class DTagdBConverter implements Linear2DBConverter {
	
	/**
	 * The voltage range of the DAQ card. 
	 */
	private double vp2p = 2; 
	
	/**
	 * The system sensitivity. (this is hydrophone sens + gain)
	 */
	private double sens = -175; //dB re 1uPa;

	/**
	 * The offset for apparent SL to the on-axis source level
	 */
	private double aslOffset = 82; 

	/**
	 * The multiplier for apparent SL to the on-axis source level
	 */
	private double asmultiplier = 0.62; 

	public DTagdBConverter(Double systemSens, Double vp2p) {
		this.vp2p = vp2p; 
		this.sens = systemSens; 
	}


	@Override
	public double linear2dB(double linearAmp) {
		
		/*
		* Need an extra divide by 2 in here since the standard scaling of PAMGUARD
		* data is -1 to +1, so data really needed to be scaled against half
		* the peak to peak voltage. 
		*/
		double dB = asmultiplier*(20*Math.log10(2*linearAmp*vp2p/2) - sens) + aslOffset; 
		
		return dB;
	}

}
