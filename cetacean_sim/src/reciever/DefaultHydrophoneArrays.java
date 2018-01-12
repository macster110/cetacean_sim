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
		 return hydrophonesArrays; 
	}
	
}
