package utils;

import java.util.ArrayList;

import simulation.probdettrack.ProbDetTrack.RecievedInfo;

/**
 * Holds a 3D histogram of data. 
 * 
 * @author Jamie Macaulay
 *
 */
public class Hist3 {

	/**
	 * Edges of the ybins
	 */
	private double[] ybinEdges;

	/**
	 * Edges of the xbins. 
	 */
	private double[] xbinEdges;

	/**
	 * The 3D histogram. 
	 */
	private double[][] histogram;

	/**
	 * The total number of data points added to the histogram
	 */
	private long totalcount;


	/**
	 * Constructor for a blank histogram. 
	 * @param xbinEdges
	 * @param ybinEdges
	 */
	public Hist3(double[] xbinEdges, double[] ybinEdges) {
		this.xbinEdges=xbinEdges; 
		this.ybinEdges=ybinEdges; 
	}


	/**
	 * Constructor for results
	 * @param simResults - the simulation results - x, y and detection status e.g. 0 or 1 for binary detection. 
	 * @param xbinEdges - the edges of the x bins
	 * @param ybinEdges - the edges of the y bins
	 * */
	public Hist3(ArrayList<RecievedInfo> recResults, double[] xbinEdges, double[] ybinEdges, double threshold) {
		this.xbinEdges=xbinEdges; 
		this.ybinEdges=ybinEdges; 

		//need to get the data into the standard format. 
		double[][] simResults = recievedInfo2HistData(recResults, threshold); 

		this.histogram=addToHist(simResults, null);
	}

	/**
	 * Constructor for results
	 * @param simResults - the simulation results - x, y and detection status e.g. 0 or 1 for binary detection. 
	 * @param xbinEdges - the edges of the x bins
	 * @param ybinEdges - the edges of the y bins
	 * @param findValue - if not null the histogram calculates the percentage of findValue's in each hist bin. 
	 * */
	public Hist3(double[][] simResults, double[] xbinEdges, double[] ybinEdges, Double findValue) {
		this.xbinEdges=xbinEdges; 
		this.ybinEdges=ybinEdges; 
		this.histogram=addToHist(simResults, findValue);
	}


	/**
	 * Constructs a Hist3 object with pre-defined histogram 
	 * @param xbinEdges - the edges of the x bins
	 * @param ybinEdges - the edges of the y bins
	 * @param hist - the histogram surface corresponding to y and x bin edges. 
	 */
	public Hist3(double[] xbinEdges, double[] yBinEdges, double[][] averageHist) {
		this.xbinEdges=xbinEdges; 
		this.ybinEdges=yBinEdges; 
		this.histogram=averageHist; 
	}


	/**
	 * Constructor for results
	 * @param simResults - the simulation results - x, y and detection status e.g. 0 or 1 for binary detection. 
	 * @param xbinEdges - the edges of the x bins
	 * @param ybinEdges - the edges of the y bins
	 * */
	public Hist3(double[][] simResults, double[] xbinEdges, double[] ybinEdges) {
		this.xbinEdges=xbinEdges; 
		this.ybinEdges=ybinEdges; 
		this.histogram=addToHist(simResults, null);
	}

	/**
	 * Generate a 3D histogram data. Either simply bins the data into a 2D histogram. If findValue is not null then
	 * will find all points which are within 2D histogram and then results for which the third column is equals
	 * findValue are found and returned as the percentage of total number of result sin that bin. 
	 * 
	 * @param simResults - 3D data
	 * @param xbinEdges - edges of the x bins
	 * @param ybinEdges - edges of the y bins. 
	 * @param findValue - the value to find. 
	 */
	@Deprecated
	private double[][] addToHist(double[][] simResults, double[] xbinEdges, double[] ybinEdges, Double findValue) {
		return addToHist(simResults,  findValue);
	}

	/**
	 * Generate a 3D histogram data. Either simply bins the data into a 2D histogram. If findValue is not null then
	 * will find all points which are within 2D histogram and then results for which the third column is equals
	 * findValue are found and returned as the percentage of total number of result sin that bin. 
	 * 
	 * @param simResults - 3D data
	 * @param findValue - the value to find. 
	 */
	public double[][] addToHist(double[][] simResults, Double findValue) {

		if (histogram==null) {
			histogram = new double[xbinEdges.length-1][ybinEdges.length-1]; 
			totalcount = 0; 
		}
		
		int histcount=0; 
		int histvalue = 0; 
		for (int i=0; i<xbinEdges.length-1; i++) {
			for (int j=0; j<ybinEdges.length-1; j++) {
				histcount=0;
				histvalue=0; 
				for (int n=0; n<simResults.length; n++) {
					//					if (n%1000==0) {
					//						System.out.println("Histograming: " + simResults[n][0] + " "+   simResults[n][1] + " " + simResults[n][2] + " " +(findValue!=null && findValue.doubleValue()==simResults[n][2]));
					//					}
//					if (n==218) {
//						System.out.println("Sim n: " + simResults[n][0]  + "  " + simResults[n][1] + "  " + simResults[n][2]+ "  " + xbinEdges[i]); 
//					}
					if (simResults[n][0]>=xbinEdges[i] && simResults[n][0]<xbinEdges[i+1] &&
							simResults[n][1]>ybinEdges[j] && simResults[n][1]<=ybinEdges[j+1]) {
						histcount++;
						if (findValue!=null && findValue.doubleValue()==simResults[n][2]) {
							//System.out.println("Add to hist: " + n + " " + simResults[n][1] + " Edge 1: " + ybinEdges[j] + " Edge 2: " + ybinEdges[j+1]); 
							histvalue++; 
						}
					}
				}
//				System.out.println("Hist: " + i + " "+ j + " value n: " + histvalue + " total n: " + histcount + " checking for ranges between: " 
//				+ xbinEdges[i] + " to " + xbinEdges[i+1] + " and depths from " + ybinEdges[j] + " to " + ybinEdges[j+1]);
				if (findValue==null) {
					histogram[i][j]= histogram[i][j]+histcount; //just standard histogram
					
				}
				else  {
					//TODO
					//if (histcount==0) histogram[i][j]=0; 
					//Note: Do not divide this by the bin number because this negates the depth distribution. i.e. dividing a small
					//number of sim results with a small number of sim attempts makes a much bigger number. Must divide 
					//by simResults.length to maintain fair comparison across bins. 
					
					//recover the total number of N rather than percentage then add new value and covert back to percentage - a little messy but meh... 
					histogram[i][j]=(histogram[i][j]*totalcount + histvalue)/ (totalcount+ simResults.length); //the percentage of values which equal findValue;
				}
			}
		}
		
		totalcount= totalcount + simResults.length; 

		return histogram; 
	}

