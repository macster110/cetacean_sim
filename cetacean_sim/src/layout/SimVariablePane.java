package layout;

import java.util.ArrayList;

import animal.SimpleOdontocete;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.simulation.ProbDetSettingsPane;
import simulation.LogNormalSimVar;
import simulation.NormalSimVariable;
import simulation.RandomSimVariable;
import simulation.SimVariable;
import simulation.SimVariable.DistributionType;


/** 
 * 
 * Pane win which a simulation variable distribution can be selected and then the values
 *  for that distribution set. 
 *  
 * @author Jamie Macaulay 
 *
 */
public class SimVariablePane extends BorderPane {

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
	 * Selects what type of varibale. 
	 */
	private ChoiceBox<String> simChoiceBox; 

	/**
	 * Results convert. 
	 */
	private ResultConverter resultConverter = new ResultConverter();

	/**
	 * The units. 
	 */
	private String units;

	/**
	 * Text field for entering minimum limits
	 */
	private Spinner<Double> minLim;

	/**
	 * Indicates whether to show limit controls.
	 */
	private boolean showLims;

	/**
	 * Text field for entering maximum limits
	 */
	private Spinner<Double> maxLim;

	/**
	 * The name of limits
	 */
	private String limName;

	/**
	 * The unit of the limits. 
	 */
	private String limUnits; 


	/**
	 * Constructor for sim varibale pane
	 * @param name - the name of the variable 
	 * @param units - the units. 
	 * @param simVariablePanes -
	 */
	public SimVariablePane(String name,  String units, SimTypePane... simVariablePanes) {
		this.name = name;
		this.units=units; 
		for (int i=0; i<simVariablePanes.length; i++) {
			simVarTypePane.add(simVariablePanes[i]);
		}
		this.setCenter(createPane(DistributionType.CUSTOM));
	}

	/**
	 * 
	 * @param name
	 */
	public SimVariablePane(String name,  String units) {
		this.name = name;
		this.units=units; 
		simVarTypePane.add(new UniformSimPane(0, 10));
		simVarTypePane.add(new NormalSimPane());
		this.setCenter(createPane(DistributionType.NORMAL));
	}

	/**
	 * Create sim variable pane which shows limit controls. 
	 * @param name - the name of the sim variable
	 * @param units- the units of the sim variable
	 * @param LimName - the name of the limits units. 
	 * @param limUnits - the limit units. 
	 */
	public SimVariablePane(String name,  String units, String limName, String limUnits) {
		this.name = name;
		this.units=units; 
		this.limName=limName; 
		this.limUnits=limUnits; 
		this.showLims=true; 
		simVarTypePane.add(new UniformSimPane(0, 10));
		simVarTypePane.add(new NormalSimPane());
		simVarTypePane.add(new LogNormalSimPane());
		this.setCenter(createPane(DistributionType.NORMAL));
	}


	/**
	 * Create a sim variable pane with some default values. Does not show limit controls 
	 * @param name
	 * @param type
	 * @param min
	 * @param max
	 * @param mean
	 * @param std
	 */
	public SimVariablePane(String name, String units, DistributionType type, double min, double max, double mean, double std) {
		this.name = name;
		this.units=units; 
		simVarTypePane.add(new UniformSimPane(min, max));
		simVarTypePane.add(new NormalSimPane());
		simVarTypePane.add(new LogNormalSimPane());
		simVarTypePane.add(new CustomSimPane());
		this.setCenter(createPane(type)); 
	}


	/**
	 * Get sim limits from the pane. 
	 * @return the limits of the sim. 
	 */
	private double[] getSimLimits() {
		if (!this.showLims) {
			return null; 
		}
		else {
			return new double[] {
					this.minLim.getValue(),
					this.maxLim.getValue(),
			}; 
		}
	}

