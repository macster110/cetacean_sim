package layout.reciever;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import layout.utils.SettingsPane;
import reciever.RecieverModel;

/**
 * Creates a simple drifting reciever  
 * @author Jamie Macaulay 
 *
 */
public class DriftingReciverPane extends BorderPane implements SettingsPane<RecieverModel> {
	
	public DriftingReciverPane() {
		this.setCenter(new Label("Hello I'm a drifitng reciever")); 
	}

	@Override
	public RecieverModel getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(RecieverModel settingsData, boolean clone) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Drifting Reciever Settings";
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
