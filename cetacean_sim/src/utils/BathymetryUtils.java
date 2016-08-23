package utils;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.interp.SibsonInterpolator2;
import edu.mines.jtk.interp.SimpleGridder2;
import edu.mines.jtk.interp.SplinesGridder2;

public class BathymetryUtils {
	
	
	/**
	 * Generate a bathymetry surface from  scatterred LatLong POINTS. The grid is returned in the cartesian Co-Ordinate frame; 
	 * @param points
	 * @param zExaggeration
	 * @return
	 */
	public static float[][] generateSurface(LatLong[] points, float zExaggeration, boolean aspectRatio){
		
		double[][] array= latLong2Cart(points);

		float[][] surface= generateSurface(array,  zExaggeration,  aspectRatio);
		
		return surface;
	}
	
	
	public static double[][] latLong2Cart(LatLong[] points){
		return latLong2Cart(points, null);
	}
	
	
	public static double[][] latLong2Cart(LatLong[] points, LatLong ref){
		LatLong refLatLong;
		if (ref==null){
			//use min lat as the reference point. 
			double minLat=Double.MAX_VALUE;  
			int index=0; 
			for (int i=0; i<points.length; i++){
				if (points[i].getLatitude()<minLat){
					minLat=points[i].getLatitude(); 
					index=i; 
				}
			}

			refLatLong=points[index].clone();
		}
		else refLatLong= ref.clone();

		double[][] array = new double[points.length][3];

		//now convert to cartesian
		double x; 
		double y;
		for (int i=0; i<points.length; i++){
			x=points[i].distanceToMetresX(refLatLong);
			y=points[i].distanceToMetresY(refLatLong);
			array[i][0]=x;
			array[i][1]=y;
			array[i][2]=points[i].getHeight();

		}
		return array;
	}

	
	/**
	 * Generate a surface from scatterred points
	 * @param points - an array of 3D scatterred points
	 * @param zExaggeration - the z exaggeration. Set to 1 fro no exaggeration
	 * @param aspectRatio - maintatin aspect ratio.
	 * @return the grid. 
	 */
	public static float[][] generateSurface(double[][] points, float zExaggeration, boolean aspectRatio){
		
		//get data into the correct format and find min values. 
		float[] f =new float[(int) Math.floor(points.length)];
		float[] x1 =new float[(int) Math.floor(points.length)];
		float[] x2 =new float[(int) Math.floor(points.length)];

		double minX=Double.MAX_VALUE;  
		double maxX=-Double.MAX_VALUE;
		double minY=Double.MAX_VALUE;  
		double maxY=-Double.MAX_VALUE;
		
		int n=0;
		for (int i=1; i<points.length; i++){
			
			//System.out.println(" i: "+ i+ " points.length: "+points.length);

			
			if (points[i] == null || n>=((int) Math.floor(points.length))) continue;
			
			//create co-ordinate
			x1[n]=(float) points[i][2]; 
			x2[n]=(float) points[i][1]; 
			f[n]=(float) -points[i][0]*zExaggeration; 
			if (f[n]==0.) f[n]=(float) Math.random();
			
			//work out m in max
			if (x1[n]>maxX) maxX=x1[n]; 
			if (x1[n]<minX) minX=x1[n]; 
			if (x2[n]>maxY)maxY=x2[n]; 
			if (x2[n]<minY) minY=x2[n]; 
			
			n++;
			
		}
		
		
		SimpleGridder2 simpleGridder2= new SimpleGridder2(f, x1,  x2);
	
		//now create the grid. 
		int samplesCount=500; 
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
		
		System.out.println("Computing surface start: " + f.length+ " " + samplesx2.length +  " "+samplesx1.length);

		float[][] surface=simpleGridder2.grid(new Sampling(samplesx1), new Sampling(samplesx2));
		
		System.out.println("Computing surface finsished");
		
		
//		//TEMP print a surface
//		for (int i=0; i<surface.length; i++){
//			System.out.println(""); 
//			for (int j=0; j<surface[i].length; j++){
//				System.out.print(surface[i][j] + " "); 
//			}
//		}
//		//TEMP
		
		return surface; 
		
	    
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
