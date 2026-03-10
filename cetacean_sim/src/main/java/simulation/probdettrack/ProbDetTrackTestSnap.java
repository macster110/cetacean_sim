package simulation.probdettrack;

import java.io.File;
import java.util.ArrayList;

import animal.AnimalModel;
import reciever.GridHydrophoneArray;
import reciever.HydrophoneArray;
import utils.Hist3;

/**
 * Test the probability of detection track simulation. 
 * @author Jamie Macaulay 
 *
 */
public class ProbDetTrackTestSnap {

	public static void main(String[] args) {
		String filename;
		//		String filename = "/Users/au671271/Desktop/tagDataTest_hp18_134a.mat";  
		filename = "/Users/au671271/Desktop/tagdata_sim_snap_10s.mat";  
		//filename = "/Users/au671271/Desktop/tagdata_sim_snap_60s.mat";  
		filename = "/Users/au671271/Desktop/tagDataTest_hp15_117a.mat";
		filename = "/Users/au671271/Desktop/tagDataTest_hp18_134a_subsmpl.mat";
		filename = "/Users/au671271/Desktop/tagDataTest_hp17_135a_tagonoff.mat";


		double noise = 90; //dB 
		double snrThresh = 16; 
		double spreadingCoeff = 20; 
		double absorptionCoeff = 0.04; 
//		double[] timeLims = new double[] {0,60}; 
//		double[] timeLims = null; //seconds from start;
		//temp!!!!!!!!!!
		double[] timeLims = new double[] {5511.024-5511.02, 50542.59-5511.02};//TEMP

		//array 
		double gridSpacing = 400; 
		double maxRange = 1200; 
		double[] depthspacing = new double[] {-5}; 

		System.out.println("Import animal data (SNAP)");

		AnimalStruct animalData = TrackMATImport.importMATTrack(new File(filename));

		AnimalModel animalModel = TrackMATImport.animalStruct2AnimalModel(animalData); 

		System.out.println("Generate simulated hydrophones");

		//create the hydrophone array 
		HydrophoneArray hydrophoneArray = new GridHydrophoneArray(animalModel.getTrack(0).xyz, gridSpacing, depthspacing, maxRange); 

		for (int i=0; i<hydrophoneArray.getArrayXYZ().length ; i++) {
			System.out.println(String.format("Reciever %d x: %.2f y: %.2f, z: %.2f", i,
					hydrophoneArray.getArrayXYZ()[i][0],  hydrophoneArray.getArrayXYZ()[i][1],  hydrophoneArray.getArrayXYZ()[i][2])); 
		}

		//create the prob track dsettings 
		ProbDetTrackSettings probDetTrackSettings = new ProbDetTrackSettings(animalModel,  hydrophoneArray,  noise,  snrThresh,  spreadingCoeff, 
				absorptionCoeff);
		probDetTrackSettings.minHeight=-80; 
		probDetTrackSettings.numDepthBins=20; 
		probDetTrackSettings.maxRange = maxRange;
		probDetTrackSettings.useRoll = true; 
		probDetTrackSettings.snapTimeLims = timeLims; 
		probDetTrackSettings.snapTime = 1; 


		System.out.println("Begin simulation: with " + hydrophoneArray.getArrayXYZ().length + " hydrophones ");

		ProbDetTrack probDetTrack = new ProbDetTrack(); 
		probDetTrack.setupProbTrack(probDetTrackSettings);

		long millis = System.currentTimeMillis(); 
		//add a status listener to print info about the current progress of the simulation. 
		probDetTrack.addStatusListener((actionFlag,   progressBootstrap,  progressSim)->{
			System.out.println(String.format("Progress sim: %.3f", 100*progressSim) + "%" +
					String.format(" Expected total time: %.2f hours", ((System.currentTimeMillis()-millis)/1000.)/progressSim/60/60)); 
		});

		System.out.println("Starting simulation");

		probDetTrack.runProbTrackSnap(probDetTrackSettings);

		System.out.println("Simulation finished");

		//now that the simulation has finished get the histograms. 
		ArrayList<Hist3> probDetResults = probDetTrack.getProbDetResults();
		ArrayList<Hist3> trackEffortResults = probDetTrack.getTrackEffortResults(); 

		Hist3 trackResult = probDetResults.get(0); 
		Hist3 trackEffortResult = trackEffortResults.get(0); 


		System.out.println("\n\n------Pdet Results------" + trackResult.getTotalcount() + " measurements");
		//print the results
		double sum = 0; 
		for (int i=0; i<trackResult.getHistogram().length; i++) {
			System.out.println(); 		
			for (int j=0; j<trackResult.getHistogram()[i].length; j++) {
				System.out.print(String.format("%.0f ", trackResult.getHistogram()[i][j]*trackResult.getTotalcount()));
				sum+=trackResult.getHistogram()[i][j]*trackResult.getTotalcount();
			}
		}
		
		System.out.println("\n\n------Pdet Sum det------" + sum + " detections");

		int effortsum = 0;
		System.out.println("\n\n------Effort Results------" + trackResult.getTotalcount() + " measurements");
		for (int i=0; i<trackEffortResult.getHistogram().length; i++) {
			System.out.println(); 		
			for (int j=0; j<trackEffortResult.getHistogram()[i].length; j++) {
				System.out.print(String.format("%.0f ", trackEffortResult.getHistogram()[i][j])); 		
				effortsum+=trackEffortResult.getHistogram()[i][j]; 
			}
		}
		
		System.out.println(String.format("Effort sum %d ", effortsum)); 	

	}

}
