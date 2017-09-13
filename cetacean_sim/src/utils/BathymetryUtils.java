package utils;

import java.util.Arrays;

import bathymetry.BathyData;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.interp.SibsonGridder2;
import edu.mines.jtk.interp.SibsonInterpolator2;
import edu.mines.jtk.interp.SimpleGridder2;
import edu.mines.jtk.interp.SplinesGridder2;

/**
 * A set of useful functions for calculating bathymetry surfaces. 
 * @author Jamie Macaulay
 *
 */
public class BathymetryUtils {
	
	
	/**
	 * Generate a bathymetry surface from  scatterred LatLong POINTS. The grid is returned in the cartesian Co-Ordinate frame; 
	 * @param points - non unifrom latitude and longitude points. 
	 * @param zExaggeration - the z exaggeration
	 * @return a surface in cartesian co-ordinates. 
	 */
	public static BathyData generateSurface(LatLong[] points, float zExaggeration, boolean aspectRatio){
		
		//calc the reference latitude and longitude. 
		LatLong refLatLong = calcRefLatLong(points);
	
		//convert to cartesian
		double[][] array= latLong2Cart(points, refLatLong);

		//create the surface
		BathyData surfaceData  =generateSurface(array,  zExaggeration,  aspectRatio);
		
		//set the reference lat long. 
		surfaceData.setRefLatLong(refLatLong);

		return surfaceData;
	}
	
	/**
	 * Convert latitude and longitude to cartesian. The referenc elatitude and longitude is the middle of the grid. 
	 * @param ref - the reference lat lon. Can be null to use a ref in the middle of the grid. 
	 * @return points converted to cartesian co-ordinate frame with (0,0,0) the reference latitude and longitude. 
	 */
	public static double[][] latLong2Cart(LatLong[] points){
		return latLong2Cart(points, null);
	}
	
