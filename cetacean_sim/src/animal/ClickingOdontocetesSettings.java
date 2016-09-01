package animal;

public class ClickingOdontocetesSettings  {
	
	
	/****************The beam profile********************/
	
	/**
	 * The beam profile. Non uniform points on the beam profile surface.
	 * Order of each elements {horizontal angle (degrees), vertical angle (degrees), transmission loss dB}
	 *  
	 */
	public double[][] beamProfile={
			{-180, -90, -50},
			{180,	90,	-50},
			{-180,	90,	-50},
			{180,	-90,-50},
			{0,	-90	,-35},
			{0,	90,	-35},
			{-180,	0,	-50},
			{-90,	0,	-35},
			{-15,	0,	-13.2},
			{-10,	0,	-7.5},
			{10,	0,	-6},
			{22,	0,	-20},
			{90,	0,	-35},
			{180,	0,	-50},
			{0,	5,	-3},
			{0,	10,	-12},
			{0,	-10,	-9},
			{0,	-3,	-1.5},
			{0,	0,	0}};
	
	/***
	 * The source level in dB re 1uPa pp; 
	 */
	public double sourceLevel=191;

		
	

}
