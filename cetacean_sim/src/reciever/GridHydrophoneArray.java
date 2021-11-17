package reciever;

import java.util.Arrays;

import utils.CetSimUtils;

/**
 * A hydrophone array arranged as a grid at a defined depth. 
 * @author Jamie Macaulay 
 *
 */
public class GridHydrophoneArray implements HydrophoneArray {

	
	/**
	 * The Cartesian co-ordinates of each receiver in meters. 
	 */
	private double[][] arrayXYZ; 
	
	
	/**
	 * The sensitivity offset for each device. This is the sensitivity difference from the defautl sensitivity. 
	 */
	private double[] sensOffset; 

	/**
	 * Create a grid of hydrophones track. 
	 * @param gridLims - the grid limits for x y and i.e {{xmin, xmax}, {ymin, ymax}, {zmin, zmax}} in meters. 
	 * @param gridSpacing - the grid spacing in meters. 
	 * @param - the absolute maximum detection range expected. No recievers greater than this range will be generated. 
	 */
	public GridHydrophoneArray(double[][] gridLims, double gridSpacing) {
		this.arrayXYZ = generateReceiverGrid(gridLims, gridSpacing); 
		this.sensOffset=new double[arrayXYZ.length]; //defualt to zero.  
	}
	
	/**
	 * Create a grid of hydrophones around a track, excluding ay which are over a maximum range from any point on the track. 
	 * @param trackxyz - an animal track in Cartesian co-ordinates i.e. x,y,z (meters)
	 * @param gridSpacing - the grid spacing in meters. 
	 * @param maxRange - the maximum allowed range between a receiver and any point on the track 
	 * @param - the absolute maximum detection range expected. No recievers greater than this range will be generated. 
	 */
	public GridHydrophoneArray(double[][] trackyz, double gridSpacing, double maxRange) {
		this.arrayXYZ = generateReceiverGrid(trackyz, gridSpacing,   maxRange); 
		this.sensOffset=new double[arrayXYZ.length]; //defualt to zero.  
	}
	
	/**
	 * Create a grid of hydrophones around a track, excluding ay which are over a maximum range from any point on the track. 
	 * @param trackxyz - an animal track in Cartesian co-ordinates i.e. x,y,z (meters)
	 * @param gridSpacing - the grid spacing in meters for the x and y -cordinates. 
	 * @param depthSpacing - the depth grid spacing in meters
	 * @param maxRange - the maximum allowed range between a receiver and any point on the track 
	 * @param - the absolute maximum detection range expected. No recievers greater than this range will be generated. 
	 */
	public GridHydrophoneArray(double[][] trackyz, double gridSpacing, double[] depthSpacing, double maxRange) {
		this.arrayXYZ = generateReceiverGrid(trackyz, gridSpacing, depthSpacing,  maxRange); 
		this.sensOffset=new double[arrayXYZ.length]; //defualt to zero.  
	}

	@Override
	public String getName() {
		return "Grid Array";
	}

	@Override
	public double[][] getArrayXYZ() {
		return arrayXYZ;
	}

	@Override
	public double[] getSensOffset() {
		return sensOffset; 
	}

	/**
	 * Create a set of points - equivalent to MATLAB x = min:spacing:max; 
	 * @param min - the minimum value. 
	 * @param max - the maximum value. 
	 * @param spacing - the spacing. 
	 * @return a set of points between min and max separated by spacing. 
	 */
	protected static double[] gridPoints(double min, double max, double spacing) {

		double[] points = new double[(int) Math.ceil((max-min)/spacing)]; 
		int n=0; 
		for (double i=min; i<max; i=i+spacing) {
			points[n] = i;
			n++; 
		}
		
		return points; 
	}
	
	/**
	 * Get the limits for a track. this the x and y limits with the maximum range added. The maximum 
	 * range is NOT added to the depth. 
	 * @param trackxyz - trackxyz - the track x,y and z. 
	 * @return the3 trackxyz; 
	 */
	public static double[][] getGridLims(double[][] trackxyz, double maxRange){
		double[] minMaxX = CetSimUtils.getMinAndMax(trackxyz[0]); 
		double[] minMaxY = CetSimUtils.getMinAndMax(trackxyz[1]); 
		double[] minMaxZ = CetSimUtils.getMinAndMax(trackxyz[2]); 
		
		System.out.println("Minmax X: " + minMaxX[0] + "  " + minMaxX[1] + "  " + trackxyz[0].length );
		System.out.println("Minmax Y: " + minMaxY[0] + "  " + minMaxY[1]);
		System.out.println("Minmax Z: " + minMaxZ[0] + "  " + minMaxZ[1]);

		double[][] lims = new double[][]{{minMaxX[0]-maxRange, minMaxX[1]+maxRange}, {minMaxY[0]-maxRange, minMaxY[1]+maxRange}, {minMaxZ[0], minMaxZ[1]}}; 
		
		return lims; 
	}
	
	
	/**
	 * Generate a grid of recievers based on a grid spacing and grid limits in 3D. 
	 * @param gridLims - the grid limits for x y and i.e {{xmin, xmax}, {ymin, ymax}, {zmin, zmax}} in meters. 
	 * @param gridSpacing - the grid spacing in meters. 
	 * @return a list of recievers x, y and z locations. 
	 */
	public static double[][] generateReceiverGrid(double[][] gridLims, double gridSpacing) {
		
		return generateReceiverGrid(gridLims, null,  gridSpacing); 
	
	}
	
	
	/**
	 * Generate a grid of recievers based on a grid spacing and grid limits in 3D. 
	 * @param gridLims - the grid limits for x y and i.e {{xmin, xmax}, {ymin, ymax}, {zmin, zmax}} in meters. 
	 * @param gridLims - the grid spacing for Z - e.g. {10, 20, 30} would mean a grid at 10, 20 and 30m. Can be null in which 
	 * case the grid will be calculated based on the zmin and zmax and grid spacing. 
	 * @param gridSpacing - the grid spacing in meters. 
	 * @return a list of recievers x, y and z locations. 
	 */
	public static double[][] generateReceiverGrid(double[][] gridLims, double[] gridZ, double gridSpacing) {
		//create the recording grid. 
		
		double[] gridX = gridPoints(gridLims[0][0], gridLims[0][1], gridSpacing); 
		double[] gridY = gridPoints(gridLims[1][0], gridLims[1][1], gridSpacing); 
		if (gridZ==null) {
			gridZ = gridPoints(gridLims[2][0], gridLims[2][1], gridSpacing); 
		}
		double[][] arrayXYZ= new double[gridX.length*gridY.length*gridZ.length][3]; 

		int n=0; 
		for (int i=0; i<gridX.length; i++) {
			for (int j=0; j<gridY.length; j++) {
				for(int k=0; k<gridZ.length; k++) {
					arrayXYZ[n] = new double[] {gridX[i], gridY[j], gridZ[k]};
					n++; 
				}
			}
		}
		
		return arrayXYZ; 
	
	}
	
