package doug;

import simulation.RandomSimVariable;

public class MeygenPositionGenerator implements RandomPositionGenerator {

	private double radius = 9;
	
	public double height = 0;
	
	public double heightAbove = 17; // surface dist above turbine centre. 
	
	public double depthBelow = 15; // bottom distance below turbine centre
	
	public double sideDistance = 2*radius;
	
	public static final int INDISC = 0;
	public static final int ABOVE = 1;
	public static final int BELOW = 2;
	public static final int SIDE = 3;
		
	private int currentLoc = INDISC;
	
	public int getCurrentLoc() {
		return currentLoc;
	}

	public void setCurrentLoc(int currentLoc) {
		this.currentLoc = currentLoc;
	}

	private double[] xRange = {0., 50.};
	
	private RandomSimVariable xRangeSim;
	
	private RandomSimVariable aboveZ, belowZ, aboveBelowY, sideY, sideZ, yzang;
	
	public MeygenPositionGenerator(double[] xRange) {
		super();
		this.xRange = xRange;
		makeRegions();
	}

	public double[] getxRange() {
		return xRange;
	}

	public void setxRange(double[] xRange) {
		this.xRange = xRange;
		xRangeSim = new RandomSimVariable("x", xRange[0], xRange[1]);
	}

	private void makeRegions() {
		if (xRange != null) {
			xRangeSim = new RandomSimVariable("x", xRange[0], xRange[1]);
		}
		aboveZ = new RandomSimVariable("AboveZ", height, height + heightAbove);
		belowZ = new RandomSimVariable("BZ", height-depthBelow, height);
		aboveBelowY = new RandomSimVariable("ABY", -radius, radius);
		sideY = new RandomSimVariable("SideY", radius, radius);
		sideZ = new RandomSimVariable("SZ", height - depthBelow, height + heightAbove);
		yzang = new RandomSimVariable("", 0, Math.PI*2);
	}
	
	@Override
	public double[] getNextXYZ() {
		switch (currentLoc) {
		case ABOVE:
			return getNextAbove();
		case BELOW:
			return getNextBelow();
		case INDISC:
			return getNextinDisc();
		case SIDE:
			return getNextToSide();
		default:
			return null;		
		}
	}

	private double[] getNextAbove() {
		while (true) {
			double y = aboveBelowY.getNextRandom();
			double z = aboveZ.getNextRandom();
			if (isInDisc(y, z) == false) {
				double[] xyz = {xRangeSim.getNextRandom(), y, z};
				return xyz;
			}
		}
	}

	private double[] getNextBelow() {
		while (true) {
			double y = aboveBelowY.getNextRandom();
			double z = belowZ.getNextRandom();
			if (isInDisc(y, z) == false) {
				double[] xyz = {xRangeSim.getNextRandom(), y, z};
				return xyz;
			}
		}
	}

	private double[] getNextinDisc() {
		double yzr = Math.sqrt(Math.random())*radius;
		double yza = yzang.getNextRandom();
		double y = Math.cos(yza)*yzr;
		double z = Math.sin(yza)*yzr + height;
		double x = xRangeSim.getNextRandom();
		double[] xyz = {x, y, z};
		return xyz;
	}

	private double[] getNextToSide() {
		double y = sideY.getNextRandom();
		if (Math.random() > 0.5) {
			y = -y;
		}
		double x = xRangeSim.getNextRandom();
		double z = sideZ.getNextRandom();
		double[] xyz = {x, y, z};
		return xyz;
	}

	private boolean isInDisc(double y, double z) {
		double r = Math.sqrt(Math.pow(z-height,2) + Math.pow(y, 2)); 
		return r <= radius;
	}
}
