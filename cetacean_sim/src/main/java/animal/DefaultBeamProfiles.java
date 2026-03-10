package animal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLDouble;
import layout.animal.BeamProfile;

public class DefaultBeamProfiles {

	private String MEASURED_BEAM_FILE = "/porpoise_beam_profile.csv"; 

	//public final URL MEASURED_BEAM_FILE = ClassLoader.getSystemResource("resources/porpoise_beam_profile.csv");

	/**
	 * The beam profile of a harbour porpoise from Koblitz et al. 2012. 
	 */
	public static double[][] porpBeam1={
			{Math.toRadians(-180), 	Math.toRadians(-90),	 -50},
			{Math.toRadians(180),	Math.toRadians(90),		-50},
			{Math.toRadians(-180),	Math.toRadians(90),		-50},
			{Math.toRadians(180),	Math.toRadians(-90),	-50},
			{Math.toRadians(0),		Math.toRadians(-90)	,	-35},
			{Math.toRadians(0),		Math.toRadians(90),		-35},
			{Math.toRadians(-180),	Math.toRadians(0),		-50},
			{Math.toRadians(-90),	Math.toRadians(0),		-35},
			{Math.toRadians(-15),	Math.toRadians(0),		-13.2},
			{Math.toRadians(-10),	Math.toRadians(0),		-7.5},
			{Math.toRadians(10),	Math.toRadians(0),		-6},
			{Math.toRadians(22),	Math.toRadians(0),		-20},
			{Math.toRadians(90),	Math.toRadians(0),		-35},
			{Math.toRadians(180),	Math.toRadians(0),		-50},
			{Math.toRadians(0),		Math.toRadians(5),		-3},
			{Math.toRadians(0),		Math.toRadians(10),		-12},
			{Math.toRadians(0),		Math.toRadians(-10),	-9},
			{Math.toRadians(0),		Math.toRadians(-3),		-1.5},
			{Math.toRadians(0),		Math.toRadians(0),		0}

	};



	/**
	 * The beam profile of a harbour porpoise from Koblitz et al. 2012 with a back end added for fun.
	 */
	public static double[][] porpBackEnd={
			{Math.toRadians(-180), 	Math.toRadians(-90),	 -50},
			{Math.toRadians(180),	Math.toRadians(90),		-50},
			{Math.toRadians(-180),	Math.toRadians(90),		-50},
			{Math.toRadians(180),	Math.toRadians(-90),	-50},
			{Math.toRadians(0),		Math.toRadians(-90)	,	-35},
			{Math.toRadians(0),		Math.toRadians(90),		-35},
			{Math.toRadians(-180),	Math.toRadians(0),		-21},
			{Math.toRadians(-90),	Math.toRadians(0),		-35},
			{Math.toRadians(-15),	Math.toRadians(0),		-13.2},
			{Math.toRadians(-10),	Math.toRadians(0),		-7.5},
			{Math.toRadians(10),	Math.toRadians(0),		-6},
			{Math.toRadians(22),	Math.toRadians(0),		-20},
			{Math.toRadians(90),	Math.toRadians(0),		-35},
			{Math.toRadians(180),	Math.toRadians(0),		-21},
			{Math.toRadians(0),		Math.toRadians(5),		-3},
			{Math.toRadians(0),		Math.toRadians(10),		-12},
			{Math.toRadians(0),		Math.toRadians(-10),	-9},
			{Math.toRadians(0),		Math.toRadians(-3),		-1.5},
			{Math.toRadians(0),		Math.toRadians(0),		0}};


	/**
	 * Create a uniform beam profile. 
	 * @return a uniform beam profile. 
	 */
	public static double[][] uniform(){
		double[][] beam = new double[200][3]; 

		int n=0; 
		for (double i = -Math.PI; i <Math.PI; i=i+Math.PI/10.) {
			for (double j = -Math.PI/2; j <Math.PI/2; j=j+Math.PI/10.) {
				beam[n][0]=i;
				beam[n][1]=j;
				n++;
			}
		}

		return beam;
	}

