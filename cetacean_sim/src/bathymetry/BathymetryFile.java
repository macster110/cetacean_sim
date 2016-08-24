package bathymetry;

import cetaceanSim.CetSimControl;
import layout.utils.SettingsPane;
import utils.BathymetryUtils;
import utils.CSVReader;

/**
 * Loads bathymetry data from a file. 
 * @author Jamie Macaulay 
 *
 */
public class BathymetryFile implements BathymetryModel {
	
	private CetSimControl cetSimControl;
	
	private BathymetryFileSettings bathFileSettings=new BathymetryFileSettings(); 
	
	float[][] bathySurface;

	/**~
	 * Constructor for bathymetry from file. 
	 * @param cetSimControl
	 */
	public BathymetryFile(CetSimControl cetSimControl){
		this.cetSimControl=cetSimControl; 
	}

	@Override
	public void loadBathy() {
		//load up data 
		double[][] bathyData=CSVReader.readCSV(bathFileSettings.filePath); 
		bathySurface = BathymetryUtils.generateSurface(bathyData, 0.25f, true);
		
	}

	@Override
	public float[][] getDepthSurface() {
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
	public SettingsPane getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}



}
