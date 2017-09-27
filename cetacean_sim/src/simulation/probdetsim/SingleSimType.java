package simulation.probdetsim;

import java.util.ArrayList;

import javafx.scene.layout.Region;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;

public class SingleSimType implements ProbDetSimType {

	/**
	 * The curerent type of simualtion. 
	 */
	private ProbDetSim probDetSim;

	public SingleSimType(ProbDetSim probDetSim) {
		this.probDetSim=probDetSim; 
	}

	@Override
	public String getName() {
		return "Single Simulation";
	}

	@Override
	public ArrayList<ProbDetResult> runSimulation(ProbDetSimSettings probDetSettings) {
		probDetSim.getMonteCarloSim().run(probDetSettings);

		//only return result if not canclled. 
		if (!probDetSim.getMonteCarloSim().isCancelled()) {
			ArrayList<ProbDetResult> results= new  ArrayList<ProbDetResult>(); 
			results.add(probDetSim.getProbDetResults());
			return results;
		}
		else return null; 
	}

	@Override
	public Region getSettingsNode() {
		// TODO Auto-generated method stub
		return null;
	}

}
