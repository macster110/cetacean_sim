package animal;

import java.util.ArrayList;
import java.util.Arrays;

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
	 * The beam profile. Non uniform points on the beam profile surface.
	 * Order of each elements {horizontal angle (degrees), vertical angle (degrees), transmission loss dB}
	 *  
	 */
	public BeamProfile beamProfile = DefaultBeamProfiles.getDefaultBeams().get(0); 

	/******Variables which are used *****/
	
	//beam surface
	public SurfaceData beamSurface;
	
	//source level
	public SimVariable sourceLevel = new NormalSimVariable("Source Level", 180, 10);
	//orientation 
	public ArrayList<SimVariable> vertAngles = new ArrayList<SimVariable>(Arrays.asList(new NormalSimVariable("Vertical Angle", 0, Math.toRadians(21), new double[] {-10000,0})));
	
	//horizontal angle
	public SimVariable horzAngle = new RandomSimVariable("Horizontal Angle", Math.toRadians(180), Math.toRadians(180));
	//orientation 
	
	//depth distribution
	public SimVariable depthDistribution = new RandomSimVariable("Depth", -200, 0);
	
	/************************************/
	
	/**
	 * Constructor for the animal. 
	 */
	public SimpleOdontocete () {
		beamSurface = SurfaceUtils.generateSurface(beamProfile.getRawBeamMeasurments());
	}
	
	/**
	 * Constructor for simple odontocetes. 
	 * @param sourceLevel - source level distribution of the animal. 
	 * @param vertAngles - vertical angles of the animal for different depth limits. 
	 * @param horzAngle - horizontal angles of the animal.  
	 * @param depthDist - the depth distribution of the animal. 
	 */
	public SimpleOdontocete (SimVariable sourceLevel, SimVariable[] vertAngles, SimVariable horzAngle, SimVariable depthDist) {
		this.sourceLevel=sourceLevel;
		this.vertAngles= new ArrayList<SimVariable>(Arrays.asList(vertAngles));
		this.horzAngle=horzAngle;
		this.depthDistribution=depthDist;
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
		
		//set the sim-variables
		this.sourceLevel = new NormalSimVariable("Source Level", sourceLevelMean, sourceLevelStd);
		
		vertAngles.clear();
		for (int i=0; i<vertAngleMean.length; i++) {
			vertAngles.add(new NormalSimVariable("Vertical Angle", vertAngleMean[i], vertAngleStd[i], lim[i]));
		}
		/*********************/
		
		//setup the beam profile
		switch(beamType) {
		case "porp":
			beamProfile = DefaultBeamProfiles.getDefaultBeams().get(0); 
			break;
		}
		
		beamSurface = SurfaceUtils.generateSurface(beamProfile.getRawBeamMeasurments());
		
		//bit of a hack but works 
		ProbDetSimSettings probDetSimSettings = new ProbDetSimSettings(); 
		probDetSimSettings.minHeight=maxDepth; 
	}
	
	
	/**
	 * Animal with uniform depth distribution and horizontal angle. Source levels and 
	 * vertical angles are normal distributions.
	 */
	public static final int SIM_UNIFORM_DEPTH_HORZ=0; 	
	

	@Override
	public String toString() {
		
		String vertAnglesString="";
		for (int i=0; i<vertAngles.size(); i++) {
			String newstring ="vert angle: " + vertAngles.get(i).toString() + " rad between "
					+ vertAngles.get(i).getLimits()[0] + " and " + vertAngles.get(i).getLimits()[1]+ "m"; 
			vertAnglesString=vertAnglesString + newstring + "\n"; 
		}
		
		String animalString = ("Simple Animal\n "
				+ "source level: " + sourceLevel.toString() +" dB re 1uPa \n"
				+ vertAnglesString
				+ "horizontal angle: " + horzAngle.toString() +" rad \n"
				+ "depth distribution: " + depthDistribution.toString() + " m");
		
		
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