	/**
	 * Create the pane. 
	 * @param - the distribution to start with. 
	 */
	private Pane createPane(DistributionType type) {

		GridPane holder = new GridPane(); 
		holder.setHgap(5);
		holder.setVgap(5);

		int row=0; 

		if (name!=null) {
			Label label = new Label(name); 
			label.setFont(new Font(CetSimView.titleFontSize));
			holder.add(label,0,row);
			row++; 
		}

		HBox hBox= new HBox(); 
		hBox.setSpacing(5);

		BorderPane simHolder= new BorderPane(); 

		simChoiceBox = new ChoiceBox<String>();
		for (int i=0; i<simVarTypePane.size(); i++) {			
			simChoiceBox.getItems().add(simVarTypePane.get(i).getSimVarType().toString()); 
		}

		simChoiceBox.setOnAction((action)->{
			currentSimVarIndex=simChoiceBox.getSelectionModel().getSelectedIndex(); 
			simHolder.setCenter(simVarTypePane.get(currentSimVarIndex).getPane());
		});
		simChoiceBox.getSelectionModel().select(getDistTypeIndex(type));

		// add all children 
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.getChildren().addAll(simHolder, new Label(units)); 

		//button for showing chart
		Button graphButton = new Button(); 
		graphButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.LINE_CHART, "18"));
		graphButton.setOnAction((action)->{
			//open graph to show distribution
			this.openDistGraph();
		});


		//create limits controls- these may not be added if showLims is false. 
		HBox limHBox = new HBox(); 
		limHBox.setAlignment(Pos.CENTER_LEFT);
		limHBox.setSpacing(5);

		//create section of the pane to input limits. 
		minLim= new Spinner<Double>(-10000000.,10000000.,0.,5.); 
		minLim.setEditable(true);
		ProbDetSettingsPane.styleSpinner(minLim);
		minLim.setPrefWidth(80);

		maxLim= new Spinner<Double>(-10000000.,10000000.,0.,5.); 
		maxLim.setEditable(true);
		ProbDetSettingsPane.styleSpinner(maxLim);
		maxLim.setPrefWidth(80);

		limHBox.getChildren().addAll(new Label(this.limName), new Label("Min"), minLim, 
				new Label("Max"), maxLim, new Label(this.limUnits));

		//add everything together 
		int column = 0;
		if (simChoiceBox.getItems().size()>1) {
			holder.add(simChoiceBox, 0, row);
		}
		holder.add(hBox, ++column, row);
		GridPane.setHgrow(hBox, Priority.ALWAYS);
		//GridPane.setRowSpan(hBox, 2);
		holder.add(graphButton, ++column, row);
		row++; 

		if (showLims) {
			holder.add(limHBox, 0,row);
			GridPane.setColumnSpan(limHBox, 3);
			row++; 
		}

		//holder.setMaxWidth(350);

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
	 * Get the sim variable.
	 * @return the sim variable. 
	 */
	public SimVariable getSimVariable() {
		return simVarTypePane.get(currentSimVarIndex).getSimVariable(); 
	}


	/**
	 *
	 * @param simVar
	 */
	public void setSimVariable(SimVariable simVar) {

		//find which type of var to set. 
//		DistributionType distType = simVar.getType(); 

		//System.out.println(simVar.getName() + " " + simVar.getType());

//		int index=0; 
//		switch (distType) {
//		case LOGNORMAL:
//			index=2; 
//			break;
//		case NORMAL:
//			index=1; 
//			break;
//		case UNIFORM:
//			index=0; 
//			break;
//		case CUSTOM:
//			index=3;
//			break;
//		default:
//			index=0; 
//			break;
//		}
		//System.out.println("NamE: " + simVar.getName() + " " + simVar.getType() +  " index: "  + index); 

		simChoiceBox.getSelectionModel().select(simVar.getType().toString());		
		
		simVarTypePane.get(simChoiceBox.getSelectionModel().getSelectedIndex()).setSimVariable(simVar); 

		if (simVar.getLimits()!=null && this.showLims) {
			//set limits
			this.minLim.getValueFactory().setValue(simVar.getLimits()[0]);
			this.maxLim.getValueFactory().setValue(simVar.getLimits()[1]);
		}

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
		 * Set params 
		 * @param simVar
		 */
		public void setSimVariable(SimVariable simVar);

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

			minSpinner= new Spinner<Double>(-50000000.,50000000.,min, 5.); 
			minSpinner.setEditable(true);
			ProbDetSettingsPane.styleSpinner(minSpinner);

			maxSpinner= new Spinner<Double>(-50000000.,50000000.,max,5.); 
			maxSpinner.setEditable(true);
			ProbDetSettingsPane.styleSpinner(maxSpinner);

			mainPane.getChildren().addAll(new Label("Min"), minSpinner, new Label("Max"), maxSpinner); 

			return mainPane; 
		}

		@Override
		public Region getPane() {
			if (randSimPane==null) randSimPane = createPane();
			return randSimPane;
		}

		@Override
		public SimVariable getSimVariable() {
			min=resultConverter.convert2Value(minSpinner.getValue()); 
			max=resultConverter.convert2Value(maxSpinner.getValue()); 

			RandomSimVariable randSimVariable  = new RandomSimVariable(name, min, max); 
			randSimVariable.setLimits(getSimLimits());

			return randSimVariable;
		}

		@Override
		public void setSimVariable(SimVariable simVar) {
			
			//System.out.println("Min value for: " +simVar.getName() + "  " + minSpinner);
			min = ((RandomSimVariable) simVar).getMin(); 
			max = ((RandomSimVariable) simVar).getMax(); 

			minSpinner.getValueFactory().setValue(resultConverter.convert2Control(min));
			maxSpinner.getValueFactory().setValue(resultConverter.convert2Control(max));

		}

	}

	/**
	 * Normal distribution sim pane. 
	 * @author Jamie Macaulay 
	 *
	 */
	public class NormalSimPane extends  AbstractSimPane {

		@Override
		public SimVariable getSimVariable() {
			double var1=resultConverter.convert2Value(meanSpinner.getValue()); 
			double var2=resultConverter.convert2Value(stdSpinner.getValue()); 

			NormalSimVariable normalVariable  = new NormalSimVariable(name, var1, var2); 
			normalVariable.setLimits(getSimLimits());

			return normalVariable;
		}

		@Override
		public void setSimVariable(SimVariable simVar) {
			double var1 = ((NormalSimVariable) simVar).getMean(); 
			double var2 = ((NormalSimVariable) simVar).getStd(); 

			meanSpinner.getValueFactory().setValue(resultConverter.convert2Control(var1));
			stdSpinner.getValueFactory().setValue(resultConverter.convert2Control(var2));
		}

		@Override
		public String var1Name() {
			return "Mean";
		}

		@Override
		public String var2Name() {
			return "Std";
		}

	}


	/**
	 * LogNormal distribution sim pane. 
	 * @author Jamie Macaulay 
	 *
	 */
	public class LogNormalSimPane extends  AbstractSimPane {

		private Spinner<Double> truncationSpinner;

		private CheckBox flipNegative; 


		public LogNormalSimPane() {
			super(); 
		}

		@Override
		public DistributionType getSimVarType() {
			return DistributionType.LOGNORMAL;
		}
		
		@Override
		public SimVariable getSimVariable() {

			double var1=resultConverter.convert2Value(meanSpinner.getValue()); 
			double var2=resultConverter.convert2Value(stdSpinner.getValue()); 
			double truncation=resultConverter.convert2Value(truncationSpinner.getValue());
			boolean neg = flipNegative.isSelected();

			LogNormalSimVar logNormalVar  = new LogNormalSimVar(name, var1, var2, truncation, neg); 
			logNormalVar.setLimits(getSimLimits());

			return logNormalVar;
		}

		@Override
		public void setSimVariable(SimVariable simVar) {

			double var1 = ((LogNormalSimVar) simVar).getScale(); 
			double var2 = ((LogNormalSimVar) simVar).getShape(); 
			double truncation = ((LogNormalSimVar) simVar).getTruncation(); 
			boolean neg = ((LogNormalSimVar) simVar).isNegative();

			meanSpinner.getValueFactory().setValue(resultConverter.convert2Control(var1));
			stdSpinner.getValueFactory().setValue(resultConverter.convert2Control(var2));
			truncationSpinner.getValueFactory().setValue(resultConverter.convert2Control(truncation));
			flipNegative.setSelected(neg);
		}

		@Override
		protected Pane createNormalPane() {
			Pane pane = super.createNormalPane(); 

			//set defaults to more standard normal values. 
			meanSpinner.getValueFactory().setValue(2.);
			((DoubleSpinnerValueFactory) meanSpinner.getValueFactory()).setAmountToStepBy(0.2);
			stdSpinner.getValueFactory().setValue(1.5);

			//set this
			stdSpinner.getValueFactory().setValue(3.);
			((DoubleSpinnerValueFactory) stdSpinner.getValueFactory()).setAmountToStepBy(0.2);

			//create the truncation spinner. 
			truncationSpinner= new Spinner<Double>(0.,50000000.,150,5.); 
			truncationSpinner.setEditable(true);
			ProbDetSettingsPane.styleSpinner(truncationSpinner);

			HBox truncBox= new HBox();
			truncBox.setSpacing(5);
			truncBox.getChildren().addAll(new Label("Truncation"), truncationSpinner, this.flipNegative= new CheckBox("Negative")); 
			truncBox.setAlignment(Pos.CENTER_LEFT);


			VBox vBox = new VBox();
			vBox.setSpacing(5); 
			vBox.getChildren().addAll(pane, truncBox);

			return vBox;
		}

		@Override
		public String var1Name() {
			return "scale";
		}

		@Override
		public String var2Name() {
			return "shape";
		}

	}


	/**
	 * Opens a dialog with a chart showing the distribution 
	 */
	private void openDistGraph() {
		Dialog<SimpleOdontocete> chartDialog = new Dialog<>();

		chartDialog.setTitle("Distribution Chart");
		DialogPane dPane = new DialogPane(); 
		dPane.getStylesheets().add("resources/darktheme.css");

		dPane.setContent(createDistributionGraph());
		dPane.setPrefSize(600, 400);
		chartDialog.setDialogPane(dPane);

		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
		chartDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
		chartDialog.setResizable(true);

		chartDialog.show();
	}


	private Node createDistributionGraph() {
		return new SimVarLineChart(this.getSimVariable());
	}

	/**
	 * @return the resultConverter
	 */
	public ResultConverter getResultConverter() {
		return resultConverter;
	}


	/**
	 * @param resultConverter the resultConverter to set
	 */
	public void setResultConverter(ResultConverter resultConverter) {
		this.resultConverter = resultConverter;
	}

} 