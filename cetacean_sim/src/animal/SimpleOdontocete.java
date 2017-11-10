package animal;

import layout.animal.BeamProfile;
import simulation.NormalSimVariable;
import simulation.RandomSimVariable;
import simulation.SimVariable;
import simulation.probdetsim.ProbDetSimSettings;
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
	public BeamProfile beamProfile = DefaultBeamProfiles.getDefaultBeams().get(0); 

	/******Variables which are used *****/
	
	//beam surface
	public SurfaceData beamSurface;
	
	//source level
	public SimVariable sourceLevel; 

	//orientation 
	public SimVariable vertAngle;
	
	//horizontal angle
	public SimVariable horzAngle;
	
	//depth distribution
	public SimVariable depthDistribution;
	
	
	/**
	 * Constructor for the animal. 
	 */
	public SimpleOdontocete () {
		beamSurface = SurfaceUtils.generateSurface(beamProfile.getRawBeamMeasurments());
	}
	
	/**
	 * Constructor for the animal with intial starting values. Used primarily to call from MATLAB 
	 * Creates an animal which has an even depth distribution with normal distributions for source levels
	 * and vertical angle.
	 */
	public SimpleOdontocete (double sourceLevelMean, double sourceLevelStd, double vertAngleMean , 
			double vertAngleStd,  double maxDepth, String beamType) {
				 
		this.verticalAngleMean=vertAngleMean;
		this.verticalAngleStd=vertAngleStd;

		this.sourceLevelMean=sourceLevelMean; //dB re 1 uPa
		this.sourceLevelStd=sourceLevelStd;
		
		switch(beamType) {
		case "porp":
			beamProfile = DefaultBeamProfiles.getDefaultBeams().get(0); 
			break;
		}
		
		beamSurface = SurfaceUtils.generateSurface(beamProfile.getRawBeamMeasurments());
		
		//bit of a hack but works 
		ProbDetSimSettings probDetSimSettings = new ProbDetSimSettings(); 
		probDetSimSettings.minHeight=maxDepth; 
		setUpAnimal(SIM_UNIFORM_DEPTH_HORZ, probDetSimSettings); 
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
			beamSurface = SurfaceUtils.generateSurface(beamProfile.getRawBeamMeasurments());

			sourceLevel = new NormalSimVariable("Source Level", sourceLevelMean, sourceLevelStd); 

			vertAngle= new NormalSimVariable("Vertical Angle", verticalAngleMean,  verticalAngleStd); 

			horzAngle= new RandomSimVariable("Horizontal Angle", -Math.PI, Math.PI); 

			depthDistribution =  new RandomSimVariable("Depth Distribution", settings.minHeight, 0); 
			break;
		default:
			setUpAnimal(SIM_UNIFORM_DEPTH_HORZ,  settings);  
			break; 
		}
	}
	
	
	@Override
	public String toString() {
		
		String animalString = ("Simple Animal\n "
				+ "source level: " + sourceLevel.toString() +"\n"
				+ "vertical angle: " + vertAngle.toString() +"\n"
				+ "horizontal angle: " + horzAngle.toString() +"\n"
				+ "depth distribution: " + depthDistribution.toString());
		
		return animalString;
	}

}
