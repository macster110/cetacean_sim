package layout;

import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box; 
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import layout.utils.SurfacePlot;
import layout.utils.Xform;

/**
 * Shows a 3D map. Bathymetry etc can be added.  
 * @author Jamie Macaulay 
 *
 */
public class MapPane3D extends BorderPane {


	/**
	 * The scene camera. 
	 */

	final PerspectiveCamera camera = new PerspectiveCamera(true);
    final double cameraDistance = 450;


	final Xform cameraXform = new Xform();
	final Xform cameraXform2 = new Xform();
	final Xform cameraXform3 = new Xform();

	/**
	 * Automatic rotation
	 */
	private Timeline timeline;
	boolean timelinePlaying = false;


	/**
	 * Keep track of mouse positions. 
	 */
	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaY;
	private double mouseDeltaX;

	double ONE_FRAME = 1.0 / 24.0;
	double DELTA_MULTIPLIER = 200.0;
	double CONTROL_MULTIPLIER = 0.1;
	double SHIFT_MULTIPLIER = 0.1;
	double ALT_MULTIPLIER = 0.5;

	/** 
	 * The main group whihc holds other groups. 
	 */
	private Group sceneRoot;
	private Xform simWorld;
	private Node headLight;
	private Node pointLight1;


	/**
	 * Group which holds the axis
	 */
	private Group axisGroup;
	private Box xAxis,yAxis,zAxis;

	/**
	 * Group whihc holds all shapes from simulation parts. 
	 */
	private Xform mapProviderGroup;
	private PointLight pointLight2; 


	public MapPane3D(CetSimView cetSimView){
		create3DMap();
	}

	private void buildCamera(Group sceneRoot) {
		sceneRoot.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		camera.setTranslateZ(-cameraDistance);
		cameraXform.ry.setAngle(320.0);
		cameraXform.rx.setAngle(40);
	}

	/**
	 * Create the basics of the 3D map. 
	 */
	public void  create3DMap(){
		
		//create the groups we want. 
		sceneRoot = new Group();
		simWorld=new Xform();
		mapProviderGroup=new Xform();

		//handle mopuse behaviour
		buildCamera(sceneRoot);
		//			handleMouse( subScene);
		initLights(simWorld);
		createAxes(simWorld);

		simWorld.getChildren().add(mapProviderGroup);

		sceneRoot.getChildren().add(simWorld);
		
		SubScene subScene = new SubScene(sceneRoot, 500,500, true, SceneAntialiasing.BALANCED);
		subScene.setFill(Color.BLACK);
		subScene.widthProperty().bind(this.widthProperty());
		subScene.heightProperty().bind(this.heightProperty());
		subScene.setDepthTest(DepthTest.ENABLE);

		handleKeyboard(subScene, simWorld);
		handleMouse(subScene, simWorld);
		subScene.setCamera(camera);

		this.setCenter(subScene);
	}
	
