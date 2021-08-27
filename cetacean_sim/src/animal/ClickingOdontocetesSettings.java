package animal;

@Deprecated
public class ClickingOdontocetesSettings  implements Cloneable {
	
	/*********************Diving************************/
		
	public double descentVertAngleStd=Math.toRadians(60);
	public double descentVertAngleMean=Math.toRadians(40);
	
	public double ascentVertAngleMean=Math.toRadians(40);
	public double ascentVertAngleStd=Math.toRadians(40);
	
	public double descentSpeedStd=0.725;
	public double descentSpeedMean=1.3625;
	
	public double ascentSpeedMean=1.25;
	public double ascentSpeedStd=0.7125;
	
	public double bottomSpeedMean=1.25;
	public double bottomSpeedStd=0.7125;
	
	public double bottomTimeMean=3;
	public double bottomTimeStd=5;
	
	public double descentHorzAngleMean=Math.toRadians(0);
	public double descentHorzAngleStd=Math.toRadians(180); //relative to tide. 
	
	public double ascentHorzAngleMean=Math.toRadians(0);
	public double ascentHorzAngleStd=Math.toRadians(10); //relative to tide. 
	
	public double surfaceTimeStd=3;
	public double surfaceTimeMean=5;
	
	
	/****************The beam profile********************/
	
	/**
	 * The beam profile. Non uniform points on the beam profile surface.
	 * Order of each elements {horizontal angle (degrees), vertical angle (degrees), transmission loss dB}
	 *  
	 */
	public double[][] beamProfile = DefaultBeamProfiles.porpBeam1; 
	
	
	/***************Acoustic Behaviour******************/
	/***
	 * The source level in dB re 1uPa pp; 
	 */
	public double sourceLevel=191;
	
	/**
	 * The inter click interval 
	 */
	public double ici=0.14; //seconds 


	@Override
	public ClickingOdontocetesSettings clone()  {

		try {
			return (ClickingOdontocetesSettings) super.clone();
		}
		catch (CloneNotSupportedException e) {
			return null;
		}
	}
	

}
