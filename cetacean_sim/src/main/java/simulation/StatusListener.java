package simulation;

/**
 * Listener for updates to the Monet Carlo Simulation
 * @author Jamie Macaulay 
 *
 */
public interface StatusListener {
	
	/**
	 * The simulation has started
	 */
	public static final int SIM_STARTED = 0; 
	
	/**
	 * The simulation has finished. Results are available.
	 */
	public static final int SIM_FINIHSED = 1;

	/**
	 * The simulation is currently running. 
	 */
	public static final int SIM_RUNNING = 2;

	/**
	 * The simulation was finished. There are no results. 
	 */
	public static final int SIM_CANCELLED = 3; 

	
	/**
	 * Called whenever the simulation is updated 
	 * @param actionFlag - the type of update. 
	 * @param progressBootstrap - the current progress of all simulation bootstraps. 
	 * @param progressSim - the progress of the current simulation. 
	 */
	public void statusAction(int actionFlag,  int progressBootstrap, double progressSim);		
	

}
