package animal;

import java.util.ArrayList;

import layout.animal.BeamProfile;

public class DefaultBeamProfiles {
	
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
			{Math.toRadians(0),		Math.toRadians(0),		0}};
	
	
	
	/**
	 * Get a list of default beam profiles. 
	 * @return list of default beam profiles. 
	 */
	public static ArrayList<BeamProfile> getDefaultBeams() {
		
		ArrayList<BeamProfile> beamProfiles = new ArrayList<BeamProfile>(); 
		beamProfiles.add(new BeamProfile("Porpoise", porpBeam1));
		
		return beamProfiles;
		
	}
	
	

}
