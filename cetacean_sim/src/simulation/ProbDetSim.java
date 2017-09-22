package simulation;

import cetaceanSim.CetSimControl;
import javafx.application.Platform;
import javafx.concurrent.Task;
import layout.simulation.ProbDetSimView;
import layout.simulation.SimulationView;
import simulation.ProbDetMonteCarlo.ProbDetResult;

/**
 * Simulates a 2D probability of detection
 * @author Jamie Macaulay 
 */
public class ProbDetSim  implements SimulationType {

	/**
	 * The view for the probability of detection simulation
	 */
	private ProbDetSimView probDetView;
	
	/**
	 * The settings for the probability of detection simulation
	 */
	private ProbDetSimSettings probDetSettings = new ProbDetSimSettings();
	
	/**
	 * The Monte Carlo simulation. 
	 */
	private ProbDetMonteCarlo probDetMonteCarlo; 

	/**
	 * Reference to the simulation control. 
	 */
	@SuppressWarnings("unused")
	private CetSimControl cetSimControl;

	/**
	 * The task for running running the Monete Carlo simulation. 
	 */
	private MonteCarloTask monteCarloTask; 

	/**
	 * Constructor for the simulation./ 
	 * @param cetSimControl
	 */
	public ProbDetSim(CetSimControl cetSimControl) {
		this.cetSimControl=cetSimControl; 
		probDetMonteCarlo= new ProbDetMonteCarlo(); 
		getSimView();//force creation of GUI 
		probDetMonteCarlo.addStatusListener((flag, bootstraps, simProb)->{
			Platform.runLater(()->{
				probDetView.notifyUpdate(flag);
				probDetView.setProgress(bootstraps, simProb); 
			});
		}); 
	}

	@Override
	public boolean run() {
		probDetMonteCarlo.run(this.probDetSettings); 
		return true;
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
		probDetMonteCarlo.stop(); 
		return true;
	}
	
	@Override
	public String getName() {
		return "Probability of Detection Sim";
	}

	/**
	 * Run or stop the simualtion 
	 * @param start - true to start the simulation. False to stop the simulation. 
	 */
	public void run(boolean start) {
		this.probDetSettings = probDetView.getParams(probDetSettings); 
		
		if (monteCarloTask!=null && monteCarloTask.isRunning()) {
			monteCarloTask.cancel(); 
		}
		
		if (start) {
			monteCarloTask = new MonteCarloTask(); 
			new Thread(monteCarloTask).start();
		}
		else {
			monteCarloTask.cancel(); 
		}
	}
	
	
	/**
	 * Task for running a Monte Carlo simulation. 
	 * @author Jamie Macaulay 
	 *
	 */
	class MonteCarloTask extends Task <Integer>{


		@Override
		protected Integer call() throws Exception {
			try {
				probDetMonteCarlo.run(probDetSettings);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return 0;
		}
		
		@Override 
		protected void cancelled() {
            super.cancelled();
            probDetMonteCarlo.stop();
        }
		
		@Override
		protected void done() {
            super.done();
            probDetView.notifyUpdate(StatusListener.SIM_FINIHSED); 
        }
		
	}

	/**
	 * Check whether the simualtion is running. 
	 * @return true if the simualtion is running. 
	 */
	public boolean isRunning() {
		return probDetMonteCarlo.isRunning();
	}

	/**
	 * Convenience function to get the number of boostraps in the current simulations. 
	 * @return the number of boostraps set for the current simulation. 
	 */
	public int getNBootstraps() {
		return this.probDetSettings.nBootStraps;
	}

	
	/**
	 * Get the results from the last simualtion
	 * @return the result 
	 */
	public ProbDetResult getProbDetResults() {
		// TODO Auto-generated method stub
		return this.probDetMonteCarlo.getResult();
	}

}
