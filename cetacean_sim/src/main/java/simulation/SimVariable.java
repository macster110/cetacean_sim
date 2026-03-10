package simulation;


public interface SimVariable {
	
	/**
	 * The type of distribution
	 * @author Jamie Macaulay 
	 *
	 */
	public enum DistributionType {UNIFORM, NORMAL, LOGNORMAL, CUSTOM};

	/**
	 * Get the type of distribution. 
	 * @return the distribution type./ 
	 */
	public DistributionType getType(); 
	
	/**
	 * Get the name of the variable. This is a unique name for each different variable and 
	 * does not need to be related to the variable type. 
	 * @return the name of the variable
	 */
	public String getName(); 
	
	/**
	 * Get the next random from the distribution. 
	 */
	public double getNextRandom();
	
	/**
	 * The limits in between which the sim variable is relevant. 
	 * This value can be null if there are no limits
	 * @return - the limits in between which the distribution applies. 
	 */
	public double[] getLimits();

	
	/**
	 * Get the name for the variable type. 
	 * @param distType  - the distribution type
	 * @return the string name of the distribution type. 
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
	
	/**
	 * Get the DistributionType from the string name of a distribution variable. 
	 * @param distType  - the distribution string type
	 * @return the type of distribution. 
	 */
	public static DistributionType getDistributionType(String distType) {
		DistributionType name =null; 
		switch (distType) {
		case "Normal":
			name=DistributionType.NORMAL; 
			break;
		case "Uniform":
			name=DistributionType.UNIFORM; 
			break;
		case "Log-normal":
			name=DistributionType.LOGNORMAL; 
			break;
		case "Custom":
			name=DistributionType.CUSTOM; 
			break;
		default:
			break;
		}
		return name;  
	}

	

}
