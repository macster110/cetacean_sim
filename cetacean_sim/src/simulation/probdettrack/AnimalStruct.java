package simulation.probdettrack;

/**
 * Holds animal data in the same way that MATLAB does
 * 
 * @author Jamie Macaulay
 *
 */
public class AnimalStruct {

	/**
	 * The AnimalStruct
	 */
	public AnimalStruct() {

	}

	/**
	 * 7x1 array of clicks where columns are... Clicks data {time (MATLAB datenum),
	 * samples, UID, length (samples), type (int), amplitude (peak to peak),
	 * amplitude lag (samples) } meters.
	 */
	public double[][] clicks;

	/**
	 * The x, y, z of the track data. Track data {time (MATLAB datenum), x, y, z}
	 * meters.
	 */
	public double[][] trackdata;

	/**
	 * The orientation data {time (MATLAB datenum), horizontal, pitch, roll}
	 * RADIANS.
	 */
	public double[][] orientation;

	/**
	 * The peak to peak voltage of the DAQ. Use in conjunction with systemSens to
	 * figure out the apparent source level.
	 */
	public Double vp2p;

	/**
	 * The system sensitivity of the DAQ in dB re 1V/uPa. Use in conjunction with
	 * systemSens to figure out the apparent source level.
	 */
	public Double systemSens;

//	/**
//	 * The id of the tag
//	 */
//	public String tagid; 

}