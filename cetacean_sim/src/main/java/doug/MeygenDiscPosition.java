package doug;

import simulation.RandomSimVariable;

public class MeygenDiscPosition implements RandomPositionGenerator {

	private  RandomSimVariable rangeSim, yzang;
	private double radius;
	private double height;
	
	public MeygenDiscPosition(double[] range, double height, double radius) {
		rangeSim = new RandomSimVariable("Range", range[0], range[1]);
		yzang = new RandomSimVariable("", 0, Math.PI*2.);
		this.height = height;
		this.radius = radius;
	}
	
	@Override
	public double[] getNextXYZ() {
		double yzr = Math.sqrt(Math.random())*radius;
		double yza = yzang.getNextRandom();
		double y = Math.cos(yza)*yzr;
		double z = Math.sin(yza)*yzr + height;
		double x = rangeSim.getNextRandom();
		double[] xyz = {x, y, z};
		return xyz;
	}

}