	/**
	 * Generate a 3D histogram data. Either simply bins the data into a 2D histogram. If findValue is not null then
	 * will find all points which are within 2D histogram and then results for which the third column is equals
	 * findValue are found and returned as the percentage of total number of result sin that bin. 
	 * 
	 * @param simResults - 3D data
	 * @param findValue - the value to find. 
	 */
	public double[][] addToHist(ArrayList<RecievedInfo> recResults, Double findValue, double threshold) {

		//need to get the data into the standard format. 
		double[][] simResults = recievedInfo2HistData(recResults, threshold); 

		return this.histogram=addToHist(simResults, findValue);
	}





	/**
	 * Generate the bin edges for a histogram 
	 * @param min - the minimum values 
	 * @param max - the maximum values 
	 * @param nBins - the number of bins. 
	 * @return - the 
	 */
	public static double[] binEdges(double min, double max, int nBins) {
		double binSize=(max-min)/nBins;
		double[] binEdges = new double[nBins+1]; 
		for (int i=0; i<nBins+1; i++) {
			binEdges[i]=min+i*binSize;
		}
		return binEdges;
	}

	/**
	 * Get the y bin edges.
	 * @return the y edges. 
	 */
	public double[] getYbinEdges() {
		return ybinEdges;
	}

	/**
	 * Get the x bin edges. 
	 * @return the x 
	 */
	public double[] getXbinEdges() {
		return xbinEdges;
	}

	/**
	 * The histogram surface. This is either the number of measurements in each bin or the 
	 * %percentage of a certain value within each bin. This depends on which constructor 
	 * was used.  
	 * @return the histogram surface. 
	 */
	public double[][] getHistogram() {
		return histogram;
	}

	/**
	 * Creatre an X or Y surface for plotting the histogram as a surface. The X and y surfaces are the 
	 * center of each histogram bin
	 * @param xbins - the x bins 
	 * @param ybins - the y bins
	 * @param x - true for x grid, false for y grid.
	 * @return the grid used for drawing surfaces. 
	 */
	public static float[][] getXYSurface(double[] xbins, double[] ybins, boolean x){
		return getXYSurface(xbins, ybins, x, true);
	}


	/**
	 * Creatre an X or Y surface for plotting the histogram as a surface. The X and y surfaces are the 
	 * center of each histogram bin
	 * @param xbins - the x bins 
	 * @param ybins - the y bins
	 * @param x - true for x grid, false for y grid.
	 * @param binEdges - true if the inputsd are grid edges. Otherwise false
	 * @return the grid used for drawing surfaces. 
	 */
	public static float[][] getXYSurface(double[] xbins, double[] ybins, boolean x, boolean binEdges){
		float[][] surface = new float[xbins.length-1][ybins.length-1]; 

		double xbinsSize=xbins[1]-xbins[0]; 
		double ybinsSize=ybins[1]-ybins[0]; 

		for (int i=0; i<xbins.length-1; i++) {
			for (int j=0; j<ybins.length-1; j++) {
				if (x) {
					surface[i][j]=(float) (xbins[i]+xbinsSize/2); 
				}
				else {
					surface[i][j]=(float) (ybins[j]+ybinsSize/2); 
				}
			}
		}
		return surface; 
	}


	/**
	 * Convert an array of RecievedInfo objects into histogram data based on a threshold. 
	 * @param recResults - the RecievedInfo array. 
	 * @param threshold - the threshold in dB. A detection occurs if the recieved level is above threshold. 
	 * @return a [distance, height, binary value] array. 
	 */
	public static double[][] recievedInfo2HistData(ArrayList<RecievedInfo> recResults, double threshold) {
		//need to get the data into the standard format. 
		double[][] simResults = new double[recResults.size()][3]; 

		//double distance2D; 
		for (int i=0; i<recResults.size(); i++) {
			//require 2D distance. 
			//distance2D = Math.sqrt(Math.pow(recResults.get(i).distance, 2) - Math.pow(recResults.get(i).height,2)); 
			simResults[i][0] = recResults.get(i).distance; //2D distance
			simResults[i][1] = recResults.get(i).height; //depth of animal

			if (recResults.get(i).recievedLevel>=threshold) {
				simResults[i][2] = 1.; 
				//System.out.println(String.format("Passed threshold? %d  received level %.1f threshold: %.1f height: %.1f recieved? %.0f", i  , recResults.get(i).recievedLevel ,threshold, recResults.get(i).height, simResults[i][2])); 
			}
			else {
				simResults[i][2] = 0.; 
			}
			

		}

		return simResults; 
	}
	

	/**
	 * Get the total number of data points added to the histogram
	 * @return the total number of points
	 */
	public long getTotalcount() {
		return totalcount;
	}



}
