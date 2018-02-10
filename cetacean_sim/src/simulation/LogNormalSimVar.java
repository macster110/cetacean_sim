package simulation;

import org.apache.commons.math3.distribution.LogNormalDistribution;

/**
 * Log normal distribution. 
 * @author Jamie Macaulay
 */
public class LogNormalSimVar implements SimVariable {
	
	/**
	 * The log normal distribution.
	 */
	private LogNormalDistribution logNormalDist;
	
	/**
	 * The limits in which the distribution applies e.g. depth. 
	 */
	private double[] limits;
	
	/**
	 * The name. 
	 */
	private String name;

	/**
	 * The truncation distance. 
	 */
	private double truncation; 
	
	/**
	 * True to flip[ the distribution. 
	 */
	private boolean negative; 


	public LogNormalSimVar(String name, double scale, double shape, double truncation, boolean negative) {
		this.name=name; 
		this.truncation=truncation; 
		this.negative=negative; 
		logNormalDist=new LogNormalDistribution(scale, shape); 
	}

	public LogNormalSimVar(String name, double scale, double shape, double truncation, boolean negative, 
			double[] simLims) {
		this.name=name; 
		this.truncation=truncation; 
		this.negative=negative; 
		logNormalDist=new LogNormalDistribution(scale, shape); 
		this.limits=simLims;
	}

	@Override
	public DistributionType getType() {
		return DistributionType.LOGNORMAL;
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public double getNextRandom() {
		double sample= logNormalDist.sample();
		if (sample>this.truncation) return getNextRandom();
		else return this.negative ? -sample : sample;
	}
	
	@Override
	public double[] getLimits() {
		return limits;
	}
	
	/**
	 * Set the limits between which the variable applies. These can be any number and dimensions e.g. depth. 
	 * Set to  null for no limits. 
	 * @param limits - the limits (minimum, maximum).
	 */
	public void setLimits(double[] limits) {
		this.limits = limits;
	}

	/**
	 * Get the scale value for the log normal distribution
	 * @return the scale value. 
	 */
	public double getScale() {
		return logNormalDist.getScale();
	}

	/**
	 * Get the shape value for the lognormal distribution. 
	 * @return the scale value. 
	 */
	public double getShape() {
		return logNormalDist.getShape();
	}

	/**
	 * Get the truncation of log normal. 
	 * @return the truncation.
	 */
	public double getTruncation() {
		return truncation;
	}
	
	/**
	 * Check whether the distribution is flipped negative.
	 * @return true if negative
	 */
	public boolean isNegative() {
		return negative;
	}

	/**
	 * Set to flip the negative distribution
	 * @param negative - the negative distribution. 
	 */
	public void setNegative(boolean negative) {
		this.negative = negative;
	}
	
	
	public String toString() {
		return String.format("Log-normal distribution: Shape: %.3f Scale %.3f Truncation %.3f", 
				logNormalDist.getScale(), logNormalDist.getShape(), this.getTruncation());
	}

}
