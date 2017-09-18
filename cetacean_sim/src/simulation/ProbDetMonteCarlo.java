package simulation;


import java.util.ArrayList;
import java.util.Random;

import utils.CetSimUtils;
import utils.SurfaceData;
import utils.SurfaceUtils;

/**
 * Run a single Monte Carlo simulation for a probability of detection 
 * @author Jamie Macaulay 
 *
 */
public class ProbDetMonteCarlo {

	
	/**
	 * The simulation progress between 0 and 1; 
	 */
	private volatile double simProgress = 0; 
	
	/**
	 * The percentage number of bootstraps executed between 0 and 1; 
	 */
	private volatile double bootStrapProgress = 0; 
	
	/**
	 * Cancel the simulation
	 */
	private volatile boolean cancel = false;
	
	/**
	 * True if the simulation is currently running. 
	 */
	private volatile boolean isRunning = false; 
	
	/**
	 * List of status listeners for updates to the Monte Carlo Simulation. 
	 */
	private ArrayList<StatusListener> statusListeners = new ArrayList<StatusListener>(); 
	
	
	/**
	 * Constructor for the Monte Carlo simulation 
	 */
	public ProbDetMonteCarlo() {

	}
	
	

	/**
	 * Get the simulation ready to run
	 */
	public void setUpMonteCarlo(ProbDetSimSettings simSettings) {
		//create 
		this.cancel=false; 
		simProgress=0;
		bootStrapProgress=0; 
	}
	
