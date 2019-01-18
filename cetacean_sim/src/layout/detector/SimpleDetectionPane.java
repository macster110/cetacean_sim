package layout.detector;

import animal.SimpleOdontocete;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import detector.Detector;
import detector.SimpleDetector;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.CetSimView;
import layout.CustomSimPane;
import layout.SimVarLineChart;
import layout.SimVariablePane;
import layout.utils.SettingsPane;
import simulation.CustomSimVar;

/**
 * 
 * Pane with settings for simple detector. 
 * 
 * @author Jamie Macaulay
 *
 */
public class SimpleDetectionPane extends BorderPane implements SettingsPane<Detector>{
	
	/**
	 * Custom simulation pane. 
	 */
	private CustomSimPane customSimPane;
	
	/**
	 * Perfect detector box. 
	 */
	private CheckBox perfectDetectorBox;

	/**
	 * The distirbution line chart
	 */
	private SimVarLineChart lineChart;

	//private SimVariablePane simVariablePane; 
	
	private Dialog<SimpleOdontocete> chartDialog;
	
	
	public SimpleDetectionPane() {
		this.setCenter(createSimpleDetectionPane());
	}
	
	/**
	 * Create the simple detection pane. 
	 * @return the detection pane. 
	 */
	private Pane createSimpleDetectionPane(){
		
		Label propogationLabel = new Label("Detection Efficiency"); 
		propogationLabel.setFont(new Font(CetSimView.titleFontSize));

		VBox mainPane = new VBox(); 
		mainPane.setSpacing(5);
		
		perfectDetectorBox = new CheckBox("Perfect Detector"); 
		perfectDetectorBox.setOnAction((action)->{
			enableSimVar(!perfectDetectorBox.isSelected()); 
		});
		
		Label customLabel = new Label("Detection Efficiency versus SNR"); 
		customSimPane = new CustomSimPane();
		
		//button for showing chart
		Button graphButton = new Button(); 
		graphButton.setGraphic(GlyphsDude.createIcon(FontAwesomeIcon.LINE_CHART, "18"));
		graphButton.setOnAction((action)->{
			//open graph to show distribution
			this.openDistGraph();
		});

		
		HBox customPaneBox = new HBox();
		customPaneBox.setSpacing(5);
		customPaneBox.getChildren().addAll(customSimPane.getPane(), graphButton);
	
	
		mainPane.getChildren().addAll(propogationLabel, perfectDetectorBox, customLabel, customPaneBox);
		
		return mainPane;
	}
	
	/**
	 * Opens a dialog with a chart showing the distribution 
	 */
	private void openDistGraph() {


		if (lineChart==null) {
			lineChart  = createDistributionGraph(); 
		}
		
		if (chartDialog==null) {
			chartDialog = new Dialog<>();
			chartDialog.setTitle("Detection Efficiency Chart");
			DialogPane dPane = new DialogPane(); 
			dPane.getStylesheets().add("resources/darktheme.css");
			dPane.setContent(lineChart  = createDistributionGraph());

			dPane.setPrefSize(600, 400);
			chartDialog.setDialogPane(dPane);
			ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
			chartDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			chartDialog.setResizable(true);
		}
		
		double[][] data = lineChart.createCustomDataSeries((CustomSimVar) customSimPane.getSimVariable());
		lineChart.updateChartSeries(data);;

		chartDialog.show();
	}


	private SimVarLineChart createDistributionGraph() {
		return new SimVarLineChart(customSimPane.getSimVariable());
	}


	/**
	 * Enable or disable the sim variable pane.
	 * @param enable - true to enable
	 */
	private void enableSimVar(boolean enable) {
		customSimPane.getPane().setDisable(!enable);
	}

	@Override
	public Detector getParams() {
		
		SimpleDetector detector = new SimpleDetector(); 
		
		detector.setDetectorDistribution(customSimPane.getSimVariable()); 
		detector.setPerfectDetector(perfectDetectorBox.isSelected()); 
		
		return detector;
	}

	@Override
	public void setParams(Detector settingsData, boolean clone) {
		SimpleDetector simpleDetector = (SimpleDetector) settingsData; 
	
		System.out.println("Set params: " + simpleDetector.getDetectoDistribution() 
		+ " MIN: " + simpleDetector.getDetectoDistribution().getMin()+ " MAX: " + simpleDetector.getDetectoDistribution().getMax()); 
		
		customSimPane.setSimVariable(simpleDetector.getDetectoDistribution());
		
		//customSimPane.setSimVariable(simpleDetector.getDetectoDistribution());
		perfectDetectorBox.setSelected(simpleDetector.isPerfectDetector());
		
	}

	@Override
	public String getName() {
		return "Detector Performance Pane";
	}

	@Override
	public Node getContentNode() {
		return this;
	}

	@Override
	public void paneInitialized() {
		// TODO Auto-generated method stub
		
	}

}
