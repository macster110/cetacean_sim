package animal;

import propogation.SimplePropogation;
import utils.CetSimUtils;

/**
 * Testing some angle stuff on simple Odontocete. 
 * @author Jamie Macaulay
 *
 */
public class SimpleOdontecteTest {
	
	public static void main(String[] args) {
		
		//create simple animal object
		SimpleOdontocete simpleAnimal = new SimpleOdontocete(); 
		
		//create a dummy propogation object (we only want to look at effects of beam profile here and not range)
		SimplePropogation propogation = new SimplePropogation(20,0.04); // no effect of propogation hack
		
		double[] reciever = new double[] {0,0,-20};
		double[] animalpos = new double[] {0,0,0}; 
		double[] animalangle = new double[] {Math.toRadians(0), Math.toRadians(90)}; 
		
		double[] testangles = doug.AnimalAngles.getRelativeAngles(reciever, animalpos, animalangle);

		System.out.println("Horz angle: " + Math.toDegrees(testangles[0]) + " Vert angle: "+ Math.toDegrees(testangles[1])); 
		
		double transloss = CetSimUtils.tranmissionTotalLoss(reciever, animalpos, animalangle, simpleAnimal.beamSurface, propogation); 
		
		System.out.println("Transloss: " + transloss); 

	}

}
