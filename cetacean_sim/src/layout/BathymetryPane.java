package layout;

import java.util.ArrayList;

import org.controlsfx.glyphfont.Glyph;

import bathymetry.BasthymetrySimple;
import bathymetry.BathymetryFile;
import bathymetry.BathymetryType;
import cetaceanSim.CetSimControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

/**
 * Holds various types of bathymetry. 
 * @author jamie
 *
 */
public class BathymetryPane extends BorderPane {
	
	private CetSimView cetSimView;
	
	/**
	 * Allows user to select bathymetry type. 
	 */
	private ComboBox<BathymetryType> comboBathy; 
	
	/**
	 * Holds a list of bathymetry types
	 */
	private ObservableList<BathymetryType> bathyList;

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
		ArrayList<BathymetryType> bathyList= new ArrayList<BathymetryType>(); 
		bathyList.add(new BasthymetrySimple());
		bathyList.add(new BathymetryFile());
		this.bathyList=FXCollections.observableArrayList(bathyList); 
		/////////////////
		
		//create the main pane. 
		createBathyPane(); 
		
	}
	
	public void createBathyPane(){
		
		HBox comboHBox=new HBox();
		comboHBox.setSpacing(5);
	
		comboBathy=new ComboBox<BathymetryType>();
		comboBathy.setItems(bathyList);
		
		//show name of the bathymetry type in the combo box
		comboBathy.setConverter(new StringConverter<BathymetryType>() {
			@Override
			public String toString(BathymetryType user) {
				if (user== null){
					return null;
				} else {
					return user.getName();
				}
			}

			@Override
			public BathymetryType fromString(String id) {
				return null;
			}
		});
		//when the type is changed change the GUI underneath
		comboBathy.setOnAction((action)->{
			if (comboBathy.getValue().getSettingsPane()!=null) holder.setCenter(comboBathy.getValue().getSettingsPane().getContentNode());
			else holder.setCenter(null); 
		});
		
		
        Glyph graphic = Glyph.create( "FontAwesome|DOWNLOAD").sizeFactor(2).color(Color.GRAY);
        Button buttonLoad=new Button();
        comboBathy.prefHeightProperty().bind(buttonLoad.heightProperty());
        buttonLoad.setGraphic(graphic);
        
        buttonLoad.setOnAction(action->{
        	//
        	comboBathy.getValue().loadBathy();
        	cetSimView.notifyUpdate(CetSimControl.BATHY_LOADED);
        });

        
		comboHBox.getChildren().addAll(comboBathy, buttonLoad);

	
		holder=new BorderPane(); 
		holder.setTop(comboHBox);
		
		
		this.setCenter(holder);
		
	}

}
