package doug;

import simulation.RandomSimVariable;

public class RangeDepthPositionGenerator implements RandomPositionGenerator {
		
	private RandomSimVariable rangeSim, depthSim, angleSim;

	public RangeDepthPositionGenerator(double[] rangeRange, double[] depthRange) {
		super();
		rangeSim = new RandomSimVariable("Range", rangeRange[0], rangeRange[1]);
		depthSim = new RandomSimVariable("Depth", depthRange[0], depthRange[1]);
		angleSim = new RandomSimVariable("Ang", 0, Math.PI*2.);
	}

	@Override
	public double[] getNextXYZ() {
		double range = rangeSim.getNextRandom();
		double depth = depthSim.getNextRandom();
		double ang = angleSim.getNextRandom();
		double[] xyz = {range*Math.cos(ang), range*Math.sin(ang), depth};
		return xyz;
	}
	
	

}
