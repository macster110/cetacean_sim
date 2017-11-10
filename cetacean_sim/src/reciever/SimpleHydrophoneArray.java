package reciever;

/**
 * The simple reciever array 
 * @author Jamie Macaulay
 *
 */
public class SimpleHydrophoneArray implements HydrophoneArray {
	
	/**
	 * The reciever array 
	 */
	double[][] recieverArray;  
	
	/**
	 * The sensitivity offset. 
	 */
	double[] sensOffset;

	/**
	 * The name of the hydrophgone array. 
	 */
	private String name="Hydrophone Array"; 



	public SimpleHydrophoneArray(double[][] recieverArray) {
		//the reciever array
		this.recieverArray=recieverArray; 
		//create a sensitivity offset of zeros. 
		sensOffset=new double[recieverArray.length]; 
	}


	public SimpleHydrophoneArray(String string, double[][] recieverArray) {
		this.name=string; 
		//the reciever array
		this.recieverArray=recieverArray; 
		//create a sensitivity offset of zeros. 
		sensOffset=new double[recieverArray.length]; 
	}
	

	public SimpleHydrophoneArray(double[][] recieverArray, double[] sens) {
		//the reciever array
		this.recieverArray=recieverArray; 
		//create a sensitivity offset of zeros. 
		sensOffset=sens;
	}



	@Override
	public double[][] getArrayXYZ() {
		return recieverArray;
	}

	@Override
	public double[] getSensOffset() {
		return sensOffset;
	}


	@Override
	public String getName() {
		return name;
	}

}
