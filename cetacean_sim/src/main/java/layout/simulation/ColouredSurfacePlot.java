package layout.simulation;

import javafx.geometry.Point3D;
import javafx.geometry.Side;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;
import layout.utils.ColourArray;
import layout.utils.ColourArray.ColourArrayType;
import simulation.probdetsim.ProbDetMonteCarlo;
import layout.utils.SurfacePlot;
import layout.utils.Utils3D;
import utils.SurfaceData;
import utils.SurfaceUtils;

/**
 * Plots a 3D surface with a colour gradient. 
 * @author Jamie Macaulay
 *
 */
public class ColouredSurfacePlot extends SurfacePlot {

	private NumberAxis yAxis;
	private NumberAxis xAxis;
	private NumberAxis zAxis;
	
	
	ColourArray colourArray = ColourArray.createStandardColourArray(100, ColourArrayType.HOT);
	private Group axisGroup; 
	
	/**
	 * Create a coloured surface plot
	 */
	public ColouredSurfacePlot(float[][] Xq, float[][] Yq, float[][] Zq) {
		this(Xq, Yq, Zq, true); 
	}
		
	
	/**
	 * Create a coloured surface plot
	 */
	public ColouredSurfacePlot(float[][] Xq, float[][] Yq, float[][] ZqIn, boolean interp) {
		super(true);		
		System.out.println("Surface without interp: " + ZqIn.length +  " by " + ZqIn[0].length);
		createAxis();
		
		float[][] Zq=Utils3D.normalise(ZqIn);
		
		
		if (interp) {
			 float[][] interpZ =interpSurfaces(Xq, Yq, Zq);
			 System.out.println("Surface is interp: " + interpZ.length +  " by " + interpZ[0].length);
			 createSurface(interpZ); 	
			 setAxisPosition(interpZ.length, interpZ[0].length); 

		}
		else {
			createSurface(Zq); 
			setAxisPosition(Zq.length, Zq[0].length); 
		}
		
		
		
		setAxisValues(Xq, Yq); 
	}
	
	
	/**
	 * Interpolate the surface so it's a t least 1000 units across for 3D drawing. 
	 * @param Xq - the X surface 
	 * @param Yq - the Y surface 
	 * @param Zq - the Z surface 
	 * @param - true to interoplate the surface to be at least 1500 across.
	 */
	private static float[][] interpSurfaces(float[][] Xq, float[][] Yq, float[][] Zq) {
		
		//number of interp X bins
		double interpXn=500; 
		double interpYn=500; 
		
//		System.out.println("Xq"); 
//		ProbDetMonteCarlo.printResult(Xq); 
//		System.out.println("Yq"); 
//		ProbDetMonteCarlo.printResult(Yq); 
//		System.out.println("Zq"); 
//		ProbDetMonteCarlo.printResult(Zq); 


		double[][] points = new double[Xq.length*Xq[0].length][3]; 
		int n=0; 
		for (int i=0; i<Xq.length; i++) {
			for (int j=0; j<Xq[0].length; j++) {
				points[n][0]=Xq[i][j]; 
				points[n][1]=Yq[i][j]; 
				points[n][2]=Zq[i][j]; 
				//System.out.println(String.format("Point for interp: %.2f %.2f %.2f " , points[n][0] , points[n][1] , points[n][2])); 
				n++;
			} 
		}
		
		//create the surface interpolator. Exagerate by a bit.
		SurfaceData surfaceData =  SurfaceUtils.generateSurface(points, 200); 
		
		//now have interpolatedc surface we can find values on a much finer grid. 
		//first need to have much fioner surface. 
		float[] limsX=Utils3D.getMinMax(Xq);
		float[] limsY=Utils3D.getMinMax(Yq);
		
		double xbin=(limsX[1]-limsX[0])/interpXn; 
		double ybin=(limsY[1]-limsY[0])/interpYn; 
		
		float[][] interpZq= new float[(int) interpXn][(int) interpYn];

		//hello
		for (int i=0; i<interpXn; i++) {
			for (int j=0; j<interpYn; j++) {
				interpZq[i][j]=surfaceData.grid.interpolate((float) (limsX[0]+i*xbin), (float) (limsY[0]+j*ybin));
			}
		}
		
		
//		System.out.println("Zq"); 
//		ProbDetMonteCarlo.printResult(Zq);
//		System.out.println("Zq Interp"); 
//		ProbDetMonteCarlo.printResult(interpZq); 

		return interpZq; 
	}
	
	
	/**
	 * Set the axis names
	 */
	public void setAxisNames(String xAxisName, String yAxisName, String zAxisName) {
		xAxis.setLabel(xAxisName);
		yAxis.setLabel(yAxisName);
		zAxis.setLabel(zAxisName);
	}

