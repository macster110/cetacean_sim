package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Builds a custom probability distribution and then allows random sampling from that distribution.
 * @author Jamie Macaulay
 *
 */
public class ProbabilitySampler {

	private Random random = new Random();
	private List<Double> probabilities;
	private double[] cdf;

	public ProbabilitySampler(List<Double> probabilities) {
		super();
		// TODO check sum of probabilities is very close to 1.0
		this.probabilities = Collections.unmodifiableList(probabilities);
		this.cdf = buildCDF(this.probabilities);
	}

	public static double[] buildCDF(List<Double> probabilities) {
		double[] cdf = new double[probabilities.size()];
		cdf[0] = probabilities.get(0);
		for (int i = 1; i < probabilities.size(); i++)
			cdf[i] = cdf[i - 1] + probabilities.get(i);
		return cdf;
	}

	public Integer sample() {
		int index = Arrays.binarySearch(cdf, random.nextDouble());
		return (index >= 0) ? index : (-index - 1);
	}


	public static void main(String[] args) {
		List<Double> probabilities = Arrays.asList(0.32, 0.68, 0.45, 0.67, 0.32);
		ProbabilitySampler probabilitySampler = new ProbabilitySampler(probabilities);
		
		System.out.println("Probabilities size: " + probabilities.size());

		int nSamples = 100000;
		final List<Integer> distribution = new ArrayList<>(Collections.nCopies(probabilities.size(), 0));
		IntStream
		.range(0, nSamples)
		.map(i -> probabilitySampler.sample())
		.forEach(randomItem -> distribution.set(randomItem, distribution.get(randomItem) + 1));
		System.out.println(distribution);
	}

}
