package simulation;

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

/**
 * A custom probability distribution. 
 * 
 * @author Jamie Macaulay 
 *
 */
public class CustomSimVar implements SimVariable {
	
	
	/**
	 * Default probability.
	 * 
	 */
	private static double[] defaultProb = {0.00990099009900990,	0.0297029702970297, 0.0495049504950495,	0.0891089108910891, 
			0.0792079207920792,	0.0693069306930693, 0.0594059405940594,	0.0495049504950495,	0.0396039603960396,
			0.0297029702970297,	0.0198019801980198,	0.00990099009900990,	0.0198019801980198,	0.0297029702970297,
			0.0396039603960396,	0.0495049504950495,	0.0594059405940594, 0.0693069306930693,	0.0792079207920792,
			0.0594059405940594,	0.0297029702970297,	0.0198019801980198,	0.00990099009900990};
	/**
	 * 
	 */
	private EnumeratedIntegerDistribution integerDistirbution; 
	
	/**
	 * The probabilities
	 */
	private double[] discreteProbabilities=defaultProb; 
	

	/**
	 * The minimum value
	 */
	private double min=-100; 

	/**
	 * The maximum value. 
	 */
	private double max=100; 
	
	/**
	 * The limits in which the distribution applies e.g. depth. 
	 */
	private double[] limits;
	
	/**
	 * The string name. 
	 */
	private String name="";
	
	/**
	 * Function for interpolating the distribution input values. 
	 */
	private PolynomialSplineFunction interpFunction; 

	
	public CustomSimVar() {
		setDistirbution(defaultProb, min, max); 
	}
	
	/**
	 * Custom simulation variable. 
	 * @param name - the name of the simulation variable. 
	 * @param probData - the probability data. 
	 * @param min - the minimum value.
	 * @param max - the maximum value. 
	 */
	public CustomSimVar(String name, double[] probData, double min, double max) {
		this.name=name; 
		setDistirbution(probData, min,max); 
	}
	
	/**
	 * Custom simulation variable. 
	 * @param name - the name
	 * @param probData - the probability data
	 * @param min - the minimum value.
	 * @param max - the maximum value. 
	 * @param simLims - the distribution limits; 
	 */
	public CustomSimVar(String name, double[] probData, double min, double max, double[] simLims) {
		this.name=name; 
		this.limits=simLims;
		setDistirbution(probData, min,max); 
	}
	
	/**
	 */
	public CustomSimVar(double[] probData, double min, double max) {
		setDistirbution(probData, min,max); 
	}

	public CustomSimVar(Double value, Double value2) {
		setDistirbution(defaultProb, min,max); 
	}
	
	/**
	 * Get the probability value for a specific portion of the distribution. 
	 * @param value - the x axis value. 
	 */
	public double getProbability(double value) {
		return interpFunction.value(value);
	}

	/**
	 * Set the custom distribution 
	 * @param discreteProbabilities
	 * @param minVal - the minimum value of the distribution. 
	 * @param maxVal - the maximum value of the distribution.
	 */
	private void setDistirbution(double[] discreteProbabilities, double minVal, double maxVal) {
		this.min=minVal;
		this.max=maxVal; 
		this.discreteProbabilities=discreteProbabilities; 
		
		//System.out.println("Custom sim variable: " + this);

		
		int[] numsToGenerate    = new int[discreteProbabilities.length]; 
		for (int i=0; i<discreteProbabilities.length; i++) {
			numsToGenerate[i]=i;
		}
		
		integerDistirbution = 
		    new EnumeratedIntegerDistribution(numsToGenerate, discreteProbabilities);
		
		LinearInterpolator interpolator= new LinearInterpolator(); 
		
		//build a function interpolate the custom values. 
		double[] xVals = new double[discreteProbabilities.length];
		double snrBin = (maxVal - minVal) / (double) (discreteProbabilities.length-1);
		for (int i=0; i<discreteProbabilities.length; i++) {
			xVals[i]=minVal+snrBin*i; 
			//System.out.println(xVals[i]);
		} 
	
		interpFunction = interpolator.interpolate(xVals,
				 discreteProbabilities);
	}

	@Override
	public DistributionType getType() {
		return DistributionType.CUSTOM;
	}

	@Override
	public String getName() {
		return name; 
	}

	@Override
	public double getNextRandom() {
		int sample = integerDistirbution.sample(); 
		
		//now need to convert randomly sample. 
		double perc = (double) sample/(double) discreteProbabilities.length;
		double val = perc*(max-min)+min;
		
		return val;
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
	
	/**
	 * Get the maximum bound of the distribution. 
	 * @return the maximum of the distribution
	 */
	public Double getMax() {
		return this.max; 
	}
	
	/**
	 * Get the minimum bound of the distribution
	 * @return the minimum bound of the distribution. 
	 */
	public Double getMin() {
		return this.min; 
	}

	public void setMin(double min) {
		this.min=min;
	}


	public void setMax(double max) {
		this.max=max; 
	}

	/**
	 * Get the probability data. 
	 * @return the probability data. 
	 */
	public double[] getProbData() {
		return discreteProbabilities;
	}


}
