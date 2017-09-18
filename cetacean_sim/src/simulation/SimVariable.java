package simulation;

public interface SimVariable {
	
	/**
	 * Get the name of the variable 
	 * @return the name of the variable
	 */
	public String getName(); 
	
	/**
	 * Get the next random from the distirbution. 
	 */
	public double getNextRandom(); 

}
