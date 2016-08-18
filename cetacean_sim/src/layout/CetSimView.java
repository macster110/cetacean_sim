package layout;

import cetaceanSim.CetSimControl;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class CetSimView extends BorderPane {

	/**
	 * Reference to the control
	 */
	private CetSimControl csControl;

	public CetSimView(CetSimControl csControl) {
		this.csControl= csControl;
		this.setCenter(new Label("Hello Cet Sim Control"));
	}

}
