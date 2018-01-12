package animal;

import java.util.ArrayList;

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
	private double[] verticalAngleMean=new double[] {Math.toRadians(3.29)};
	private double[] verticalAngleStd=new double[] {Math.toRadians(27.524)};
	private double[][] verticalDepthLims=new double[][] {{-100000,0}}; 
	
	private double horizontalAngleMean=Math.toRadians(0);
	private double horizontalAngleatd=Math.toRadians(50);

	private double sourceLevelMean=180; //dB re 1 uPa
	private double sourceLevelStd=10;
	
	/**
	 * The depth distribution (not yet used)
	 */
	private double[][] depthDistributions;

	/**
	 * Uniform depth distirbution between two limits. 
	 */
	private double[] depthLims= new double[]{-100000,0}; 


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
	public ArrayList<SimVariable> vertAngles;
	
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
	 * @param sourceLevelMean - the mean source level
	 * @param sourceLevelStd - the standard deviation in source level
	 * @param vertAngleMean - the mean vertical angle
	 * @param vertAngleStd - the standard deviation in vertical angle.
	 * @param maxDepth - the max depth for the simulation 
	 * @param beamType - the beam type flag. 
	 */
	public SimpleOdontocete (double sourceLevelMean, double sourceLevelStd, double vertAngleMean , 
			double vertAngleStd,  double maxDepth, double minDepth, String beamType) {
		this(sourceLevelMean, sourceLevelStd, new double[]{vertAngleMean}, new double[]{vertAngleStd},
				new double[][] {{-100000,0}}, maxDepth, minDepth, beamType);
	}
	
	/**
	 * Constructor for the animal with in starting values. Used primarily to call from MATLAB 
	 * Creates an animal which has an even depth distribution with normal distributions for source levels
	 * and vertical angle.
	 */
	public SimpleOdontocete (double sourceLevelMean, double sourceLevelStd, double[] vertAngleMean , 
			double[] vertAngleStd, double[][] lim,  double maxDepth, double minDepth, String beamType) {
		
		this.verticalAngleMean=vertAngleMean;
		this.verticalAngleStd=vertAngleStd;
		this.verticalDepthLims=lim; 
		
		this.depthLims=new double[]{maxDepth, minDepth};

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
	 * Set up the animal
	 * @param flag - the default flag. 
	 * @param settings -reference ot simulation settings. 
	 */
	public void setUpAnimal(int flag, ProbDetSimSettings settings) {

		switch (flag) {
		case SIM_UNIFORM_DEPTH_HORZ:
			beamSurface = SurfaceUtils.generateSurface(beamProfile.getRawBeamMeasurments());

			sourceLevel = new NormalSimVariable("Source Level", sourceLevelMean, sourceLevelStd); 

			//vertical angles can change with depth. 
			vertAngles= new ArrayList<SimVariable>();
			NormalSimVariable variable; 
			for (int i=0; i<verticalAngleMean.length; i++) {
				 variable = new NormalSimVariable("Vertical Angle", verticalAngleMean[i],  verticalAngleStd[i]);
				 variable.setLimits(this.verticalDepthLims[i]);
				 vertAngles.add(variable); 
			}

			horzAngle= new RandomSimVariable("Horizontal Angle", -Math.PI, Math.PI); 

			depthDistribution =  new RandomSimVariable("Depth Distribution", Math.max(settings.minHeight,this.depthLims[0]), this.depthLims[1]); 
			break;
		default:
			setUpAnimal(SIM_UNIFORM_DEPTH_HORZ,  settings);  
			break; 
		}
	}
	
	
	@Override
	public String toString() {
		
		String vertAnglesString="";
		for (int i=0; i<vertAngles.size(); i++) {
			String newstring ="vert angle: " + vertAngles.get(i).toString() + " between "
					+ vertAngles.get(i).getLimits()[0] + " and " + vertAngles.get(i).getLimits()[1]+ "m"; 
			vertAnglesString=vertAnglesString + newstring + "\n"; 
		}
		
		String animalString = ("Simple Animal\n "
				+ "source level: " + sourceLevel.toString() +"\n"
				+ vertAnglesString
				+ "horizontal angle: " + horzAngle.toString() +"\n"
				+ "depth distribution: " + depthDistribution.toString());
		
		
		return animalString;
	}
	

	/**
	 * Get the vertical angle for the animal depending on the depth. If there are
	 * no variables withrin the depth ranges there's a problem and the first sim variable is returning 
	 * @param depth - the depth of the animal
	 * @param range - the range of the animal
	 * @return the SimVaribale for the specified depth and range. 
	 */
	public SimVariable getVertAngle(double depth) {
		double[] limits; 
		for (int i=0; i<this.vertAngles.size(); i++) {
			limits= vertAngles.get(i).getLimits(); 
			if (depth>=limits[0] && depth<limits[1]) {
				return  vertAngles.get(i); 
			}
		}
		System.err.println("Could not find correct vert. angle distribution for specified depth of " +depth );
		return vertAngles.get(0); 
	}

}
