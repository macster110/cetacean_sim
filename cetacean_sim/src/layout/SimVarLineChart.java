package layout;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import simulation.CustomSimVar;
import simulation.SimVariable;
import utils.CetSimUtils;

/**
 * Line chart to show a distributions
 * @author Jamie Macaulay 
 *
 */
public class SimVarLineChart extends BorderPane{

	/**
	 * The sim variable which is being plotted. 
	 */
	private SimVariable simVariable;

	private int nSamples=50000; 
	
	private LineChart<Number,Number> lineChart; 
	
	private XYChart.Series<Number, Number> series;

	private NumberAxis xAxis;

	private NumberAxis yAxis; 

	/**
	 * Sim variable line chart. 
	 */
	public SimVarLineChart(SimVariable simVariable) {
		this.simVariable=simVariable; 
		this.setCenter(createChart()); 
		this.getStylesheets().add("resources/darktheme.css");
	}
	
	public void updateChartSeries(double[][] dataVals) {
		//populating the series with data
		series = new XYChart.Series<Number, Number>();
		series.setName("Probability Distribution");
		lineChart.getData().clear();
		
		for (int i=0; i<dataVals[0].length; i++) {
			//System.out.println("Data Values are: x: " + dataVals[i][0] + " y: "+  dataVals[i][1]);
			series.getData().add(new XYChart.Data(dataVals[0][i], dataVals[1][i]));
		}
		
		lineChart.getData().add(series);
		
		double[] minMax = CetSimUtils.getMinAndMax(dataVals[0]); 
		
		double inset= Math.abs((minMax[1]-minMax[0])*0.2);
		xAxis.setLowerBound(minMax[0]-inset);
		xAxis.setUpperBound(minMax[1]+inset);
	}

	/**
	 * Create the data series. 
	 * @return
	 */
	private Node createChart() {
		//defining the axes
		xAxis = new NumberAxis();
		 yAxis = new NumberAxis();
		xAxis.setLabel(simVariable.getName());
		yAxis.setLabel("Probability (Normalised)");
		//creating the chart
		lineChart = 
				new LineChart<Number,Number>(xAxis,yAxis);

		lineChart.setTitle(SimVariable.getSimVarName(simVariable.getType()) + " Probability Distribution");
	    lineChart.setCreateSymbols(false); //hide dots
	    
		//defining a series
		series = new XYChart.Series<Number, Number>();
		series.setName("Probability Distribution");
		
		//update the chart series
		double[][] dataVals = createDistirbutionDataSeries(simVariable); 
		updateChartSeries(dataVals); 
		
		// set limits
		yAxis.setAutoRanging(true);
		
		xAxis.setAutoRanging(true);		


		return lineChart;
	}


	int nBins=25;
	
	
	/**
	 * Create the data series from a custom sim variable based on the pre-defined
	 * probability data point in the variable .
	 */
	public double[][] createCustomDataSeries(CustomSimVar simVariable) {
		int nSamples = 1000; 
		double binSize = (simVariable.getMax()-simVariable.getMin())/(double) nSamples;
		double[][] graphData=new double[2][nSamples]; 
		for (int i=0; i<nSamples; i++) {
			graphData[0][i]=simVariable.getMin()+i*binSize;
			//System.out.println("graphData[0][i] " + graphData[0][i] + " binSiz: " + binSize);
			graphData[1][i]=simVariable.getProbability(graphData[0][i]); 
		}
		return graphData; 
	}
	
	
	/**
	 * Create the data series from the sim-variable. A bit processor intensive
	 * but means this code can be kept generic. 
	 */
	private double[][] createDistirbutionDataSeries(SimVariable simVariable) {
		//samples 
		double[] samples=new double[nSamples]; 
		for (int i=0; i<nSamples; i++) {
			samples[i]=simVariable.getNextRandom(); 
			//System.out.println(samples[i]);
		} 

		//now bin the data.
		double[] minMax = CetSimUtils.getMinAndMax(samples); 

		double min=minMax[0];
		double max=minMax[1];
		double binSize=(max-min)/(double) nBins; 

		max=min+binSize; 
		double[][] graphData=new double[2][nBins]; 
		for (int i=0; i<nBins; i++) {
			graphData[0][i]=min+binSize/2;
			for (int j=0; j<samples.length; j++) {
				if (samples[j]>=min && samples[j]<max) {
					graphData[1][i]++;
				}
			}
			min=max; 
			max=min+binSize;
		}

		//normalise.  
		minMax = CetSimUtils.getMinAndMax(graphData[1]);
		for (int i=0; i<nBins; i++) {
			graphData[1][i]=graphData[1][i]/minMax[1]; 
		}

		return graphData;
	}


}
