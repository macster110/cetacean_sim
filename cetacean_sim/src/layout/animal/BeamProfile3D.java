package layout.animal;

import java.util.function.Function;

import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import layout.utils.Utils3D;

import org.fxyz.cameras.CameraTransformer;
import org.fxyz.geometry.Point3Dfxy;
import org.fxyz.shapes.primitives.IcosahedronMesh;
import org.fxyz.utils.Palette.ColorPalette;

/**
 * Show a sphere with the beam profile on the surface.
 * @author Jamie Macaulay
 *
 */
public class BeamProfile3D extends BorderPane {

	private PerspectiveCamera camera;
	private final double sceneWidth = 300;
	private final double sceneHeight = 300;
	private final CameraTransformer cameraTransform = new CameraTransformer();

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	private IcosahedronMesh ico;
	private Rotate rotateY;

	/**
	 * The IcosahedronMesh has a bug so that diameter does not work- sphere's are always 1f. So this scales sphere so 
	 * that text extra is easier. 
	 */
	private double scale=100; 
	
	/**
	 * Initial surface function is nuill 
	 */
	private Function<Point3Dfxy, Number> dens =( p-> {
		return 0; //p.x*p.y*p.z;
	});

	/**
	 * The sub scene. 
	 */
	private SubScene scene;
	private long lastEffect;

	public BeamProfile3D() throws Exception {
		scene= create3DPane();
		this.setCenter(scene); 
		//render text properly
		this.setCache(true);
		this.setCacheHint(CacheHint.SCALE_AND_ROTATE);
	}

	//	                (float)(3d*Math.pow(Math.sin(p.phi),2)*Math.pow(Math.abs(Math.cos(p.theta)),0.1)+
	//	                Math.pow(Math.cos(p.phi),2)*Math.pow(Math.abs(Math.sin(p.theta)),0.1));
	//	    private Density dens = p->p.x*p.y*p.z;
	//	    private long lastEffect;

	public SubScene create3DPane() throws Exception {

		Group sceneRoot = new Group();
		SubScene scene = new SubScene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.DISABLED);
		//	        scene.setFill(Color.BLACK);
		camera = new PerspectiveCamera(true);        

		//setup camera transform for rotational support
		cameraTransform.setTranslate(0, 0, 0);
		cameraTransform.getChildren().add(camera);
		camera.setNearClip(0.001);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-600);
		cameraTransform.ry.setAngle(-45.0);
		cameraTransform.rx.setAngle(-10.0);
		//add a Point Light for better viewing of the grid coordinate system
		//	        PointLight light = new PointLight(Color.WHITE);
		//	        cameraTransform.getChildren().add(light);
		//	        light.setTranslateX(camera.getTranslateX());
		//	        light.setTranslateY(camera.getTranslateY());
		//	        light.setTranslateZ(camera.getTranslateZ());        
		scene.setCamera(camera);

		rotateY = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
		
		Group group = new Group();
		group.getChildren().add(cameraTransform);    
		ico = new IcosahedronMesh(5,100f); //FIXME- bug diameter does not work here so have to scale 