	/**
	 * Load in a measured beam profile. 
	 * @param - the default beam loss for extremes of the surface which may not have been adequetely sampled.
	 * @return a measured beam profile loaded from file. 
	 */
	public double[][] measuredBeam(double cornerBeamLoss){

		//		System.out.println("Beam Profile path: " + MEASURED_BEAM_FILE.getPath()); 

		double[][] beamProfile;


		//		String file = DefaultBeamProfiles.class.getProtectionDomain().getCodeSource().getLocation()
		//			    .toString();

		//		System.out.println("Helloo Profile path: " + file); 

		//note, cannot use the MATLAB .mat reader for some reason- requires a URL to work - erm...stumped. 
		beamProfile = loadBeamFromTextFile(MEASURED_BEAM_FILE);

		//need to convert to radians and add corners which may not have been adequetely samples
		double[][] beamProfileR = new double[beamProfile.length+6][3]; 

		for (int i=0; i<beamProfile.length; i++) {
			beamProfileR[i][0] = Math.toRadians(beamProfile[i][0]); 
			beamProfileR[i][1] = Math.toRadians(beamProfile[i][1]);
			beamProfileR[i][2] = beamProfile[i][2];
		}

		//Now add guesstimate of the corners to aid surface interpolation. 
		beamProfileR[beamProfile.length][0]  = Math.toRadians(180); 
		beamProfileR[beamProfile.length][1] =  Math.toRadians(-90); 
		beamProfileR[beamProfile.length][2] = cornerBeamLoss;

		beamProfileR[beamProfile.length+1][0]  = Math.toRadians(180); 
		beamProfileR[beamProfile.length+1][1] = Math.toRadians(90); 
		beamProfileR[beamProfile.length+1][2] = cornerBeamLoss;

		beamProfileR[beamProfile.length+2][0]  = Math.toRadians(-180); 
		beamProfileR[beamProfile.length+2][1] = Math.toRadians(-90); 
		beamProfileR[beamProfile.length+2][2] = cornerBeamLoss;

		beamProfileR[beamProfile.length+3][0]  = Math.toRadians(-180); 
		beamProfileR[beamProfile.length+3][1] = Math.toRadians(90); 
		beamProfileR[beamProfile.length+3][2] = cornerBeamLoss;

		beamProfileR[beamProfile.length+4][0]  = Math.toRadians(0); 
		beamProfileR[beamProfile.length+4][1] = Math.toRadians(90); 
		beamProfileR[beamProfile.length+4][2] = cornerBeamLoss;

		beamProfileR[beamProfile.length+5][0]  = Math.toRadians(0); 
		beamProfileR[beamProfile.length+5][1] = Math.toRadians(-90); 
		beamProfileR[beamProfile.length+5][2] = cornerBeamLoss;

		return beamProfileR; 

	}


	/**
	 * Get a list of default beam profiles. 
	 * @return list of default beam profiles. 
	 */
	public ArrayList<BeamProfile> getDefaultBeams() {

		ArrayList<BeamProfile> beamProfiles = new ArrayList<BeamProfile>(); 
		beamProfiles.add(new BeamProfile("Porpoise Measured", measuredBeam(0-45))); 
		beamProfiles.add(new BeamProfile("Uniform", uniform()));

		//need to add measured beam profile from file. 
		beamProfiles.add(new BeamProfile("Porpoise_old", porpBeam1));
		beamProfiles.add(new BeamProfile("Porpoise Back End_old", porpBackEnd));

		return beamProfiles;

	}

	private double[][] loadBeamFromTextFile(String fileurl) {

		try {
			InputStream input = DefaultBeamProfiles.class.getResourceAsStream(fileurl);
			if (input == null) {
				System.err.println("Could not find resource: " + fileurl);
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(input));

			// First pass: count lines
			ArrayList<String> lines = new ArrayList<>();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				lines.add(inputLine);
			}
			in.close();

			double[][] data = new double[lines.size()][3];
			for (int i = 0; i < lines.size(); i++) {
				String[] dataS = lines.get(i).split(",");
				data[i][0] = Double.valueOf(dataS[0]);
				data[i][1] = Double.valueOf(dataS[1]);
				data[i][2] = Double.valueOf(dataS[2]);
			}

			System.out.println("Number beam measurments read from .csv: " + lines.size());

			return data;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Load a porpoise beam profile from a file. 
//	 * @param file - the .mat file to load data from
	 * @return a double array of horizontal angle (degrees), vertical angle(degrees) and beam loss (dB)
	 */
	private double[][]  loadBeamFromFile(URL file) {
		MatFileReader mfr = null; 
		try {
			if (file ==null) {
				System.out.println("The imported file is null");
				return null;
			}

			mfr = new MatFileReader(file.getFile());

			//get array of a name "my_array" from file
			if (mfr.getMLArray("beam_field")!=null) {
				//get the beam field array
				MLDouble beamField = (MLDouble) mfr.getMLArray("beam_field");
				return beamField.getArray(); 
			}


			//System.out.println(mfr.getContent()); 
			else return null;

		}
		catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
	}



}
