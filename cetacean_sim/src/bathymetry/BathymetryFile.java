package bathymetry;

import cetaceanSim.CetSimControl;
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
	private float[][] bathySurface;
	
	/**
	 * Change the settings for bathymetry file. 
	 */
	private SettingsPane bathySettingsPane; 

	/**~
	 * Constructor for bathymetry from file. 
	 * @param cetSimControl
	 */
	public BathymetryFile(CetSimControl cetSimControl){
		this.cetSimControl=cetSimControl; 
	}

	@Override
	public void loadBathy() {
		//load up data CSV 
		if (bathFileSettings.filePaths.get(0)!=null){
			
			String extension = "";

			int i = bathFileSettings.filePaths.get(0).getName().lastIndexOf('.');
			if (i > 0) {
			    extension = bathFileSettings.filePaths.get(0).getName().substring(i+1);
			}

			/*******************CSV File***********************/
			if (extension=="csv")
				try{
					double[][] bathyData=CSVReader.readCSV(bathFileSettings.filePaths.get(0).getAbsolutePath()); 
					//now figure out what kind of file it is 
					if (bathyData.length>0 && bathyData[0].length==3){
						LatLong[] latLon=new LatLong[bathyData.length]; 
						for (int j=0; j<bathyData.length; j++){
							latLon[j]=new LatLong(bathyData[j][0], bathyData[j][1], bathyData[j][2]); 
							bathySurface = BathymetryUtils.generateSurface(latLon, 2f, true);
						}
					}
				}

			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public float[][] getDepthSurface() {
		return bathySurface;
	}
	
	@Override
	public float getGridXSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getGridYSize() {
		// TODO Auto-generated method stub
		return 0;
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
	public SettingsPane getSettingsPane() {
		if (bathySettingsPane==null){
			bathySettingsPane=new BathyFilePane(this);
			bathySettingsPane.paneInitialized();
		}
		return bathySettingsPane;
	}



}
