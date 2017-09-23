package utils;

/**
 * Holds a 3D histogram of data. 
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
	 * Constructor for results
	 * @param simResults
	 * @param xbinEdges - the edges of the x bins
	 * @param ybinEdges - the edges of the y bins
	 * */
	public Hist3(double[][] simResults, double[] xbinEdges, double[] ybinEdges) {
		this.xbinEdges=xbinEdges; 
		this.ybinEdges=ybinEdges; 
		this.histogram=generateHist3D(simResults,xbinEdges,ybinEdges, null);
	}
	
	/**
	 * Constructor for results
	 * @param simResults
	 * @param xbinEdges - the edges of the x bins
	 * @param ybinEdges - the edges of the y bins
	 * @param findValue - if not null the hsitgram calulates the percentage of findValue's in each hist bin. 
	 * */
	public Hist3(double[][] simResults, double[] xbinEdges, double[] ybinEdges, Double findValue) {
		this.xbinEdges=xbinEdges; 
		this.ybinEdges=ybinEdges; 
		this.histogram=generateHist3D(simResults,xbinEdges,ybinEdges, null);
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
	 * Generate a 3D histogram data. 
	 * @param simResults - 3D data
	 * @param xbinEdges - edges of the x bins
	 * @param ybinEdges - edges of the y bins. 
	 */
	private double[][] generateHist3D(double[][] simResults, double[] xbinEdges, double[] ybinEdges, Double findValue) {
		double[][] histogram = new double[xbinEdges.length-1][ybinEdges.length-1]; 
		
		int histcount=0; 
		int histvalue = 0; 
		for (int i=0; i<xbinEdges.length-1; i++) {
			for (int j=0; j<ybinEdges.length-1; j++) {
				histcount=0;
				for (int n=0; n<simResults.length; n++) {
					if (simResults[n][0]>xbinEdges[i] && simResults[n][0]<=xbinEdges[i+1] &&
							simResults[n][1]>ybinEdges[i] && simResults[n][1]<=ybinEdges[i+1])
						histcount++;
						if (findValue!=null && findValue.doubleValue()==simResults[i][2]) {
							histvalue++; 
						}
				}
				
				if (findValue==null) histogram[i][j]=histcount; //just standard histogram 
				else  histogram[i][j]=histvalue/histcount; //the percentage of values which equal findValue;
			}
		}
		return histogram; 
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
		double[] binEdges = new double[nBins]; 
		for (int i=0; i<nBins; i++) {
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


	

		
	

}