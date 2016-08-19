package animal;

/**
 * Defines a animal in the simulation
 * @author Jamie Macaulay 
 *
 */
public interface AnimalType {
	
		/**
		 * Get the track of the animal. 
		 * @return the track of an animal. 
		 */
		double[][] getTrack(long timeStart, long longTimeEnd, double[][] bathySurface, double[][] tide );
		
		/**
		 * Get the transmission loss compared to on axis for different parts of an animals beam profile. 
		 * @param horzAngle - the horizontal angle of the beam in RADIANS. 
		 * @param vertAngle - the vertical angle of the beam in RADIANS
		 * @return the tranmission loss in dB; 
		 */
		double getBeamProfileTL(double horzAngle, double vertAngle); 
		
		/**
		 * Get the vocalisation waveform from different parts of the animals beam profile. 
		 * @param horzAngle - the horizontal angle
		 * @param vertAngle - the vertical angle. 
		 * @param sR the sample rate. 
		 * @return a time series of click waveform. Each measurment is one bin. 
		 */
		double[] getVocWav(double horzAngle, double vertAngle, int sR);

		/**
		 * Get a time series of vocalisations
		 * @param timeStart - time to start the click train in millis. 
		 * @param longTimeEnd - time to end end click train in millis
		 * @return an array of vocalisation starts times and amplitude in dB re 1uPa
		 */
		double[][] getVocSeries(long timeStart, long longTimeEnd);
	


}
