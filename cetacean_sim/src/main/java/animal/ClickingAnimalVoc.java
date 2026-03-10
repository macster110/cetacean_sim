package animal;

/**
 * Clicking animal vocalisations. 
 * @author Jamie Macaulay 
 *
 */
public class ClickingAnimalVoc implements AnimalVocalisations {
	
	/**
	 * The vocalisation times in seconds from the simulation start
	 */
	private double[] vocTimes; 
	
	/**
	 * The vocalisation amplitudes in dB re 1uPa pp @ 1m on-axis. 
	 */
	private double[] vocAmplitudes; 
	
	/**
	 * Constructor for a time series of vocalisations and amplitudes. 
	 * @param vocTimes -  vocalisation times in seconds from the simulation start. 
	 * @param vocAmplitudes - vocalisation amplitudes in dB re 1uPa pp @ 1m on-axis. 
	 */
	public ClickingAnimalVoc(double[] vocTimes, double[] vocAmplitudes) {
		this.vocTimes = vocTimes; 
		this.vocAmplitudes = vocAmplitudes; 
	}
	
	@Override
	public double[] getVocTimes() {
		return vocTimes;
	}

	@Override
	public double[] getVocAmplitudes() {
		return vocAmplitudes;
	}

	@Override
	public double[] getVocWav(int n, double horzAngle, double vertAngle, int sR) {
		// TODO Auto-generated method stub
		return null;
	}

}
