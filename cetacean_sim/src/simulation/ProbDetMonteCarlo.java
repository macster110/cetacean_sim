package simulation;


import java.util.ArrayList;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import utils.CetSimUtils;
import utils.Hist3;

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
	 * The y edges of the histogram 
	 */
	private double[] yBinEdges;

	/**
	 * The x edges of the histogram. 
	 */
	private double[] xBinEdges;

	/**
	 * The results of the simualtion. 
	 */
	private ProbDetResult result; 
	
	
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
		this.isRunning=false; 
		simProgress=0;
		bootStrapProgress=0; 
		this.yBinEdges=Hist3.binEdges(0, simSettings.maxRange, simSettings.rangeBin);
		this.xBinEdges=Hist3.binEdges(0, simSettings.maxDepth, simSettings.depthBin);
	}
	
	
	/** 
	 * Run the simulation
	 */
	private int runMonteCarlo(ProbDetSimSettings simSettings) {
		
		System.out.println("Is this cancelled? " + this.cancel); 
		
		notifyStatusListeners(StatusListener.SIM_STARTED, 0, 0 ); 

		isRunning=true;
		
		//all simualtion results are stored here 
		ArrayList<Hist3> results = new ArrayList<Hist3>(); 
	
		double x, y; //cartesian co-ordinates of animal
		double bearing, range, depth; //cylindrical co-ordinates of animal (depth is used in both cylindrical and cartesian)
		double vertAngle, horzAngle; //the horizontal and vertical angle of the animal in RADIANS. 
		double sourceLevel;
		
		double[] animalPos; 
		double[] animalAngle;
		
		int nRecievers = simSettings.recievers.length; 
		double[] recievedLevels = new double[nRecievers]; 
		int aboveThresh = 0; 
		
		//quite memory intensive but easiest way to tstore results and then grid
		double[][] simResults = new double[simSettings.nRuns][3]; 
		
		for (int i=0; i<simSettings.nBootStraps; i++) {
			notifyStatusListeners(StatusListener.SIM_RUNNING, i, 0 ); 
			for (int j=0; j<simSettings.nRuns; j++) {
				
				if (cancel) {
					isRunning=false;
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
					recievedLevels[k] = sourceLevel+CetSimUtils.tranmissionTotalLoss(simSettings.recievers[k], 
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
							+"   " + String.format("%.1f", (100.*j/(double) simSettings.nRuns)) + "%" ); 
				}
			}
			
			//now must split these results into a 3D chart. 
			results.add(new Hist3(simResults, this.xBinEdges, this.yBinEdges, new Double(1))); 
			printResult(results.get(i).getHistogram());

		}
		

		//now create an average histogram. 
		Hist3[] histResults = averageHistograms(results); 
		
		//create the result object
		this.result = new ProbDetResult(histResults, simSettings); 
		
		//set the running flag to false. 
		isRunning=false;
		
		//notify status listeners that the simulation has indeed finished. 
		notifyStatusListeners(StatusListener.SIM_FINIHSED, simSettings.nBootStraps, 1. ); 

		return 1; 
	}
	
	
	
	/**
	 * Average the histograms to give a histogram of average values and a histogram of standard deviation
	 * @return a histogram of mean values for all simulations and histogram of standard deviation. 
	 */
	private Hist3[] averageHistograms(ArrayList<Hist3> results) {
		
		if (results==null) return null; 
		
		int histWidth=results.get(0).getHistogram().length; 
		int histHeight=results.get(0).getHistogram()[0].length; 
		
		double[] xbinEdges= results.get(0).getXbinEdges(); 
		double[] ybinEdges= results.get(0).getYbinEdges(); 

		StandardDeviation stdCalculator = new StandardDeviation(); 

		double[][] averageHist = new double[histWidth][histHeight]; 
		double[][] stdHist = new double[histWidth][histHeight]; 
		
		double[] meanArray; 
		double mean; 
		
		for (int i=0; i<histWidth; i++) {
			for (int j=0; j<histHeight; j++) {
				meanArray = new double[results.size()];
				mean=0; 
				for (int n=0; n<results.size(); n++) {
					meanArray[n]=results.get(n).getHistogram()[i][j];
					mean+=meanArray[n]; 
				}		
				//calc mean
				mean=mean/results.size();
				averageHist[i][j]=mean; 
				//calc std;
				stdHist[i][j]=stdCalculator.evaluate(meanArray, mean); 
			}
		}
		
		Hist3 histMean = new Hist3(xbinEdges, yBinEdges, averageHist); 
		Hist3 histStd = new Hist3(xbinEdges, yBinEdges, stdHist); 

		Hist3[] hist3Results= new Hist3[2];  
		hist3Results[0]=histMean; 
		hist3Results[1]=histStd; 
		
		return hist3Results;
	}
	
	/**
	 * Get the mean of all the probability of detection simulations from the last run. 
	 * @return the mean histogram surface of the simulation
	 */
	public Hist3 getMeanProbDet() {
		return this.result.probSurfaceMean; 
	}
	
	/**
	 * Get the standard deviation surface of the probability of detection simulation. 
	 * @return the standard deviation histogram surface of the simualtion. 
	 */
	public Hist3 getStdProbDet() {
		return this.result.probSurfaceStd; 
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
	
	
	/**
	 * Holds the results of the simulation
	 * @author Jamie Macaulay 
	 *
	 */
	public class ProbDetResult {
	
		/**
		 * Results object.  
		 * @param result - the results. 
		 * @param simSettings - the sim settings for the histogram.
		 */
		public ProbDetResult(Hist3[] result, ProbDetSimSettings simSettings) {
			this.probSurfaceMean=result[0];
			this.probSurfaceStd=result[1];
			this.simSettings=simSettings; 
		}
		
		/**
		 * The mean probability surface
		 */
		public Hist3 probSurfaceStd; 
		
		/**
		 * The standard deviation surface
		 */
		public Hist3 probSurfaceMean;
		
		//TODO clone. 
		/**
		 * The settings used in this simulation. 
		 */
		public ProbDetSimSettings simSettings; 
	}


	/**
	 * Get the result object for the last successfully run simulation. 
	 * @return the prob det results. 
	 */
	public ProbDetResult getResult() {
		return result; 
	}
	
	/**
	 * Print results form the simulation 
	 * @param results. The results to print. 
	 */
	public static void printResult(double[][] reuslt) {
		
		int histWidth=reuslt.length; 
		int histHeight=reuslt[0].length; 
		
		for (int i=0; i<histWidth; i++) {
			System.out.println("");
			for (int j=0; j<histHeight; j++) {
				System.out.print(reuslt[i][j]+" ");
			}
		}
	}
	
}

