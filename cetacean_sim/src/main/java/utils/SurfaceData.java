package utils;

import edu.mines.jtk.interp.SibsonGridder2;

/**
 * Holds information on interpolated grid. 
 * @author Jamie Macaulay 
 *
 */
public class SurfaceData {
	
	public SurfaceData() {
		
	}
	
	public SurfaceData(SibsonGridder2 grid, double minX, double maxX, double minY, double maxY) {
		this.grid=grid;
		this.minX=minX;
		this.maxX=maxX;
		this.minY=minY;
		this.maxY=maxY; 
	}
	
	/**
	 *The interpolated grid.  
	 */
	public SibsonGridder2 grid;
	
	/**
	 * Minimum x value of the data points
	 */
	double minX;
	
	/**
	 * The maximum x value of points
	 */
	double maxX;
	
	/**
	 * The minimum y value of points. 
	 */
	double minY;
	
	/**
	 * The maximum x value of points. 
	 */
	double maxY;
	
}