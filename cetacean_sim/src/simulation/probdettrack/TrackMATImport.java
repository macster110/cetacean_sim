package simulation.probdettrack;

import java.io.File;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

import animal.AnimalModel;


/**
 * import a structure of tag data. 
 * @author Jamie Macaulay
 *
 */
public class TrackMATImport {

	/**
	 * Import an animal structure. Note that there cannot be a table in the structure. 
	 * The MAT library does not cope with tables. 
	 * @param file - the MAT file containing a structure of tag data. 
	 * @return the corresponding java object. 
	 */
	public static AnimalStruct importMATTrack(File file) {

		MatFileReader mfr = null; 
		try {
			if (file ==null) {
				System.err.println("The imported file is null");
				return null;
			}

			mfr = new MatFileReader(file);

			//get array of a name "my_array" from file
			MLStructure mlArrayRetrived;
			if (mfr.getMLArray("tagdata2")!=null) {
				//if exported by cetsim with sim results. 
				mlArrayRetrived = (MLStructure) mfr.getMLArray("tagdata2");
				
				//System.out.println("mlArrayRetrived: " + mfr.getContent()); 

			}
			else {
				System.err.println("Could not find the tagdata structure");
				return null; 
			}

			return struct2Object(mlArrayRetrived); 

		}
		catch (Exception e){
			e.printStackTrace();
			return null; 
		}
	}


	public static AnimalStruct struct2Object(MLStructure tagdata) {

		AnimalStruct animalStruct = new AnimalStruct(); 

		//loaqd the 7 column clicks array
		MLDouble clicks = (MLDouble) tagdata.getField("clicks");
		//System.out.println("clicks: " + clicks.getN()); 
		animalStruct.clicks  = clicks.getArray(); 
		
//		MLChar tagid = (MLChar) tagdata.getField("tagid", 0); 
//		animalStruct.tagid  = tagid.contentToString(); 

		//load the track data
		MLDouble trackdata = (MLDouble) tagdata.getField("trackdata");
		animalStruct.trackdata  = trackdata.getArray(); 

		//load the orientation data - not this is int radians. 
		MLDouble orientation = (MLDouble) tagdata.getField("orientation");
		animalStruct.orientation  = orientation.getArray(); 
		
		//the ADC peak to peak voltage
		MLDouble vp2p=(MLDouble) tagdata.getField("vp2p", 0);
		animalStruct.vp2p  = vp2p.get(0); 

		
		//the sens of the hydrophone in samples per second. 
		MLDouble systemSens=(MLDouble) tagdata.getField("sens", 0);
		animalStruct.systemSens  = systemSens.get(0); 


//		MLDouble depthbin=(MLDouble) mlArrayRetrived.getField("depthbin", 0);
//		MLDouble rangebin=(MLDouble) mlArrayRetrived.getField("rangebin", 0);
//		MLInt32 evenXY=(MLInt32) mlArrayRetrived.getField("evenxy", 0);
		
		return animalStruct; 
	}
	
	/**
	 * Convert an animal struct (which parallesl the MATLAB structure) in an AnimalModel object. 
	 * @param animalStruct - the struct to use. 
	 */
	public static AnimalModel AnimalStruct2AnimalModel(AnimalStruct animalStruct) {
		//TODO
		return null; 
	}


	/**
	 * Test importing track data from MATLAB
	 * @param args - the arguments. 
	 */
	public static void main(String[] args) {
		String filename = "/Users/au671271/Desktop/tagDataTest.mat";  
		
		AnimalStruct animalStruct = importMATTrack(new File(filename)); 
		
		System.out.println("Imported track data"); 
		System.out.println("clicks: " + animalStruct.clicks.length + " x " + animalStruct.clicks[0].length); 
		System.out.println("trackdata: " + animalStruct.trackdata.length + " x " + animalStruct.trackdata[0].length); 
		System.out.println("orientation: " + animalStruct.orientation.length + " x " + animalStruct.orientation[0].length); 
		System.out.println("vp2p: " + animalStruct.vp2p);
		System.out.println("system sens: " + animalStruct.systemSens);

	}

}
