package layout.animal;

import animal.ClickingOdontocete;
import animal.ClickingOdontocetesSettings;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import layout.utils.SettingsPane;

/**
 * Settings Pane for a clicking Odontocetes. 
 * @author Jamie Macaulay 
 *
 */
public class ClickingOdontocetesPane extends BorderPane implements SettingsPane<ClickingOdontocete> {
	
	/*************Diving**************/
	
	private TextField descentVertAngleStd;
	private TextField descentVertAngleMean;
	
	private TextField ascentVertAngleMean;
	private TextField ascentVertAngleStd;
	
	private TextField descentSpeedStd;
	private TextField descentSpeedMean;
	
	private TextField ascentSpeedMean;
	private TextField ascentSpeedStd;
	
	private TextField bottomSpeedMean;
	private TextField bottomSpeedStd;
	
	private TextField bottomTimeMean;
	private TextField bottomTimeStd;
	
	private TextField descentHorzAngleMean;
	private TextField descentHorzAngleStd;
	
	private TextField ascentHorzAngleMean;
	private TextField ascentHorzAngleStd;
	
	private TextField surfaceTimeStd;
	private TextField surfaceTimeMean;
	
	
	private TableView table = new TableView(); 
	
	
	/*********Beam Profile***********/
	
    private BeamProfile3D beamProfile3D;
    
	private BeamProfile2D beamProfile2D;

	

	/**
	 * Constructor for clicking odontocetes setting pane. 
	 * @param clickingOdontocete 
	 */
	public ClickingOdontocetesPane(ClickingOdontocete clickingOdontocete){
		createPane();
	}
	
	/**
	 * Create the clicking odontocetes pane. 
	 */
	private void createPane(){

		HBox mainPaneHolder = new HBox(); 
		mainPaneHolder.setSpacing(5);
		
		mainPaneHolder.getChildren().addAll(createDivePane(), createBeamProfilePane());


		this.setCenter(mainPaneHolder);

	}
	
	/**
	 * Create the dive pane. 
	 * @return the dive pane. 
	 */
	private Pane createDivePane(){
				
		GridPane gridPane = new GridPane();  
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		
		gridPane.add(new Label("Mean"),1,0);
		gridPane.add(new Label("Std"),2, 0);
		
		gridPane.add(new Label("Vertical Descent Angle"),0,1);
		gridPane.add(descentVertAngleMean = new TextField(), 1,1);
		gridPane.add(descentVertAngleStd = new TextField(), 2,1);
		gridPane.add(new Label("\u00b0"), 3,1);
		
		gridPane.add(new Label("Horizontal Descent Angle"),0,2);
		gridPane.add(descentHorzAngleMean = new TextField(), 1,2);
		gridPane.add(descentHorzAngleStd = new TextField(), 2,2);
		gridPane.add(new Label("\u00b0"), 3,2);
		
		gridPane.add(new Label("Vertical Ascent Angle"),0,3);
		gridPane.add(ascentVertAngleMean = new TextField(), 1,3);
		gridPane.add(ascentVertAngleStd = new TextField(), 2,3);
		gridPane.add(new Label("\u00b0"), 3,3);
		
		gridPane.add(new Label("Horizontal Ascent Angle"),0,4);
		gridPane.add(ascentHorzAngleMean = new TextField(), 1,4);
		gridPane.add(ascentHorzAngleStd = new TextField(), 2,4);
		gridPane.add(new Label("\u00b0"), 3,4);

		gridPane.add(new Label("Descent Speed"),0,5);
		gridPane.add(descentSpeedMean = new TextField(), 1,5);
		gridPane.add(descentSpeedStd = new TextField(), 2,5);
		gridPane.add(new Label("m/s"), 3,5);

		gridPane.add(new Label("Ascent Speed"),0,6);
		gridPane.add(ascentSpeedMean = new TextField(), 1,6);
		gridPane.add(ascentSpeedStd = new TextField(), 2,6);
		gridPane.add(new Label("m/s"), 3,6);
		
		gridPane.add(new Label("Bottom Speed"),0,7);
		gridPane.add(bottomSpeedMean = new TextField(), 1,7);
		gridPane.add(bottomSpeedStd = new TextField(), 2,7);
		gridPane.add(new Label(" m/s"), 3,7);
		
		gridPane.add(new Label("Bottom Time"),0,8);
		gridPane.add(bottomTimeMean = new TextField(), 1,8);
		gridPane.add(bottomTimeStd = new TextField(), 2,8);
		gridPane.add(new Label("s"), 3,8);
		
		gridPane.add(new Label("Surface Time"),0,9);
		gridPane.add(surfaceTimeMean = new TextField(), 1,9);
		gridPane.add(surfaceTimeStd = new TextField(), 2,9);
		gridPane.add(new Label("s"), 3,9);
		
		return gridPane;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Pane createVocBehaviourPane(){
		
		BorderPane holder = new BorderPane(); 
        
		return holder;
 
	}
	
	/**
	 * Create beam profile pane.
	 * @return the beam profile pane. 
	 */
	public Pane createBeamProfilePane(){
		
		BorderPane holder = new BorderPane(); 
		
	    table.setEditable(true);
	    
        TableColumn firstNameCol = new TableColumn("Vertical Angle");
        TableColumn lastNameCol = new TableColumn("Horizontal Angle");
        TableColumn emailCol = new TableColumn("Transmission Loss");
        
        table.getColumns().addAll(firstNameCol, lastNameCol, emailCol);
        
                
        holder.setTop(table);
        
		try {
			beamProfile3D = new BeamProfile3D();
	        holder.setBottom(beamProfile3D);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		HBox hbox=new HBox(); 
		hbox.setSpacing(5);
		hbox.getChildren().addAll(holder, beamProfile2D = new BeamProfile2D());
		        
		return hbox;
 
	}
	
	
	
	

	@Override
	public ClickingOdontocete getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(ClickingOdontocete settingsData, boolean clone) {
		beamProfile3D.setSurface(settingsData.getBeamProfile()); 
		beamProfile2D.setSurface(settingsData.getBeamProfile()); 

		
	}

	@Override
	public String getName() {
		return "Clicking Odontocetes Settings";
	}

	@Override
	public Node getContentNode() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void paneInitialized() {
		// TODO Auto-generated method stub
		
	}

}
