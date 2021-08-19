package reciever;

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
	 * Create a grid of hydrophones
	 * @param gridLims - the grid limits for x y and i.e {{xmin, xmax}, {ymin, ymax}, {zmin, zmax}} in meters. 
	 * @param gridSpacing - the grid spacing in meters. 
	 */
	public GridHydrophoneArray(double[][] gridLims, double gridSpacing) {
		
		//create the recording grid. 
		
		double[] gridX = gridPoints(gridLims[0][0], gridLims[0][1], gridSpacing); 
		double[] gridY = gridPoints(gridLims[1][0], gridLims[1][1], gridSpacing); 
		double[] gridZ = gridPoints(gridLims[2][0], gridLims[2][1], gridSpacing); 
		
		double[][] arrayXYZ= new double[gridX.length*gridY.length*gridZ.length][3]; 
		
		//sens offset default is zero
		double[] sensOffset= new double[gridX.length*gridY.length*gridZ.length]; 

		int n=0; 
		for (int i=0; i<gridX.length; i++) {
			for (int j=0; j<gridY.length; j++) {
				for(int k=0; k<gridZ.length; k++) {
					arrayXYZ[n] = new double[] {gridX[i], gridY[j], gridZ[k]};
					n++; 
				}
			}
		}
		this.arrayXYZ=arrayXYZ; 
		this.sensOffset=sensOffset; 
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

		double[] points = new double[(int) Math.floor((max-min)/spacing)]; 
		int n=0; 
		for (double i=min; i<max; i=i+spacing) {
			points[n] = i;
			n++; 
		}
		
		return points; 
	}
}
