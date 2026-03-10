package layout.propogation;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import layout.CetSimView;
import layout.simulation.ProbDetSettingsPane;
import layout.utils.SettingsPane;
import propogation.Propogation;
import propogation.SimplePropogation;

/**
 * Pane with controls for simple propagation.
 * 
 * @author Jamie Macaulay 
 *
 */
public class SimplePropogationPane extends BorderPane implements SettingsPane<Propogation> {
	
	private Spinner<Double> spreading;
	private Spinner<Double> absorbption;

	public SimplePropogationPane() {
		this.setCenter(createPropogationPane());
	}
	
	private Pane createPropogationPane() {
		
		int row =0; 
		GridPane mainPane = new GridPane(); 
		Label propogationLabel = new Label("Propogation"); 
		propogationLabel.setFont(new Font(CetSimView.titleFontSize));
		GridPane.setColumnSpan(propogationLabel, 4);
		mainPane.add(propogationLabel, 0, row);
		
		row++;
		HBox propogationHolder = new HBox();
		propogationHolder.setAlignment(Pos.CENTER);
		propogationHolder.setSpacing(5); 
//		mainPane.add(new Label(" Spreading = "), 0, row);
		spreading = new Spinner<Double>(0.,20.,20.,0.5); 
		
		ProbDetSettingsPane.styleSpinner(spreading);
//		mainPane.add(spreading, 1, row);
//		mainPane.add(new Label("*log10(R) +"), 2, row);
		absorbption= new Spinner<Double>(0.000000001,300.,0.04,0.01); 
		ProbDetSettingsPane.styleSpinner(absorbption);
//		mainPane.add(absorbption, 3, row);
//		mainPane.add(new Label("*R"), 4, row);
		
		propogationHolder.getChildren().addAll(new Label(" Spreading = "), spreading, new Label("*log10(R) +"),
				absorbption, new Label("*R")); 
		
		GridPane.setColumnSpan(propogationHolder, 5);
		mainPane.add(propogationHolder, 0, row);
		
		return mainPane; 
	}

	@Override
	public Propogation getParams() {
		return new SimplePropogation(this.spreading.getValue(), this.absorbption.getValue());
	}

	@Override
	public void setParams(Propogation settingsData, boolean clone) {
		
		SimplePropogation propogation = (SimplePropogation) settingsData; 

		spreading.getValueFactory().setValue(propogation.spreading);
		absorbption.getValueFactory().setValue(propogation.absorption);
	}

	@Override
	public String getName() {
		return "Propogation Settings";
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
