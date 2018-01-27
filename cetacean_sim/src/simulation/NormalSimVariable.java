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
	
	/**
	 * Limits between which the variable applies. Cna be null 
	 */
	double[] limits= null; 
	

	public NormalSimVariable(String name, double mean, double std){
		this.name=name;
		this.mean=mean;
		this.std=std; 
	}
	
	public NormalSimVariable(String name, double mean, double std, double[] depthLims){
		this.name=name;
		this.mean=mean;
		this.std=std; 
		this.limits=depthLims;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getNextRandom() {
		return r.nextGaussian()*std + mean;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.NORMAL;
	}

	/**
	 * Get the mean 
	 * @return the mean of the distribution
	 */
	public double getMean() {
		return mean;
	}
	
	/**
	 * Get the standard deviation 
	 * @return the standard deviation of the distribution
	 */
	public double getStd() {
		return std;
	}
	
	public String toString() {
		return String.format("Normal distribution: Mean: %.3f Std %.3f", mean, std);
	}

	@Override
	public double[] getLimits() {
		return limits;
	}
	
	/**
	 * Set the limits between which the variable applies. These can be any number and dimensions e.g. depth. 
	 * Set to  null for no limits. 
	 * @param limits - the limits (min, max).
	 */
	public void setLimits(double[] limits) {
		this.limits = limits;
	}


}