	/** 
	 * Run the simulation
	 */
	public int runMonteCarlo(ProbDetSimSettings simSettings) {
		
		notifyStatusListeners(StatusListener.SIM_STARTED, 0, 0 ); 

		isRunning=true;
		
		//all simualtion results are stored here 
		ArrayList<ProbDetResult> results = new ArrayList<ProbDetResult>(); 
	
		double x, y; //cartesian co-ordinates of animal
		double bearing, range, depth; //cylindrical co-ordinates of animal (depth is used in both cylindrical and cartesian)
		double vertAngle, horzAngle; //the horizontal and vertical angle of the animal in RADIANS. 
		double sourceLevel;
		
		double[] animalPos; 
		double[] animalAngle;
		
		int nRecievers = simSettings.hydrophonePositions.length; 
		double[] recievedLevels = new double[nRecievers]; 
		int aboveThresh = 0; 
		
		//quite memory intensive but easiest way to tstore results and then grid
		double[][] simResults = new double[simSettings.nRuns][3]; 
		
		for (int i=0; i<simSettings.nBootStraps; i++) {
			notifyStatusListeners(StatusListener.SIM_RUNNING, i, 0 ); 
			for (int j=0; j<simSettings.nRuns; j++) {
				
				if (cancel) {
					notifyStatusListeners(StatusListener.SIM_CANCELLED, 0, 0 ); 
					return -1; 
				}
				
				//create random position for animal
				bearing = Math.random()*2*Math.PI;
				range = Math.random()*simSettings.maxRange;
				//depth is from the animal depth distribution. 
				depth = simSettings.simpleOdontocete.depthDistribution.getNextRandom(); 
				
				
				x=Math.sin(bearing)*range; 
			    y=Math.cos(bearing)*range; 
			    
			    animalPos= new double[] {x, y, depth}; 
				
				//calculate the vertical angle of the animal
				vertAngle = simSettings.simpleOdontocete.vertAngle.getNextRandom();
				
				//calculate the horizontal anlfe of the animal 
				horzAngle = simSettings.simpleOdontocete.horzAngle.getNextRandom();
				
				animalAngle = new double[] {horzAngle, vertAngle};
				
				//calculate the source level
				sourceLevel = simSettings.simpleOdontocete.sourceLevel.getNextRandom();
				
				aboveThresh=0; 
				for (int k=0; k<nRecievers; k++) {
					//now have position of the animal position need to figure out what the source level, transmission loss due to
					//beam profile and the transmission loss in general. 

					//the 
					recievedLevels[k] = sourceLevel+CetSimUtils.tranmissionTotalLoss(simSettings.hydrophonePositions[k], 
							animalPos, animalAngle, simSettings.simpleOdontocete.beamSurface, simSettings.propogation); 
					if (recievedLevels[k]> simSettings.noiseThreshold) aboveThresh++;
				}
				
				//now count the number of receiver levels that are above the 
				simResults[i][0]=range; 
				simResults[i][1]=depth; 

				if (aboveThresh>=simSettings.minRecievers) {
					simResults[i][2]=1; 
				}
				else {
					simResults[i][2]=0; 
				}
				
				//print out some of the progress. 
				if (j%1000==0) {
					notifyStatusListeners(StatusListener.SIM_RUNNING, i, (j/(double) simSettings.nRuns) ); 
					System.out.println("Progress: Sim: " + i + " of "  + simSettings.nBootStraps 
							+"   " + (100.*j/(double) simSettings.nRuns) + "%" ); 
				}
			}
			
			//now must split these results into a 3D chart. 
			ProbDetResult result = new ProbDetResult(); 
		}
		
		isRunning=false;

		return 1; 
	}
	
	
	/**
	 * 3D histogram of results. 
	 * @return a grid of histogram of results.
	 */
	public double[][] hist3(double[] data, double[] xbinEdges, double[] ybinEdges) {
		//TODO. 
		
		// create the results grid. 
		double[][] histGrid= new double[xbinEdges.length-1][ybinEdges.length-1];
		
		return histGrid;
		
	}

	
	/**
	 * Cancel the simulation 
	 */
	public void setCancelled() {
		this.cancel=true;
	}

	
	/**
	 * Get the progress of the current simulation between 0 and 1. 
	 * @return the current progress between 0 and 1. 
	 */
	public double getSimProgress() {
		return simProgress;
	}

	
	/**
	 * Get the percentage number of bootstraps completed 
	 * @return percentage bootstraps complete between 0 and 1. 
	 */
	public double getBootStrapProgress() {
		return bootStrapProgress;
	}
	
	
	/**
	 * Holds simulation  result. 
	 * @author Jamie Macaulay
	 *
	 */
	public class ProbDetResult {
		
	}
	
	
	/**
	 * Run the simulation without a GUI. 
	 * @param args
	 */
	public static void main(String[] args) {
		ProbDetSimSettings simSettings = new ProbDetSimSettings(); 
		simSettings.simpleOdontocete.setUpAnimal(0, simSettings);
		ProbDetMonteCarlo monteCarloSimulation = new ProbDetMonteCarlo(); 
		
		//now run the simulation 
		monteCarloSimulation.setUpMonteCarlo(simSettings); 
		monteCarloSimulation.runMonteCarlo(simSettings); 
		
	}

	
	/**
	 * True if running. 
	 * @return true if running. 
	 */
	public boolean isRunning() {
		return isRunning;
	}

	
	/**
	 * Run the simulation with the current settings. 
	 */
	public void run(ProbDetSimSettings simSettings) {
		if (simSettings==null) {
			System.err.println("The settings class is null. Simulation cannot run.");
		}
		//now run the simulation 
		this.setUpMonteCarlo(simSettings); 
		this.runMonteCarlo(simSettings); 
	}
	
	
	/**
	 * Stop the simulation. 
	 */
	public void stop() {
		this.cancel=true;
	}
	
	/**
	 * Notify the status listeners of a chnage in state or progress of the simulation 
	 * @param actionFlag - the action flag. Shows state of simulation
	 * @param progressBootstrap - the number of bootstraps completed. 
	 * @param progressSim - the prgress of the simualtion. 
	 */
	private void notifyStatusListeners(int actionFlag,  int progressBootstrap, double progressSim){
		for (int i=0; i<statusListeners.size(); i++) {
			statusListeners.get(i).statusAction(actionFlag, progressBootstrap, progressSim);
		}
	}
	
	/**
	 * Clear all status listeners from the simualtion. 
	 */
	public void clearStatusListeners() {
		statusListeners.clear();
	}
	
	/**
	 * Remove a status listener. 
	 * @param status true if the status listener was removed. False if it wasn;t in the list. 
	 */
	public boolean removeStatusListener(StatusListener status) {
		 return this.statusListeners.remove(status);
	}


	/**
	 * Add a status listener to the Monte Carlo Simulation. This returns status flags and 
	 * progress information. 
	 * @param status - the status listener to add
	 */
	public void addStatusListener(StatusListener status) {
		this.statusListeners.add(status);
		
	}
	
 
	
}

