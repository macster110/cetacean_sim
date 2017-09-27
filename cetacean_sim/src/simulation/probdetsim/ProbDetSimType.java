package simulation.probdetsim;

import java.util.ArrayList;

import javafx.scene.layout.Region;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;

/**
 * The type of probability of detection simulation to run. e.g. running a simulation with settings a bunch of noise. 
 * @author Jamie Macaulay
 *
 */
public interface ProbDetSimType {
	
	/**
	 * Get the name of this type of simulation
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Run the simulations
	 * @param probDetSettings 
	 * @return all simualtion results 
	 */
	public ArrayList<ProbDetResult> runSimulation(ProbDetSimSettings probDetSettings); 
	
	/**
	 * Node for changing the seetings for this particular type of simulation. 
	 * @return the settings node. 
	 */
	public Region getSettingsNode(); 

}
