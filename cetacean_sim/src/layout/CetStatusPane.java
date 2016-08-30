package layout;


import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Pane whihc shows current status of simulation
 * @author Jamie Macaulay
 *
 */
public class CetStatusPane extends VBox {
	
	/**
	 * Shows status updates
	 */
	private Label messageLabel; 
	
	/**
	 * Shows the current progress. 
	 */
	private ProgressBar progressBar; 
	
	
	/**
	 * The current status pane. 
	 */
	public CetStatusPane(){
		messageLabel=new Label("Hello Staus");
		messageLabel.setTextFill(Color.WHITE);
		progressBar=new ProgressBar();
		progressBar.prefWidthProperty().bind(this.prefWidthProperty());
		this.setSpacing(5);
		this.getChildren().addAll(messageLabel, progressBar);
		
		this.setMaxWidth(200);
		this.setMaxHeight(200);

	}
	
	/**
	 * Set the message
	 * @param message - the progress message. 
	 */
	public void setMessage(String message){
		messageLabel.setText(message);
	}
	
	/**
	 * Set the current progress. 
	 * @param progress the progress. 
	 */
	public void setProgress(int progress ){
		progressBar.setProgress(progress);
	}
	
}
