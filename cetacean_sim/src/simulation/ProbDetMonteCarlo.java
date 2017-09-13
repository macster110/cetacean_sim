package simulation;


import java.util.Random;

import utils.SurfaceData;
import utils.SurfaceUtils;

/**
 * Run a single Monte Carlo simulation for a probability of detection 
 * @author Jamie Macaulay 
 *
 */
public class ProbDetMonteCarlo {
	
	ProbDetSimSettings simSettings;
	
	private SurfaceData beamSurface; 
	
	/**
	 * Random number generator for vertical angle
	 */
	private Random vertNormal;

	/**
	 * Random number generator for horizontal angle. 
	 */
	private Random horzNormal;
	
	
	
	public ProbDetMonteCarlo() {

	}
	
	/**
	 * Get the simulation ready to run
	 */
	public void setUpMonteCarlo() {
		//create 
		 beamSurface = SurfaceUtils.generateSurface(simSettings.simpleOdontocete.beamProfile);
		 
		 vertNormal= new Random(); 
		 horzNormal= new Random(); 
		
	}
	
	/** 
	 * Run the simulation
	 */
	public void runMonteCarlo() {
	
		double x, y, z; //cartesian co-ordinates of animal
		double bearing, range, depth; //cylindrical co-ordinates of animal
		
		double vertAngle, horzAngle; //the horizontal and vertical angle of the animal in radians. 
		
		for (int i=0; i<simSettings.nBootStraps; i++) {
			for (int j=0; j<simSettings.nRuns; j++) {
				
				//create random position for animal
				bearing = Math.random()*2*Math.PI;
				range = Math.random()*simSettings.maxRange;
				if (simSettings.useDepthDist) {
					depth = randomDepthDist(simSettings.depthDistribution); 
				}
				else {
					depth = Math.random()*simSettings.maxDepth; 
				}
				
				
				//calculate the vertical angle of the animal
				vertAngle = vertNormal.nextGaussian()*simSettings.simpleOdontocete.verticalAngleStd
						+simSettings.simpleOdontocete.verticalAngleMean;
				
				
				//calculate the horizontal anlfe of the animal 
				horzAngle = horzNormal.nextGaussian()*simSettings.simpleOdontocete.verticalAngleStd
						+simSettings.simpleOdontocete.verticalAngleMean;
				
				//now have position of the porpoise need to figure out what the source level, tranmission loss due to
				//beam profile and the tranmission loss in general. 
				
			}
		}
		
		
	}
	
	public double randomDepthDist(double[][] depthDistribution) {
		
		return Math.random()*200; 
	}

}