	/**
	 * Create the number axis. 
	 */
	private void createAxis() {
		yAxis = new NumberAxis(); 
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(100);
		yAxis.setTickUnit(50);
		yAxis.setLabel("y (m)");
		yAxis.setAutoRanging(false);
		yAxis.setSide(Side.LEFT);

		xAxis = new NumberAxis(); 
		xAxis.setLowerBound(0);
		xAxis.setUpperBound(100);
		xAxis.setSide(Side.BOTTOM);
		xAxis.setTickUnit(50);
		xAxis.setLabel("x(m)");
		xAxis.setAutoRanging(false);
		
		
		zAxis = new NumberAxis(); 
		zAxis.setLowerBound(0);
		zAxis.setUpperBound(100);
		zAxis.setSide(Side.BOTTOM);
		zAxis.setLabel("z(m)");
		zAxis.setAutoRanging(false);
		zAxis.getTransforms().add(new Rotate(90, new Point3D(0,1,0))); 
		zAxis.getTransforms().add(new Rotate(0, new Point3D(1,0,0))); 

		//need to have z axis outside the same group as the x axis and the y axis as 
		//there seems to be java bug that stops the x and y axis from renderring properly 
		axisGroup = new Group(); 
		axisGroup.getChildren().addAll(xAxis, yAxis); 
		axisGroup.setCache(true);
		axisGroup.setCacheHint(CacheHint.SCALE_AND_ROTATE);

		this.getChildren().addAll(axisGroup, zAxis);
	}
	
	
	/***
	 * Set axis values for the surface. 
	 */
	private void setAxisValues(float[][] Xq, float[][] Yq) {
		
		float[] limsX=Utils3D.getMinMax(Xq);
		xAxis.setLowerBound(limsX[0]);
		xAxis.setUpperBound(limsX[1]);
		
		float[] limsY=Utils3D.getMinMax(Yq);
		yAxis.setLowerBound(limsY[0]);
		yAxis.setUpperBound(limsY[1]);
		
		System.out.println(String.format("xmin: %.2f xmax: %.2f ymin: %.2f ymax: %.2f", limsX[0], limsX[1], limsY[0], limsY[1]));

		yAxis.layout();
		xAxis.layout();
		
		zAxis.setLowerBound(0);
		zAxis.setUpperBound(1);
		zAxis.setTickUnit(0.1);


	}
	
	/**
	 * Set the position of the axis. 
	 */
	private void setAxisPosition(double xwidth, double ywidth) {
		
		xAxis.layoutXProperty().bind(yAxis.widthProperty());
		xAxis.setLayoutY(ywidth);
		xAxis.setPrefWidth(xwidth);
		
		//yAxis.layoutXProperty().bind(yAxis.widthProperty().add(xwidth/2).multiply(-1));
//		yAxis.setLayoutX(-xwidth/2-100);
//		yAxis.setLayoutY(-ywidth/2);
		yAxis.setPrefHeight(ywidth);


		axisGroup.layoutXProperty().bind(yAxis.widthProperty().multiply(-1).subtract(xwidth/2));
		axisGroup.setLayoutY(-ywidth/2);

		zAxis.setLayoutX(-xwidth/2);
		zAxis.setLayoutY(ywidth/2);

	}
	
	/**
	 * Create the surface.
	 * @param Xq - the x axis data in mesh grid format
	 * @param Yq - the y axis data in mesh grid format
	 * @param Zq - the surface data. 
	 */
	private void createSurface(float[][] Zq) {
		//create a coloured image of the data. 
		Image diffuseMap = createImage(Zq); 
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(diffuseMap);
        material.setSpecularColor(Color.WHITE);
		setHeightData(Zq, 1,  material, true, true);  

		
	}
	
	/**
     * Create texture for UV mapping
     * @param size
     * @param data
     * @return
     */
    public Image createImage(float[][] data) {

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

        WritableImage wr = new WritableImage(width, height);
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                float value = data[x][y];

                double gray = normalizeValue(value, minValue, maxValue, 0., 1.);

                //gray = clamp(gray, 0, 1);

               // Color color = Color.YELLOWGREEN.interpolate(Color.DODGERBLUE, gray);
                
                color=colourArray.getColour(gray);

                pw.setColor(x, y, color);
            }
        }
        return wr;
    }
    
