package utils;

import edu.mines.jtk.interp.SibsonGridder2;

public class SurfaceUtils extends BathymetryUtils {
	
	
	/**
	 * Generate a surface from a series of scattered points. 
	 * @param points - scattered points, each is a 3D co-ordinates
	 * @return the surface data. 
	 */
	public static SurfaceData generateSurface(double[][] points) {
		return generateSurface(points, 1); 
	}
	
	
	/**
	 * Generate a surface from a series of scattered points. 
	 * @param points - scattered points, each is a 3D co-ordinates.
	 * @param zExaggeration - the z exaggeration.
	 * @return the surface data.
	 */
	public static SurfaceData generateSurface(double[][] points, float zExaggeration) {
		//get data into the correct format and find min values. 
		float[] f =new float[(int) Math.floor(points.length)];
		float[] x1 =new float[(int) Math.floor(points.length)];
		float[] x2 =new float[(int) Math.floor(points.length)];

		double minX=Double.MAX_VALUE;  
		double maxX=-Double.MAX_VALUE;
		double minY=Double.MAX_VALUE;  
		double maxY=-Double.MAX_VALUE;
		
		int n=0;
		for (int i=0; i<points.length; i++){
			
			
			if (points[i] == null || n>=points.length) continue;
			
			//create co-ordinate
			x1[n]=(float) (points[i][0])-Float.MIN_VALUE;
			x2[n]=(float) (points[i][1])-Float.MIN_VALUE;
			f[n]=(float) points[i][2]*zExaggeration; 
			
			//if (f[n]==0.) f[n]=(float) Math.random();
			
			//System.out.println(" i: "+ i+ " points: "+ x1[n] + " " + x2[n] +  " " +  f[n]);

			
			//work out m in max
			if (x1[n]>maxX) maxX=x1[n]; 
			if (x1[n]<minX) minX=x1[n]; 
			if (x2[n]>maxY)	maxY=x2[n]; 
			if (x2[n]<minY) minY=x2[n]; 
			
			n++;
			
		}
		
		//System.out.println("Min max values for grid: " + minX + " "+  maxX + " " + minY + " "+ maxY); 
		
		SibsonGridder2 simpleGridder2= new SibsonGridder2(f, x1,  x2);
		
		//System.out.println("Interp value for : 28, -168: " + simpleGridder2.interpolate(28f, -162f)); 

		SurfaceData surfaceData = new SurfaceData(simpleGridder2, minX, maxX, minY, maxY); 
		
		return surfaceData; 
	}
	


}
