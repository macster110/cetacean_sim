package simulation;

import java.util.Random;

/**
 * Simulation variable which is drawn from a Gaussian/Normal distribution 
 * @author Jamie Macaulay
 *
 */
public class NormalSimVariable implements SimVariable {
	
	Random r = new Random(); 
	
	/**
	 * The mean of the distribution
	 */
	double mean;
	
	/**
	 * The standard deviation of the distribution. 
	 */
	double std; 
	
	/**
	 *The name of the distribution. 
	 */
	String name;
	
	public NormalSimVariable(String name, double mean, double std){
		this.name=name;
		this.mean=mean;
		this.std=std; 
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getNextRandom() {
		return r.nextGaussian()*std + mean;
	}

}