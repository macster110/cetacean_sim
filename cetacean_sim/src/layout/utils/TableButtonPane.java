package layout.utils;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Create a VBox or HBox with create (+ icon), edit (settings icon) and delete (- icon) buttons. 
 * @author Jamie Macualay 
 *
 */
public class TableButtonPane extends Pane {
	
	/**
	 *Pane holding the buttons. 
	 */
	Pane mainPane; 
	
	/**
	 * Button to add new item
	 */
	Button addButton; 
	
	/**
	 * Button to edit/open settings
	 */
	Button settingsButton; 
	
	/**
	 * Button to remove/delete
	 */
	Button deleteButton; 
	
	public Text addIcon=GlyphsDude.createIcon(MaterialIcon.ADD);

	
	public 	Text settingsIcon=GlyphsDude.createIcon(MaterialIcon.SETTINGS);

	public 	Text deleteIcon=GlyphsDude.createIcon(MaterialIcon.DELETE);

	
	public TableButtonPane(Orientation orientation){
		if (orientation==Orientation.VERTICAL) {
			mainPane=new VBox(); 
			((VBox) mainPane).setSpacing(10); 
		}
		else {
			mainPane=new HBox(); 
			((HBox) mainPane).setSpacing(10); 
		}
		mainPane.setPadding(new Insets(15, 12, 15, 12));
		
		addButton=new Button();
		addButton.setGraphic(addIcon);
		HBox.setHgrow(addButton, Priority.ALWAYS);

		
		settingsButton=new Button(); 
		settingsButton.setGraphic(settingsIcon);
		HBox.setHgrow(settingsButton, Priority.ALWAYS);

		deleteButton=new Button(); 
		deleteButton.setGraphic(deleteIcon);
		HBox.setHgrow(deleteButton, Priority.ALWAYS);
		
		settingsButton.setMaxWidth(Double.MAX_VALUE);
		deleteButton.setMaxWidth(Double.MAX_VALUE);
		addButton.setMaxWidth(Double.MAX_VALUE);


		mainPane.getChildren().addAll(addButton, settingsButton, deleteButton);
		
		this.getChildren().add(mainPane);

	}
	
	/**
	 * Get the add button. 
	 * @return the button with add icon. 
	 */
	public Button getAddButton() {
		return addButton;
	}

	/**
	 * Get the delete button.
	 * @return the button with delete icon. 
	 */
	public Button getDeleteButton() {
		return deleteButton;
	}

	/**
	 * Get the settings button.
	 * @return the button with settings icon. 
	 */
	public Button getSettingsButton() {
		return settingsButton;
	}


}
