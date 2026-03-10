package simulation;

import layout.simulation.SimulationView;

/**
 * Basic functions for a simualtion 
 * @author Jamie Macaulay
 *
 */
public interface SimulationType {
	
	/**
	 * Run the simulation
	 * @return true if the run is successful. 
	 */
	public boolean run(); 
	
	/**
	 * Stop the simulation from running
	 * @return true if the simulation stops successfully. 
	 */
	public boolean stop(); 

	
	/**
	 * Get the simulation view. This handles GUI components of the simulation. 
	 * @return the simulation view. 
	 */
	public SimulationView getSimView();

	/*
	 * Get the name of the simulation.
	 */
	public String getName(); 

}
