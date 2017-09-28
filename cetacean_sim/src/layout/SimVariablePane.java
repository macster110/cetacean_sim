package layout;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import layout.simulation.ProbDetSettingsPane;
import simulation.NormalSimVariable;
import simulation.RandomSimVariable;
import simulation.SimVariable;


/** 
 * 
 * Pane win which a simulation varibale distribution can be selected and then the values fvor that distribution set. 
 * @author Jamie Macaulay 
 *
 */
public class SimVariablePane extends BorderPane {
	
	public enum DistributionType {UNIFORM, NORMAL};
	
	/**
	 * The list of availbale sim varible panes. 
	 */
	public ArrayList<SimTypePane> simVarTypePane = new ArrayList<SimTypePane>();
	
	/**
	 * The 
	 */
	public int currentSimVarIndex=0;
	
	/**
	 * The name of the variable 
	 */
	private String name; 
	
	/**
	 * 
	 * @param name
	 */
	public SimVariablePane(String name) {
		this.name = name;
		this.setCenter(createPane(DistributionType.NORMAL));
	}
	
	/**
	 * 
	 * @param name
	 * @param type
	 * @param min
	 * @param max
	 * @param mean
	 * @param std
	 */
	public SimVariablePane(String name, DistributionType type, double min, double max, double mean, double std) {
		this.name = name;
		simVarTypePane.add(new UniformSimPane(min, max));
		simVarTypePane.add(new NormalSimPane(mean, std));
		this.setCenter(createPane(type)); 
	}
	
	
	/**
	 * Create the pane. 
	 * @param - the distirbution to start with. 
	 */
	private Pane createPane(DistributionType type) {
		
		VBox holder = new VBox(); 
		holder.setSpacing(5);
		
		Label label = new Label(name); 
		holder.getChildren().add(label); 
		
		HBox hBox= new HBox(); 
		hBox.setSpacing(5);
		
		BorderPane simHolder= new BorderPane(); 
		
		ChoiceBox<String> cb = new ChoiceBox<String>();
		for (int i=0; i<DistributionType.values().length; i++) {
			cb.getItems().add(getSimVarName(DistributionType.values()[i]));
		}
		
		cb.setOnAction((action)->{
			currentSimVarIndex=cb.getSelectionModel().getSelectedIndex(); 
			simHolder.setCenter(simVarTypePane.get(currentSimVarIndex).getPane());
		});
		
		cb.getSelectionModel().select(getDistTypeIndex(type));
		
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(cb, simHolder); 
		
		holder.getChildren().add(hBox);
		
		return holder; 
	}
	
	/**
	 * Get the distribution type. 
	 * @param distType - the type. 
	 * @return the distribution type. 
	 */
	private int getDistTypeIndex(DistributionType distType) {
		for (int i=0; i<simVarTypePane.size(); i++) {
			if (simVarTypePane.get(i).getSimVarType()==distType) return i; 
		}
		return -1; 
	}

	
	/**
	 * Get the name for the varibale type. 
	 * @param distType  - the distribution type
	 * @return the string name of the distirbution type. 
	 */
	public String getSimVarName(DistributionType distType) {
		String name =""; 
		switch (distType) {
		case NORMAL:
			name="Normal"; 
			break;
		case UNIFORM:
			name="Uniform"; 
			break;
		default:
			break;
		}
		return name;  
	}
	
			
	/**
	 * Get the sim variable.
	 * @return the sim variable. 
	 */
	public SimVariable getSimVariable() {
		return simVarTypePane.get(currentSimVarIndex).getSimVariable(); 
	}

	
	/**
	 * Pane which is specific to the type of variable 
	 * @author Jamie Macaulay 
	 *
	 */
	public interface SimTypePane {
		
		/**
		 * The name of the varibale type  
		 * @return
		 */
		public String getSimVarName(); 
		
		/**
		 * The enum representation of the varibale 
		 * @return the enum type for this SimVariable Pane
		 */
		public DistributionType getSimVarType(); 
		
		/**~
		 * Get the pane to change settings for this type of distribution
		 * @return the region to set variable params
		 */
		public Region getPane(); 
		
