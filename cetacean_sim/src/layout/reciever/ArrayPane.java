package layout.reciever;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import layout.utils.SettingsPane;
import reciever.DefaultHydrophoneArrays;
import reciever.Hydrophone;
import reciever.HydrophoneArray;
import reciever.SimpleHydrophoneArray;

/**
 * Simple pane to create a hydrophone array 
 * @author Jamie Macaulay 
 *
 */
public class ArrayPane extends BorderPane implements SettingsPane<HydrophoneArray> {

	/**
	 * The recievers. 
	 */
	private  ObservableList<Hydrophone>  recieversArray = FXCollections.observableArrayList();
	
	/**
	 * List of the default arrays 
	 */
	private ComboBox<String> defaultArrayBox;

	/**
	 * The table to hold date
	 */
	private TableView<Hydrophone> table;

	/**
	 * Create the array pane
	 */
	public ArrayPane() {
		this.setCenter(createPane());
	}
	
	/**
	 * Create the pane.
	 * @return the pane. 
	 */
	private Pane createPane() {
		this.table = createTable();
		
		//recievers.add(new Hydrophone(new double[] {0,0,0}, 0));
		
		//set table data 
		table.setItems(recieversArray);
		table.setEditable(true);
		
		defaultArrayBox = new ComboBox<String>(); 
		ArrayList<HydrophoneArray> defaultArrays =  DefaultHydrophoneArrays.defaultHydrophoneArrays(); 
		for (int i=0; i<defaultArrays.size(); i++) {
			defaultArrayBox.getItems().add(defaultArrays.get(i).getName()); 
		}
		defaultArrayBox.setOnAction((action)->{
			setTableData(defaultArrays.get(defaultArrayBox.getSelectionModel().getSelectedIndex())); 
		});
		
		defaultArrayBox.setPadding(new Insets(5,5,5,5));
	
		defaultArrayBox.getSelectionModel().select(0);
		setTableData(defaultArrays.get(0)); 
		
		VBox topHolder = new VBox();  
		topHolder.setSpacing(5);
		topHolder.getChildren().addAll(new Label("Default Arrays"), defaultArrayBox, new Label("Hydrophone Positions")); 
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topHolder);
		borderPane.setCenter(table);
		
		return borderPane;
	}
	
	
	/**
	 * @param hydrophoneArray 
	 * 
	 */
	public void setTableData(HydrophoneArray hydrophoneArray) {
		recieversArray.clear();
		
		for (int i=0; i<hydrophoneArray.getArrayXYZ().length; i++) {
			recieversArray.add(new Hydrophone(hydrophoneArray.getArrayXYZ()[i], hydrophoneArray.getSensOffset()[i])); 
		}
		table.setEditable(true);
		System.out.println("Helloo table editable: " + table.getEditingCell());
	}
	

	/**
	 * Create the table. 	
	 */
	private TableView<Hydrophone> createTable() {
	

		TableView<Hydrophone> table = new TableView<Hydrophone>(); 
	    table.getSelectionModel().cellSelectionEnabledProperty().set(true);
	    
		table.setEditable(true);
		table.setTableMenuButtonVisible(true);

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		

		TableColumn<Hydrophone,Number>  xrec = new TableColumn<Hydrophone,Number>("x (m)");
		xrec.setCellValueFactory(cellData -> cellData.getValue().xProperty());
		xrec.setEditable(true);
		xrec.setCellFactory(TextFieldTableCell.<Hydrophone, Number>forTableColumn(new NumberStringConverter()));

		TableColumn<Hydrophone,Number>  yrec = new TableColumn<Hydrophone,Number>("y (m)");
		yrec.setCellValueFactory(cellData -> cellData.getValue().yProperty());
		yrec.setEditable(true);
		yrec.setCellFactory(TextFieldTableCell.<Hydrophone, Number>forTableColumn(new NumberStringConverter()));


		TableColumn<Hydrophone,Number>  zrec = new TableColumn<Hydrophone,Number>("z (m)");
		zrec.setCellValueFactory(cellData -> cellData.getValue().zProperty());
		zrec.setEditable(true);
		zrec.setCellFactory(TextFieldTableCell.<Hydrophone, Number>forTableColumn(new NumberStringConverter()));

		TableColumn<Hydrophone,Number>  sensOffset = new TableColumn<Hydrophone,Number>("Sens. offset (dB)");
		sensOffset.setCellValueFactory(cellData -> cellData.getValue().sensProperty());
		sensOffset.setEditable(true);
		sensOffset.setCellFactory(TextFieldTableCell.<Hydrophone, Number>forTableColumn(new NumberStringConverter()));

		table.getColumns().addAll(xrec, yrec, zrec, sensOffset);
		
		return table;

	}

	@Override
	public HydrophoneArray getParams() {
		
		double[][] hydrophones = new double[recieversArray.size()][3]; 
		double[] sens = new double[recieversArray.size()]; 
		
		for (int i=0; i<hydrophones.length; i++) {
			hydrophones[i]= new double[] {recieversArray.get(i).getX(), recieversArray.get(i).getY(), recieversArray.get(i).getZ()}; 
			sens[i]=recieversArray.get(i).getSens();
		} 
		
		HydrophoneArray hydrophoneArray= new SimpleHydrophoneArray(hydrophones, sens); 
		
		return hydrophoneArray;
	}

	@Override
	public void setParams(HydrophoneArray settingsData, boolean clone) {
		recieversArray.clear();
		
		Hydrophone hydrophone; 
		for (int i=0; i<settingsData.getArrayXYZ().length; i++) {
			hydrophone = new Hydrophone(settingsData.getArrayXYZ()[i], settingsData.getSensOffset()[i]);
			recieversArray.add(hydrophone); 
		} 
	}

	@Override
	public String getName() {
		return "Reciever Array";
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

