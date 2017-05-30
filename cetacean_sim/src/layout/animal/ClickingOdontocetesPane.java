package layout.animal;

import animal.ClickingOdontocete;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import layout.CetSimView;
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
	
	
	private TableView<BeamProfileMeasurment> table = new TableView<BeamProfileMeasurment>(); 
	
	
	/*********Beam Profile***********/
	
    private BeamProfile3D beamProfile3D;
    
	private BeamProfile2D beamProfile2D;
	
	
	/**
	 * A clone or reference to the settings class. 
	 */
	private ClickingOdontocete settings;
	
	/**
	 * Lis tof beam profile measurments. 
	 */
	private ObservableList<BeamProfileMeasurment> beamProfileData;
	
	/*********Acoustic Behaviour*****************/
	
	private TextField sourceLevel;
	
	private TextField ici; 

	

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
		
		VBox holder1=new VBox(); 
		holder1.setSpacing(5);
		holder1.getChildren().addAll(createDivePane(), createVocBehaviourPane());
	
		HBox mainPaneHolder = new HBox(); 
		mainPaneHolder.setSpacing(5);
		
		mainPaneHolder.getChildren().addAll(holder1, createBeamProfilePane());

		this.setPadding(new Insets(10,0,10,0));
		this.setCenter(mainPaneHolder);

	}
	
	/**
	 * Create the dive pane. 
	 * @return the dive pane. 
	 */
	private Pane createDivePane(){
		
		VBox vBox= new VBox(); 
		vBox.setSpacing(5);
		
		Label label = new Label("Dive Behaviour"); 
		label.setFont(new Font(CetSimView.titleFontSize));
		
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
		
		vBox.getChildren().addAll(label, gridPane);
		
		return vBox;
		
	}
	
	/**
	 * Create pane foir acoustic behaviour. 
	 * @return the acoustic behaviouyr pane. 
	 */
	public Pane createVocBehaviourPane(){
		
		VBox vBox= new VBox(); 
		vBox.setSpacing(5);
		
		Label label = new Label("Acoustic Behaviour"); 
		label.setFont(new Font(CetSimView.titleFontSize));
		
		GridPane gridPane = new GridPane();  
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		
		gridPane.add(new Label("Source Level"),0,0);
		gridPane.add(sourceLevel = new TextField(), 1,0);
		gridPane.add(new Label("dB re 1uPa pp"), 2,0);
		
		gridPane.add(new Label("ICI"),0,1);
		gridPane.add(ici = new TextField(), 1,1);
		gridPane.add(new Label("s"), 2,1);
		
		vBox.getChildren().addAll(label, gridPane);

		
		return vBox;
 
	}
	
	/**
	 * Create beam profile pane.
	 * @return the beam profile pane. 
	 */
	public Pane createBeamProfilePane(){
		
		VBox vBox= new VBox(); 
		vBox.setSpacing(5);
		
		Label label = new Label("Beam Profile"); 
		label.setFont(new Font(CetSimView.titleFontSize));
		
		BorderPane holder = new BorderPane(); 
		
		// create the table for beam profile measurments. 
	    table.setEditable(true);
	   
    	TableColumn<BeamProfileMeasurment,Number>  horzColumn = new TableColumn<BeamProfileMeasurment,Number>("Horizontal Angle");
    	horzColumn.setCellValueFactory(cellData -> cellData.getValue().horzAngle.multiply(180).divide(Math.PI));
    	setTableCellFactory(horzColumn);
    	horzColumn.setEditable(true);
    	
		TableColumn<BeamProfileMeasurment,Number>  vertColumn = new TableColumn<BeamProfileMeasurment,Number>("Vertical Angle");
		vertColumn.setCellValueFactory(cellData -> cellData.getValue().vertAngle.multiply(180).divide(Math.PI));
    	setTableCellFactory(vertColumn);
    	vertColumn.setEditable(true);

    	TableColumn<BeamProfileMeasurment,Number>  tlColumn = new TableColumn<BeamProfileMeasurment,Number>("Tranmission Loss");
    	tlColumn.setCellValueFactory(cellData -> cellData.getValue().tL);
    	setTableCellFactory(tlColumn);
    	tlColumn.setEditable(true);

        table.getColumns().addAll(horzColumn, vertColumn, tlColumn);
                
        holder.setTop(table);
        

        // create 3D beam profile 

        Button refresh3D= new Button("",  GlyphsDude.createIcon(MaterialIcon.REFRESH,"25")); 
        refresh3D.setOnAction((action)->{
        	refreshBeamProfile();
        });
        StackPane.setAlignment(refresh3D, Pos.TOP_RIGHT);

        try {
			beamProfile3D = new BeamProfile3D();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        StackPane stackPane= new StackPane(); 
        stackPane.getChildren().addAll(beamProfile3D, refresh3D);

        holder.setBottom(stackPane);



        //crete 2D beam profile

        Button refresh2D= new Button("",  GlyphsDude.createIcon(MaterialIcon.REFRESH,"25")); 
        refresh2D.setOnAction((action)->{
        	refreshBeamProfile();
        });
        StackPane.setAlignment(refresh2D, Pos.TOP_RIGHT);

        beamProfile2D = new BeamProfile2D();
        StackPane stackPane2= new StackPane(); 
        stackPane2.getChildren().addAll(beamProfile2D, refresh2D);

        HBox hbox=new HBox(); 
        hbox.setSpacing(5);
        hbox.getChildren().addAll(holder, stackPane2);

		vBox.getChildren().addAll(label, hbox); 
		        
		return vBox;
 
	}
	
	/**
	 * Set the table cells so they only show 2 decinmla place.s 
	 */
	private void setTableCellFactory(TableColumn<BeamProfileMeasurment,Number>  povrsinaCol){
		povrsinaCol.setCellFactory(tc -> new TableCell<BeamProfileMeasurment, Number>() {
		    @Override
		    protected void updateItem(Number value, boolean empty) {
		        super.updateItem(value, empty) ;
		        if (empty) {
		            setText(null);
		        } else {
		            setText(String.format("%.2f", value.doubleValue()));
		        }
		    }
		});
	}

	

	@Override
	public ClickingOdontocete getParams() {
		
		// TODO Auto-generated method stub
		return settings;
	}

	@Override
	public void setParams(ClickingOdontocete settingsData, boolean clone) {
		
		if (clone) this.settings=settingsData.clone();
		else this.settings=settingsData;

		/*******************Diving Behaviour********************/
		
		descentVertAngleStd.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().descentVertAngleStd)));
		descentVertAngleMean.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().descentVertAngleMean)));
		
		ascentVertAngleMean.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().ascentVertAngleMean)));
		ascentVertAngleStd.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().ascentVertAngleStd)));
		
		descentSpeedStd.setText(String.format("%.2f", settings.getSettings().descentSpeedStd));
		descentSpeedMean.setText(String.format("%.2f", settings.getSettings().descentSpeedMean));
		
		ascentSpeedMean.setText(String.format("%.2f", settings.getSettings().ascentSpeedMean));
		ascentSpeedStd.setText(String.format("%.2f", settings.getSettings().ascentSpeedStd));
		
		bottomSpeedMean.setText(String.format("%.2f", settings.getSettings().bottomSpeedMean));
		bottomSpeedStd.setText(String.format("%.2f", settings.getSettings().bottomSpeedStd));
	
		bottomTimeMean.setText(String.format("%.2f", settings.getSettings().bottomTimeMean));
		bottomTimeStd.setText(String.format("%.2f", settings.getSettings().bottomTimeStd));
		
		descentHorzAngleMean.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().descentHorzAngleMean)));
		descentHorzAngleStd.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().descentHorzAngleStd)));
		
		ascentHorzAngleMean.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().ascentHorzAngleMean)));
		ascentHorzAngleStd.setText(String.format("%.2f", Math.toDegrees(settings.getSettings().ascentHorzAngleStd)));
		
		surfaceTimeStd.setText(String.format("%.2f", settings.getSettings().surfaceTimeStd));
		surfaceTimeMean.setText(String.format("%.2f", settings.getSettings().surfaceTimeMean));
		
		/********************Beam Profile*************************/
		beamProfileData= createBeamProfileList(settings.getSettings().beamProfile);
        table.setItems(beamProfileData);
        table.setEditable(true);
        
        refreshBeamProfile();
        
        /****************Acoustic Behaviour**********************/
        
        sourceLevel.setText(String.format("%.2f", settings.getSettings().sourceLevel));
        
        ici.setText(String.format("%.2f", settings.getSettings().ici));
       
		
	}
	
	private void refreshBeamProfile(){
		
		
        //now set the beam profile in the panes. 
		beamProfile3D.setSurface(settings.getBeamProfile()); 
		beamProfile2D.setSurface(settings.getBeamProfile());
	}
	
	/**
	 * Create an observable list of beam profile measurments from 2D double array./ 
	 * @param beamProfile - a 2D beam profile array {{horz angle, vert angle TL (dB)},...}
	 * @return an observable list of beam profile measurments. 
	 */
	private ObservableList<BeamProfileMeasurment> createBeamProfileList(double[][] beamProfile){
		
		ObservableList<BeamProfileMeasurment> observableList= FXCollections.observableArrayList();
		
		BeamProfileMeasurment measurment; 
		for (int i=0; i< beamProfile.length; i++){
			measurment= new BeamProfileMeasurment(beamProfile[i][0], beamProfile[i][1], beamProfile[i][2]); 
			observableList.add(measurment);
		}
		
		return observableList; 
	}
	
	
	/**
	 * Store a beam profile measurment. 
	 * @author Jamie Macaulay 
	 *
	 */
	class BeamProfileMeasurment{
		
		/**
		 * Horizontal angle. RADIANS. 
		 */
		public DoubleProperty horzAngle=new SimpleDoubleProperty(); 
		
		/**
		 * Vertical angle. RADIANS
		 */
		public DoubleProperty vertAngle=new SimpleDoubleProperty(); 
		
		/**
		 * Transmission loss. dB. 
		 */
		public DoubleProperty tL=new SimpleDoubleProperty(); 
		
		/**
		 * Constructor for a beam profile measurment
		 * @param horz - the horizsontal angle of the beam profile
 		 * @param vert - the vertical angle of the beam profile measurement. 
		 * @param TL - the tranmsiison loss compared to on axis. 
		 */
		 BeamProfileMeasurment(double horz, double vert, double TL){
			 horzAngle.setValue(horz);
			 vertAngle.setValue(vert);
			 tL.setValue(TL);
		 }
				
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
