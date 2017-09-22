package layout;

import java.util.ArrayList;

import cetaceanSim.SimUnit;

/***
 * Manages shapes/groups being added to and removed from the map. 
 * @author Jamie Macaulay
 *
 */
public class Map3DManager  {

	private MapPane3D mapPane3D;
	
	/**
	 * A list of classes which can add shapes the graph. 
	 */
	private ArrayList<MapShapeProvider> mapProviders;

	/**
	 * Reference to the view. 
	 */
	@SuppressWarnings("unused")
	private CetSimView cetSimView; 

	public Map3DManager(CetSimView cetSimView, MapPane3D mapPane3D) {
		this.cetSimView=cetSimView; 
		this.mapPane3D=mapPane3D; 
		mapProviders=new ArrayList<MapShapeProvider>(); 
	}
	
	/**
	 * Add a map provider. 
	 * @param mapProvider the mpa provider to add
	 */
	public void addMapProvider(MapShapeProvider mapProvider){
		if (mapProvider==null) return; 
		mapProviders.add(mapProvider);
		mapPane3D.getDynamicGroup().getChildren().add(mapProvider.getMapShapes());
	}
	
	/**
	 * Remove a map Provider. 
	 * @param mapProvider
	 */
	public boolean removeMapProvider(MapShapeProvider mapProvider){
		if (mapProvider==null) return false; 
		mapPane3D.getDynamicGroup().getChildren().remove(mapProvider.getMapShapes());
		return mapProviders.remove(mapProvider);
	}
	
	/**
	 * Update the providers. Remove any providers which are no longer in use and add any new providers from the model.
	 */
	public void updateProviders(ArrayList<SimUnit> simUnits) {
		//try to be efficient here....only remove providers whihc need to to be removed. 
		ArrayList<MapShapeProvider> toRemove= new ArrayList<MapShapeProvider>(); 

		//remove all providers whihc are no longer in use. 
		boolean kk =false;
		for (int i=0; i<mapProviders.size(); i++){
			for (int j=0; j<simUnits.size(); j++){
				if (simUnits.get(j).getMapShapeProvider()==mapProviders.get(i)){
					kk=true; 
				}
			}
			if (!kk) toRemove.add(mapProviders.get(i)); 
			kk=false; 
		}
		
		//have to use map manager remove functions
		for (int k=0; k<toRemove.size(); k++){
			removeMapProvider(toRemove.get(k)); 
		}
		
		//now add in all map provider whihc are not included. 
		for (int i=0; i<simUnits.size(); i++){
			System.out.println("Sim unit: "+simUnits.get(i));
			if (simUnits.get(i)==null) continue; 
			if (mapProviders.contains(simUnits.get(i).getMapShapeProvider())){
				//no need to re add model
				continue;
			}
			else {
				//add map provider
				addMapProvider(simUnits.get(i).getMapShapeProvider()); 
			}
		
		}
	}
	
	

}
