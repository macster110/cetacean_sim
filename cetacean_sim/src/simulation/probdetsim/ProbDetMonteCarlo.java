package simulation.probdetsim;


import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import detector.Detector;
import simulation.RandomSimVariable;
import simulation.StatusListener;
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
	 * Print out progress data
	 */
	private boolean print; 
	
	/**
	 * Records the angles of detected animals. Note, this can take a lot of memory 
	 */
	private boolean recordAngles=true;

	/*
	 * Animal horizontal and vertical angles in RADIANS from the last simulation to have run. Note that 
	 * this is the last single simulation, so all previous bootstraps will have not been included. 
	 */
	private double[][] angles;
	
	/**
	 * A prefix whihc can be addded to progress reported. This allows users to keep a track of different 
	 * simulations.
	 */
	private String prefix=""; 


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
		//System.out.println("DEPTH BIN: "+simSettings.minHeight + " " + simSettings.depthBin);
		this.xBinEdges=Hist3.binEdges(0, simSettings.maxRange, simSettings.rangeBin);
		this.yBinEdges=Hist3.binEdges(simSettings.minHeight, 0, simSettings.depthBin);
		simSettings.printSettings();
	}
	
	
	/** 
	 * Run the simulation
	 */
	private int runMonteCarlo(ProbDetSimSettings simSettings) {
		
		//System.out.println("Is this cancelled? " + this.cancel); 
		
		notifyStatusListeners(StatusListener.SIM_STARTED, 0, 0 ); 
		
		//simSettings.simpleOdontocete.depthDistribution = new RandomSimVariable("Depth", simSettings.minHeight, 0);

		isRunning=true;
		
		//all simualtion results are stored here 
		ArrayList<Hist3> results = new ArrayList<Hist3>(); 
	
		double x, y; //cartesian co-ordinates of animal
		double bearing, range, depth; //cylindrical co-ordinates of animal (depth is used in both cylindrical and cartesian)
		double vertAngle, horzAngle; //the horizontal and vertical angle of the animal in RADIANS. 
		double sourceLevel;
		
		
		double[] animalPos; 
		double[] animalAngle;
		
		int nRecievers = simSettings.recievers.getArrayXYZ().length; 
		double[] recievedLevels = new double[nRecievers]; 
		double meanRecievedLvl=0; 
		int aboveThresh = 0; 
		int ndet = 0; //the number of successful detections
		
		//quite memory intensive but easiest way to tstore results and then grid
		double[][] simResults = new double[simSettings.nRuns][3]; 
		
		for (int i=0; i<simSettings.nBootStraps; i++) {
			
			notifyStatusListeners(StatusListener.SIM_RUNNING, i, 0 ); 
			
			//if recording detected angles then allocate an array 
			if (recordAngles && i==simSettings.nBootStraps-1) angles=new double[simSettings.nRuns][4];
			ndet=0;
			
			for (int j=0; j<simSettings.nRuns; j++) {
				
				if (cancel) {
					isRunning=false;
					notifyStatusListeners(StatusListener.SIM_CANCELLED, 0, 0 ); 
					return -1; 
				}
				
				//create random position for animal
				bearing = Math.random()*2*Math.PI;
				
				//calculate range either to have uniform distribution of ranges or
				//to have even spacing between points in the x and y plane. 
				if (simSettings.evenXY==ProbDetSimSettings.UNIFORM_HORZ_RANGE) 
					range = Math.random()*simSettings.maxRange;
				else if (simSettings.evenXY==ProbDetSimSettings.UNIFORM_XY) 
					range = simSettings.maxRange * Math.sqrt(Math.random());
				else range = Math.random()*simSettings.maxRange; //just in case something goes wrong in flags. 

				//depth is from the animal depth distribution. 
				depth = simSettings.simpleOdontocete.depthDistribution.getNextRandom(); 
				
				
				x=Math.sin(bearing)*range; 
			    y=Math.cos(bearing)*range; 
			    
			    animalPos= new double[] {x, y, depth}; 
				
				//calculate the vertical angle of the animal
				vertAngle = simSettings.simpleOdontocete.getVertAngle(depth).getNextRandom();
				
				//calculate the horizontal anlfe of the animal 
				horzAngle = simSettings.simpleOdontocete.horzAngle.getNextRandom();
			
				animalAngle = new double[] {horzAngle, vertAngle};

				//calculate the source level
				sourceLevel = simSettings.simpleOdontocete.sourceLevel.getNextRandom();
				
				aboveThresh=0; 
				meanRecievedLvl=0; 
				double transloss; 
				for (int k=0; k<nRecievers; k++) {
					//now have position of the animal position need to figure out what the source level, transmission loss due to
					//beam profile and the transmission loss in general. 
					transloss = CetSimUtils.tranmissionTotalLoss(simSettings.recievers.getArrayXYZ()[k], 
							animalPos, animalAngle, simSettings.simpleOdontocete.beamSurface, simSettings.propogation); 
					recievedLevels[k] = sourceLevel+transloss;
					
					//System.out.println("Recieved level: " + recievedLevels[k] + "dB" + " srclevel: " + sourceLevel + " transloss: " + transloss);
					if (recievedLevels[k] > (simSettings.noise+simSettings.snrThreshold) &&
							wasClassified(simSettings.detector, recievedLevels[k]-simSettings.noise)){
						//System.out.println("Was recieved: Recieved level: " + recievedLevels[k] + "dB" + " srclevel: " + sourceLevel + " transloss: " + transloss + "Min recievers: " +simSettings.minRecievers );
						aboveThresh++;
					}
					meanRecievedLvl+=recievedLevels[k]; 
				}
				meanRecievedLvl=meanRecievedLvl/nRecievers; 
				
				
				//now count the number of receiver levels that are above the 
				simResults[j][0]=range; 
				simResults[j][1]=depth; 
			
				if (aboveThresh>=simSettings.minRecievers) {
					simResults[j][2]=1; 
					//record angles if required. 
					//System.out.println("Positive: " + ndet); 
					if (recordAngles && i==simSettings.nBootStraps-1) {
						angles[ndet][0]=range; 
						angles[ndet][1]=depth; 
						angles[ndet][2]=horzAngle; 
						angles[ndet][3]=vertAngle; 
					}
					ndet++; 
				}
				else {
					simResults[j][2]=0; 
				}
								
//				//print out some of the progress. 
				if (j%5000==0) {
					notifyStatusListeners(StatusListener.SIM_RUNNING, i, (j/(double) simSettings.nRuns) ); 
					if (this.print)	System.out.println(prefix + "Progress: Sim: " + i + " of "  + simSettings.nBootStraps 
							+"   " + String.format("%.1f", (100.*j/(double) simSettings.nRuns)) + "%"); 
//					System.out.println("Progress: Sim: " + i + " of "  + simSettings.nBootStraps 
//							+"   " + String.format("%.1f", (100.*j/(double) simSettings.nRuns)) + "%" +  
//							" Result sample: " + simResults[j][0] + " "+ simResults[j][1]+ " "
//							+ simResults[j][2] + " mean recieved level: " + meanRecievedLvl + " aboveThresh: " + aboveThresh); 
				}
			}
			
//			System.out.println("Bin edges");
//			System.out.print("X bin edges: ");
//			printResult(xBinEdges);
//			System.out.println("");
//			System.out.print("Y bin edges: ");
//			printResult(yBinEdges);


			//now must split these results into a 3D chart. 
			results.add(new Hist3(simResults, this.xBinEdges, this.yBinEdges, new Double(1))); 
			
			
			//printResult(results.get(i).getHistogram());

		}
		
		
		//trim of array 
		if (recordAngles) angles=Arrays.copyOf(angles, ndet); 
		
		notifyStatusListeners(StatusListener.SIM_RUNNING,simSettings.nBootStraps, 1.); 
		if (this.print)	System.out.println("Progress: Sim: " + simSettings.nBootStraps + " of "  + simSettings.nBootStraps 
				+"   " + String.format("%.1f", (100.)) + "%"); 

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
	 * Works out the probability that a detection will be automatically classified based on 
	 * it's SNR and return a true or false based on that probability. Will therefore give different
	 * results on different class with the same input. 
	 * @param detector - the detector object
	 * @param SNR - the SNR
	 * @return true of detector. False if not detected. 
	 */
	private boolean wasClassified(Detector detector, double SNR) {
		//simSettings.detector.getProbClassified(SNR); 
		double p = detector.getProbClassified(SNR);
		double rand = Math.random(); 
		
		//System.out.println("SNR: " + SNR +" p: " + p);
		if (rand>p) return false;
		else return true; 
	}
	
	
	/**
	 * Average the histograms to give a histogram of average values and a histogram of standard deviation
	 * @return a histogram of mean values for all simulations and histogram of standard deviation. 
	 */
	private Hist3[] averageHistograms(ArrayList<Hist3> results) {
		
		if (results==null) {
			System.out.println("averageHistograms: The results are NULL"); 
			return null; 
		}
		
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
				//calc std
				stdHist[i][j]=stdCalculator.evaluate(meanArray, mean); 
			}
		}
		
		Hist3 histMean = new Hist3(xbinEdges, ybinEdges, averageHist); 
		Hist3 histStd = new Hist3(xbinEdges, ybinEdges, stdHist); 

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
	 * True if running. 
	 * @return true if running. 
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	
	/**
	 * Run the probability of detection simualtion using a new instance  default settings file. This
	 * will always return the same result and is only used for testing purposes. 
	 */
	public void run() {
		ProbDetSimSettings simSettings= new ProbDetSimSettings(); 
		//simSettings.simpleOdontocete.setUpAnimal(SimpleOdontocete.SIM_UNIFORM_DEPTH_HORZ, simSettings);
		run(simSettings, true); 
	}

	/**
	 * Run the probability of detection simualtion
	 * @param simSettings - the settings for the simualtion
	 */
	public void run(ProbDetSimSettings simSettings) {
		 run(simSettings, false); 
	}
	
	/**
	 * Run the probability of detection simualtion
	 * @param simSettings - the settings for the simualtion
	 * @param print - true to print some summary information to terminal. 
	 */
	public void run(ProbDetSimSettings simSettings, boolean print) {
		this.print=print;
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
	
	
	/*
	 * Print the current results. 
	 */
	public void printResult() {
		if (result==null) {
			System.out.println("The prob. det. result is null:"); 
		}
		
		System.out.println("Mean P: ");
		printResult(this.result.probSurfaceMean.getHistogram()); 
		System.out.println("");
		System.out.println("Std P: ");
		printResult(this.result.probSurfaceStd.getHistogram()); 
	}
	
	
	/**
	 * Print the result. 
	 * @param result - the results.
	 */
	public static void printResult(double[] result){
		System.out.println("");
		for (int i=0; i<result.length; i++) {
			System.out.print(result[i]+" ");
		}
	}
	
	
	/**
	 * Print results form the simulation 
	 * @param results. The results to print. 
	 */
	public static void printResult(double[][] result) {
		
		int histWidth=result.length; 
		int histHeight=result[0].length; 
		
		for (int i=0; i<histWidth; i++) {
			System.out.println("");
			for (int j=0; j<histHeight; j++) {
				System.out.print(String.format("%.2f ",result[i][j]));
			}
		}
	}
	
	/**
	 * Print results form the simulation 
	 * @param results. The results to print. 
	 */
	public static void printResult(float[][] result) {
		
		int histWidth=result.length; 
		int histHeight=result[0].length; 
		
		for (int i=0; i<histWidth; i++) {
			System.out.println("");
			for (int j=0; j<histHeight; j++) {
				System.out.print(String.format("%.2f ",result[i][j]));
			}
		}
	}
	
	
//	/**
//	 * A convenicne function for calling the simualtion from MATLAB. Creates a settings class and runs the simualtion. 
//	 * @param vertMean - the mean vertical angle in RADIANS. 
//	 * @param vertStd  - the standard deviation of vertical angle in RADIANS. 
//	 */
//	public double runMonteCarloML(ProbDetSimSettings simSettings) {
//		System.out.println("Hello Monte Carlo in Java"); 
//		return 45; 
//	}

	/**
	 * Check whether the simulation last run was cancelled. 
	 * @return true if cancelled. 
	 */
	public boolean isCancelled() {
		return this.cancel;
	}
	
	
	/**
	 * Check whether animal angles are being recorded for the simulation. Angles are only recorded
	 * for the last simulation run. 
	 * @return true if angles are being saved 
	 */
	public boolean isRecordAngles() {
		return recordAngles;
	}

	
	/**
	 * Set whether animal angles are being recorded for the simulation. Angles are only recorded
	 * for the last simulation run. 
	 * @param true to save animal angles. 
	 */
	public void setRecordAngles(boolean recordAngles) {
		this.recordAngles = recordAngles;
	}
	
	/**
	 * Get all angles from the last single simulation run in which the animal was detected. this will be null
	 * unless isRecordAngles(). 
	 * @return the angles form the last angle simulation.
	 */
	public double[][] getAngles() {
		if (angles==null) return null; 
		else return angles; 
	}
	
	/**
	 * Get the prefix which is printed before progress reporting. 
	 * @return the prefix string. 
	 */
	public String getPrefix() {
		return prefix;
	}


	/**
	 * Set the prefix string. This is printed before progress printing if the print
	 * value is set to true. 
	 * @param prefix - the string prefix for progress printing. 
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	
	
	
	
	/**
	 * Run the simulation without a GUI. 
	 * @param args
	 */
	public static void main(String[] args) {
		ProbDetSimSettings simSettings = new ProbDetSimSettings(); 
		//simSettings.simpleOdontocete.setUpAnimal(0, simSettings);
		ProbDetMonteCarlo monteCarloSimulation = new ProbDetMonteCarlo(); 
		
		//now run the simulation 
		monteCarloSimulation.setUpMonteCarlo(simSettings); 
		monteCarloSimulation.setPrefix("Test Sim:");
		monteCarloSimulation.run(simSettings, true);
	}
	

	
}

