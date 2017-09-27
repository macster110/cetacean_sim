package simulation.probdetsim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import layout.utils.Utils3D;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;
import utils.Hist3;

/**
 * Export probability fo detection data to a .mat file
 * @author Jamie Macaulay 
 *
 */
public class ProbDetMTExport {
	
	/**
	 * Save the MT file. 
	 * @param file - the file
	 * @param mlArray - the mlArray to save. 
	 */
	public void saveMTFile(File file, ArrayList<MLArray> mlArray) {
		try {
			MatFileWriter filewrite=new MatFileWriter(file.getAbsolutePath(), mlArray);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("File save failed");
			alert.setHeaderText("Failure saving the file");
			alert.setContentText("There was a proible trying to save the last prob_det results as a .mat file");

			alert.showAndWait();
			e.printStackTrace();
		}
	}
	
	/**
	 * Convert results to a MATLAB structure.
	 * @param probResult - probvability of detection results. 
	 * @return the results. 
	 */
	public MLArray  resultsToStruct(ProbDetResult probResult) {
		
		MLStructure mlStruct = new MLStructure("prob_det", new int[] {1,1}); 
		
		MLStructure probDetMeanStruct = surfaceToStruct(probResult.probSurfaceMean,  "prob_det_mean");
		MLStructure probDetStdStruct = surfaceToStruct(probResult.probSurfaceMean,  "prob_det_std");
		
		mlStruct.setField("prob_det_mean", probDetMeanStruct);
		mlStruct.setField("prob_det_std", probDetStdStruct);

		return mlStruct;
	}

	
	/**
	 * Convert a probability fo detection surface to MATLAB structure with fields p, range and depth 
	 * which are all 2D matrices representing a surface. 
	 * @param hist3 - the surface
	 */
	public MLStructure surfaceToStruct(Hist3 hist3, String name) {
		//the histogram 
		MLDouble hist3p = new MLDouble("p", hist3.getHistogram()); 
		
		float[][] rangeSurf = Hist3.getXYSurface(hist3.getXbinEdges(), 
				hist3.getYbinEdges(), true); 
		
		float[][] depthSurf = Hist3.getXYSurface(hist3.getXbinEdges(), 
				hist3.getYbinEdges(), false); 
		
		MLDouble hist3range = new MLDouble("range", Utils3D.float2double(rangeSurf)); 
		MLDouble hist3depth = new MLDouble("depth", Utils3D.float2double(depthSurf)); 
		
		
		MLStructure mlStruct = new MLStructure(name, new int[] {1,1}); 
		
		mlStruct.setField("p", hist3p, 0);
		mlStruct.setField("range", hist3range, 0);
		mlStruct.setField("depth", hist3depth, 0);
		
		return mlStruct; 
	} 
	
//	/**
//	 * Convert the settings class to a structure compatible with MATLAb library  
//	 * @param probDetSDimSettings - prob det sim settings 
//	 * @return structure representing settings. 
//	 */
//	public MLStructure settingsToStrcut(ProbDetSimSettings probDetSDimSettings) {
//		
//		MLStructure mlStruct = new MLStructure("settings", new int[] {1,1}); 
//		
//		
//	}

}
