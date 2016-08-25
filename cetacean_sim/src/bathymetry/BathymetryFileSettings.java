package bathymetry;

import java.io.File;
import java.util.ArrayList;

public class BathymetryFileSettings {
	
	public ArrayList<File> filePaths= new ArrayList<File>(); 

	public BathymetryFileSettings() {
		/**
		 * The current bathymetry file path 
		 */
		filePaths.add(new File("E:/Google Drive/SMRU_research/Kyle Rhea 2014/bathymetry_data/Kyle Rhea Bthy.csv"));
	}
	
	

}
