package simulation.probdettrack;

import java.util.ArrayList;

import animal.AnimalModel;
import animal.AnimalVocalisations;
import reciever.HydrophoneArray;
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


	private ArrayList<Hist3> probDetResults;


	/**
	 * Get the simulation ready to run
	 */
	public void setupProbTrack(ProbDetTrackSettings simSettings) {
		//print the settings. 
		simSettings.printSettings();
		//System.out.println("DEPTH BIN: "+simSettings.minHeight + " " + simSettings.depthBin);
		this.xBinEdges=Hist3.binEdges(0, simSettings.maxRange, simSettings.rangeBin);
		this.yBinEdges=Hist3.binEdges(simSettings.minHeight, 0, simSettings.depthBin);
	}


	/** 
	 * Run the simulation
	 */
	private void runProbTrack(ProbDetTrackSettings simSettings) {

		//easy access to the recievers object. 
		HydrophoneArray recievers = simSettings.recievers; 		

		//System.out.println("Is this cancelled? " + this.cancel); 
		AnimalModel animal = simSettings.animal; 

		ArrayList<RecievedInfo> recInfo = new ArrayList<RecievedInfo>(); 

		double[] recieverPos; 
		//iterate through each animal
		for (int n=0; n<animal.getNumberOfAnimals().intValue(); n++) {

			//get the animal vocalisation objects. 
			AnimalVocalisations animalVocalisations = animal.getVocSeries(n); 

			// get the track points for the vocalisation times. 
			double[][] trackXYZ = animal.getTrack(n).getTrackPoint(animalVocalisations.getVocTimes()); 

			// get the track points for the vocalisation times. 
			double[][] trackOrient = animal.getTrack(n).getTrackAngle(animalVocalisations.getVocTimes()); 

			SurfaceData beamSurface = SurfaceUtils.generateSurface(animal.getBeamProfileTL().getRawBeamMeasurments());

			// the recieved info array. 
			ArrayList<RecievedInfo> trackRecInfo = new ArrayList<RecievedInfo>(); 

			//iterate through each click
			double[] animalPos;
			double[] animalAngle;
			double transloss; 
			double distance; 
			double recievedLevel; 
			for (int i = 0; i<animalVocalisations.getVocTimes().length ; i++) {

				//iterate through each receiver and calculate the received level on each
				for (int j = 0; j<recievers.getArrayXYZ().length ; j++) {
					recieverPos = recievers.getArrayXYZ()[j]; 

					//create the track points. 
					animalPos = new double[] {trackXYZ[0][i], trackXYZ[1][i], trackXYZ[2][i]}; 
					animalAngle = new double[] {trackOrient[0][i], trackOrient[1][i]}; 

					//the transmission loss.
					transloss = CetSimUtils.tranmissionTotalLoss(recieverPos, animalPos, 
							animalAngle, beamSurface, simSettings.propogation); 

					recievedLevel = animalVocalisations.getVocAmplitudes()[i] + transloss; 

					//the 3D distance 
					distance = CetSimUtils.distance(animalPos, recieverPos); 

					//add the results to an array
					trackRecInfo.add(new RecievedInfo((float) recievedLevel, (float) distance, (float) trackXYZ[2][i], j)); 
				}
			}

			//now have a giant array of distances and received levels.

			probDetResults.add(new Hist3(trackRecInfo, this.xBinEdges, this.yBinEdges, simSettings.noise + simSettings.snrThreshold)); 

		}

		this.recievedLevels = recInfo; 

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
		 * The <b>3D</b> distance in meters. 
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