	/**
	 * Convert latitude and longitude to cartesian. 
	 * @param points an array of latitude longitude points. 
	 * @param ref - the reference lat lon. Can be null to use a ref in the middle of the grid. 
	 * @return points converted to cartesian co-ordinate frame with (0,0,0) the reference latitude and longitude. Null values are removed.
	 */
	public static double[][] latLong2Cart(LatLong[] points, LatLong ref){
		LatLong refLatLong;
		if (ref==null){
			refLatLong=calcRefLatLong(points);
		}
		else refLatLong= ref.clone();

		double[][] array = new double[points.length][3];

		//now convert to cartesian
		double x; 
		double y;
		int n=0; 
		for (int i=0; i<points.length; i++){
			if (points[i]==null) continue; 
			x=points[i].distanceToMetresX(refLatLong);
			y=points[i].distanceToMetresY(refLatLong);
			//System.out.println("distance x "+ x + " dsstance y: " +y);
			array[n][0]=x;
			array[n][1]=y;
			array[n][2]=points[i].getHeight();
			n++; 

		}
		
		//return only the non null elements
		return Arrays.copyOf(array, n);
	}
	
	
	/**
	 * Calculate a referernce LatLong form an array of non uniform points. The reference is in the middle of the data. i.e. where 
	 * the middle an interpolated grid of data would be. 
	 * @param points - set of latitude and longitude points, can be non uniform. 
	 * @return the reference LatLong. 
	 */
	public static LatLong calcRefLatLong(LatLong[] points){
		
		LatLong refLatLong;

		//make the ref the middle of the grid. 
		int index=0; 

		//use min lat as the reference point. Also a  bit of data validation to handle nuyll values;l 
		double minLat=Double.MAX_VALUE;  
		double minLon=Double.MAX_VALUE;  
		double maxLat=-Double.MAX_VALUE;  
		double maxLon=-Double.MAX_VALUE;  

		for (int i=0; i<points.length; i++){

			if (points[i]==null) continue; 

			if (points[i].getLatitude()<minLat){
				minLat=points[i].getLatitude(); 
			}
			if (points[i].getLongitude()<minLon){
				minLon=points[i].getLongitude(); 
			}
			if (points[i].getLatitude()>maxLat){
				maxLat=points[i].getLatitude(); 
			}
			if (points[i].getLongitude()>maxLon){
				maxLon=points[i].getLongitude(); 
			}

			index++; 

		}
		refLatLong=new LatLong(((maxLat-minLat)/2)+minLat, ((maxLon-minLon)/2)+minLon, 0); 
		System.out.println("Ref lat long: minLat "+ minLat+ " maxLat "+maxLat +  " minLon: "+minLon + " maxLon "+maxLon );
		return refLatLong;
	}

	
	/**
	 * Generate a surface from scatterred points
	 * @param points - an array of 3D scatterred points
	 * @param zExaggeration - the z exaggeration. Set to 1 fro no exaggeration
	 * @param aspectRatio - maintatin aspect ratio.
	 * @return the grid. 
	 */
	public static BathyData generateSurface(double[][] points, float zExaggeration, boolean aspectRatio){
		
		SurfaceData surfaceData = SurfaceUtils.generateSurface(points, zExaggeration);
		
		SibsonGridder2 simpleGridder2=surfaceData.grid; 
		double minX=surfaceData.minX; 
		double maxX=surfaceData.maxX; 
		double minY=surfaceData.minY; 
		double maxY=surfaceData.maxY; 
	
		//now create the grid. 
		int samplesCount=300; 
		double binx1=(maxX-minX)/samplesCount; 

		//System.out.println("x1: ");
		double[] samplesx1= new double[samplesCount];
		for (int i=0; i<samplesCount; i++){
			samplesx1[i]=minX+i*binx1; 
			//System.out.println(samplesx1[i]+ " ");
		}
		
		
		double[] samplesx2;
		double binx2;
		if (aspectRatio){
			samplesCount=(int) ((maxY-minY)/binx1); 
			samplesx2=new double[samplesCount];
			binx2=binx1;
		}
		else{
			binx2=(maxY-minY)/samplesCount; 
			samplesx2=new double[samplesCount];
		}
		
		//System.out.println("x2: ");
		for (int i=0; i<samplesCount; i++){
			samplesx2[i]=minY+i*binx2; 
		}
		
		BathyData bathyData=new BathyData(); 
		bathyData.setSamplesX(new Sampling(samplesx1));
		bathyData.setSamplesY(new Sampling(samplesx2));
		bathyData.setInterpSurface(simpleGridder2);
		bathyData.setGridXsize(binx1);
		bathyData.setGridYsize(binx2);
		//generate a surface
		bathyData.setInterpSurface(simpleGridder2.grid(bathyData.getSamplesX(), bathyData.getSamplesY()));
		
	
//		//TEMP print a surface
//		for (int i=0; i<surface.length; i++){
//			System.out.println(""); 
//			for (int j=0; j<surface[i].length; j++){
//				System.out.print(surface[i][j] + " "); 
//			}
//		}
//		//TEMP
		
		return bathyData; 
		
	}

	
//	/**
//	 * Generate a surface surface from a binch of scatterred points
//	 * @param points a 3D scatter of points. 
//	 */
//	public static float[][] generateSurface(double[][] points){
//		
//		//get data into the correct format and find min values. 
//		int skip=1000; 
//		Coordinate[] coords =new Coordinate[(int) Math.floor(points.length/skip)];
//		
//		double minX=Double.MAX_VALUE;  
//		double maxX=-Double.MAX_VALUE;
//		double minY=Double.MAX_VALUE;  
//		double maxY=-Double.MAX_VALUE;
//		
//		
//		int n=0;
//		for (int i=1; i<points.length; i=i+skip){
//			
//			if (points[i] == null || n>=((int) Math.floor(points.length/skip))) continue;
//			
//			coords[n]=new Coordinate();
//			//create co-ordinate
//			coords[n].x=points[i][2]; 
//			coords[n].y=points[i][1]; 
//			coords[n].z=points[i][0]; 
//			
//			//work out m in max
//			if (coords[n].x>maxX) maxX=coords[n].x; 
//			if (coords[n].x<minX) minX=coords[n].x; 
//			if (coords[n].y>maxY){
//				System.out.println(" maxY: "+maxY+ " "+ coords[n].y);
//				maxY=coords[n].y; 
//			}
//			if (coords[n].y<minY) minY=coords[n].y; 
//			
//			System.out.println("coords: x: " + coords[n].x+ " y: "+ coords[n].y +" z: "+ coords[n].z);
//			
//			n++; 
//			
//		}
//
//		BarnesSurfaceInterpolator interp= new BarnesSurfaceInterpolator(coords); 
//		
//		//now need to create an envelope.
//		Envelope envelope = new Envelope(); 
//		envelope.init(minX, maxX, minY, maxY);
//		
//		System.out.println("minX " + minX+ " maxX: "+ maxX +" z: "+ " minY: "+minY+ " maxY: "+maxY);
//
//			
//		//compute the surface. 
//		System.out.println("Computing surface start: " + coords.length);
//		float[][] surface=interp.computeSurface(envelope, 300, 300);
//		
//		System.out.println("Computing surface finsished");
//		
//		
//		//TEMP print a surface
//		for (int i=0; i<surface.length; i++){
//			System.out.println(""); 
//			for (int j=0; j<surface[i].length; j++){
//				System.out.print(surface[i][j] + " "); 
//			}
//		}
//		//TEMP
//		
//		return surface; 
//		
//	    
//	}

}
