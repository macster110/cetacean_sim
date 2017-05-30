package layout.animal;

import animal.SimpleCetacean;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import layout.utils.SettingsPane;

public class SimpleAnimalPane extends BorderPane implements SettingsPane<SimpleCetacean> {
	
	private SimpleCetacean settings;

	public SimpleAnimalPane (SimpleCetacean simpleCetacean){
		this.setCenter(new Label("Hey, I''m a simple animal!"));
	}

	@Override
	public SimpleCetacean getParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParams(SimpleCetacean settingsData, boolean clone) {
		if (clone) this.settings=settingsData.clone();
		else this.settings=settingsData;
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Simple Animal";
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
