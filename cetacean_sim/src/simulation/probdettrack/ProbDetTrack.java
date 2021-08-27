package simulation.probdettrack;

import java.util.ArrayList;

import animal.AnimalModel;
import animal.AnimalVocalisations;
import reciever.HydrophoneArray;
import simulation.StatusListener;
import utils.CetSimUtils;
import utils.Hist3;
import utils.SurfaceData;
import utils.SurfaceUtils;

/**
 * Calculates the whether clicks from an animal along a defined track were detected or not
 * on a given reciever. 
 * @author Jamie Macaulay 
 *
 */
public class ProbDetTrack {

	/**
	 * The settings for the simulation
	 */
	public ProbDetTrackSettings probTrackSettings; 

	/**
	 * The results.
	 */
	public ArrayList<RecievedInfo> recInfo; 

	/**
	 * The y edges of the histogram 
	 */
	private double[] yBinEdges;

	/**
	 * The x edges of the histogram. 
	 */
	private double[] xBinEdges;

	/**
	 * The histogram results. 
	 */
	private ArrayList<RecievedInfo> recievedLevels;

	/**
	 * The probability of detection results for each track. 
	 */
	private ArrayList<Hist3> probDetResults = new  ArrayList<Hist3>();
	
	/**
	 * List of status listeners for updates to the Monte Carlo Simulation. 
	 */
	private ArrayList<StatusListener> statusListeners = new ArrayList<StatusListener>();


	/**
	 * Get the simulation ready to run
	 */
	public void setupProbTrack(ProbDetTrackSettings simSettings) {
		//print the settings. 
		simSettings.printSettings();
		
		System.out.println("DEPTH BIN: "+simSettings.minHeight + " " + simSettings.numDepthBins);
		this.xBinEdges=Hist3.binEdges(0, simSettings.maxRange, simSettings.numRangeBins);
		this.yBinEdges=Hist3.binEdges(simSettings.minHeight, 0, simSettings.numDepthBins);
		
		System.out.println("Y bin edges: ");
		for (int i=0; i<yBinEdges.length; i++) {
			System.out.print(String.format("%.1f ", yBinEdges[i]));
		}
	}


	/** 
	 * Run the simulation
	 */
	public void runProbTrack(ProbDetTrackSettings simSettings) {

		//easy access to the recievers object. 
		HydrophoneArray recievers = simSettings.recievers; 		

		//System.out.println("Is this cancelled? " + this.cancel); 
		AnimalModel animal = simSettings.animal; 

		ArrayList<RecievedInfo> recInfo = new ArrayList<RecievedInfo>(); 
		
		notifyStatusListeners(StatusListener.SIM_STARTED,  0, 0);


		double[] recieverPos; 
		//iterate through each animal
		System.out.println("Number of animals: " + animal.getNumberOfAnimals().intValue()); 

		for (int n=0; n<animal.getNumberOfAnimals().intValue(); n++) {

			notifyStatusListeners(StatusListener.SIM_RUNNING,  n, 0);

			//get the animal vocalisation objects. 
			AnimalVocalisations animalVocalisations = animal.getVocSeries(n); 

			// get the track points for the vocalisation times. 
			double[][] trackXYZ = animal.getTrack(n).getTrackPoints(animalVocalisations.getVocTimes()); 
			
			double[] depthMinMax = CetSimUtils.getMinAndMax(trackXYZ[2]); 
			System.out.println("Depth minmax: " + depthMinMax[0] + "  " + depthMinMax[1]); 

			// get the track points for the vocalisation times. 
			double[][] trackOrient = animal.getTrack(n).getTrackAngles(animalVocalisations.getVocTimes()); 
			
			

			SurfaceData beamSurface = SurfaceUtils.generateSurface(animal.getBeamProfileTL().getRawBeamMeasurments());

			// the received info array. 
			System.out.println("Number of voc: " + animalVocalisations.getVocTimes().length); 
			System.out.println("Number of recievers: " + recievers.getArrayXYZ().length); 
			
			
			//iterate through each click
			double[] animalPos;
			double[] animalAngle;
			double transloss; 
			double distance; 
			double recievedLevel; 
			int nn = 0; 
			float noDetRL = (float) (simSettings.noise - simSettings.snrThreshold); 
			
			Hist3 pDet = new Hist3( this.xBinEdges, this.yBinEdges); 

			for (int i = 0; i<animalVocalisations.getVocTimes().length ; i++) {
				//for (int i = 0; i<100000 ; i++) {

				//iterate through each receiver and calculate the received level on each

				//the maximum value for an integer. 
				ArrayList<RecievedInfo> trackRecInfo = new ArrayList<RecievedInfo>(recievers.getArrayXYZ().length); 

				for (int j = 0; j<recievers.getArrayXYZ().length ; j++) {
					recieverPos = recievers.getArrayXYZ()[j]; 

					//create the track points. 
					animalPos = new double[] {trackXYZ[0][i], trackXYZ[1][i], trackXYZ[2][i]}; 
					animalAngle = new double[] {trackOrient[0][i], trackOrient[1][i]}; 
					
					//the 3D distance 
					distance = CetSimUtils.distance2D(animalPos, recieverPos); 
					
					if (distance>simSettings.maxRange) {
						//tru to save p[rocessing time. 
						trackRecInfo.add(new RecievedInfo( noDetRL, (float) distance, (float) trackXYZ[2][i], j)); 
						continue; 
					}
					
//					if (i<100) {
					//System.out.println(String.format("Animal Pos: %d xyz: %.2f %.2f %.2f meters" , i , trackXYZ[0][i],trackXYZ[1][i] ,trackXYZ[2][i])); 
//					System.out.println(String.format("Animal Angle: %d angs: %.2f %.2f RAD",  i , trackOrient[0][i] , trackOrient[1][i])); 
//					System.out.println(String.format("Reciever Pos: %d xyz: %.2f %.2f %.2f meters distance: %.0f meters", i, recieverPos[0], recieverPos[1], recieverPos[2], distance)); 
//					}
					//the transmission loss.
					transloss = CetSimUtils.tranmissionTotalLoss(recieverPos, animalPos, 
							animalAngle, beamSurface, simSettings.propogation); 

					recievedLevel = animalVocalisations.getVocAmplitudes()[i] + transloss; 


//					if (distance<1000) {
//						System.out.println(String.format("Received level: %.1f distance %.1f source level %.1f TL: %.1f ", recievedLevel, distance,  animalVocalisations.getVocAmplitudes()[i], transloss)); 
//					}
			
					//add the results to an array
					trackRecInfo.add(new RecievedInfo((float) recievedLevel, (float) distance, (float) trackXYZ[2][i], j)); 
				
					nn++; 
					
					//notifyStatusListeners(StatusListener.SIM_RUNNING,  n , nn/(double) animalVocalisations.getVocTimes().length*recievers.getArrayXYZ().length); 
				}
			
				if (i%1000 == 0) {
					notifyStatusListeners(StatusListener.SIM_RUNNING,  n , i/(double)animalVocalisations.getVocTimes().length); 
				}
				
				//now must add the results to the histrogram 
				pDet.addToHist(trackRecInfo, null,  simSettings.noise + simSettings.snrThreshold); 
			}

			//now have a giant array of distances and received levels.

			probDetResults.add(pDet); 

		}
		
		notifyStatusListeners(StatusListener.SIM_FINIHSED, animal.getNumberOfAnimals().intValue(), 1);


		this.recievedLevels = recInfo; 

	}

