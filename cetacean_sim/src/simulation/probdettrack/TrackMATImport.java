package simulation.probdettrack;

import java.io.File;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import com.jmatio.types.MLStructure;

import animal.AnimalModel;
import animal.ClickingOdontocete;
import animal.DTagdBConverter;
import animal.DefaultBeamProfiles;
import animal.Linear2DBConverter;
import utils.CetSimUtils;
import utils.LatLong;


/**
 * import a structure of tag data. 
 * 
 * @author Jamie Macaulay
 *
 */
public class TrackMATImport {


	/**
	 * Import an animal structure.

		Note that there cannot be a table in the structure. 
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


		if (tagdata.getField("beamprofile")!=null) {
			//load the orientation data - not this is int radians. 
			MLDouble beamProfile = (MLDouble) tagdata.getField("beamprofile");
			animalStruct.beamProfile  = beamProfile.getArray(); 
		}
		else  {
			DefaultBeamProfiles defaultBeamProfiles = new DefaultBeamProfiles(); 
			//default porpoise beam. 
			animalStruct.beamProfile = defaultBeamProfiles.getDefaultBeams().get(0).getRawBeamMeasurments(); 
		}

		//the sens of the hydrophone in samples per second. 
		MLDouble systemSens=(MLDouble) tagdata.getField("sens", 0);
		animalStruct.systemSens  = systemSens.get(0); 


		//		MLDouble depthbin=(MLDouble) mlArrayRetrived.getField("depthbin", 0);
		//		MLDouble rangebin=(MLDouble) mlArrayRetrived.getField("rangebin", 0);
		//		MLInt32 evenXY=(MLInt32) mlArrayRetrived.getField("evenxy", 0);

		return animalStruct; 
	}

	/**
	 * Convert an animal struct (which parallels the MATLAB structure) into an AnimalModel object. 
	 * @param animalStruct - the struct to use. 
	 */
	public static AnimalModel animalStruct2AnimalModel(AnimalStruct animalStruct) {

		//the animal model reference time is is the start of the track
		double simStart = animalStruct.trackdata[0][0]; 

		//use the sens and dB from the animal struct for dB conversion. 
		DTagdBConverter dBConverter = new DTagdBConverter(animalStruct.systemSens, animalStruct.vp2p); 

		return  animalStruct2AnimalModel( animalStruct,  simStart,  dBConverter); 
	}

	/**
	 * Convert an animal struct (which parallels the MATLAB structure) into an AnimalModel object. 
	 * @param animalStruct - the struct to use. 
	 * @param simStart - the start of the simulation in MATLAB date number format. This is when seconds are referenced to. 
	 * @param 
	 */
	public static AnimalModel animalStruct2AnimalModel(AnimalStruct animalStruct, double simStart, Linear2DBConverter dBconverter ) {

		//the start of the simulation
		double[] trackTimes = new double[animalStruct.trackdata.length]; 
		double[][] trackxyz = new double[3][animalStruct.trackdata.length]; 
		double[][] trackang = new double[2][animalStruct.trackdata.length]; 
		double[] vocTimes = new double[animalStruct.clicks.length]; 


		double[] vocAmp = new double[animalStruct.clicks.length]; //this needs to be in dB re 1uPa pp on-axis


		LatLong refLatLong = new LatLong(animalStruct.trackdata[0][1], animalStruct.trackdata[0][2]); 
		LatLong latLong; 
		//now need to populate all these with data. 
		for (int i=0; i<trackTimes.length; i++) {
			//convert to seconds. 
			trackTimes[i] = (animalStruct.trackdata[i][0] - simStart)*60*24*24; 
			//transpose the track matrix to allow java to easily grab all x,y and/or z co-ords in a double[];

			//need to convert from latitude and longitude to northings and eastings. 
			latLong = new LatLong(animalStruct.trackdata[i][1], animalStruct.trackdata[i][2]); 

			trackxyz[0][i] = refLatLong.distanceToMetresX(latLong); //Eastings
			trackxyz[1][i] = refLatLong.distanceToMetresY(latLong); //Northings
			trackxyz[2][i] = animalStruct.trackdata[i][3]; //depth
			
			//System.out.println(" Track data: " + trackxyz[2][i]); 

			//the track angle
			trackang[0][i] = animalStruct.orientation[i][1]; //horizontal angle radians. 
			trackang[1][i] = animalStruct.orientation[i][2]; //horizontal angle radians. 

			//		    vertang =asind((animalpos(3)-trackbefore(4))/diststraight(trackbefore(2:4), animalpos));
			//		    horzang = atan2d( animalpos(1)-trackbefore(2), animalpos(2)-trackbefore(3));
			//check if the horizontal angle is NaN
			if (i<100) {
				System.out.println("NaN val?: " + trackang[0][i]);
			}

			if (Double.isNaN(trackang[0][i])) {
				if (i==0) {
					trackang[0][i]=0; 
				}
				else {
					//replace with the orientation of the track. 
					trackang[0][i] = Math.atan2(trackxyz[0][i]-trackxyz[0][i-1], trackxyz[1][i]-trackxyz[1][i-1]); 
				}
				//System.out.println("New val horz: " + trackang[0][i]);
			}

			//check if the pitch angle is NaN
			if (Double.isNaN(trackang[1][i])) {
				if (i==0) {
					trackang[1][i]=0; 
				}
				else {
					trackang[1][i] = Math.asin((trackxyz[2][i]-trackxyz[2][i-1])/CetSimUtils.distance(new double[] {trackxyz[0][i], trackxyz[1][i], trackxyz[2][i]},
							new double[] {trackxyz[0][i-1], trackxyz[1][i-1], trackxyz[2][i-1]})); 
				}
				//System.out.println("New val vert: " + trackang[1][i]);
			}

		}

		
		//the vocalisation times. 
		for (int i=0; i<vocTimes.length; i++) {
			//convert to seconds. 
			vocTimes[i] = (animalStruct.clicks[i][0] - simStart)*60*24*24; 

			//transpose the track matrix to allow java to easily grab all x,y and/or z co-ords in a double[];
			vocAmp[i] = dBconverter.linear2dB(animalStruct.clicks[i][5]);
			//now convert this to a dB measurement
		}

		return new ClickingOdontocete(trackTimes, trackxyz, trackang, vocTimes, vocAmp, animalStruct.beamProfile); 
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

		AnimalModel animalModel = animalStruct2AnimalModel(animalStruct);

	}

}
