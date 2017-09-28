package simulation.probdetsim;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import layout.simulation.ProbDetSettingsPane;
import simulation.probdetsim.ProbDetMonteCarlo.ProbDetResult;

/**
 * Runs a simualtion with lots of different noise values
 * @author Jamie Macaulay 
 *
 */
public class NoiseSimType implements ProbDetSimType {
	
	/**
	 * Thye noise settings
	 */
	private Pane noiseSettings;

	/**
	 * The prob det sim
	 */
	private ProbDetSim probDetSim; 
	
	public NoiseSimType(ProbDetSim probDetSim) {
		this.probDetSim=probDetSim;
	}
	
	/**
	 * The minimum noise to simulate
	 */
	public double minNoise=90.;
	
	/**~
	 * The maximum noise to simulate
	 */
	public double maxNoise=150.;
	
	/**
	 * The noise bin to simulate. 
	 */
	public double noiseBin=1.;

	/**
	 * Min noise spinner
	 */
	private Spinner<Double> minNoiseSpinner;

	/**
	 * The noise bin spinner
	 */
	private Spinner<Double> noiseBinSpinner;

	/**
	 * The max noise spinner. 
	 */
	private Spinner<Double> maxNoiseSpinner;


	@Override
	public String getName() {
		return "Noise Variation";
	}

	@Override
	public Region getSettingsNode() {
		if (noiseSettings==null) {
			noiseSettings=  createNoisePane(); 
		}
		return noiseSettings;
	}

	@Override
	public ArrayList<ProbDetResult> runSimulation(ProbDetSimSettings probDetSettings) {
	 getParams(); 
		 
		double[] noiseValues = createNoiseArray(); 
		
		ArrayList<ProbDetResult> probDetResult = new ArrayList<ProbDetResult>(); 
		
		for (int i=0; i<noiseValues.length; i++) {
			
			final int ii=i; 
			Platform.runLater(()->{
				probDetSim.getProbDetSimView().setCustomProgressText(String.format("Noise value: %.2f dB re 1uPa" , noiseValues[ii]));  
			});
		
			System.out.println("Calculating for values for noise " + noiseValues[i]); 

			probDetSettings.noiseThreshold=noiseValues[i];
			
			probDetSim.getMonteCarloSim().run(probDetSettings);
			
			if (probDetSim.getMonteCarloSim().isCancelled()) return null; 
			
			probDetResult.add(probDetSim.getMonteCarloSim().getResult()); 
		}
		
		return probDetResult;
	}
	
	/**
	 * Create the noise array 
	 * @return the noise array 
	 */
	public double[] createNoiseArray() {
		int bin = (int) Math.ceil((maxNoise-minNoise)/noiseBin);
	
		double[] noiseArray = new double[bin]; 
				
		for (int i=0; i<bin; i++) {
			noiseArray[i]=minNoise+i*noiseBin;
		}
		
		return noiseArray; 
	}
	
	/**
	 * Get the params.
	 */
	public void getParams() {
		this.maxNoise=maxNoiseSpinner.getValue(); 
		this.minNoise=minNoiseSpinner.getValue(); 
		this.noiseBin=noiseBinSpinner.getValue(); 
	}
	
	/**
	 * Create the pane for changing the noise settings. 
	 * @return the pane for changing the noise settings. 
	 */
	private Pane  createNoisePane() {
		
		GridPane mainPane =new GridPane(); 
		mainPane.setHgap(5);
		mainPane.setVgap(5);
		
		mainPane.setPadding(new Insets(5,0,5,0));

		int row=0; 
		
		mainPane.add(new Label("Noise: min"), 0, row);

		minNoiseSpinner = new Spinner<Double>(0.,300.,85.,1.); 
		mainPane.add(minNoiseSpinner, 1, row);
		ProbDetSettingsPane.styleSpinner(minNoiseSpinner);

		mainPane.add(new Label("bin"), 2, row);
		noiseBinSpinner= new Spinner<Double>(0.0001,300.,1.,0.5); 
		ProbDetSettingsPane.styleSpinner(noiseBinSpinner);
		mainPane.add(noiseBinSpinner, 3, row);

		mainPane.add(new Label("max"), 4, row);
		maxNoiseSpinner = new Spinner<Double>(0.,300,150.,1.); 
		ProbDetSettingsPane.styleSpinner(maxNoiseSpinner);
		mainPane.add(maxNoiseSpinner, 5, row);
		
		return mainPane; 
	}

	@Override
	public void simTypeSelected() {
		// TODO Auto-generated method stub
		
	}

}
