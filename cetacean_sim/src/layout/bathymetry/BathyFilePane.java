package layout.bathymetry;

import java.io.File;
import java.util.ArrayList;

import org.controlsfx.glyphfont.Glyph;

import bathymetry.BathymetryFile;
import bathymetry.BathymetryFileSettings;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import layout.utils.SettingsPane;

/**
 * Allows users to select a binary file. 
 * @author Jamie Macaulay
 *
 */
public class BathyFilePane extends BorderPane implements SettingsPane<BathymetryFileSettings> {
	
	
	/**
	 * File extendsion allowed for open tags. 
	 */
	private final ArrayList<String> oTFileExtensions=new ArrayList<String>();

	/**
	 * Text field showing the file path. 
	 */
	private ComboBox<String> filePath;
	
    /**
     * Directory chooser for opening folder of DSG files. 
     */
	final FileChooser bathyFileChooser =
            new FileChooser();

	/**
	 * Reference ot the current settings. 
	 */
	private BathymetryFileSettings settings;

	/**
	 * Rference to the BathymetryFile type the settings pane belongs to. 
	 */
	private BathymetryFile bathymetryFile;
	
	
	/**
	 * @param bathymetryFile 
	 * 
	 */
	public BathyFilePane(BathymetryFile bathymetryFile){
		this.bathymetryFile=bathymetryFile; 
		this.setCenter(createCSVFilePane());
		this.setPadding(new Insets(10,12,10,0));
	}
	
	/**
	 * Create pane to allow users to select a folder of .DSG files to process. 
	 * @return pane allowing users to select folder of .dsg files. 
	 */
	private Pane createCSVFilePane(){
		
		oTFileExtensions.add("dsg"); 
		
		//create HBox with text field and file path and browse button. 
		HBox openFilePath=new HBox();  

		//openFilePath.setMaxWidth(Double.MAX_VALUE);
		openFilePath.setSpacing(10);
		filePath=new ComboBox<String>(); 
		filePath.setEditable(true);
		filePath.setMaxWidth(Double.MAX_VALUE);
		//filePath.setEditable(false);
		HBox.setHgrow(filePath, Priority.ALWAYS);
		HBox.setHgrow(openFilePath, Priority.ALWAYS);

		
		Button browseButton =new Button("", GlyphsDude.createIcon(MaterialIcon.FOLDER,"25"));
		browseButton.setOnAction((action)->{
			configureFileChooser(
					bathyFileChooser, "Open Bathymetry Files") ;
			File bathyFile = bathyFileChooser.showOpenDialog(null);
	           
	    		if (bathyFile!=null){
	    			filePath.getItems().add(0,bathyFile.getAbsolutePath());
	    			filePath.getSelectionModel().select(0);
	    			filePath.setTooltip(new Tooltip(bathyFile.getAbsolutePath()));
	    			settings.filePaths.add(0,bathyFile); 
	    		}
	    		else{
	    			//filePath.setText("");
	    			filePath.setTooltip(new Tooltip("No folder set"));
	    		}
	    		
		});		
		filePath.prefHeightProperty().bind(browseButton.heightProperty());

		
		openFilePath.getChildren().addAll(filePath, browseButton); 

		return openFilePath;
	}
	
	
	/**
	 * Configure a file chooser to only open .DSG files. 
	 * @param fileChooser - file chooser to configure
	 * @param title - the title of the file chooser. 
	 */
	private static void configureFileChooser(
			final FileChooser fileChooser, String title) {      
		fileChooser.setTitle(title);
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
				);                 
		fileChooser.getExtensionFilters().removeAll( fileChooser.getExtensionFilters());
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("CSV", "*.csv"));
	}

	@Override
	public BathymetryFileSettings getParams() {
		return settings;		
	}

	@Override
	public void setParams(BathymetryFileSettings settings, boolean clone) {
		this.settings=settings; 
		
	}

	@Override
	public String getName() {
		return "Bathymetry File Chooser";
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
