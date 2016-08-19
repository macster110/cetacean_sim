package bathymetry;

/**
 * The bathymetry type. 
 * @author jamie
 *
 */
public interface BathymetryType {
	
	/**
	 * Called to chnage settings. May open a dialog. 
	 *  
	 */
	public void changeSettings(); 
	
	public double[][] getBathySurface(); 

}
