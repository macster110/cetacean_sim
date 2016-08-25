package bathymetry;

import edu.mines.jtk.interp.Gridder2;

/**
 * Class to hold bathymetry data. 
 * @author Jamie Macaulay
 *
 */
public class BathyData {
	
	/**
	 * The bathymetry surfcae
	 */
	private float[][] bathySurface; 
	
	/**
	 * The surface. Use this to get points from the surface 
	 */
	private Gridder2 interpGrid; 

	/*
	 * The x mesh size of the grid. 
	 */
	private double gridXsize;
	
	/**
	 * The y mesg size of the grid. 
	 */
	private double gridYsize;

	
	/**
	 * Get the interpolated surface. 
	 * @return
	 */
	public Gridder2 getInterpSurface(){
		return interpGrid; 
	}
	
	/**
	 * Get the interpolated surface. 
	 * @return
	 */
	public void setInterpSurface(Gridder2 gridder2){
		this.interpGrid=gridder2; 
	}
	
	/**
	 * Get the depth surface. This is calulated from the interp surface. 
	 * @return
	 */
	public float[][] getDepthSurface() {
		return bathySurface;
	}
	
	/**
	 * Set the grid. This is the surface used mainly for GUI operations. 
	 * @return the bathymetry grid.
	 */
	public void setInterpSurface(float[][] grid){
		this.bathySurface=grid; 
	}
	
	/**
	 * The x size of each grid point
	 * @return the bin size of the x dimension
	 */
	public float getGridXSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * The y size of each grid point
	 * @return the bin size of the y dimensions
	 */
	public float getGridYSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setGridXsize(double gridXsize) {
		this.gridXsize = gridXsize;
	}

	public void setGridYsize(double gridYsize) {
		this.gridYsize = gridYsize;
	}


}
