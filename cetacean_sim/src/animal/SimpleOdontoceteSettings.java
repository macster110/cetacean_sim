package animal;

/**
 * Very simple clicking odontocete for probability of detection simulations 
 * @author Jamie Macaulay
 *
 */
public class SimpleOdontoceteSettings extends ClickingOdontocetesSettings {
	
	/**
	 * Animal Params. 
	 */
	
	public double verticalAngleMean=Math.toRadians(3.29);
	public double verticalAngleStd=Math.toRadians(27.524);
	
	public double horizontalAngleMean=Math.toRadians(0);
	public double horizontalAngleatd=Math.toRadians(50);

	public double sourceLevelMean=180; //dB re 1 uPa
	public double sourceLevelStd=10;

	/****************The beam profile********************/

	/**
	 * The beam profile. Non uniform points on the beam profile surface.
	 * Order of each elements {horizontal angle (degrees), vertical angle (degrees), transmission loss dB}
	 *  
	 */
	public double[][] beamProfile = DefaultBeamProfiles.porpBeam1; 

}