	/**
	 * Generate a grid of receivers based on a grid spacing, a track and a maximum range The receivers will be based on a grid around the track but
	 * any receiver which is a greater distance than maxRange from any point on the track will be excluded. 
	 * @param trackxyz - an animal track in cartesian co-ordinates i.e. x,y,z (meters)
	 * @param gridSpacing - the grid spacing of x, y and z in meters. 
	 * @param maxRange - the maximum range (3D) a receiver can be from the track. 
	 * @return a list of receivers x, y and z locations. 
	 */
	public static double[][] generateReceiverGrid(double[][] trackxyz, double gridSpacing, double maxRange) {
		
		return generateReceiverGrid(trackxyz,  gridSpacing, null,  maxRange);
		
	}

	

	/**
	 * Generate a grid of receivers based on a grid spacing, a track and a maximum range The receivers will be based on a grid around the track but
	 * any receiver which is a greater distance than maxRange from any point on the track will be excluded. 
	 * @param trackxyz - an animal track in cartesian co-ordinates i.e. x,y,z (meters)
	 * @param gridSpacing - the grid spacing of x and y in meters. 
	 * @param depthSpacing - the depth spacing lims in meters e.g. 10, 20, 30 means the grid will be at 10, 20 and 30 meters. 
	 * @param maxRange - the maximum range (3D) a receiver can be from the track. 
	 * @return a list of receivers x, y and z locations. 
	 */
	public static double[][] generateReceiverGrid(double[][] trackxyz, double gridSpacing, double[] depthSpacing, double maxRange) {
		
		//calculate the grid limits. 
		double[][] gridLims = getGridLims(trackxyz,  maxRange);
		
		//the receiver grid. 
		double[][] receiverGrid = generateReceiverGrid(gridLims, depthSpacing, gridSpacing);
		
		return filtReceivers(receiverGrid, trackxyz,  maxRange); 
		
	}

	/**
	 * Filter the receivers so that they are less than a maximum range 
	 * @param recievers - a list of the reciever x,y,z co-ordinates in meters. 
	 * @param trackxyz - list of the track co-ordinates x,y,z (transposed)
	 * @param maxRange - the maximum range between any point on the track and a reciever in meters. 
	 * @return a list of recievers which are within maxRange of the track. 
	 */
	public static double[][] filtReceivers(double[][] receiverGrid, double[][] trackxyz, double maxRange) {
		//now filter out all the hydrophones with a great range than this. 
		double[][] arrayXYZfilt= new double[receiverGrid.length][3]; 

		System.out.println("Filtering the recievers by maximum range: " +  receiverGrid.length); 
		int n=0; 
		for (int i=0; i<receiverGrid.length; i++) {
			if (i%1000==0) {
				System.out.println("Filtering " + i + " of " + receiverGrid.length); 
			}
//			double minRange = Double.MAX_VALUE; 
			double dist; 
			//now iterate through the track. Only do every tenth track point to help speed things up. 
			for (int j =0; j<trackxyz[0].length; j=j+10) {
				dist = CetSimUtils.distance(new double[] {trackxyz[0][j], trackxyz[1][j], trackxyz[2][j]} , receiverGrid[i]); 
				if (dist<=maxRange) {
					arrayXYZfilt[n] = receiverGrid[i]; 
					n++; 
					//System.out.println("Last j was " + j)
					break; //
				}
			}
//			//System.out.println( String.format("recievedPos: xyz: %.2f %.2f %.2f minRange %.2f", receiverGrid[i][0], receiverGrid[i][1], receiverGrid[i][2], minRange));
//			if (minRange<=maxRange) {
//				arrayXYZfilt[n] = receiverGrid[i]; 
//				n++; 
//			}
		}
		System.out.println("Filtering done: " +  n + " recievers left"); 

		//trim the array. 
		double[][] recieverFilt = Arrays.copyOf(arrayXYZfilt, n); 
		
		return recieverFilt; 
	}
	
}