		/**
		 * Get the sim variable
		 * @return the sim varibale. 
		 */
		public SimVariable getSimVariable();
		
	}
	
	/**
	 * Pane for uniform sim varibale. i.e. a uniform random distribution
	 * @author Jamie Macaulay 
	 *
	 */
	public class UniformSimPane implements SimTypePane {
		
		private double min; 
		
		private double max;
		
		private Spinner<Double> minSpinner; 
		
		private Spinner<Double> maxSpinner; 
		
		private Pane randSimPane;

		public UniformSimPane(double min, double max) {
			this.min=min;
			this.max=max;
		}
		
		@Override
		public String getSimVarName() {
			return "Uniform";
		}

		
		@Override
		public DistributionType getSimVarType() {
			return DistributionType.UNIFORM;
		}
		
		/**
		 * Create pane for a uniform random variable 
		 * @return pane with controls for changing min and max; 
		 */
		private Pane createPane() {
			HBox mainPane = new HBox(); 
			mainPane.setAlignment(Pos.CENTER);

			mainPane.setSpacing(5);
			
			minSpinner= new Spinner<Double>(0.,50000000.,min, 5.); 
			minSpinner.setEditable(true);
			ProbDetSettingsPane.styleSpinner(minSpinner);
			
			maxSpinner= new Spinner<Double>(0.,50000000.,max,5.); 
			maxSpinner.setEditable(true);
			ProbDetSettingsPane.styleSpinner(maxSpinner);

			mainPane.getChildren().addAll(new Label("min"), minSpinner, new Label("max"), maxSpinner); 
			
			return mainPane; 
		}

		@Override
		public Region getPane() {
			if (randSimPane==null) randSimPane = createPane();
			return randSimPane;
		}

		@Override
		public SimVariable getSimVariable() {
			min=minSpinner.getValue(); 
			max=maxSpinner.getValue(); 

			RandomSimVariable randSimVariable  = new RandomSimVariable(name, min, max); 

			return randSimVariable;
		}
		
	}
	
	/**
	 * Normal distribution sim pane. 
	 * @author Jamie Macaulay 
	 *
	 */
	public class NormalSimPane implements SimTypePane {

		/**
		 * Spinenr for mean value.
		 */
		private Spinner<Double> meanSpinner;
		
		/**
		 * Spinner for standard deviation of distribution. 
		 */
		private Spinner<Double> stdSpinner;
		
		/**
		 * The mean level
		 */
		private double mean=0; 
		
		/**
		 * The standard deviation
		 */
		private double std=20; 
		
		/**
		 * The pane. 
		 */
		private Pane normalPane; 
		
		public NormalSimPane(double mean, double std) {
			this.mean=mean; 
			this.std=std; 
		}

		@Override
		public String getSimVarName() {
			return "Normal";
		}

		@Override
		public DistributionType getSimVarType() {
			return DistributionType.NORMAL;
		}
		
		private Pane createNormalPane() {
			HBox mainPane = new HBox(); 
			mainPane.setSpacing(5);
			mainPane.setAlignment(Pos.CENTER);

			meanSpinner= new Spinner<Double>(0.,50000000.,mean,5.); 
			meanSpinner.setEditable(true);
			ProbDetSettingsPane.styleSpinner(meanSpinner);

			stdSpinner= new Spinner<Double>(0.,50000000.,std,5.); 
			stdSpinner.setEditable(true);
			ProbDetSettingsPane.styleSpinner(stdSpinner);

			mainPane.getChildren().addAll(new Label("mean"), meanSpinner, new Label("std"), stdSpinner); 
			
			return mainPane; 
		}

		@Override
		public Region getPane() {
			if (normalPane==null) normalPane=createNormalPane(); 
			return  normalPane; 
		}

		@Override
		public SimVariable getSimVariable() {
			mean=meanSpinner.getValue(); 
			std=stdSpinner.getValue(); 

			NormalSimVariable randSimVariable  = new NormalSimVariable(name, mean, std); 

			return randSimVariable;
		}
		
	}
	
} 