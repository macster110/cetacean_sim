package propogation;

/**
 * Calculate the acoustic transmission loss between two points. 
 * @author Jamie Macaulay 
 *
 */
public interface Propogation {
	
	/**
	 * Get the transmission loss in dB between two points
	 * @param point1 - the first point in Cartesian (x, y, z)
	 * @param point2 - the second point in Cartesian (x,y,z)
	 * @return
	 */
	public double getTranmissionLoss(double[] point1, double[] point2); 


}
