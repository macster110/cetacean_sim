package reciever;

import java.util.ArrayList;

/**
 * Some default hydrophone positions. 
 * @author Jamie Macaulay 
 *
 */
public class DefaultHydrophoneArrays {
	
	/**
	 * Vertical array of PLABuoy in long configuration 2014 
	 */
	public static double[][] PLABuoyLong= new double[][]
	{	{0,0,-13.3100},
		{0,0,-17.7000},
		{0,0,-26.2200},
		{0,0,-30.3900}
	}; 
			
	
	/**
	 * Vertical array of PLABuoy in short configuration 2014 
	 */
	public static double[][] PLABuoyShort= new double[][]
	{	{0,0,-4.874},
		{0,0,-9.264},
		{0,0,-11.006},
		{0,0,-15.176}
	}; 		
	
	
	/**
	 * Vertical array of PLABuoy in short configuration 2014 
	 */
	public static double[][] VerticalArrayShort= new double[][]
	{	{0,0,-25.498},
		{0,0,-13.509},
		{0,0,-3.565},
		{0,0,-9.313},
		{0,0,-21.671},
		{0,0,-17.759},
	}; 		
	
	
	/**
	 * Vertical array of PLABuoy in short configuration 2014 
	 */
	public static double[][] VerticalArrayLong= new double[][]
	{	{0,0,-25.498},
		{0,0,-13.509},
		{0,0,-3.565},
		{0,0,-9.313},
		{0,0,-21.671},
		{0,0,-17.759},
		{0,0,-37.351},
		{0,0,-42.861}
	}; 		
	
	/**
	 * Vertical array of PLABuoy in short configuration 2014 
	 */
	public static double[][] VerticalArrayLongQuad= new double[][]
	{	{0,0,-25.498},
		{0,0,-13.509},
		{0,0,-3.565},
		{0,0,-9.313},
		{0,0,-21.671},
		{0,0,-17.759},
		{0,0,-37.351},
		{0,0,-42.861},
        {-0.056 +2.84 ,   -0.32725-8.108,   -3.141},
        {-0.3915 +2.84,   0.0125-8.108,    -2.643},     
        {-0.0565 +2.84,   0.326-8.108,     -3.141},  
        { 0.2065 +2.84,   0.0125-8.108,    -2.643}   
	}; 		
	
	/**
	 * The Meygen tidal turbine in the Pentalnd Firth. 
	 */
	public static double[][] meygenTurbines = new double[][] 
	{
		{3.54700000000000,	3.54700000000000,	-30.1220000000000},
		{3.50400000000000,	3.44200000000000,	-30.2190000000000},
		{3.60600000000000,	3.54500000000000,	-30.2590000000000},
		{3.46300000000000,	3.58500000000000,	-30.2390000000000},
		{3.54700000000000,	-3.54700000000000,	-30.1220000000000},
		{3.60600000000000,	-3.54500000000000,	-30.2590000000000},
		{3.50400000000000,	-3.44200000000000,	-30.2190000000000},
		{3.46300000000000,	-3.58500000000000,	-30.2390000000000},
		{-6.97500000000000,	0.0480000000000000,	-30.6900000000000},
		{-6.86900000000000,	0.0480000000000000,	-30.7320000000000},
		{-7.01300000000000,	0.0480000000000000	,-30.7320000000000},
		{-6.94100000000000,	-0.0822000000000000	,-30.7110000000000},
	}; 
	
	
	/**
	 * Get a list of default hydrophone arrays.
	 * @return a list of hydrophone arrays. 
	 */
	public static ArrayList<HydrophoneArray> defaultHydrophoneArrays (){
		 ArrayList<HydrophoneArray> hydrophonesArrays= new ArrayList<HydrophoneArray>();
		 hydrophonesArrays.add(new SimpleHydrophoneArray("PLABuoy Long", PLABuoyLong)); 
		 hydrophonesArrays.add(new SimpleHydrophoneArray("PLABuoy Short", PLABuoyShort)); 
		 hydrophonesArrays.add(new SimpleHydrophoneArray("Vertical Array Long", VerticalArrayLong)); 
		 hydrophonesArrays.add(new SimpleHydrophoneArray("Vertical Array Short", VerticalArrayShort)); 
		 hydrophonesArrays.add(new SimpleHydrophoneArray("Vertical Array Long Quad", VerticalArrayLongQuad)); 
		 hydrophonesArrays.add(new SimpleHydrophoneArray("Meygen Turbine", meygenTurbines)); 

		 return hydrophonesArrays; 
	}
	
}
