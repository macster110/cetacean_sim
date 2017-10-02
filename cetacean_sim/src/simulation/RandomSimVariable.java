package simulation;

import java.util.Random;

/**
 * Generates a uniforma random value between a minimum and a maximum value
 * @author macst
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

}
