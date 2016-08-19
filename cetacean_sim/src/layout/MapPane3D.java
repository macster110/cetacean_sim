package layout;

import org.fxyz.shapes.composites.SurfacePlot;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Shows a 3D map. Bathymetry etc can be added.  
 * @author Jamie Macaulay 
 *
 */
public class MapPane3D extends BorderPane {
	
	
	
	   private PerspectiveCamera camera;
	    private final double sceneWidth = 600;
	    private final double sceneHeight = 600;
	    private double cameraDistance = 5000;
	    private SurfacePlot surfacePlot;
	    private double scenex, sceney, scenez = 0;
	    private double fixedXAngle, fixedYAngle, fixedZAngle = 0;
	    
		/**
		 * The camera transforms 
		 */
		private Rotate rotateY;
		private Rotate rotateX;
		private Translate translate;
		
		/**
		 * Keep track of mouse positions. 
		 */
		private double mousePosX;
		private double mousePosY;
		private double mouseOldX;
		private double mouseOldY;
		private double mouseDeltaY;
		private double mouseDeltaX;
		
	
	public MapPane3D(CetSimView cetSimView){
		create3DMap();
	}
	
	public void  create3DMap(){
		
	     	Group sceneRoot = new Group();
	        SubScene subScene = new SubScene(sceneRoot, 500,500, true, SceneAntialiasing.BALANCED);
	        subScene.widthProperty().bind(this.widthProperty());
	        subScene.heightProperty().bind(this.heightProperty());
	        subScene.setDepthTest(DepthTest.ENABLE);
	        
	        subScene.setFill(Color.BLACK);
	        camera = new PerspectiveCamera(true);    
	        camera.setFarClip(15000);
	        camera.setNearClip(0.1);
	        camera.setDepthTest(DepthTest.ENABLE);
	        camera.getTransforms().addAll (
	                rotateY=new Rotate(-45, Rotate.Y_AXIS),
	                rotateX=new Rotate(-45, Rotate.X_AXIS),
	                translate=new Translate(0, 0, -1000));
//	        camera.setNearClip(0.1);
//	        camera.setFarClip(10000.0);
//	        camera.setTranslateZ(-1000);
	        subScene.setCamera(camera);

	        
	        
	        //TEMP 
	        int size = 10;
	        float [][] arrayY = new float[2*size][2*size];
	        
	        //The Sombrero
	        for(int i=-size;i<size;i++) {
	            for(int j=-size;j<size;j++) {
	                double R = Math.sqrt((i * i)  + (j * j)) + 0.00000000000000001;
	                arrayY[i+size][j+size] = ((float) -(Math.sin(R)/R)) * 100;
	            }
	        }
	        //TEMP
	        
	        
	        surfacePlot = new SurfacePlot(arrayY, 10, Color.AQUA, true, false);

	        sceneRoot.getChildren().addAll(surfacePlot);

	        PointLight light = new PointLight(Color.WHITE);
	        sceneRoot.getChildren().add(light);
	        light.setTranslateZ(sceneWidth / 2);
	        light.setTranslateY(-sceneHeight + 10);

	        PointLight light2 = new PointLight(Color.WHITE);
	        sceneRoot.getChildren().add(light2);
	        light2.setTranslateZ(-sceneWidth + 10);
	        light2.setTranslateY(-sceneHeight + 10);
	        
	        //handle mopuse behaviour
	       
			handleMouse( subScene);
	        
	        this.setCenter(subScene);
	}
	

	 private void handleMouse(SubScene scene) {
	    	
	        scene.setOnMousePressed(new EventHandler<MouseEvent>() {



				@Override public void handle(MouseEvent me) {
	                mousePosX = me.getSceneX();
	                mousePosY = me.getSceneY();
	                mouseOldX = me.getSceneX();
	                mouseOldY = me.getSceneY();
	            }
	        });
	        
	        scene.setOnScroll(new EventHandler<ScrollEvent>() {

				@Override public void handle(ScrollEvent event) {
	            	System.out.println("Scroll Event: "+event.getDeltaX() + " "+event.getDeltaY()); 
             	translate.setZ(translate.getZ()+  event.getDeltaY() *0.001*translate.getZ());   // + 
	            }
	        });
	        
	        
	        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {



				@Override
	            public void handle(MouseEvent me) {
	                mouseOldX = mousePosX;
	                mouseOldY = mousePosY;
	                mousePosX = me.getSceneX();
	                mousePosY = me.getSceneY();
	                mouseDeltaX = (mousePosX - mouseOldX);
	                mouseDeltaY = (mousePosY - mouseOldY);

	                double modifier = 1.0;
	                double modifierFactor = 0.1;

	                if (me.isControlDown()) {
	                    modifier = 0.1;
	                }
	                if (me.isShiftDown()) {
	                    modifier = 10.0;
	                }
	                if (me.isPrimaryButtonDown()) {
	                	rotateY.setAngle(rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0);  // +
	                	rotateX.setAngle(rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0);  // -
	                }
	                if (me.isSecondaryButtonDown()) {
	                	translate.setX(translate.getX() -mouseDeltaX * modifierFactor * modifier * 5);
	                	translate.setY(translate.getY() - mouseDeltaY * modifierFactor * modifier * 5);   // +
	                }
	              
	               
	            }
	        });
	  }
	
	
	

}
