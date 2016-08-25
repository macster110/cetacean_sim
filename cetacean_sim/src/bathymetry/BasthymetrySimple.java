package bathymetry;

import layout.utils.SettingsPane;

/**
 * Really simple Bathymetry whihc is just a single depth. 
 * @author Jamie Macaulay
 *
 */
public class BasthymetrySimple implements BathymetryModel {

	@Override
	public void loadBathy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float[][] getDepthSurface() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public float getGridXSize(){
		return 0; 
	}
	
	@Override
	public float getGridYSize(){
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
		return "Simple Bathmetry";
	}

	@Override
	public SettingsPane getSettingsPane() {
		// TODO Auto-generated method stub
		return null;
	}



}
