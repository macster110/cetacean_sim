package utils;

import org.apache.commons.math3.util.MathUtils;

import doug.TestAngles;
import propogation.Propogation;

/**
 * Some useful functions for simulations. 
 * @author Jamie Macaulay 
 *
 */
public class CetSimUtils extends SurfaceUtils {
	
	/**
	 * 
	 * @param array one dimensional array of doubles
	 * @return two element array with the min and max values of the input array
	 */
	public static double[] getMinAndMax(double[] array) {
		double[] minmax = {Double.MAX_VALUE, Double.MIN_VALUE};
		for (int i = 0; i < array.length; i++) {
		  minmax[0] = Math.min(minmax[0], array[i]);
		  minmax[1] = Math.max(minmax[1], array[i]);
		}
		return minmax;
	}
	
	public static double distance(double[] point1, double[] point2) {
		return Math.sqrt(Math.pow(point1[0]-point2[0],2) + Math.pow(point1[1]-point2[1],2) + Math.pow(point1[2]-point2[2],2)); 
	}
	
	@Deprecated
	public static double[] getRelativeAngle(double[] recieverPos, double[] animalPos, double[] animalAngle) {
		
		double range = distance(recieverPos, animalPos); 
		
//		System.out.println("Distance: " + range); 
		
		//find the absolute horizontal and vertical angle between the receiver and the animal. 
		
		//horizontal angle,
		//just as many trig functions if you do this vector formalisation
		double horzAngle=Math.atan2(recieverPos[0]-animalPos[0], recieverPos[1]-animalPos[1]);
		horzAngle=MathUtils.normalizeAngle(horzAngle+animalAngle[0], 0.0); //normalise the angle between -Pi and Pi
//		System.out.println("horzAngle: " + Math.toDegrees(horzAngle)); 

		
		//vertical angle
		double vertAngle=Math.asin((recieverPos[2]-animalPos[2])/range)+animalAngle[1];
		//the addition of the vertical angle may have flipped made the animal go beyond -PI/2 and PI/2
		//so might need to get vertical angle back into PI/2 bounds and flip horizontal angle/ 
		if (vertAngle<-Math.PI/2 || vertAngle>Math.PI/2) {
			vertAngle= (vertAngle<-Math.PI/2 ? -Math.PI: Math.PI) - vertAngle; 
		}		
		
		vertAngle = MathUtils.normalizeAngle(vertAngle, 0.);
		
		double[] relAngle = {horzAngle, vertAngle};
		return relAngle;
	}
	/**
	 * Calculate the beam loss between an animal and receiver. The co-ordinate system is such that 
	 * @param recieverPos - the position of the receiver in x y and z. 
	 * @param animalPos - the position of the animal in x y and z.
	 * @param animalAngle - the horizontal and vertical angle of the animal in RADIANS. 
	 * @param animalBeamProfile - the interpolated beam profile surface in horizontal, vertical angle and TL 
	 * @return the beam loss in negative dB. 
	 */
	public static double beamLoss(double[] recieverPos, double[] animalPos, double[] animalAngle, SurfaceData animalBeamProfile) {
		
		//double range = distance(recieverPos, animalPos); 
		
//		System.out.println("Distance: " + range); 
		
		//find the absolute horizontal and vertical angle between the receiver and the animal. 
		
		//horizontal angle,
		//just as many trig functions if you do this vector formalisation
//		double horzAngle=Math.atan2(recieverPos[0]-animalPos[0], recieverPos[1]-animalPos[1]);
//		horzAngle=MathUtils.normalizeAngle(horzAngle+animalAngle[0], 0.0); //normalise the angle between -Pi and Pi
////		System.out.println("horzAngle: " + Math.toDegrees(horzAngle)); 
//
//		
//		//vertical angle
//		double vertAngle=Math.asin((recieverPos[2]-animalPos[2])/range)+animalAngle[1];
//		//the addition of the vertical angle may have flipped made the animal go beyond -PI/2 and PI/2
//		//so might need to get vertical angle back into PI/2 bounds and flip horizontal angle/ 
//		if (vertAngle<-Math.PI/2 || vertAngle>Math.PI/2) {
//			vertAngle= (vertAngle<-Math.PI/2 ? -Math.PI: Math.PI) - vertAngle; 
//		}		
//		System.out.println("vertAngle: " + Math.toDegrees(vertAngle)); 
//		animalAngle[0] = Math.PI; 
//		animalAngle[1] = 0;
		
		double[] relativeAngles = TestAngles.getRelativeAngles(recieverPos, animalPos, animalAngle);
//		relativeAngles[0] = relativeAngles[1] = 0;
		
		//System.out.println("Find surface for horz: " + ((float) relativeAngles[0]) + "  vert: " + (float) relativeAngles[1]);
	
		return animalBeamProfile.grid.interpolate((float) relativeAngles[0], (float) relativeAngles[1]); 
	}
	
	/**
	 * Calculate the total transmission loss due to beam profile loss, spreading and absorption. 
	 * @param recieverPos - the position of the receiver in x y and z. 
	 * @param animalPos - the position of the animal in x y and z.
	 * @param animalAngle - the horizontal and vertical angle fo the animal. in RADIANS
	 * @param animalBeamProfile - the interpolated beam profile surface in horizontal, vertical angle and TL 
	 * @return the beam loss in negative dB. 
	 */
	public static double tranmissionTotalLoss(double[] recieverPos, double[] animalPos, double[] animalAngle, SurfaceData animalBeamProfile, Propogation propogation) {
		
//		System.out.println("Beam loss: " + beamLoss(recieverPos, animalPos, animalAngle, animalBeamProfile) 
//		+ " Propogation: " + propogation.getTranmissionLoss(recieverPos, animalPos));

		return beamLoss(recieverPos, animalPos, animalAngle, animalBeamProfile) + propogation.getTranmissionLoss(recieverPos, animalPos); 
	
	}

}
