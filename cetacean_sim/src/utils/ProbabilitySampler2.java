package utils;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

public class ProbabilitySampler2 {

	public static void main(String[] args) {
//		int[] numsToGenerate           = new int[]    { 1,   2,    3,   4,    5   };
//		double[] discreteProbabilities = new double[] { 0.1, 0.25, 0.3, 0.25, 0.1 };
		
		double[] discreteProbabilities = {0.00990099009900990,	0.0297029702970297, 0.0495049504950495,	0.0891089108910891, 
				0.0792079207920792,	0.0693069306930693, 0.0594059405940594,	0.0495049504950495,	0.0396039603960396,
				0.0297029702970297,	0.0198019801980198,	0.00990099009900990,	0.0198019801980198,	0.0297029702970297,
				0.0396039603960396,	0.0495049504950495,	0.0594059405940594, 0.0693069306930693,	0.0792079207920792,
				0.0594059405940594,	0.0297029702970297,	0.0198019801980198,	0.00990099009900990};
		
		int[] numsToGenerate    = new int[discreteProbabilities.length]; 
		for (int i=0; i<discreteProbabilities.length; i++) {
			numsToGenerate[i]=i;
		}
		
		EnumeratedIntegerDistribution distribution = 
		    new EnumeratedIntegerDistribution(numsToGenerate, discreteProbabilities);

		int numSamples = 10000;
		int[] samples = distribution.sample(numSamples);
		
		System.out.println("Samples: " + samples.length);
	}
	
	

}
