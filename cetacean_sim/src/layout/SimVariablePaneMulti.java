package layout;

import java.util.ArrayList;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import simulation.SimVariable;

/**
 * A sim variable pane that allows multiple distributions between two limits. 
 * @author Jamie Macaulay
 *
 */
public class SimVariablePaneMulti extends BorderPane {
	
	private ArrayList<SimVariablePane> simVariablePanes= new ArrayList<SimVariablePane> ();
	
	private VBox simHolder;


	private MenuButton removeButton;
	
	private String name;

	private String simUnits;

	private String limitsName;

	private String limitsSymbol;

	/**
	 * The default results converter 
	 */
	private ResultConverter resultConverter; 
	
	public SimVariablePaneMulti(String string, String simSymbol, String limitsName, String limitsSymbol){
		this.name=string; 
		this.simUnits=simSymbol; 
		this.limitsName=limitsName; 
		this.limitsSymbol=limitsSymbol; 
		this.setCenter(createPane()); 
		addNewVariable(); 
		layoutHolder(); 
	}
	
	/**
	 * Create the pane for the variables. 
	 */
	private Pane createPane() {
		
		BorderPane mainHolder = new BorderPane(); 
		
		simHolder = new VBox(); 
		simHolder.setSpacing(25);
		
		VBox buttonPane = new VBox(); 
		buttonPane.setSpacing(5); 
		buttonPane.setAlignment(Pos.TOP_LEFT);
		
		Button addButton = new Button(); 
		addButton.setGraphic(GlyphsDude.createIcon(MaterialIcon.ADD, "18"));
		addButton.setOnAction((action->{
			addNewVariable(); 
			enableMenuButton();
		}));
		
		removeButton= new MenuButton(); 
		removeButton.setGraphic(GlyphsDude.createIcon(MaterialIcon.REMOVE, "18"));
		removeButton.showingProperty().addListener((obsVal, oldVal, newVal)->{
			populateRemoveMenu(removeButton.getItems()); 
			enableMenuButton() ;
		});
	
		buttonPane.getChildren().addAll(addButton, removeButton); 
		buttonPane.setPadding(new Insets(0,0,5,5));
		
		Label label = new Label(name);
		label.setFont(new Font(CetSimView.titleFontSize));

		
		mainHolder.setTop(label); 
		mainHolder.setCenter(simHolder);
		mainHolder.setRight(buttonPane);
		
		return mainHolder; 
	}
	
	/**
	 * Disable or enable the menu buttons. 
	 */
	private void enableMenuButton() {
		if (simVariablePanes.size()<=1) {
			removeButton.setDisable(true);
		}
		else {
			removeButton.setDisable(false);
		}
	}
	
	/**
	 * Populate the remove menu item list. 
	 * @param items- list of menu items. 
	 */
	private void populateRemoveMenu(ObservableList<MenuItem> items) {
		items.clear();
		MenuItem menuItem; 
		for (int i=0; i<simVariablePanes.size(); i++) {
			menuItem= new MenuItem(("Sim Variable: " + i)); 
			final int index=i; 
			menuItem.setOnAction((action)->{
				 removeSimVariable(index); 
			});
			items.add(menuItem); 
		}
	}

	/**
	 * Add a new sim variable pane to the holder. 
	 */
	private void addNewVariable() {
		SimVariablePane variablePane = new SimVariablePane(null,
				simUnits, this.limitsName, this.limitsSymbol); 
		variablePane.setResultConverter(this.resultConverter);
		simVariablePanes.add(variablePane); 
		layoutHolder(); 
	}
	
	/**
	 * Remove a sim variable pane form the holder. 
	 * @param i - the variable to remove. 
	 */
	private void removeSimVariable(int i) {
		if (simVariablePanes.size()<=1) return; 
		simVariablePanes.remove(i); 
		layoutHolder(); 
	}


	private void layoutHolder() {
		simHolder.getChildren().clear();
		for (int i=0; i<simVariablePanes.size(); i++) {
			simHolder.getChildren().add(simVariablePanes.get(i)); 
		}
		enableMenuButton();

	}

	/**
	 * Get the sim variable.
	 * @return the sim variable. 
	 */
	public ArrayList<SimVariable> getSimVariable() {
		ArrayList<SimVariable> simVariables = new ArrayList<>(); 
		for (int i=0; i<this.simVariablePanes.size(); i++) {
			simVariables.add(simVariablePanes.get(i).getSimVariable()); 
		}
		return simVariables;
	}

	/**
	 * Set the result converter 
	 * @param resultsConverter - the results converter
	 */
	public void setResultConverter(ResultConverter resultsConverter) {
		this.resultConverter=resultsConverter; 
		
	}

	/**
	 * Set the sim variables. 
	 * @param variableList - the sim variables. 
	 */
	public void setSimVariables(ArrayList<SimVariable> variableList) {
		simVariablePanes.clear();
		SimVariablePane simVarPane; 
		for (int i=0; i<variableList.size(); i++) {
			simVarPane = new SimVariablePane(null, this.simUnits,  this.limitsName, this.limitsSymbol); 
			simVarPane.setResultConverter(this.resultConverter);
			simVarPane.setSimVariable(variableList.get(i));
			simVariablePanes.add(simVarPane); 
		}
		layoutHolder(); 
	}

}
