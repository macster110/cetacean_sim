package layout.animal;


import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;


/**
 * Shows an example beam profile for a specifed source level. 
 * @author jamie
 *
 */
public class BeamProfile2D extends BorderPane  {
	
	private WritableImage curreentBeamProfile; 
	
	private Canvas canvas; 
		
	public BeamProfile2D(){
		BorderPane holder = new BorderPane(); 
	
		Canvas canvas= new Canvas(); 
		canvas.widthProperty().bind(holder.widthProperty());
		canvas.heightProperty().bind(holder.heightProperty());

	}
	
	
	
	

}


