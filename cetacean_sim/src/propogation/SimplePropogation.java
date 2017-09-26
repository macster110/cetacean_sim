package propogation;

/*
 *Very simple propagation using cylindrical->spherical spreading and an absorption coefficient. 
 */
public class SimplePropogation implements Propogation {
	
	/**
	 * The spreading coefficient
	 */
	public double spreading=20; 
	
	/**
	 * The absorption coefficient. 
	 */
	public double absorption=0.04; 
	
	public SimplePropogation(double spreadingCoef, double absorptionCoeff) {
		this.spreading=spreadingCoef; 
		this.absorption=absorptionCoeff; 
	}

	@Override
	public double getTranmissionLoss(double[] point1, double[] point2) {
		//calculate the straight line distance between the two points. 
		double distance = Math.sqrt(Math.pow(point1[0]-point2[0],2) + Math.pow(point1[1]-point2[1],2) + Math.pow(point1[2]-point2[2],2)); 
		double TL = spreading*Math.log10(distance) + absorption*distance; 
		return -TL;
	}

	
}
