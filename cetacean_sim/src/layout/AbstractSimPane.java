package layout;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import layout.SimVariablePane.SimTypePane;
import layout.simulation.ProbDetSettingsPane;
import simulation.SimVariable.DistributionType;

/**
 * Pane which holds a distribution with two variables. 
 * @author Jamie Macaulay 
 *
 */
public abstract class AbstractSimPane implements SimTypePane {

	/**
	 * Spinenr for mean value.
	 */
	protected Spinner<Double> meanSpinner;

	/**
	 * Spinner for standard deviation of distribution. 
	 */
	protected Spinner<Double> stdSpinner;


	/**
	 * The pane. 
	 */
	protected Pane normalPane;
	

	@Override
	public String getSimVarName() {
		return "Normal";
	}

	@Override
	public DistributionType getSimVarType() {
		return DistributionType.NORMAL;
	} 

	protected Pane createNormalPane() {
		HBox mainPane = new HBox(); 
		mainPane.setSpacing(5);
		mainPane.setAlignment(Pos.CENTER);

		meanSpinner= new Spinner<Double>(-5000000.,50000000.,0,5.); 
		meanSpinner.setEditable(true);
		ProbDetSettingsPane.styleSpinner(meanSpinner);

		stdSpinner= new Spinner<Double>(0.,50000000.,20,5.); 
		stdSpinner.setEditable(true);
		ProbDetSettingsPane.styleSpinner(stdSpinner);

		mainPane.getChildren().addAll(new Label(var1Name()), meanSpinner, new Label(var2Name()), stdSpinner); 

		return mainPane; 
	}
	
	/**
	 * The name of the second variable in the distribution e.g mean
	 * @return the string name of the second variable. 
	 */
	public abstract String var1Name(); 
	
	/**
	 * The name of the second variable in the distribution e.g std
	 * @return the string name of the second variable. 
	 */
	public abstract String var2Name(); 
	

	@Override
	public Region getPane() {
		if (normalPane==null) normalPane=createNormalPane(); 
		return  normalPane; 
	}

//	@Override
//	public SimVariable getSimVariable() {
//		mean=getResultsConverter().convert2Value(meanSpinner.getValue()); 
//		std=getResultsConverter().convert2Value(stdSpinner.getValue()); 
//
//		NormalSimVariable normalVariable  = new NormalSimVariable(name, mean, std); 
//		normalVariable.setLimits(getSimLimits());
//
//		return normalVariable;
//	}

//	@Override
//	public void setSimVariable(SimVariable simVar) {
//		mean = ((NormalSimVariable) simVar).getMean(); 
//		std = ((NormalSimVariable) simVar).getStd(); 
//
//		meanSpinner.getValueFactory().setValue(getResultsConverter().convert2Control(mean));
//		stdSpinner.getValueFactory().setValue(getResultsConverter().convert2Control(std));
//	}
}