	/**
	 * Create a 3D axis. 
	 * @param- size of the axis
	 */
	public static Group buildAxes(double axisSize, Color xAxisDiffuse, Color xAxisSpectacular,
			Color yAxisDiffuse, Color yAxisSpectacular,
			Color zAxisDiffuse, Color zAxisSpectacular,
			Color textColour) {
		Group axisGroup=new Group(); 
        double length = 2d*axisSize;
        double width = axisSize/100d;
        double radius = 2d*axisSize/100d;
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(xAxisDiffuse);
        redMaterial.setSpecularColor(xAxisSpectacular);
        final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(yAxisDiffuse);
        greenMaterial.setSpecularColor( yAxisSpectacular);
        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(zAxisDiffuse);
        blueMaterial.setSpecularColor(zAxisSpectacular);
        
        Text xText=new Text("x"); 
        xText.setStyle("-fx-font: 20px Tahoma;");
        xText.setFill(textColour);
        Text yText=new Text("z"); 
        yText.setStyle("-fx-font: 20px Tahoma; ");
        yText.setFill(textColour);
        Text zText=new Text("y"); 
        zText.setStyle("-fx-font: 20px Tahoma; ");
        zText.setFill(textColour);

        xText.setTranslateX(axisSize+5);
        yText.setTranslateY(-(axisSize+5));
        zText.setTranslateZ(axisSize+5);

        Sphere xSphere = new Sphere(radius);
        Sphere ySphere = new Sphere(radius);
        Sphere zSphere = new Sphere(radius);
        xSphere.setMaterial(redMaterial);
        ySphere.setMaterial(greenMaterial);
        zSphere.setMaterial(blueMaterial);
         
        xSphere.setTranslateX(axisSize);
        ySphere.setTranslateY(-axisSize);
        zSphere.setTranslateZ(axisSize);
         
        Box xAxis = new Box(length, width, width);
        Box yAxis = new Box(width, length, width);
        Box zAxis = new Box(width, width, length);
        xAxis.setMaterial(redMaterial);
        yAxis.setMaterial(greenMaterial);
        zAxis.setMaterial(blueMaterial);
         
        axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
        axisGroup.getChildren().addAll(xText, yText, zText);
        axisGroup.getChildren().addAll(xSphere, ySphere, zSphere);
        return axisGroup;
    }
	

	private void createAxes(Group sceneRoot) {
		 Group axis = buildAxes(300.,Color.DARKRED, Color.RED,
				 Color.DARKGREEN, Color.GREEN,
				 Color.CYAN, Color.BLUE,
				 Color.WHITE); 
		 sceneRoot.getChildren().add(axis); 
	}


	private void initLights(Group sceneRoot){
		headLight = new PointLight();        
		headLight.translateXProperty().bindBidirectional(camera.translateXProperty());
		headLight.translateYProperty().bindBidirectional(camera.translateYProperty());
		headLight.translateZProperty().bindBidirectional(camera.translateZProperty());	        
		headLight.setRotationAxis(Rotate.Y_AXIS);

		pointLight1 = new PointLight();
		pointLight1.setTranslateX(-1000);
		pointLight1.setTranslateY(-1000);
		pointLight1.setTranslateZ(-1000);
		
		pointLight2 = new PointLight();
		pointLight2.setTranslateX(-1000);
		pointLight2.setTranslateY(1000);
		pointLight2.setTranslateZ(1000);

		//	        ambientLight = new AmbientLight();
		//	        ambientLight.setTranslateY(-1000);

		sceneRoot.getChildren().addAll(pointLight1, pointLight2, headLight);
	}

	public Group getRootGroup(){
		return sceneRoot;
	}


	public SurfacePlot createSurface(float[][] surface){
		return new SurfacePlot(surface, 20, Color.BLACK, Color.DARKGRAY, true, false);
	}


	/**
	 * Get group to add map providers to. 
	 * @return the map provider group. 
	 */
	public Group getProviderGroup() {
		return mapProviderGroup;
	}

