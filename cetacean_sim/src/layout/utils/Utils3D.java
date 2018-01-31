package layout.utils;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

/**
 * Useful classes for simulations. 
 * @author Jamie Macaulay
 *
 */
public class Utils3D {
	
	
	public static Group buildAxes(double axisSize, Color colour, String textx, String texty, String textz,
			Color textColour) {
		return buildAxes(axisSize,  colour,  colour,
				colour,  colour,
				 colour,  colour,
				 textColour, textx, texty, textz);
	}
	
	public static Group buildAxes(double axisSize, Color xAxisDiffuse, Color xAxisSpectacular,
			Color yAxisDiffuse, Color yAxisSpectacular,
			Color zAxisDiffuse, Color zAxisSpectacular,
			Color textColour) {
		return buildAxes(axisSize,  xAxisDiffuse,  xAxisSpectacular,
				 yAxisDiffuse,  yAxisSpectacular,
				 zAxisDiffuse,  zAxisSpectacular,
				 textColour, "x", "y", "z");
	}
	
	/**
	 * Convert and array of the doubles to an array of floats
	 * @param array - the double array 
	 * @return float array with equivalent values 
	 */
	public static float[][] double2float(double[][] array) {
		
		if (array==null || array.length==0) return null; 
		
		float[][] floatArray = new float[array.length][array[0].length];
		
		for (int i=0; i<array.length; i++) {
			for (int j=0; j<array[0].length; j++) {
				floatArray[i][j]=(float) array[i][j]; 
			}
		}
		
		return floatArray; 
	}
	
	
	/**
	 * Convert and array of the doubles to an array of floats
	 * @param array - the double array 
	 * @return float array with equivalent values 
	 */
	public static double[][] float2double(float[][] array) {
		
		if (array==null || array.length==0) return null; 
		
		double[][] doubleArray = new double[array.length][array[0].length];
		
		for (int i=0; i<array.length; i++) {
			for (int j=0; j<array[0].length; j++) {
				doubleArray[i][j]=(double) array[i][j]; 
			}
		}
		
		return doubleArray; 
	}
	
	
	/**
	 * Get the minimum and maximum of a surface. 
	 * @param data - a surface 
	 * @return the minimum and maximum values. 
	 */
	public static float[] getMinMax(float[][] data) {      

		int width = (int) data.length;
		int height = (int) data[0].length; 

		float minValue= Float.MAX_VALUE;
		float maxValue = -Float.MAX_VALUE;


		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if ( data[x][y]<minValue) {
					minValue=data[x][y]; 
				}
				if ( data[x][y]>maxValue) {
					maxValue=data[x][y]; 
				}
			}
		}

		float[] minMax= new float[2];
		minMax[0]=minValue;
		minMax[1]=maxValue;
		return minMax; 
	}

	/**
	 * Create a 3D axis. 
	 * @param- size of the axis
	 */
	public static Group buildAxes(double axisSize, Color xAxisDiffuse, Color xAxisSpectacular,
			Color yAxisDiffuse, Color yAxisSpectacular,
			Color zAxisDiffuse, Color zAxisSpectacular,
			Color textColour, String textx, String texty, String textz) {
		Group axisGroup=new Group(); 
        double length = 2d*axisSize;
        double width = axisSize/100d;
        double radius = 2d*axisSize/100d;
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(xAxisDiffuse);
        redMaterial.setSpecularColor(xAxisSpectacular);
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(yAxisDiffuse);
        greenMaterial.setSpecularColor( yAxisSpectacular);
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(zAxisDiffuse);
        blueMaterial.setSpecularColor(zAxisSpectacular);
        
        Text xText=new Text(textx); 
        xText.setStyle("-fx-font: 20px Tahoma;");
        xText.setFill(textColour);
        xText.setCache(true);
        Text yText=new Text(texty); 
        yText.setStyle("-fx-font: 20px Tahoma; ");
        yText.setFill(textColour);
        yText.setCache(true);
        Text zText=new Text(textz); 
        zText.setStyle("-fx-font: 20px Tahoma; ");
        zText.setCache(true);
        zText.setFill(textColour);

        xText.setTranslateX(axisSize*1.1);
        yText.setTranslateY((axisSize*1.1));
        zText.setTranslateZ((axisSize*1.1));
        zText.getTransforms().add(new Rotate(90, new Point3D(0,1,0)));
        
        Sphere xSphere = new Sphere(radius);
        Sphere ySphere = new Sphere(radius);
        Sphere zSphere = new Sphere(radius);
        xSphere.setMaterial(redMaterial);
        ySphere.setMaterial(greenMaterial);
        zSphere.setMaterial(blueMaterial);
         
        xSphere.setTranslateX(axisSize);
        ySphere.setTranslateY(axisSize);
        zSphere.setTranslateZ(axisSize);
         
        Box xAxis = new Box(length, width, width);
        Box yAxis = new Box(width, length, width);
        Box zAxis = new Box(width, width, length);
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
         
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.getChildren().addAll(xText, yText, zText);
        axisGroup.getChildren().addAll(xSphere, ySphere, zSphere);
        return axisGroup;
    }

	/**
	 * Normalise an array so the maximum value is 1. 
	 * @param zqIn
	 * @return
	 */
	public static float[][] normalise(float[][] zqIn) {
		float[] minMax= getMinMax(zqIn);
		
		float[][] zqOut= new float[zqIn.length][zqIn[0].length]; 
		for (int i=0; i<zqIn.length; i++) {
			for (int j=0; j<zqIn[0].length; j++) {
				zqOut[i][j]=zqIn[i][j]/minMax[1]; 
			}
		}
		return zqOut;
	}

}