//    
//    /**
//     * Create the axis for the 
//     * @param Xq
//     * @param Yq
//     * @return
//     */
//    public Group createAxis(float[][] Xq, float[][] Yq) {
//    	
////    	
////        Box xAxis = new Box(Xq.length, width, width);
////        Box yAxis = new Box(width, length, width);
////        Box zAxis = new Box(width, width, length);
//    	
//    }
    
    
    private static double normalizeValue(double value, double min, double max, double newMin, double newMax) {

        return (value - min) * (newMax - newMin) / (max - min) + newMin;

    }

    private static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }   
    
    
    /**
     * Create an array of the given size with values of perlin noise for testing the data.
     * @param size - the size of the noise
     * @return the noise surface. 
     */
    public static float[][] createTestData( int size, double amplification) {
        float[][] noiseArray = new float[(int) size][(int) size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                double frequency = 10.0 / (double) size;

                double noise = ImprovedNoise.noise(x * frequency, y * frequency, 0);

                noiseArray[x][y] =(float) (noise*amplification);
            }
        }

        return noiseArray;

    }
    
    
    /**
     * Perlin noise generator, Used to test the surface 
     * 
     * // JAVA REFERENCE IMPLEMENTATION OF IMPROVED NOISE - COPYRIGHT 2002 KEN PERLIN.
     * // http://mrl.nyu.edu/~perlin/paper445.pdf
     * // http://mrl.nyu.edu/~perlin/noise/
     */
    public final static class ImprovedNoise {
    static public double noise(double x, double y, double z) {
       int X = (int)Math.floor(x) & 255,                  // FIND UNIT CUBE THAT
           Y = (int)Math.floor(y) & 255,                  // CONTAINS POINT.
           Z = (int)Math.floor(z) & 255;
       x -= Math.floor(x);                                // FIND RELATIVE X,Y,Z
       y -= Math.floor(y);                                // OF POINT IN CUBE.
       z -= Math.floor(z);
       double u = fade(x),                                // COMPUTE FADE CURVES
              v = fade(y),                                // FOR EACH OF X,Y,Z.
              w = fade(z);
       int A = p[X  ]+Y, AA = p[A]+Z, AB = p[A+1]+Z,      // HASH COORDINATES OF
           B = p[X+1]+Y, BA = p[B]+Z, BB = p[B+1]+Z;      // THE 8 CUBE CORNERS,

       return lerp(w, lerp(v, lerp(u, grad(p[AA  ], x  , y  , z   ),  // AND ADD
                                      grad(p[BA  ], x-1, y  , z   )), // BLENDED
                              lerp(u, grad(p[AB  ], x  , y-1, z   ),  // RESULTS
                                      grad(p[BB  ], x-1, y-1, z   ))),// FROM  8
                      lerp(v, lerp(u, grad(p[AA+1], x  , y  , z-1 ),  // CORNERS
                                      grad(p[BA+1], x-1, y  , z-1 )), // OF CUBE
                              lerp(u, grad(p[AB+1], x  , y-1, z-1 ),
                                      grad(p[BB+1], x-1, y-1, z-1 ))));
    }
    static double fade(double t) { return t * t * t * (t * (t * 6 - 15) + 10); }
    static double lerp(double t, double a, double b) { return a + t * (b - a); }
    static double grad(int hash, double x, double y, double z) {
       int h = hash & 15;                      // CONVERT LO 4 BITS OF HASH CODE
       double u = h<8 ? x : y,                 // INTO 12 GRADIENT DIRECTIONS.
              v = h<4 ? y : h==12||h==14 ? x : z;
       return ((h&1) == 0 ? u : -u) + ((h&2) == 0 ? v : -v);
    }
    static final int p[] = new int[512], permutation[] = { 151,160,137,91,90,15,
    131,13,201,95,96,53,194,233,7,225,140,36,103,30,69,142,8,99,37,240,21,10,23,
    190, 6,148,247,120,234,75,0,26,197,62,94,252,219,203,117,35,11,32,57,177,33,
    88,237,149,56,87,174,20,125,136,171,168, 68,175,74,165,71,134,139,48,27,166,
    77,146,158,231,83,111,229,122,60,211,133,230,220,105,92,41,55,46,245,40,244,
    102,143,54, 65,25,63,161, 1,216,80,73,209,76,132,187,208, 89,18,169,200,196,
    135,130,116,188,159,86,164,100,109,198,173,186, 3,64,52,217,226,250,124,123,
    5,202,38,147,118,126,255,82,85,212,207,206,59,227,47,16,58,17,182,189,28,42,
    223,183,170,213,119,248,152, 2,44,154,163, 70,221,153,101,155,167, 43,172,9,
    129,22,39,253, 19,98,108,110,79,113,224,232,178,185, 112,104,218,246,97,228,
    251,34,242,193,238,210,144,12,191,179,162,241, 81,51,145,235,249,14,239,107,
    49,192,214, 31,181,199,106,157,184, 84,204,176,115,121,50,45,127, 4,150,254,
    138,236,205,93,222,114,67,29,24,72,243,141,128,195,78,66,215,61,156,180
    };
    static { for (int i=0; i < 256 ; i++) p[256+i] = p[i] = permutation[i]; }
    }


}