	/**
	 * Get the results form the si
	 * @return
	 */
	public ArrayList<Hist3> getProbDetResults() {
		return probDetResults;
	}


	/**
	 * Notify the status listeners of a change in state or progress of the simulation 
	 * @param actionFlag - the action flag. Shows state of simulation
	 * @param progressBootstrap - the number of bootstraps completed. 
	 * @param progressSim - the progress of the simualtion. 
	 */
	private void notifyStatusListeners(int actionFlag,  int progressBootstrap, double progressSim){
		for (int i=0; i<statusListeners.size(); i++) {
			statusListeners.get(i).statusAction(actionFlag, progressBootstrap, progressSim);
		}
	}
	
	/**
	 * Clear all status listeners from the simulation. 
	 */
	public void clearStatusListeners() {
		statusListeners.clear();
	}
	
	/**
	 * Remove a status listener. 
	 * @param status true if the status listener was removed. False if it wasn't in the list. 
	 */
	public boolean removeStatusListener(StatusListener status) {
		 return this.statusListeners.remove(status);
	}


	/**
	 * Add a status listener to the track simulation. This returns status flags and 
	 * progress information. 
	 * @param status - the status listener to add
	 */
	public void addStatusListener(StatusListener status) {
		this.statusListeners.add(status);
		
	}
	
	
	/**
	 * A class with fields actually has a smaller memory footprint than a double[] array. 
	 * Note: use floats to reduce size in memory
	 * 
	 * 
	 * @author Jamie Macaulay 
	 *
	 */
	public class RecievedInfo {

		/**
		 * The received level in dB re 1uPa pp
		 */
		public float recievedLevel; 

		/**
		 * The <b>2D</b> distance in meters. 
		 */
		public float distance; 

		/**
		 * The depth of the animal. Under water is negative. So ten meters under water -> 10. 
		 */
		public float height; 

		/**
		 * The receiver ID i.e. the index of the receiver in the receiver list. 
		 */
		public int recieverID; 


		public RecievedInfo(float recievedLevel, float distance, float height, int recieverID) {
			this.recievedLevel = recievedLevel; 
			this.distance = distance; 
			this.recieverID = recieverID; 
			this.height=height;
		}
	}

}
