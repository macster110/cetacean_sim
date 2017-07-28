package layout.simulation;

import javafx.scene.Node;

/**
 * Handles the GUI for a simulation
 * @author Jamie Macaulay 
 *
 */
public interface SimulationView {
	
	
	public Node getSidePane(); 

	public Node getCenterPane();
	
	public void notifyUpdate(int updateType); 

}
