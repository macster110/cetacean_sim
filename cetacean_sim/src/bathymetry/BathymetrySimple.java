package bathymetry;

import layout.MapShapeProvider;
import layout.utils.SettingsPane;

/**
 * Really simple Bathymetry whihc is just a single depth. 
 * @author Jamie Macaulay
 *
 */
public class BathymetrySimple implements BathymetryModel {

	@Override
	public void loadBathy() {
		// TODO Auto-generated method stub
		
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

	@Override
	public MapShapeProvider getMapShapeProvider() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public BathyData getDepthSurface() {
		// TODO Auto-generated method stub
		return null;
	}



}
