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
	 * The Latitude, Longitude and height of the track data. Track data {time (MATLAB datenum), lat (decimal), long (decimal), z (meters)}
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

	/**
	 * A list of beam profile measurments which can be used to construct a beam profile 
	 *  (horizontal angle (RAD), vertical angle (RAD), Beam loss (dB))
	 */
	public double[][] beamProfile; 
//	/**
//	 * The id of the tag
//	 */
//	public String tagid; 

}