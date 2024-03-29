package simulation.probdetsim;

import java.io.File;
import java.util.ArrayList;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLStructure;

import cetaceanSim.CetSimControl;
import javafx.application.Platform;
import javafx.concurrent.Task;
import layout.simulation.ProbDetSimView;
import layout.simulation.SimulationView;
import simulation.SimulationType;
import simulation.StatusListener;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;

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
	 * List of probability of detection simulation types. 
	 */
	private ArrayList<ProbDetSimType> probDetSimTypes = new ArrayList<ProbDetSimType>(); 


	/**
	 * The index of the current sprob. det. simulation type
	 */
	private int probSimTypeIndex = 0; 

	/**
	 * The current results from the last completed simualtion. 
	 */
	private ArrayList<ProbDetResult> currentResult = new ArrayList<ProbDetResult>();

	/**
	 * The probability detection export class. 
	 */
	private ProbDetMTExport mtExport; 

	
	/**
	 * Constructor for the simulation./ 
	 * @param cetSimControl
	 */
	public ProbDetSim(CetSimControl cetSimControl) {
		this.cetSimControl=cetSimControl; 
		probDetMonteCarlo= new ProbDetMonteCarlo(); 

		//create all the different type of simmulations
		probDetSimTypes.add(new SingleSimType(this));
		probDetSimTypes.add(new NoiseSimType(this)); 

		getSimView();//force creation of GUI 
		//add status listener to recieve notification flags from the algorithm
		probDetMonteCarlo.addStatusListener((flag, bootstraps, simProb)->{
			Platform.runLater(()->{
				probDetView.notifyUpdate(flag);
				probDetView.setProgress(bootstraps, simProb); 
			});
		}); 

		//MATLAB import and export functi
		mtExport = new ProbDetMTExport(); 
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
		monteCarloTask.cancel(); 
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
	public void runSim(boolean start) {
		currentResult=null;
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
				currentResult=probDetSimTypes.get(probSimTypeIndex).runSimulation(probDetSettings); 
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
		return this.probDetMonteCarlo.getResult();
	}

	/**
	 * Save the latest data to a file. 
	 * @param file - the file to save to. 
	 */
	public void saveProbDetData(File file) {

		MLArray mlaArray = mtExport.resultsToStruct(this.currentResult);

		ArrayList<MLArray> results = new ArrayList<MLArray>(); 
		results.add(mlaArray); 

		mtExport.saveMTFile(file, results);

	}

	/**
	 * Import a settings file from a file.
	 * @param file - the file. 
	 */
	public boolean importProbDetData(File file ) {
		MatFileReader mfr = null; 
		try {
			if (file ==null) {
				System.out.println("The imported file is null");
				return false;
			}
			
			mfr = new MatFileReader(file);
			
			//get array of a name "my_array" from file
			MLStructure mlArrayRetrived;
			if (mfr.getMLArray("prob_det")!=null) {
				//if exported by cetsim with sim results. 
				MLStructure structu = (MLStructure) mfr.getMLArray("prob_det");
				mlArrayRetrived = (MLStructure)  structu.getField("settings"); 
			}
			else mlArrayRetrived = (MLStructure) mfr.getMLArray("settings");
			
			//System.out.println(mfr.getContent()); 
			
			ProbDetSimSettings probDetSettings = mtExport.structToSettings(mlArrayRetrived);
			
			//prob det settings. 
			if (probDetSettings!=null) {
				this.probDetSettings=probDetSettings;
				probDetView.setParams(probDetSettings);
				return true;
			}			
			else {
				System.out.println("The MATLAB to Java conversion did not work null");
			}
			
			return false;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}


	/**
	 * Get the Monte Carlo algorithm. 
	 * @return the monte carlo algorithm. 
	 */
	public ProbDetMonteCarlo getMonteCarloSim() {
		return probDetMonteCarlo; 
	}

	/**
	 * Gedt the types of probability of detection simualtion which are available. e.g.
	 * just one simualtion or changing noise values. 
	 * @return the type of simualatioon available. 
	 */
	public ArrayList<ProbDetSimType> getProbDetSimTypes() {
		return this.probDetSimTypes;
	}

	/**
	 * Set the index of the current simualtion 
	 * @param selectedIndex - the index of the simualtion type. 
	 */
	public void setSimIndex(int selectedIndex) {
		this.probSimTypeIndex=selectedIndex;
	}

	/**
	 * Get the simulation type index.
	 * @return the simualtion index. 
	 */
	public int getProbDetTypeIndex() {
		return this.probSimTypeIndex;
	}

	/**
	 * Get the current simulation type. 
	 * @return the simulation type.
	 */
	public ProbDetSimType getCurrentSimType() {
		return probDetSimTypes.get(probSimTypeIndex);
	}

	/**
	 * Get the view for the simulation. 
	 * @return the simulation view. 
	 */
	public ProbDetSimView getProbDetSimView() {
		return probDetView;
	}

	/**
	 * Get the settings for the simualtion.
	 * @return the sim settings.  
	 */
	public ProbDetSimSettings getProbDetSettings() {
		return probDetSettings;

	}

}
