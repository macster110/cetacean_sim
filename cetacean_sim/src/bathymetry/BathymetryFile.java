package bathymetry;

import cetaceanSim.CetSimControl;
import javafx.application.Platform;
import javafx.concurrent.Task;
import layout.MapShapeProvider;
import layout.bathymetry.BathyFilePane;
import layout.utils.SettingsPane;
import utils.BathymetryUtils;
import utils.CSVReader;
import utils.LatLong;

/**
 * Loads bathymetry data from a file. 
 * @author Jamie Macaulay 
 *
 */
public class BathymetryFile implements BathymetryModel {
	
	/**
	 * Reference to the control class. 
	 */
	private CetSimControl cetSimControl;
	
	/**
	 * The settings for a bathymetry loaded form file. 
	 */
	private BathymetryFileSettings bathFileSettings=new BathymetryFileSettings(); 
	
	/**
	 * The current Bathymetry surface 
	 */
	private BathyData bathySurface;
	
	/**
	 * Change the settings for bathymetry file. 
	 */
	private SettingsPane<BathymetryFileSettings> bathySettingsPane; 
	
	/**
	 * The bathymetry surface 3D shape. 
	 */
	private BathymetrySurface bathy3DSurface; 

	/**~
	 * Constructor for bathymetry from file. 
	 * @param cetSimControl
	 */
	public BathymetryFile(CetSimControl cetSimControl){
		this.cetSimControl=cetSimControl; 
		bathy3DSurface=new BathymetrySurface(this); 
	}

	@Override
	public void loadBathy() {
		
		//load bathymetry 
	     Task<Integer> task = new Task<Integer>() {
	         @Override protected Integer call() throws Exception {
	        	 loadBathyData(this);
	             return 0;
	         }
	     };
		

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
		
	}
	
	public void loadBathyData(Task<Integer> task){
		
		Platform.runLater(() ->{
			cetSimControl.setSimMessage("Loading bathymetry from file"); 
			cetSimControl.notifyUpdate(CetSimControl.LOAD_DATA_START);
		});

		//load up data CSV 
		if (bathFileSettings.filePaths.get(0)!=null){
			
			String extension = "";

			int i = bathFileSettings.filePaths.get(0).getName().lastIndexOf('.');
			if (i > 0) {
			    extension = bathFileSettings.filePaths.get(0).getName().substring(i+1);
			}

			/*******************CSV File***********************/
			
			System.out.println(" Extensions: " +  extension + "  "+ bathFileSettings.filePaths.get(0).getAbsolutePath());
			if (extension.equalsIgnoreCase("csv")){
				try{
					Platform.runLater(() ->{
						cetSimControl.setSimMessage("Importing bathymetry from csv file"); 
					});
					double[][] bathyData=CSVReader.readCSV(bathFileSettings.filePaths.get(0).getAbsolutePath()); 
					//now figure out what kind of file it is 
					if (bathyData.length>0 && bathyData[0].length==3){
						LatLong[] latLon=new LatLong[bathyData.length]; 
						for (int j=0; j<bathyData.length; j++){
							//don;t allow 0 values...
							if (bathyData[j][2]!=0 && bathyData[j][1]!=0){
								latLon[j]=new LatLong(bathyData[j][2], bathyData[j][1], bathyData[j][0]); 
							}
							//System.out.println(latLon[j].getLatitude()+ " "+latLon[j].getLongitude());
						}
						Platform.runLater(() ->{
							cetSimControl.setSimMessage("Interpolating bathymetry surface"); 
						});
						bathySurface = BathymetryUtils.generateSurface(latLon, 1f, true);
					}
				}

				catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		Platform.runLater(() ->{
			cetSimControl.setSimMessage("Generating 3D shapes");
			bathy3DSurface.newBathySurface(bathySurface);
			cetSimControl.notifyUpdate(CetSimControl.BATHY_LOADED);
			cetSimControl.notifyUpdate(CetSimControl.LOAD_DATA_END);
		});
		
	}

	@Override
	public BathyData getDepthSurface() {
		return bathySurface;
	}
	

	@Override
	public double getDepth(double x, double y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bathymetry File";
	}

	@Override
	public SettingsPane<BathymetryFileSettings> getSettingsPane() {
		if (bathySettingsPane==null){
			bathySettingsPane=new BathyFilePane(this);
			bathySettingsPane.paneInitialized();
			bathySettingsPane.setParams(bathFileSettings, false);
		}
		return bathySettingsPane;
	}


	@Override
	public MapShapeProvider getMapShapeProvider() {
		return bathy3DSurface;
	}



}
