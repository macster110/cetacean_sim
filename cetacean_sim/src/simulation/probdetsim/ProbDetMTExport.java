package simulation.probdetsim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

import edu.mines.jtk.dsp.Sampling;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import layout.utils.Utils3D;
import propogation.SimplePropogation;
import simulation.CustomSimVar;
import simulation.LogNormalSimVar;
import simulation.NormalSimVariable;
import simulation.RandomSimVariable;
import simulation.SimVariable;
import simulation.SimVariable.DistributionType;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;
import utils.Hist3;
import utils.SurfaceUtils;

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
	public MLArray  resultsToStruct(ArrayList<ProbDetResult> probResult) {

		MLStructure mlStruct = new MLStructure("prob_det", new int[] {probResult.size(),1}); 

		for (int i=0; i<probResult.size(); i++) {

			MLStructure probDetMeanStruct = surfaceToStruct(probResult.get(i).probSurfaceMean,  "prob_det_mean");
			MLStructure probDetStdStruct = surfaceToStruct(probResult.get(i).probSurfaceMean,  "prob_det_std");
			MLStructure settingsStruct = settingsToStrcut(probResult.get(i).simSettings);

			mlStruct.setField("prob_det_mean", probDetMeanStruct, i);
			mlStruct.setField("prob_det_std", probDetStdStruct, i);
			mlStruct.setField("settings", settingsStruct, i);
		}

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

	/**
	 * Convert the settings class to a structure compatible with MATLAb library  
	 * @param probDetSDimSettings - prob det sim settings 
	 * @return structure representing settings. 
	 */
	public MLStructure settingsToStrcut(ProbDetSimSettings settings) {

		MLStructure mlStruct = new MLStructure("settings", new int[] {1,1});

		
		mlStruct.setField("maxrange", mlDouble(settings.maxRange), 0);
		mlStruct.setField("maxdepth", mlDouble(-settings.minHeight), 0);
		mlStruct.setField("depthbin", mlDouble(settings.depthBin), 0);
		mlStruct.setField("rangebin", mlDouble(settings.rangeBin), 0);
		mlStruct.setField("thresh", mlDouble(settings.noiseThreshold), 0);
		mlStruct.setField("minhydrophone", mlDouble(settings.minRecievers), 0);
		mlStruct.setField("hydrophonedepth", new MLDouble(null, settings.recievers.getArrayXYZ()), 0);
		mlStruct.setField("spreading_coeff", mlDouble(((SimplePropogation) settings.propogation).getSpreadingCoeff()), 0);
		mlStruct.setField("abs_coeff",  mlDouble(((SimplePropogation) settings.propogation).getSpreadingCoeff()), 0);
		mlStruct.setField("n",  mlDouble(settings.nBootStraps), 0);
		mlStruct.setField("N",  mlDouble(settings.nRuns), 0);


		//		//animal 
		//		//vert angle 
		//		//TODO
		//		mlStruct.setField("vertangletype", new MLChar(null, SimVariable.getSimVarName(settings.simpleOdontocete.vertAngle.getType())));
		//		if (settings.simpleOdontocete.vertAngle.getType()==DistributionType.NORMAL) {
		//			mlStruct.setField("vertangle",  mlDouble(Math.toDegrees(((NormalSimVariable) settings.simpleOdontocete.vertAngle).getMean())), 0);
		//			mlStruct.setField("vertstd", mlDouble(Math.toDegrees(((NormalSimVariable) settings.simpleOdontocete.vertAngle).getStd())), 0);
		//		}
		//		if (settings.simpleOdontocete.vertAngle.getType()==DistributionType.UNIFORM) {
		//			mlStruct.setField("vertmin",  mlDouble(Math.toDegrees(((RandomSimVariable) settings.simpleOdontocete.vertAngle).getMin())), 0);
		//			mlStruct.setField("vertmax", mlDouble(Math.toDegrees(((RandomSimVariable) settings.simpleOdontocete.vertAngle).getMax())), 0);
		//		}


		//src level
		mlStruct.setField("sourcelevel", simVar2MLStrcut(null, settings.simpleOdontocete.sourceLevel),0);

		//horizontal angle 
		mlStruct.setField("horzangle",  simVar2MLStrcut(null, settings.simpleOdontocete.horzAngle),0); 

		//TODO
		mlStruct.setField("vertangle",  simVar2MLStrcut(null, settings.simpleOdontocete.vertAngles.get(0)),0); 

		mlStruct.setField("depthdist",  simVar2MLStrcut(null, settings.simpleOdontocete.depthDistribution),0); 


		Sampling x = new Sampling(settings.simpleOdontocete.beamProfile.getHorzGrid()); 
		Sampling y = new Sampling(settings.simpleOdontocete.beamProfile.getVertGrid()); 
		float[][] grid = settings.simpleOdontocete.beamProfile.getSurface().grid(x, y);


		float[][] horzGrid = Hist3.getXYSurface(settings.simpleOdontocete.beamProfile.getHorzGrid(), 
				settings.simpleOdontocete.beamProfile.getVertGrid(), true, false); 

		float[][] vertGrid = Hist3.getXYSurface(settings.simpleOdontocete.beamProfile.getHorzGrid(), 
				settings.simpleOdontocete.beamProfile.getVertGrid(), false, false); 


		mlStruct.setField("horzbeam",  new MLDouble(null, Utils3D.float2double(horzGrid)),0);
		mlStruct.setField("vertbeam",  new MLDouble(null, Utils3D.float2double(vertGrid)),0);
		mlStruct.setField("tlBeam",    new MLDouble(null, Utils3D.float2double(grid)),0);

		return mlStruct; 
	}

	/**
	 * Create a sim variable structure in MATLAB from and a sim variable. 
	 * @return a matlab structure representing the 
	 */
	public MLStructure simVar2MLStrcut(String name, SimVariable simVar) {

		//define the MATLAB structure
		MLStructure mlStruct = new MLStructure(name, new int[] {1,1});

		//define all possible variables which can be in the struct. 
		mlStruct.setField("type",  new MLChar(null, SimVariable.getSimVarName(simVar.getType())));
		mlStruct.setField("mean",   mlDouble(0), 0);
		mlStruct.setField("std",   mlDouble(0), 0);
		mlStruct.setField("scale",   mlDouble(0), 0);
		mlStruct.setField("shape",   mlDouble(0), 0);
		mlStruct.setField("truncation",   mlDouble(0), 0);
		mlStruct.setField("min",   mlDouble(0), 0);
		mlStruct.setField("max",   mlDouble(0), 0);
		mlStruct.setField("customp",   mlDouble(0), 0);
		mlStruct.setField("limits",   new MLDouble(null, new double[]{0,1}, 2), 0);

		//switch to get type
		switch (simVar.getType()) {
		case CUSTOM:
			CustomSimVar customSimVar = (CustomSimVar) simVar; 
			mlStruct.setField("customp", new MLDouble(null, customSimVar.getProbData(), customSimVar.getProbData().length), 0);
			mlStruct.setField("min", mlDouble(customSimVar.getMin()), 0); 
			mlStruct.setField("max", mlDouble(customSimVar.getMax()), 0); 
			break;
		case LOGNORMAL:
			LogNormalSimVar logNormal = (LogNormalSimVar) simVar; 
			mlStruct.setField("scale", mlDouble(logNormal.getScale()), 0);
			mlStruct.setField("shape", mlDouble(logNormal.getShape()), 0); 
			mlStruct.setField("truncation", mlDouble(logNormal.getTruncation()), 0); 
			mlStruct.setField("negative", logNormal.isNegative() ? mlDouble(1) : mlDouble(0), 0); 

			break;
		case NORMAL:
			NormalSimVariable normalSim = (NormalSimVariable) simVar; 
			mlStruct.setField("mean", mlDouble(normalSim.getMean()), 0);
			mlStruct.setField("std", mlDouble(normalSim.getStd()), 0); 
			break;
		case UNIFORM:
			RandomSimVariable uniformSim = (RandomSimVariable) simVar; 
			mlStruct.setField("min", mlDouble(uniformSim.getMin()), 0);
			mlStruct.setField("max", mlDouble(uniformSim.getMax()), 0); 
			break;
		default:
			break;

		}

		if (simVar.getLimits()!=null) {
			mlStruct.setField("limits",  new MLDouble(null, simVar.getLimits(), 2), 0); 
		}

		//MATLAB structure
		return mlStruct;
	}



	/**
	 * Single double value in MATLAB
	 * @param value - the value  
	 * @return
	 */
	private MLDouble mlDouble(double value) {
		return new MLDouble(null, new double[]{value}, 1);
	}

}
