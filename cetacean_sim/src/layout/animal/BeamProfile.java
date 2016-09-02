package layout.animal;

import org.apache.commons.math3.analysis.interpolation.PiecewiseBicubicSplineInterpolatingFunction;
import org.apache.commons.math3.analysis.interpolation.PiecewiseBicubicSplineInterpolator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.interp.Gridder2;
import edu.mines.jtk.interp.SibsonGridder2;
import javafx.scene.CacheHint;

/**
 * Creatyes a beam profile from scatterred points and holds beam profile datra. 
 * @author Jamie Macaulay 
 *
 */
public class BeamProfile {
	
	/**
	 * The gridder 2. This is the interpolated beam profile. 
	 */
	private Gridder2 beamProfileSurface; 
	
	double[] horzGrid;
	
	double[] vertGrid;

	/**
	 * An already generated surface for easy lookup. 
	 */
	private PiecewiseBicubicSplineInterpolatingFunction lookUpSurface;

	public BeamProfile(){

	}
	
	/**
	 * Create the beam profile.
	 * @param beamProfile - the beam prfoile. 
	 */
	public void createBeamProfile(double[][] beamProfile) {
		//have a dispersed set of points. Need to create an interpolation surface. #
		float[] horzAngle=new float[beamProfile.length];
		float[] vertAngle=new float[beamProfile.length];
		float[] tl=new float[beamProfile.length];

		for (int i=0; i<beamProfile.length ;i++){
			horzAngle[i]=(float) Math.toRadians(beamProfile[i][0]);
			vertAngle[i]=(float) Math.toRadians(beamProfile[i][1]);
			tl[i]=-(float) beamProfile[i][2];
			System.out.println("TL input: "+tl[i]);
		}
		//create the beam profile surface. 
		beamProfileSurface=new  SibsonGridder2(tl,horzAngle,vertAngle); 
		
		//now that's random smaples. Lets make a surface so we can extract values fast. 
		//annoyingly have to use a different interpolation aglortihm 
		horzGrid=new double[360+1];
		vertGrid=new double[180+1];
		for (int i=0; i<360+1; i++){
			horzGrid[i]=Math.toRadians(i-180);
		}
		for (int i=0; i<180+1; i++){
			vertGrid[i]=Math.toRadians(i-90);
		}
		
		float[][] grid = beamProfileSurface.grid(new Sampling(horzGrid), new Sampling(vertGrid));
		
//		//TEMP print a surface
//		for (int i=0; i<grid.length; i++){
//			System.out.println(""); 
//			for (int j=0; j<grid[i].length; j++){
//				System.out.print(grid[i][j] + " "); 
//			}
//		}
//		//TEMP
		
		double[][] gridd=convertFloatsToDoubles(grid); 
		
		
		@SuppressWarnings("deprecation")
		PiecewiseBicubicSplineInterpolator interpolator = new PiecewiseBicubicSplineInterpolator(); 
		 lookUpSurface=interpolator.interpolate(vertGrid, horzGrid,
				gridd);
//		lookupSurface = new BiCubicSplineFast(vertGrid, horzGrid, gridd);
		
//		//TEMP
//		for (int i=0; i<horzGrid.length; i++){
//			System.out.println("Test horz angle: "+Math.toDegrees(horzGrid[i]) + " TL: "+getTL(horzGrid[i],0));
//		}
//		
//		for (int i=0; i<vertGrid.length; i++){
//			System.out.println("Test vert angle: "+Math.toDegrees(vertGrid[i]) + " TL: "+getTL(0,vertGrid[i]));
//		}
//		
//		//TEMP
	
	}
	
	/**
	 * Get the transmission loss for a section of the beam. 
	 * @param horzAngle - the horizontal angle in RADIANS (-pi -> pi)
	 * @param vertAngle - the vertical angle in RADIANS (-pi/2 -> pi/2)
	 * @return the transmission loss. 
	 */
	public double getTL(double horzAngle, double vertAngle){
		if (lookUpSurface.isValidPoint(vertAngle, horzAngle)){
			return lookUpSurface.value(vertAngle, horzAngle); 
		}
		return 0; 
	}
	
	/**
	 * Get the surface. x is horizontal angle (RADIANS), y is vertical angle (RADIANS) and z is transmission loss. 
	 * @return the interpolated beam profile surface.
	 */
	public Gridder2 getSurface(){
		return beamProfileSurface;
	}
	
	/**
	 * Get the beam tranmission loss at a specified position for an animal at a specified position and orientation. 
	 * @param position - the position of the reciever. 
	 * @param animalPosition - the animla position in cartesian co-ordinates. 
	 * @param animalOrientation - unit vector of animal orientation. 
	 * @return the beam transmission loss (does NOT include propogation loss)
	 */
	public double getBeamTL(double[] recieverPosition, double[] animalPosition, double[] animalOrientatiol){
		
		Vector3D receiverPos= new Vector3D(recieverPosition);
		Vector3D animalPos= new Vector3D(animalPosition);
		Vector3D animalOrient= new Vector3D(animalOrientatiol);
		
		//horizontal angle. 
		double x= recieverPosition[0]-animalPosition[0];
		double y= recieverPosition[1]-animalPosition[1];
		
		double horz=Math.atan2(x, y); 

		//verticalangle 
		double vert;
		if (recieverPosition[2]==animalPosition[2]) vert=0; //quick calc. 
		else{
			double z= recieverPosition[2]-animalPosition[1];
			double r=receiverPos.distance(animalPos);
			vert=Math.asin(z/r); 
		}
		
		//now need to add animla orientation;
		//TODO; 
		
		return getTL(horz, vert); 
	} 
	
	/**
	 * Convert 2D float array to 2D double array.
	 * @param input - the input
	 * @return 2D double array. 
	 */
	private static double[][] convertFloatsToDoubles(float[][] input)
	{
	    if (input == null)
	    {
	        return null; // Or throw an exception - your choice
	    }
	    
	    double[][] output = new double[input.length][360];
	    for (int i = 0; i < input.length; i++)
	    {
	    	double[] row=new double[input[i].length];
	    	for (int j=0; j<input[i].length; j++){
	    		row[j]=input[i][j]; 
	    	}
	        output[i] = row;
	    }
	    return output;
	}

}
