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
	 * The system sensitivity (this is hydrophone sensitivity + gain)
	 */
	private double sens = -180; //dB re 1V/1uPa;

	/**
	 * The offset for apparent SL to the on-axis source level
	 */
	private double aslOffset = 41.27; 

	/**
	 * The multiplier for apparent SL to the on-axis source level
	 */
	private double asmultiplier = 0.8866; 

	
	public DTagdBConverter(Double systemSens, Double vp2p) {
		this.vp2p = vp2p; 
		this.sens = systemSens; 
	}

	
	public DTagdBConverter(Double systemSens, Double vp2p, Double asmultiplier, Double aslOffset) {
		this.vp2p = vp2p; 
		this.sens = systemSens; 
		this.asmultiplier=asmultiplier;
		this.aslOffset=aslOffset;
	}

	@Override
	public double linear2dB(double linearAmp) {
		
		/*
		* Need an extra divide by 2 in here since the standard scaling of PAMGUARD
		* data is -1 to +1, so data really needed to be scaled against half
		* the peak to peak voltage. 
		*/
		double dB = asmultiplier*(20*Math.log10(linearAmp*vp2p/2) - sens) + aslOffset; 
		
		return dB;
	}
	
	public static void  main(String[] args) {
		double sens = -180; 
		double vp2p = 2; 

		DTagdBConverter dTagdBConverter = new DTagdBConverter(sens, vp2p); 
		
		double linearAmp = 0.25; //-1 to 1
		
		double SL = dTagdBConverter.linear2dB(linearAmp);
		
		double AOL = 20*Math.log10(linearAmp*vp2p/2) - sens; 
		
		System.out.println(String.format("The SL for an apparant linear amplitude of %.2f is an "
				+ "AOL of %.1f and SL of %.1f dB", linearAmp, AOL, SL)); 
	}

}
