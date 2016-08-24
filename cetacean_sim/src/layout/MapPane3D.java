package layout;


import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import layout.utils.SurfacePlot;

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
		
		/** 
		 * The main group whihc holds other groups. 
		 */
		private Group sceneRoot;
		private Node headLight;
		private Node pointLight;
		private Node ambientLight;
		private Group axisGroup;
		
		private MeshView meshview;
		
		 private Box xAxis,yAxis,zAxis;
		
	
	public MapPane3D(CetSimView cetSimView){
		create3DMap();
	}
	
	/**
	 * Create the basics of the 3D map. 
	 */
	public void  create3DMap(){
		
			sceneRoot = new Group();
	        SubScene subScene = new SubScene(sceneRoot, 500,500, true, SceneAntialiasing.BALANCED);
	        subScene.widthProperty().bind(this.widthProperty());
	        subScene.heightProperty().bind(this.heightProperty());
	        subScene.setDepthTest(DepthTest.ENABLE);
	        
	        subScene.setFill(Color.BLACK);
	        camera = new PerspectiveCamera(true);    
	        camera.setFarClip(1500000);
	        camera.setNearClip(0.1);
	        camera.setDepthTest(DepthTest.ENABLE);
	        camera.getTransforms().addAll (
	                rotateY=new Rotate(-45, Rotate.Y_AXIS),
	                rotateX=new Rotate(-45, Rotate.X_AXIS),
	                translate=new Translate(0, 0, -100));
//	        camera.setNearClip(0.1);
//	        camera.setFarClip(10000.0);
//	        camera.setTranslateZ(-1000);
	        subScene.setCamera(camera);
	        
	        
	        //handle mopuse behaviour
	       
			handleMouse( subScene);
			initLights( sceneRoot);
			createAxes(sceneRoot);
	        
	        this.setCenter(subScene);
	}
	
	
    private void createAxes(Group sceneRoot) {
        axisGroup = new Group();
        
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.DARKRED);
        redMaterial.setSpecularColor(Color.RED);
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.DARKGREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.DARKBLUE);
        blueMaterial.setSpecularColor(Color.BLUE);
        final Sphere red = new Sphere(50);
        red.setMaterial(redMaterial);
        final Sphere blue = new Sphere(50);
        blue.setMaterial(blueMaterial);
        xAxis = new Box(24.0, 0.05, 0.05);
        yAxis = new Box(0.05, 24.0, 0.05);
        zAxis = new Box(0.05, 0.05, 24.0);
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
        
        axisGroup.getChildren().addAll(xAxis,yAxis,zAxis);
        sceneRoot.getChildren().add(axisGroup);
}
	
	
	 private void initLights(Group sceneRoot){
	        headLight = new PointLight();        
	        headLight.translateXProperty().bindBidirectional(camera.translateXProperty());
	        headLight.translateYProperty().bindBidirectional(camera.translateYProperty());
	        headLight.translateZProperty().bindBidirectional(camera.translateZProperty());	        
	        headLight.setRotationAxis(Rotate.Y_AXIS);
	        
	        
	        pointLight = new PointLight();
	        pointLight.setTranslateX(-1000);
	        pointLight.setTranslateY(-1000);
	        pointLight.setTranslateZ(-1000);
	        
//	        ambientLight = new AmbientLight();
//	        ambientLight.setTranslateY(-1000);
	        
	        sceneRoot.getChildren().addAll(pointLight, headLight);
	}
	
	public Group getRootGroup(){
		return sceneRoot;
	}


	public SurfacePlot createSurface(float[][] surface){
        return new SurfacePlot(surface, 20, Color.BLACK, Color.DARKGRAY, true, false);
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
	                    modifier = 50;
	                }
	                if (me.isShiftDown()) {
	                    modifier = 0.1;
	                }
	                if (me.isPrimaryButtonDown()) {
	                	rotateY.setAngle(rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0);  // +
	                	rotateX.setAngle(rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0);  // -
	                }
	                if (me.isSecondaryButtonDown()) {
	                	translate.setX(translate.getX() -mouseDeltaX * modifierFactor * modifier * 50);
	                	translate.setY(translate.getY() - mouseDeltaY * modifierFactor * modifier * 50);   // +
	                }
	              
	               
	            }
	        });
	  }
	 

	
	
	

}