//		ico.setDrawMode(DrawMode.LINE);
		ico.setCullFace(CullFace.NONE);

		// NONE
		//	        ico.setTextureModeNone(Color.ROYALBLUE);
		// IMAGE
		//	        ico.setTextureModeImage(getClass().getResource("res/0ZKMx.png").toExternalForm());
		// PATTERN
		//	        ico.setTextureModePattern(2d);
		// DENSITY
		ico.setTextureModeVertices3D(1520,dens);
		ico.setColorPalette(ColorPalette.JET);
		// FACES
		//	        ico.setTextureModeFaces(256);


		ico.getTransforms().addAll(new Scale(scale, scale, scale));

		group.getChildren().add(ico);

		//create the axis
		group.getChildren().add(createAxis());		
		
		sceneRoot.getChildren().addAll(group);        

		//First person shooter keyboard movement 
		scene.setOnKeyPressed(event -> {
			double change = 10.0;
			//Add shift modifier to simulate "Running Speed"
			if(event.isShiftDown()) { change = 50.0; }
			//What key did the user press?
			KeyCode keycode = event.getCode();
			//Step 2c: Add Zoom controls
			if(keycode == KeyCode.W) { camera.setTranslateZ(camera.getTranslateZ() + change); }
			if(keycode == KeyCode.S) { camera.setTranslateZ(camera.getTranslateZ() - change); }
			//Step 2d:  Add Strafe controls
			if(keycode == KeyCode.A) { camera.setTranslateX(camera.getTranslateX() - change); }
			if(keycode == KeyCode.D) { camera.setTranslateX(camera.getTranslateX() + change); }
		});        

		scene.setOnMousePressed((MouseEvent me) -> {
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
			mouseOldX = me.getSceneX();
			mouseOldY = me.getSceneY();
		});

		scene.setOnMouseDragged((MouseEvent me) -> {
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
			mouseDeltaX = (mousePosX - mouseOldX);
			mouseDeltaY = (mousePosY - mouseOldY);

			double modifier = 10.0;
			double modifierFactor = 0.1;

			if (me.isControlDown()) {
				modifier = 0.1;
			}
			if (me.isShiftDown()) {
				modifier = 50.0;
			}
			if (me.isPrimaryButtonDown()) {
				cameraTransform.ry.setAngle(((cameraTransform.ry.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // +
				cameraTransform.rx.setAngle(((cameraTransform.rx.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // -
			} else if (me.isSecondaryButtonDown()) {
				double z = camera.getTranslateZ();
				double newZ = z + mouseDeltaX * modifierFactor * modifier;
				camera.setTranslateZ(newZ);
			} else if (me.isMiddleButtonDown()) {
				cameraTransform.t.setX(cameraTransform.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
				cameraTransform.t.setY(cameraTransform.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
			}
		});

//			        lastEffect = System.nanoTime();
//			        AtomicInteger count=new AtomicInteger();
//			        AnimationTimer timerEffect = new AnimationTimer() {
//		
//			            @Override
//			            public void handle(long now) {
//			                if (now > lastEffect + 100_000_000l) {
//			                    double cont1=0.1+(count.get()%60)/10d;
//			                    double cont2=0.1+(count.getAndIncrement()%30)/10d;
//		//	                    dens = p->(float)(3d*Math.pow(Math.sin(p.phi),2)*Math.pow(Math.abs(Math.cos(p.theta)),cont1)+
//		//	                            Math.pow(Math.cos(p.phi),2)*Math.pow(Math.abs(Math.sin(p.theta)),cont2));
//			                    //dens = p->10*cont1*Math.pow(Math.abs(p.x),cont1)*Math.pow(Math.abs(p.y),cont2)*Math.pow(p.z,2);
//			                    double t=count.getAndIncrement()%10;
//			                    dens = p->(double)(p.x+t)*(p.y+t)*(p.z+t);
//			                    ico.setDensity(dens);
//		//	                    ico.setColors((int)Math.pow(2,count.get()%16));
//		//	                    ico.setLevel(count.get()%8);
//		//	                    ico.setDiameter(0.5f+10*(float)cont1);
//			                    lastEffect = now;
//			                }
//			            }
//			        };
//			        
//			        Timeline timeline = new Timeline();
//			        timeline.setCycleCount(Timeline.INDEFINITE);
//			        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(50), new KeyValue(rotateY.angleProperty(), 360)));
//			                
//			        timerEffect.start();

		return scene; 
		//	        timeline.play();
	}
	
	private Group createAxis(){
		return Utils3D.buildAxes(scale+0.6*scale, Color.DARKGRAY, "right", "up", "front", Color.BLACK); 
	}

	/**
	 * Set the surface for the sphere. 
	 * @param gridder2
	 */
	public void setSurface(BeamProfile bp){
		dens =( p-> {
			double r	=	p.magnitude();
			double horz	=	Math.atan2(p.x,p.z);
			double vert	=	Math.atan(p.y/r);
			
			//System.out.println(" p.x: "+p.x+ " p.y: "+p.y+ " p.z: "+ p.z);
		
			
			return (bp.getTL(horz, vert));
			//return 1; //p.x*p.y*p.z;
//			double t=10; 
			//return (double) (p.x+t)*(p.y+t)*(p.z+t);
		});
		
		ico.setDensity(dens);
	}


}
