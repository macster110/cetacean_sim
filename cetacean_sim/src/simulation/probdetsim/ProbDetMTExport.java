package simulation.probdetsim;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLInt32;
import com.jmatio.types.MLStructure;

import animal.SimpleOdontocete;
import edu.mines.jtk.dsp.Sampling;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import layout.ResultConverter;
import layout.utils.Utils3D;
import propogation.SimplePropogation;
import reciever.HydrophoneArray;
import reciever.SimpleHydrophoneArray;
import simulation.CustomSimVar;
import simulation.LogNormalSimVar;
import simulation.NormalSimVariable;
import simulation.RandomSimVariable;
import simulation.SimVariable;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;
import utils.Hist3;

/**
 * Export probability fo detection data to a .mat file. This allows the program to interact with 
 * the MATLAB library. 
 * <p>
 * Note that in MATLAB settings are stored in degrees but in Java they are stored as Radians. 
 * 
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
	 * @param probResult - probability of detection results. 
	 * @return the results. 
	 */
	public MLArray resultsToStruct(ArrayList<ProbDetResult> probResult) {

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
	 * Convert a MATLAB structure to a probability simulation settings. 
	 * @param settings - the MATLAB structure with simulation settings
	 * @return the probability settings class. 
	 */
	public ProbDetSimSettings structToSettings(MLStructure mlArrayRetrived) {

//		MatFileReader mfr = null; 
		try {
//			mfr = new MatFileReader( "C:\\Users\\macst\\Desktop\\testImportSettings.mat" );
//
//			//get array of a name "my_array" from file
//			MLStructure mlArrayRetrived = (MLStructure) mfr.getMLArray( "settings" );

//			MLChar speciesML = (MLChar) mlArrayRetrived.getField("species", 0); 
//			String species = speciesML.getString(0); 
			
			MLDouble maxRange=(MLDouble) mlArrayRetrived.getField("maxrange", 0);
			MLDouble maxDepth=(MLDouble) mlArrayRetrived.getField("maxdepth", 0);
			MLDouble depthbin=(MLDouble) mlArrayRetrived.getField("depthbin", 0);
			MLDouble rangebin=(MLDouble) mlArrayRetrived.getField("rangebin", 0);
			MLInt32 evenXY=(MLInt32) mlArrayRetrived.getField("evenxy", 0);


			MLDouble nBootStraps=(MLDouble) mlArrayRetrived.getField("n", 0);
			MLDouble nRuns=(MLDouble) mlArrayRetrived.getField("N", 0);

			//threshold
			MLDouble thresh=(MLDouble) mlArrayRetrived.getField("thresh", 0);

			//hydrophones
			MLDouble hydrophones=(MLDouble) mlArrayRetrived.getField("hydrophones", 0);
			MLDouble minhydrophones=(MLDouble) mlArrayRetrived.getField("minhydrophones", 0);

			//propogation
			MLDouble spreading_coeff=(MLDouble) mlArrayRetrived.getField("spreading_coeff", 0);
			MLDouble abs_coeff=(MLDouble) mlArrayRetrived.getField("abs_coeff", 0);

			MLStructure sourceLevelML = 	(MLStructure) mlArrayRetrived.getField("sourcelevel", 0);
			MLStructure horzangleML = 		(MLStructure) mlArrayRetrived.getField("horzangle", 0);
			MLStructure depthdistML = 		(MLStructure) mlArrayRetrived.getField("depthdist", 0);
			MLStructure vertangleML = 		(MLStructure) mlArrayRetrived.getField("vertangle", 0);

			//beam profile
			MLDouble beamraw = 		(MLDouble) mlArrayRetrived.getField("beamraw", 0);

			SimVariable sourceLevel= 	mlStruct2SimVariable(sourceLevelML, "Source Level",0); 
			SimVariable horzangle= 	mlStruct2SimVariable(horzangleML, "Horz Angle",0, new Degrees2Radians()); 
			SimVariable depthdist= 	mlStruct2SimVariable(depthdistML, "Depth Dist",0); 
			
			//there can be multiple vertical angles. 
			SimVariable[] vertangle= new SimVariable[vertangleML.getM()]; 
			for (int i=0; i<vertangle.length; i++ ) {
				 vertangle[i]= 	mlStruct2SimVariable(vertangleML, ("Vert Angle" + i), i, new Degrees2Radians()); 
			}
			
			//now create a ProbDetSimSettings object
			ProbDetSimSettings probDetSimSettings = new ProbDetSimSettings(); 
			
			//basic sim settings 
			probDetSimSettings.maxRange=maxRange.get(0);
			probDetSimSettings.minHeight=maxDepth.get(0);
			probDetSimSettings.depthBin=depthbin.get(0).intValue();
			probDetSimSettings.rangeBin=rangebin.get(0).intValue();
			probDetSimSettings.nBootStraps=nBootStraps.get(0).intValue();
			probDetSimSettings.nRuns=nRuns.get(0).intValue();
			probDetSimSettings.evenXY=evenXY.get(0).intValue(); 
			
			//noise
			probDetSimSettings.noiseThreshold=thresh.get(0);
			
			//Hydrophones
			HydrophoneArray hydrophoneArray =new SimpleHydrophoneArray(hydrophones.getArray()); 
			probDetSimSettings.recievers = hydrophoneArray; 
			
			//Propagation
			SimplePropogation propogation = new SimplePropogation(spreading_coeff.get(0)
					, abs_coeff.get(0)); 
			probDetSimSettings.propogation=propogation; 
			
			//Animal
			probDetSimSettings.simpleOdontocete= new SimpleOdontocete(sourceLevel, vertangle, horzangle, 
					depthdist, Utils3D.beam2Radians(beamraw.getArray()), maxDepth.get(0)); 

			
			/***Print this stuff out***/
			System.out.println("maxRange: " + maxRange.get(0)); 
			System.out.println("maxDepth: " + maxDepth.get(0)); 
			System.out.println("depthbin: " + depthbin.get(0)); 
			System.out.println("rangebin: " + rangebin.get(0)); 
			System.out.println("nBootStraps: " + nBootStraps.get(0)); 
			System.out.println("nRuns: " + nRuns.get(0)); 
			System.out.println("--------------------------"); 
			System.out.println("thresh: " + thresh.get(0)); 
			System.out.println("--------------------------"); 
			System.out.println("spreading_coeff: " + spreading_coeff.get(0)); 
			System.out.println("abs_coeff: " + abs_coeff.get(0)); 
			System.out.println("--------------------------"); 
			System.out.println("minhydrophones: " + minhydrophones.get(0)); 
			//print out hydrophones
			for (int i=0; i<hydrophones.getM(); i++) {
				System.out.println("Hydrophone: " + i + " " + hydrophones.get(i, 0) + "  " + hydrophones.get(i, 1) + " " +
						hydrophones.get(i, 2) ); 
			}
			System.out.println("--------------------------"); 
			//now print sim-variables. 
			System.out.println(sourceLevel.toString()); 
			System.out.println(horzangle.toString()); 
			System.out.println(depthdist.toString()); 
			System.out.println("Vert Angle");
			for (int i=0; i<vertangle.length; i++ ) {
				System.out.println(vertangle.toString()); 
			}
			System.out.println("--------------------------"); 
			/***************************/
			
			return probDetSimSettings;

		} catch (Exception e) {
			e.printStackTrace();
		}


		return null; 
	}


	/**
	 * Convert the settings class to a structure compatible with MATLAB library  
	 * @param probDetSDimSettings - prob det sim settings 
	 * @return structure representing settings. 
	 */
	public MLStructure settingsToStrcut(ProbDetSimSettings settings) {

		MLStructure mlStruct = new MLStructure("settings", new int[] {1,1});

		mlStruct.setField("maxrange", mlDouble(settings.maxRange), 0);
		mlStruct.setField("maxdepth", mlDouble(-settings.minHeight), 0);
		mlStruct.setField("depthbin", mlDouble(settings.depthBin), 0);
		mlStruct.setField("rangebin", mlDouble(settings.rangeBin), 0);
		mlStruct.setField("evenxy", new MLInt32(null, new int[]{settings.evenXY}, 1), 0); //TODO

		
		mlStruct.setField("thresh", mlDouble(settings.noiseThreshold), 0);
		mlStruct.setField("minhydrophones", mlDouble(settings.minRecievers), 0);
		mlStruct.setField("hydrophones", new MLDouble(null, settings.recievers.getArrayXYZ()), 0);
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
		mlStruct.setField("horzangle",  simVar2MLStrcut(null, settings.simpleOdontocete.horzAngle, new Radians2Degrees()),0); 

		//TODO - need to set multiple fields here. 
		mlStruct.setField("vertangle",  simVar2MLStrcut(null, settings.simpleOdontocete.vertAngles.get(0), new Radians2Degrees()),0); 

		mlStruct.setField("depthdist",  simVar2MLStrcut(null, settings.simpleOdontocete.depthDistribution),0); 


		Sampling x = new Sampling(settings.simpleOdontocete.getBeamProfile().getHorzGrid()); 
		Sampling y = new Sampling(settings.simpleOdontocete.getBeamProfile().getVertGrid()); 
		float[][] grid = settings.simpleOdontocete.getBeamProfile().getSurface().grid(x, y);


		float[][] horzGrid = Hist3.getXYSurface(settings.simpleOdontocete.getBeamProfile().getHorzGrid(), 
				settings.simpleOdontocete.getBeamProfile().getVertGrid(), true, false); 

		float[][] vertGrid = Hist3.getXYSurface(settings.simpleOdontocete.getBeamProfile().getHorzGrid(), 
				settings.simpleOdontocete.getBeamProfile().getVertGrid(), false, false); 

		mlStruct.setField("horzbeam",  new MLDouble(null, Utils3D.float2double(horzGrid)),0);
		mlStruct.setField("vertbeam",  new MLDouble(null, Utils3D.float2double(vertGrid)),0);
		mlStruct.setField("tlBeam",    new MLDouble(null, Utils3D.float2double(grid)),0);
		mlStruct.setField("beamRaw",    new MLDouble(null, Utils3D.beam2Degrees(settings.simpleOdontocete.getBeamProfile().getRawBeamMeasurments())));


		return mlStruct; 
	}


	public SimVariable mlStruct2SimVariable(MLStructure structure, String name, int index) {
		return mlStruct2SimVariable( structure,  name,  index,  new ResultConverter());
	}
	/**
	 * Convert an ML structure into a sim variable. 
	 * @param structure - the structure. This can be a single struct or a list
	 * @param index - the index of the structure if an array. 
	 * @return the sim variable. 
	 */
	public SimVariable mlStruct2SimVariable(MLStructure structure, String name, int index, ResultConverter resultsConverter) {

		MLChar typeML = (MLChar) structure.getField("type", index); 
		String type = typeML.getString(0); 
		
		MLDouble limits = (MLDouble) structure.getField("limits", index); 
		double[] simLims=limits.getArray()[0];

		SimVariable simVar = null; 
		switch (type) {
		case "normal":
			MLDouble meanML = (MLDouble) structure.getField("mean", index); 
			MLDouble stdML = (MLDouble) structure.getField("std", index); 
			simVar = new NormalSimVariable(name, resultsConverter.convert2Value(meanML.get(0)), resultsConverter.convert2Value(stdML.get(0)), simLims ); 
			break; 
		case "uniform":
			MLDouble minML = (MLDouble) structure.getField("min", index); 
			MLDouble maxML = (MLDouble) structure.getField("max", index); 
			simVar = new RandomSimVariable(name, resultsConverter.convert2Value(minML.get(0)), resultsConverter.convert2Value(maxML.get(0)), simLims); 
			break; 
		case "log-normal":
			MLDouble shape = (MLDouble) structure.getField("shape", index); 
			MLDouble scale = (MLDouble) structure.getField("scale", index); 
			MLDouble truncation = (MLDouble) structure.getField("truncation", index); 
			MLDouble flipnegative = (MLDouble) structure.getField("flipnegative", index); 
			boolean flipNegBool = flipnegative.get(0)==1 ? true : false; 
			simVar = new LogNormalSimVar(name, resultsConverter.convert2Value(scale.get(0)), resultsConverter.convert2Value(shape.get(0)), 
					resultsConverter.convert2Value(truncation.get(0)), flipNegBool, simLims); 
			break; 
		case "custom":
			MLDouble minMLC = (MLDouble) structure.getField("min", index); 
			MLDouble maxMLC = (MLDouble) structure.getField("max", index); 
			MLDouble customArray = (MLDouble) structure.getField("customp", index); 
			simVar = new CustomSimVar(name, customArray.getArray()[0], resultsConverter.convert2Value(minMLC.get(0)), 
					resultsConverter.convert2Value(maxMLC.get(0)), simLims); 
			break; 
		}
		
		//System.out.println(type.getString(0)); 		
		return simVar; 
	}

	/**
	 * Create a sim variable structure in MATLAB from a sim variable object. 
	 * @param name - the name of the structure. Can be null
	 * @param the sim variable to convert
	 * @return a MATLAB structure representing the sim variable
	 */
	public MLStructure simVar2MLStrcut(String name, SimVariable simVar) {
		return simVar2MLStrcut(name, simVar, new ResultConverter());
	}


	/**
	 * Create a sim variable structure in MATLAB from a sim variable object. 
	 * @param name - the name of the structure. Can be null
	 * @param the sim variable to convert
	 * @param the results converter e.g. for degrees to radians or vice versa. 
	 * @return a MATLAB structure representing the sim variable.
	 */
	public MLStructure simVar2MLStrcut(String name, SimVariable simVar, ResultConverter resultsConvert) {

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
			mlStruct.setField("min", mlDouble(resultsConvert.convert2Value(customSimVar.getMin())), 0); 
			mlStruct.setField("max", mlDouble(resultsConvert.convert2Value(customSimVar.getMax())), 0); 
			break;
		case LOGNORMAL:
			//FIXME not sure about results converter here- need to take log?
			LogNormalSimVar logNormal = (LogNormalSimVar) simVar; 
			mlStruct.setField("scale", mlDouble(resultsConvert.convert2Value(logNormal.getScale())), 0);
			mlStruct.setField("shape", mlDouble(resultsConvert.convert2Value(logNormal.getShape())), 0); 
			mlStruct.setField("truncation", mlDouble(resultsConvert.convert2Value(logNormal.getTruncation())), 0); 
			mlStruct.setField("negative", logNormal.isNegative() ? mlDouble(1) : mlDouble(0), 0); 

			break;
		case NORMAL:
			NormalSimVariable normalSim = (NormalSimVariable) simVar; 
			mlStruct.setField("mean", mlDouble(resultsConvert.convert2Value(normalSim.getMean())), 0);
			mlStruct.setField("std", mlDouble(resultsConvert.convert2Value(normalSim.getStd())), 0); 
			break;
		case UNIFORM:
			RandomSimVariable uniformSim = (RandomSimVariable) simVar; 
			mlStruct.setField("min", mlDouble(resultsConvert.convert2Value(uniformSim.getMin())), 0);
			mlStruct.setField("max", mlDouble(resultsConvert.convert2Value(uniformSim.getMax())), 0); 
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
	 * Results converter for converting degrees to radians. 
	 * @author Jamie Macaulay
	 *
	 */
	private class Degrees2Radians extends ResultConverter {
		
		public double convert2Value(double value) {
			return Math.toRadians(value); 
		}
	}
	
	/**
	 * Results converter for converting radians to degrees. 
	 * @author Jamie Macaulay
	 *
	 */
	private class Radians2Degrees extends ResultConverter {
		
		public double convert2Value(double value) {
			return Math.toDegrees(value); 
		}
	}


	/**
	 * Single double value in MATLAB
	 * @param value - the value  
	 * @return
	 */
	private MLDouble mlDouble(double value) {
		return new MLDouble(null, new double[]{value}, 1);
	}

	public static void main(String[] args) {
		ProbDetMTExport probDetMTExport = new ProbDetMTExport(); 
		probDetMTExport.structToSettings(null); 
	}

}
