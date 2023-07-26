package animal;

import utils.CetSimUtils;

/**
 * An animal track. The position of an animal and location of vocalisations. 
 * @author Jamie Macaulay 
 *
 */
public class AnimalTrack {


	/**
	 * The Cartesian co-ordinates from the start of the track in  meters
	 */
	public double[][] xyz; 

	/**
	 * The times of each point on the tracks in seconds from simulation start
	 */
	public double[] times; 

	/**
	 * The horizontal and vertical angle of the animal at each point on the track (RADIANS). 
	 */
	public double[][] animalsAngs; 


	AnimalTrack(double[] times, double[][] xyz, double[][] angs){
		this.times=times;
		this.xyz=xyz; 
		this.animalsAngs=angs; 
	}

	
	public double[][] getTrackxyz() {
		return xyz;
	}

	/**
	 * Set the track x, y and z co-ordinates. Note that each the dimensions of the array double[3][N] with N
	 * the number of track points. This makes accessing all x, y or z co-ordinates faster for interpolation. 
	 * @param xyz - the input x, y and z data
	 */
	public void setTrackxyz(double[][] xyz) {
		this.xyz = xyz;
	}

	public double[] getTrackTimes() {
		return times;
	}

	public void setTrackTimes(double[] times) {
		this.times = times;
	}

	public double[][] getAnimalsAngs() {
		return animalsAngs;
	}

	/**
	 * Set the angles for the animal 
	 * @param animalsAngs
	 */
	public void setAnimalsAngs(double[][] animalsAngs) {
		this.animalsAngs = animalsAngs;
	}

	/**
	 * Get the location of the animal at a certain time. 
	 * @param timesTrk- the times to find points for from track start in seconds. 
	 * return - the  3D Cartesian location (meters)
	 */
	public double[][] getTrackPoints(double[] timesTrk) {
		
		double[][] trackxyz = new double[3][]; 
		//System.out.println("Interp linear track: " + xyz[0].length);
		
//		double[] trackTimes = CetSimUtils.getMinAndMax(times); 
//		double[] vocTimes = CetSimUtils.getMinAndMax(timesTrk); 
		System.out.println("Min track time: " + times[0] + " max: " + times[times.length-1]); 
		System.out.println("Min voc time: " + timesTrk[0] + " max: " + timesTrk[timesTrk.length-1]); 

		trackxyz[0] = CetSimUtils.interpLinear(times, xyz[0], timesTrk); 
		trackxyz[1] = CetSimUtils.interpLinear(times, xyz[1], timesTrk); 
		trackxyz[2] = CetSimUtils.interpLinear(times, xyz[2], timesTrk); 
		
		return trackxyz; 
	}
	
	/**
	 * Get the orientation of the animal at a certain time. 
	 * @param timesTrk- the times to find points for from track start in seconds. 
	 * @return - the horizontal and verticla angle in RADIANS. 
	 */
	public double[][] getTrackAngles(double[] timesTrk) {
		double[][] trackAngs = new double[3][]; 
		//TODO interpolating angles might be tricky....hmmmm.
		
//		double[][] trackAngs = new double[2][]; 
//		trackAngs[0] = CetSimUtils.interpLinear(times, animalsAngs[0], timesTrk); 
//		trackAngs[1] = CetSimUtils.interpLinear(times, animalsAngs[1], timesTrk); 
		
		//probably just need to take closest angle. 
		trackAngs[0] = CetSimUtils.findClosest(times, animalsAngs[0], timesTrk); 
		trackAngs[1] = CetSimUtils.findClosest(times, animalsAngs[1], timesTrk); 
		trackAngs[2] = CetSimUtils.findClosest(times, animalsAngs[2], timesTrk); 

		
		return trackAngs; 
	}

}