	private void handleMouse(SubScene subScene, final Node root) {

		subScene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent me) {
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			}
		});
		
		subScene.setOnScroll(new EventHandler<ScrollEvent>() {
					double modifier = 1.0;
					double modifierFactor =1;
					@Override public void handle(ScrollEvent event) {
		            	System.out.println("Scroll Event: "+event.getDeltaX() + " "+event.getDeltaY()); 
						double z = camera.getTranslateZ();
						double newZ = z + event.getDeltaY() * modifierFactor * modifier*0.001*camera.getTranslateZ();
						camera.setTranslateZ(newZ); 
		            }
		        });


		subScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
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
					cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0);  // +
					cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0);  // -
				} else if (me.isSecondaryButtonDown()) {
					cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 3);  // -
					cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 3);  // -
				} else if (me.isMiddleButtonDown()) {
					cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 3);  // -
					cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 3);  // -
				}
			}
		});
	}

	private void handleKeyboard(SubScene subScene, final Node root) {
		final boolean moveCamera = true;
		subScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Duration currentTime;
				switch (event.getCode()) {
				case Z:
					if (event.isShiftDown()) {
						cameraXform.ry.setAngle(0.0);
						cameraXform.rx.setAngle(0.0);
						camera.setTranslateZ(-300.0);
					}
					cameraXform2.t.setX(0.0);
					cameraXform2.t.setY(0.0);
					break;
				case X:
					if (event.isControlDown()) {
						if (axisGroup.isVisible()) {
							axisGroup.setVisible(false);
						} else {
							axisGroup.setVisible(true);
						}
					}
					break;
				case S:
					//	                        if (event.isControlDown()) {
						//	                            if (moleculeGroup.isVisible()) {
					//	                                moleculeGroup.setVisible(false);
					//	                            } else {
					//	                                moleculeGroup.setVisible(true);
					//	                            }
					//	                        }
					break;
				case SPACE:
					if (timelinePlaying) {
						timeline.pause();
						timelinePlaying = false;
					} else {
						timeline.play();
						timelinePlaying = true;
					}
					break;
				case UP:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() - 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 10.0 * ALT_MULTIPLIER);
					} else if (event.isControlDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() - 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) { 
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 2.0 * ALT_MULTIPLIER);
					} else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z + 5.0 * SHIFT_MULTIPLIER;
						camera.setTranslateZ(newZ);
					}
					break;
				case DOWN:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() + 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 10.0 * ALT_MULTIPLIER);
					} else if (event.isControlDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() + 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 2.0 * ALT_MULTIPLIER);
					} else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z - 5.0 * SHIFT_MULTIPLIER;
						camera.setTranslateZ(newZ);
					}
					break;
				case RIGHT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() + 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 10.0 * ALT_MULTIPLIER);
					} else if (event.isControlDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() + 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 2.0 * ALT_MULTIPLIER);
					}
					break;
				case LEFT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() - 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 10.0 * ALT_MULTIPLIER);  // -
					} else if (event.isControlDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() - 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 2.0 * ALT_MULTIPLIER);  // -
					}
					break;
				}
			}
		});
	}


	//		 private void handleMouse(SubScene scene) {
	//    	
	//        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
	//			@Override public void handle(MouseEvent me) {
	//                mousePosX = me.getSceneX();
	//                mousePosY = me.getSceneY();
	//                mouseOldX = me.getSceneX();
	//                mouseOldY = me.getSceneY();
	//            }
	//        });
	//        
	//        scene.setOnScroll(new EventHandler<ScrollEvent>() {
	//
	//			@Override public void handle(ScrollEvent event) {
	//            	System.out.println("Scroll Event: "+event.getDeltaX() + " "+event.getDeltaY()); 
	//         	translate.setZ(translate.getZ()+  event.getDeltaY() *0.001*translate.getZ());   // + 
	//            }
	//        });
	//        
	//        
	//        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
	//
	//			@Override
	//            public void handle(MouseEvent me) {
	//                mouseOldX = mousePosX;
	//                mouseOldY = mousePosY;
	//                mousePosX = me.getSceneX();
	//                mousePosY = me.getSceneY();
	//                mouseDeltaX = (mousePosX - mouseOldX);
	//                mouseDeltaY = (mousePosY - mouseOldY);
	//
	//                double modifier = 1.0;
	//                double modifierFactor = 0.1;
	//
	//                if (me.isControlDown()) {
	//                    modifier = 50;
	//                }
	//                if (me.isShiftDown()) {
	//                    modifier = 0.1;
	//                }
	//                if (me.isPrimaryButtonDown()) {
	//                	rotateY.setAngle(rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0);  // +
	//                	rotateX.setAngle(rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0);  // -
	//                }
	//                if (me.isSecondaryButtonDown()) {
	//                	translate.setX(translate.getX() -mouseDeltaX * modifierFactor * modifier * 5);
	//                	translate.setY(translate.getY() - mouseDeltaY * modifierFactor * modifier * 5);   // +
	//                }
	//              
	//               
	//            }
	//        });
	//  }





}
