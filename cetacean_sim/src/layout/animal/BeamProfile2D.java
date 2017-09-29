package layout.animal;

import org.fxyz.utils.Palette;
import org.fxyz.utils.Palette.ColorPalette;

import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import layout.utils.ColourArray;
import layout.utils.ColourArray.ColourArrayType;


/**
 * Shows an example beam profile for a specifed source level. 
 * @author Jamie Macaulay 
 *
 */
public class BeamProfile2D extends BorderPane  {
		
	/**
	 * The canvas to draw on. 
	 */
	private Canvas canvas; 
	
	/**
	 * The maximum x value in meters
	 */
	public double xMin=-300;
	
	/**
	 * The minimum x value in meters
	 */
	public double xMax=+300; 
	
	
	/**
	 * The min y value in meters
	 */
	public double yMin=-200;
	
	/**
	 * The maximum y value in meters
	 */
	public double yMax=800;

	/**
	 * The writable image height
	 */
	int imageHeight=300;
	
	
	/**
	 * The writable image width
	 */
	int imageWidth=300;
	
	
	/**
	 * TYhe minimum dB for the colour map.
	 */
	public double dBMin=110;
	
	/**
	 * The maxiumum dB for the colour map. 
	 */
	public double dBMax=191; 

	/**
	 * The current writable image
	 */
	private WritableImage currentBeamImage;
	
	/**
	 * The z location relative to the animal
	 */
	public double zSlice=0;  
	
	/**
	 * The number of colours in the colour paletter
	 */
	int nColours=1000; 
	
	/**
	 * Colour array 
	 */
	public Palette colourArray= new Palette(1000,ColorPalette.JET);
	
	/**
	 * The animal orientation
	 */
	double[] animalOrientation={0,1,0};
	
	/**
	 * The colour array to use for the image
	 */
	private ColourArray currentColourArray = ColourArray.createStandardColourArray(100, ColourArrayType.HOT);


	public BeamProfile2D(){
		BorderPane holder = new BorderPane(); 
	
		canvas= new Canvas(50,50); 
		canvas.widthProperty().bind(holder.widthProperty());
		canvas.heightProperty().bind(holder.heightProperty());
		
		canvas.widthProperty().addListener((oldVal, newVal, obsVal)->{
			repaint();		
		});

		canvas.heightProperty().addListener((oldVal, newVal, obsVal)->{
			repaint();		
		});
		
		holder.setCenter(canvas);
		holder.setPrefWidth(300);
		
		NumberAxis yAxis = new NumberAxis(); 
		yAxis.setLowerBound(yMin);
		yAxis.setUpperBound(yMax);
		yAxis.setTickUnit(50);
		yAxis.setLabel("y(m)");
		yAxis.setAutoRanging(false);

		yAxis.setSide(Side.LEFT);
		
		NumberAxis xAxis = new NumberAxis(); 
		xAxis.setLowerBound(xMin);
		xAxis.setUpperBound(xMax);
		xAxis.setSide(Side.BOTTOM);
		xAxis.setTickUnit(50);
		xAxis.setLabel("y(m)");
		xAxis.setAutoRanging(false);
		
		//have to ave x Axis holder becuas eof the way Border Pane works out.  
		Pane corner = new Pane(); 
		corner.prefWidthProperty().bind(yAxis.widthProperty());
		HBox.setHgrow(xAxis, Priority.ALWAYS);
		HBox hbox=new HBox(corner, xAxis);
		hbox.prefWidthProperty().bind(holder.widthProperty());
		
		this.setLeft(yAxis);
		this.setBottom(hbox);


		this.setCenter(holder);
		
//		Button test = new Button("Hello"); 
//		test.setOnAction((action)->{
//			repaint();
//		});
//		this.setBottom(test);

	}
	
	/**
	 * Reapint the canvas. 
	 */
	private void repaint() {
		canvas.getGraphicsContext2D().drawImage(currentBeamImage, 0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	
	
	/**
	 * Set the surface for the sphere. 
	 * @param gridder2
	 */
	public void setSurface(BeamProfile bp){
		createWritableImage(bp);		
		repaint(); 
	}
	
	/**
	 * Create an image of recieved level.
	 * @return a writable image shwoing teh beam profile surface 
	 */
	private WritableImage createWritableImage(BeamProfile bp){
	
		double minsl=100; 
		double maxsl=160; 
		
		double sourcellevel= 191; 
		
		currentBeamImage=new WritableImage(imageWidth, imageHeight); 
		
		//so we don;t have to keep calulating
		double xmeters=xMax-xMin; 
		double ymeters=yMax-yMin; 

		double[] receiverPos=new double[3];
		receiverPos[2]=zSlice;
		double[] animalPos=new double[3];
		double tl; 
		int x; 
		int y; 
		double r; 
		double slCol; 

		//now need to write the image. 
		for (int i=0; i<imageWidth; i++){
			for (int j=0; j<imageHeight; j++){	
				
				//now how does this image translate. 
				x = (int) (xmeters*((double) i/imageWidth)+xMin);
				y=(int) (ymeters*((double) j/imageHeight)+yMin);
				
				receiverPos[0]=x;
				receiverPos[1]=y; 

				tl=bp.getBeamTL(receiverPos, animalPos, animalOrientation); 
				
				r=Math.sqrt(Math.pow(receiverPos[0],2)+Math.pow(receiverPos[1],2)+Math.pow(receiverPos[2],2));
				
				tl=20*Math.log10(r)+0.04*r + tl; 
				
				double sl=sourcellevel-tl;
				if (sl<minsl) sl=minsl; 
				if (sl>maxsl) sl=maxsl; 
				
				//find value for colour array between 0 and 1.
				slCol=(sl-minsl)/(maxsl-minsl); 
				
//				System.out.println(" x(m) " + x+ " y(m) "+ y+" sl: "+sl + " color: "+(int) (255*(191/sl))+ " "+(imageHeight-j));
				
				currentBeamImage.getPixelWriter().setColor(i, imageHeight-j-1, currentColourArray.getColour(slCol));
//				currentBeamImage.getPixelWriter().setColor(i, imageHeight-j-1, colourArray.getColor(i));

			}
		}
		
		return currentBeamImage;
	}

}


