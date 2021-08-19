package animal;

/**
 * Get a series of vocalisations from an animal
 * 
 * @author Jamie Macaulay
 *
 */
public interface AnimalVocalisations  {
	
	/**
	 * 	Get the time of the vocalisations in seconds from the start of the simulation
	 * @return the times of each vocalisation in seconds from the start of the simulation
	 */
	public double[] getVocTimes();

	/**
	 * Get the vocalisation amplitudes. These correspond to getVocTimes
	 * @return the on axis vocalisation amplitudes in dB re 1uPa pp; 
	 */
	public double[] getVocAmplitudes();


	/**
	 * Get the vocalisation waveform from different parts of the animals beam profile. 
	 * @param n - the index of the vocalisation form the time series in getVocsERIES
	 * @param horzAngle - the horizontal angle
	 * @param vertAngle - the vertical angle. 
	 * @param sR the sample rate. 
	 * @return a time series of click waveform. Each measurment is one bin. 
	 */
	public double[] getVocWav(int n, double horzAngle, double vertAngle, int sR);

}
