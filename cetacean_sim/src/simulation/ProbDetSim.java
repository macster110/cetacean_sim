package simulation;

import cetaceanSim.CetSimControl;
import layout.simulation.ProbDetSimView;
import layout.simulation.SimulationView;

/**
 * Simulates a 2D probability of detection
 * @author Jamie Macaulay 
 */
public class ProbDetSim  implements SimulationType {
	
	private ProbDetSimView probDetView;
	
	private ProbDetSimSettings probDetSettings = new ProbDetSimSettings();

	/**
	 * The simulation control. 
	 */
	private CetSimControl cetSimControl; 

	/**
	 * Constructor for the simulation./ 
	 * @param cetSimControl
	 */
	public ProbDetSim(CetSimControl cetSimControl) {
		this.cetSimControl=cetSimControl; 
	}

	@Override
	public boolean run() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SimulationView getSimView() {
		if (probDetView==null) {
			probDetView = new ProbDetSimView(this); 
		}
		return probDetView;
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Probability of Detection Sim";
	}

}
