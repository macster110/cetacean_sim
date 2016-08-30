package layout.bathymetry;

import java.util.ArrayList;

import org.controlsfx.glyphfont.Glyph;

import bathymetry.BasthymetrySimple;
import bathymetry.BathymetryFile;
import bathymetry.BathymetryModel;
import cetaceanSim.CetSimControl;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import layout.CetSimView;

/**
 * Holds various types of bathymetry. 
 * @author jamie
 *
 */
public class BathymetryPane extends BorderPane {
	
	/**
	 * Reference to the GUI. 
	 */
	private CetSimView cetSimView;
	
	/**
	 * Allows user to select bathymetry type. 
	 */
	private ComboBox<BathymetryModel> comboBathy; 
	
	/**
	 * Holds a list of bathymetry types
	 */
	private ObservableList<BathymetryModel> bathyList;
	

	/**
	 * The main holder 
	 */
	private BorderPane holder;  

	/**
	 * Constructor for the bathymetry pane
	 */
	public BathymetryPane(CetSimView cetSimView){
		this.cetSimView=cetSimView; 
		
		//Bathymetry List//
		ArrayList<BathymetryModel> bathyList= new ArrayList<BathymetryModel>(); 
		bathyList.add(new BasthymetrySimple());
		bathyList.add(new BathymetryFile(cetSimView.getCetSimControl()));
		this.bathyList=FXCollections.observableArrayList(bathyList); 
		/////////////////
		
		this.setMaxWidth(Double.MAX_VALUE);
		//create the main pane. 
		createBathyPane(); 
		
	}
	
	/**
	 * Create the basic controls for the bathymetry pane. 
	 */
	private void createBathyPane(){
		
		HBox comboHBox=new HBox();
		comboHBox.setSpacing(5);
	
		comboBathy=new ComboBox<BathymetryModel>();
		comboBathy.setItems(bathyList);
		
		//show name of the bathymetry type in the combo box
		comboBathy.setConverter(new StringConverter<BathymetryModel>() {
			@Override
			public String toString(BathymetryModel user) {
				if (user== null){
					return null;
				} else {
					return user.getName();
				}
			}

			@Override
			public BathymetryModel fromString(String id) {
				return null;
			}
		});
		comboBathy.setMaxWidth(Double.MAX_VALUE);
		
		//when the type is changed change the GUI underneath
		comboBathy.setOnAction((action)->{
			if (comboBathy.getValue().getSettingsPane()!=null) holder.setCenter(comboBathy.getValue().getSettingsPane().getContentNode());
			else holder.setCenter(null); 
			cetSimView.getCetSimControl().setBathymetry(comboBathy.getValue());
			
			cetSimView.notifyUpdate(CetSimControl.SIM_DATA_CHANGED);

		});
		
		
        Text graphic = GlyphsDude.createIcon(MaterialIcon.ARROW_DOWNLOAD,"25");
        Button buttonLoad=new Button();
        comboBathy.prefHeightProperty().bind(buttonLoad.heightProperty());
        buttonLoad.setGraphic(graphic);
        
        buttonLoad.setOnAction(action->{
        	comboBathy.getValue().loadBathy();
        	cetSimView.notifyUpdate(CetSimControl.BATHY_LOADED);
        });

        
		comboHBox.getChildren().addAll(comboBathy, buttonLoad);

	
		holder=new BorderPane(); 
		holder.setTop(comboHBox);
		
		
		this.setCenter(holder);
		
	}

}
