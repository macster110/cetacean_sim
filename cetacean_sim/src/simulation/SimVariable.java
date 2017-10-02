package simulation;

public interface SimVariable {
	
	public enum DistributionType {UNIFORM, NORMAL};

	/**
	 * Get the type of distirbution. 
	 * @return the distirbution type./ 
	 */
	public DistributionType getType(); 
	
	/**
	 * Get the name of the variable. This is a unique name for each different variable and 
	 * does not need to be realted to the varible type. 
	 * @return the name of the variable
	 */
	public String getName(); 
	
	/**
	 * Get the next random from the distirbution. 
	 */
	public double getNextRandom(); 

}
