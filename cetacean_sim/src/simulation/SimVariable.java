package simulation;


public interface SimVariable {
	
	/**
	 * The type of distirbution
	 * @author Jamie Macaulay 
	 *
	 */
	public enum DistributionType {UNIFORM, NORMAL, LOGNORMAL, CUSTOM};

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
	
	/**
	 * The limits in between which the sim variable is relevent. 
	 * This value can be null if there are no limits
	 * @return - the limits in between whihc the distribution applies. 
	 */
	public double[] getLimits();

	
	/**
	 * Get the name for the varibale type. 
	 * @param distType  - the distribution type
	 * @return the string name of the distirbution type. 
	 */
	public static String getSimVarName(DistributionType distType) {
		String name =""; 
		switch (distType) {
		case NORMAL:
			name="Normal"; 
			break;
		case UNIFORM:
			name="Uniform"; 
			break;
		case LOGNORMAL:
			name="Log-normal"; 
			break;
		case CUSTOM:
			name="Custom"; 
			break;
		default:
			break;
		}
		return name;  
	}

	

}
