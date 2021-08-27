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
public class ProbDetTrackTest {
	
		public static void main(String[] args) {
			
		String filename = "/Users/au671271/Desktop/tagDataTest.mat";  
		double noise = 90; //dB 
		double snrThresh = 16; 
		double spreadingCoeff = 20; 
		double absorptionCoeff = 0.04; 
		
		//array 
		double gridSpacing = 100; 
		double maxRange = 1000; 
		
		System.out.println("Import animal data");
		
		AnimalStruct animalData = TrackMATImport.importMATTrack(new File(filename));
		
		AnimalModel animalModel = TrackMATImport.animalStruct2AnimalModel(animalData); 
		
		System.out.println("Generate simulated hydrophones");
				
		//create the hydrophone array 
		HydrophoneArray hydrophoneArray = new GridHydrophoneArray(animalModel.getTrack(0).xyz, gridSpacing, maxRange); 
		
		for (int i=0; i<hydrophoneArray.getArrayXYZ().length ; i++) {
			System.out.println(String.format("Reciever %d x: %.2f y: %.2f, z: %.2f", i,
					hydrophoneArray.getArrayXYZ()[i][0],  hydrophoneArray.getArrayXYZ()[i][1],  hydrophoneArray.getArrayXYZ()[i][2])); 
		}
				
		ProbDetTrackSettings probDetTrackSettings = new ProbDetTrackSettings(animalModel,  hydrophoneArray,  noise,  snrThresh,  spreadingCoeff, 
				 absorptionCoeff);
		probDetTrackSettings.minHeight=-30; 
		probDetTrackSettings.numDepthBins=10; 

		
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

		probDetTrack.runProbTrack(probDetTrackSettings);
		
		System.out.println("Simulation finished");
		
		//now that the simulation has finished get the histograms. 
		ArrayList<Hist3> probDetResults = probDetTrack.getProbDetResults();
		
		Hist3 trackResult = probDetResults.get(0); 
		
		System.out.println("------Histogram Results------" + trackResult.getTotalcount() + " measurements");
		//print the results
		for (int i=0; i<trackResult.getHistogram().length; i++) {
			System.out.println(); 		
			for (int j=0; j<trackResult.getHistogram()[i].length; j++) {
				System.out.print(String.format("%.0f ", trackResult.getHistogram()[i][j])); 		
			}
		}

		
	}

}
