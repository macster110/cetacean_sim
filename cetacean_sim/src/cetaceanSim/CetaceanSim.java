package cetaceanSim;
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import layout.CetSimView;
 
public class CetaceanSim extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cetacean Sim");
    
        //intialise the control class. 
        CetSimControl.create(); 
        
        StackPane root = new StackPane();
        
        CetSimView view = null; 
        root.getChildren().add(view= new CetSimView(CetSimControl.getInstance()));
        CetSimControl.getInstance().setView(view);
        
        Scene scene=new Scene(root, 1200, 800); 
		//lets go dark 
        scene.getStylesheets().add("resources/darktheme.css");
        
        primaryStage.setScene(scene);
    	
        primaryStage.show();
    }
}