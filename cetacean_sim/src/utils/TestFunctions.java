package utils;

import animal.SimpleOdontocete;
import propogation.Propogation;
import propogation.SimplePropogation;

/**
 * Test various functions  
 * @author Jamie Macaulay
 *
 */
public class TestFunctions {
	
	
	/**
	 * Test the transmission loss functions
	 */
	public static void testTransmissionLoss() {
		
		double[] recieverPos= new double[]{0,0,-10}; 		
		double[] animalLocation= new double[]{0,-30,-10}; 		
		double[] animalAngle = new double[] {Math.toRadians(45), Math.toRadians(0)}; 
		double onAxisSL=180; 
		
		SimpleOdontocete simpleOdontocete =new SimpleOdontocete(); 

		Propogation propogation =new SimplePropogation(20,0.04); 

		double vertAngle; 
		double recieveddB; 
		double transTotal ;
		for (int i=0; i<19; i++) {
			vertAngle=-90+10*i; 
			animalAngle = new double[] {Math.toRadians(0), Math.toRadians(vertAngle)}; 
			transTotal =  CetSimUtils.tranmissionTotalLoss(recieverPos, animalLocation, animalAngle, simpleOdontocete.beamSurface, propogation);
			recieveddB= onAxisSL + transTotal;
			System.out.println("Recieved dB " + recieveddB + " for an angle of " + vertAngle + " trans. loss " +transTotal);

		}
		
	}

	
	public static void main(String[] args) {
		testTransmissionLoss() ;

		
		
	}

}
