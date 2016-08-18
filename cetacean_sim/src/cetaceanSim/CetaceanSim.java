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
        CetSimControl csControl=new CetSimControl(); 
        
        StackPane root = new StackPane();
        
        root.getChildren().add(new CetSimView(csControl));
        
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}