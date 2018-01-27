package simulation;

import java.util.Random;

/**
 * Generates a uniform random value between a minimum and a maximum value
 * @author Jamie Macaulay
 *
 */
public class RandomSimVariable implements SimVariable {
	
	private Random r = new Random(); 
	
	/**
	 * The name of the variable 
	 */
	String name; 
	
	/**
	 * The minimum value of the variable 
	 */
	double min;
	
	/**
	 * The maximum, value of the variable 
	 */
	double max;

	/**
	 * The limits in between which this distribution applies. 
	 */
	private double[] limits; 
	
	
	public RandomSimVariable(String name, double min, double max) {
		this.name=name;
		this.min=min;
		this.max=max; 
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public double getNextRandom() {
		return (r.nextDouble()*(max-min)) + min;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.UNIFORM;
	}

	/**
	 * Get the minimum 
	 * @return the minimum 
	 */
	public double getMin() {
		return min;
	}
	
	/**
	 * The maximum value
	 * @return the max value. 
	 */
	public double getMax() {
		return max;
	}
	
	public String toString() {
		return String.format("Uniform distribution: Min: %.3f  Max %.3f", min, max);
	}

	@Override
	public double[] getLimits() {
		return limits;
	}
	
	/**
	 * Set the limits between which the variable applies. These cna be any number and dimensions e.g. depth. 
	 * Set to  null for no limits. 
	 * @param limits - the limits (min, max).
	 */
	public void setLimits(double[] limits) {
		this.limits = limits;
	}
}
