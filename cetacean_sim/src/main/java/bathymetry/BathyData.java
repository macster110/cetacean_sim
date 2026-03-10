package bathymetry;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.interp.Gridder2;
import utils.LatLong;

/**
 * Class to hold bathymetry data. 
 * @author Jamie Macaulay
 *
 */
public class BathyData {
	
	/**
	 * The reference latitude and longitude. i.e. where the (0,0,0) cartesian co-ordintae system is referenced to. 
	 */
	private LatLong refLatLong;

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
	 * The surface. Use this to get points from the surface 
	 */
	private Sampling sampleSX; 

	/*
	 * The x mesh size of the grid. 
	 */
	private Sampling samplesY; 

	
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
	public float[][] getBathySurface() {
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
		return (float) gridXsize;
	}

	/**
	 * Get the y size of each grid square
	 * @return the bin size of the y dimensions
	 */
	public float getGridYSize() {
		// TODO Auto-generated method stub
		return (float) gridYsize;
	}
	
	/**
	 * Get the x size of each grid square
	 * @return the bin size of the y dimensions
	 */
	public void setGridXsize(double gridXsize) {
		this.gridXsize = gridXsize;
	}

	public void setGridYsize(double gridYsize) {
		this.gridYsize = gridYsize;
	}
	
	
	public Sampling getSamplesX() {
		return sampleSX;
	}

	public void setSamplesX(Sampling sampleSX) {
		this.sampleSX = sampleSX;
	}

	public Sampling getSamplesY() {
		return samplesY;
	}

	public void setSamplesY(Sampling samplesY) {
		this.samplesY = samplesY;
	}
	
	/**
	 * Get the reference LatLong. i.e. where the (0,0,0) cartesian co-ordintae system is referenced to. 
	 * @return the reference LatLong. 
	 */
	public LatLong getRefLatLong() {
		return refLatLong;
	}
	
	/**
	 * Set the reference LatLong. i.e. where the (0,0,0) cartesian co-ordintae system is referenced to. 
	 * @param the reference LatLong. 
	 */
	public void setRefLatLong(LatLong refLatLong) {
		this.refLatLong = refLatLong;
	}


}
