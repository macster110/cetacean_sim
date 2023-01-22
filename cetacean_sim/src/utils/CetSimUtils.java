package utils;

import java.util.Arrays;

import org.apache.commons.math3.util.MathUtils;

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
		double[] minmax = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};

		for (int i = 0; i < array.length; i++) {
//			if (i%500==0) {
//				System.out.println("Val: " + i + "  " + array[i]); 
//			}
			if (!Double.isNaN(array[i])) {
				minmax[0] = Math.min(minmax[0], array[i]);
				minmax[1] = Math.max(minmax[1], array[i]);
			}
		}

		return minmax;
	}

	/**
	 * Calculate the distance between two 3D Cartesian points. 
	 * @param point1 - the first point (x,y,z)
	 * @param point2 - the first point (x,y,z)
	 * @return the distance in units of the points. 
	 */
	public static double distance(double[] point1, double[] point2) {
		return Math.sqrt(Math.pow(point1[0]-point2[0],2) + Math.pow(point1[1]-point2[1],2) + Math.pow(point1[2]-point2[2],2)); 
	}


	/**
	 * Calculate 2D (X Y) the distance between two 2D or 3D Cartesian points. 
	 * @param point1 - the first point (x,y) or (x,y,z)
	 * @param point2 - the first point (x,y) or (x,y,z)
	 * @return the distance in units of the points. 
	 */
	public static double distance2D(double[] point1, double[] point2) {
		return Math.sqrt(Math.pow(point1[0]-point2[0],2) + Math.pow(point1[1]-point2[1],2)); 
	}


	/**
	 * Equivalent to the linspace function in MATLAB. Creates a series of equally spaced numbers between two limits. 
	 * @param min - the minimum limit. 
	 * @param max - the maximum limit. 
	 * @param N - the number of points. 
	 * @return N equally spaced numbers between min max with. 
	 */
	public static double[] linspace(double min, double max, int N) {  
		double[] d = new double[N];  
		for (int i = 0; i < N; i++){  
			d[i] = min + i * (max - min) / (N - 1);  
		}  
		return d;  
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
		//System.out.println("Distance: " + range); 
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
		//		System.out.println("Animal angle: " + ((float) animalAngle[0]) + "  vert: " + (float) animalAngle[1]);
		//		System.out.println("Animal pos: x " + ((float) animalPos[0]) + "  y: " + (float) animalPos[1]+ "  z: " + (float) animalPos[2]);
		//		System.out.println("reciever pos: x " + ((float) recieverPos[0]) + "  y: " + (float) recieverPos[1]+ "  z: " + (float) recieverPos[2]);
		
		double[] relativeAngles = null;

		if (animalAngle.length==2)
			relativeAngles = AnimalAngles.getRelativeAngles(recieverPos, animalPos, animalAngle);
		else if (animalAngle.length==3){
			relativeAngles = AnimalAngles.getRelativeAngles2(recieverPos, animalPos, animalAngle);
		}
		else {
			System.err.println("CetSimUtils: beamLoss: The animal orientation vector must be length 2 or 3"); 
			return Double.NaN;
		}
		//		relativeAngles[0] = relativeAngles[1] = 0;
		try {
			//		System.out.println("Find surface for horz: " + ((float) relativeAngles[0]) + "  vert: " + (float) relativeAngles[1]);
			return animalBeamProfile.grid.interpolate((float) relativeAngles[0], (float) relativeAngles[1]); 
		}
		catch (Exception e) {

			e.printStackTrace();
			System.out.println("recieverPos: " + recieverPos[0] + "  " + recieverPos[1]+ "  " + recieverPos[2]); 
			System.out.println("animalPos: " + animalPos[0] + "  " + animalPos[1]+ "  " + animalPos[2]); 
			if (animalAngle.length==2) {
				System.out.println("animalAngle: " + animalAngle[0] + "  " + animalAngle[1]); 
			}
			else {
				System.out.println("animalAngle: " + animalAngle[0] + "  " + animalAngle[1]+  "  " + animalAngle[2]); 
			}
			System.out.println("relativeAngles: " + relativeAngles[0] + "  " + relativeAngles[1]);
			
			return -50; 
		}
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

	/**
	 * Linear interpolation of data. Similar to MATLAB interp1 function. 
	 * @param x - the x values. 
	 * @param y - the y values. = 
	 * @param xi - the interpolated x values to find y values for. 
	 * @return the yi values for corresponding xi values. 
	 * @throws IllegalArgumentException
	 */
	public static final double[] interpLinear(double[] x, double[] y, double[] xi) throws IllegalArgumentException {

		if (x.length != y.length) {
			throw new IllegalArgumentException("X and Y must be the same length");
		}
		if (x.length == 1) {
			throw new IllegalArgumentException("X must contain more than one value");
		}
		double[] dx = new double[x.length - 1];
		double[] dy = new double[x.length - 1];
		double[] slope = new double[x.length - 1];
		double[] intercept = new double[x.length - 1];

		// Calculate the line equation (i.e. slope and intercept) between each point
		for (int i = 0; i < x.length - 1; i++) {
			dx[i] = x[i + 1] - x[i];
			if (dx[i] == 0) {
				throw new IllegalArgumentException("X must be montotonic. A duplicate " + "x-value was found");
			}
			if (dx[i] < 0) {
				throw new IllegalArgumentException("X must be sorted");
			}
			dy[i] = y[i + 1] - y[i];
			slope[i] = dy[i] / dx[i];
			intercept[i] = y[i] - x[i] * slope[i];
		}

		// Perform the interpolation here
		double[] yi = new double[xi.length];
		for (int i = 0; i < xi.length; i++) {
			if ((xi[i] > x[x.length - 1]) || (xi[i] < x[0])) {
				yi[i] = Double.NaN;
			}
			else {
				int loc = Arrays.binarySearch(x, xi[i]);
				if (loc < -1) {
					loc = -loc - 2;
					yi[i] = slope[loc] * xi[i] + intercept[loc];
				}
				else {
					yi[i] = y[loc];
				}
			}
		}

		return yi;
	}

	/**
	 * Efficient implementation of a binary search algorithm. 
	 * @param numbers - the numbers to search
	 * @param start - the start index of the array to search (0 for whole array)
	 * @param end - the end index of the array to search (numbers.length-1 for whole array )
	 * @param myNumber - the number to search for. 
	 * @return the index of the closest number in numbers array to myNumber. 
	 */
	private static int nearestNumberBinarySearch(double[] numbers, int start, int end, double myNumber) {
		int mid = (start + end) / 2;
		if (numbers[mid] == myNumber)
			return mid;
		if (start == end - 1)
			if (Math.abs(numbers[end] - myNumber) >= Math.abs(numbers[start] - myNumber))
				return start;
			else
				return end;
		if(numbers[mid]> myNumber)
			return nearestNumberBinarySearch(numbers, start,mid, myNumber);
		else
			return nearestNumberBinarySearch(numbers,mid, end, myNumber);

	}


	/**
	 * Interpolate using a nearest neighbour search. 
	 * @param x - the x values. 
	 * @param y - the y values.
	 * @param xi - the nearest neighbour x values to find y values for. 
	 * @return the yi values for corresponding xi values. 

	 * @throws IllegalArgumentException
	 */
	public static double[] findClosest(double[] x, double[] y, double[] xi) throws IllegalArgumentException {

		if (x.length != y.length) {
			throw new IllegalArgumentException("X and Y must be the same length");
		}
		if (x.length == 1) {
			throw new IllegalArgumentException("X must contain more than one value");
		}

		double[] yi = new double[xi.length];

		for (int i=0; i<xi.length; i++) {
			//System.out.println("Find closest: " +  i + " of " + xi.length); 
			//much more efficient methods than  code commented out belwo. 
			int ind = nearestNumberBinarySearch(x, 0, x.length-1, xi[i]); 
			yi[i]= y[ind];
			//			double min = Double.MAX_VALUE;
			//			int minind = -1;
			//			for (int j=0; j<x.length; j++) {
			//				if (Math.abs(x[j]-xi[i])<min) {
			//					minind=j; 
			//					min = Math.abs(x[j]-xi[i]); 
			//				}
			//			}
			//			yi[i] = y[minind]; 
		}

		return yi;
	}


}
