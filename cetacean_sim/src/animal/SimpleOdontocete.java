package animal;

import simulation.NormalSimVariable;
import simulation.ProbDetSimSettings;
import simulation.RandomSimVariable;
import simulation.SimVariable;
import utils.SurfaceData;
import utils.SurfaceUtils;

/**
 * Very simple clicking odontocete for probability of detection simulations 
 * @author Jamie Macaulay
 *
 */
public class SimpleOdontocete {
	
	

	/****************Variables which are saved********************/
	/**
	 * Animal Parameters. 
	 */
	
	private double verticalAngleMean=Math.toRadians(3.29);
	private double verticalAngleStd=Math.toRadians(27.524);
	
	private double horizontalAngleMean=Math.toRadians(0);
	private double horizontalAngleatd=Math.toRadians(50);

	private double sourceLevelMean=180; //dB re 1 uPa
	private double sourceLevelStd=10;
	
	
	
	/**
	 * The depth distribution (not yet used)
	 */
	private double[][] depthDistributionS;

	/**
	 * The beam profile. Non uniform points on the beam profile surface.
	 * Order of each elements {horizontal angle (degrees), vertical angle (degrees), transmission loss dB}
	 *  
	 */
	public double[][] beamProfile = DefaultBeamProfiles.porpBeam1; 

	/******Variables which are used *****/
	
	public SurfaceData beamSurface;
	
	
	public SimVariable sourceLevel; 

	//orientation 
	public SimVariable vertAngle;
	
	public SimVariable horzAngle;
	
	//depth distribution
	public SimVariable depthDistribution;
	
	
	/**
	 * Constructor for the animal. 
	 */
	public SimpleOdontocete () {
		beamSurface = SurfaceUtils.generateSurface(beamProfile);
	}
	
	/**
	 * Animal with uniform depth distribution and horizontal angle. Source levels and 
	 * vertical angles are normal distributions.
	 */
	public static final int SIM_UNIFORM_DEPTH_HORZ=0; 	
	
	
	/**
	 * Set upo the 
	 * @param flag
	 * @param settings
	 */
	public void  setUpAnimal(int flag, ProbDetSimSettings settings) {

		switch (flag) {
		case SIM_UNIFORM_DEPTH_HORZ:
			beamSurface = SurfaceUtils.generateSurface(beamProfile);

			sourceLevel = new NormalSimVariable("Source Level", sourceLevelMean, sourceLevelStd); 

			vertAngle= new NormalSimVariable("Vertical Angle", verticalAngleMean,  verticalAngleStd); 

			horzAngle= new RandomSimVariable("Horizontal Angle", -Math.PI, Math.PI); 

			depthDistribution =  new RandomSimVariable("Horizontal Angle", settings.minHeight, 0); 
			break;
		default:
			setUpAnimal(SIM_UNIFORM_DEPTH_HORZ,  settings);  
			break; 
		}
	}

}
