package bathymetry;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import utils.BarnesSurfaceInterpolator;

public class BathymetryUtils {
	
	
	/**
	 * Generate a surface surface from a binch of scatterred points
	 * @param points a 3D scatter of points. 
	 */
	public static float[][] generateSurface(double[][] points){
		
		//get data into the correct format and find min values. 
		Coordinate[] coords =new Coordinate[points.length];
		
		double minX=Double.MAX_VALUE;  
		double maxX=Double.MIN_VALUE;
		double minY=Double.MAX_VALUE;  
		double maxY=Double.MIN_VALUE;
		
		for (int i=0; i<coords.length; i++){
			if (coords[i].x>maxX) maxX=coords[i].x; 
			if (coords[i].x<minX) minX=coords[i].x; 
			if (coords[i].y>maxY) maxY=coords[i].y; 
			if (coords[i].y<minY) minY=coords[i].y; 
				
			coords[i].x=points[i][0]; 
			coords[i].y=points[i][1]; 
			coords[i].z=points[i][2]; 
		}

		BarnesSurfaceInterpolator interp= new BarnesSurfaceInterpolator(coords); 
		
		//now need to create an envelope.
		Envelope envelope = new Envelope(); 
		envelope.init(minX, maxX, minY, maxY);
			
		//compute the surface. 
		float[][] surface=interp.computeSurface(envelope, 300, 300);
		
		return surface; 
		
	    
	}

}
