package reciever;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Simple hydrophone class which holds  co-ordinates for hydrophones and a sensitivity value. 
 * @author Jamie Macaulay 
 *
 */
public class Hydrophone {

	private final SimpleDoubleProperty x;

	private final SimpleDoubleProperty y;

	private final SimpleDoubleProperty z;

	private final SimpleDoubleProperty sens;

	public Hydrophone(double[] xyz, double sens){
		this.x = new SimpleDoubleProperty(xyz[0]);
		this.y = new SimpleDoubleProperty(xyz[1]);
		this.z = new SimpleDoubleProperty(xyz[2]);
		this.sens = new SimpleDoubleProperty(sens);
	}

	/**
	 * @return the x
	 */
	 public DoubleProperty xProperty() {
		 return x;
	 }

	 /**
	  * @return the y
	  */
	 public DoubleProperty yProperty() {
		 return y;
	 }

	 /**
	  * @return the z
	  */
	 public DoubleProperty zProperty() {
		 return z;
	 }

	 /**
	  * @return the sens
	  */
	 public DoubleProperty sensProperty() {
		 return sens;
	 }

	 /**
	  * @return the x
	  */
	 public double getX() {
		 return x.get();
	 }

	 /**
	  * @return the y
	  */
	 public double getY() {
		 return y.get();
	 }

	 /**
	  * @return the z
	  */
	 public double getZ() {
		 return z.get();
	 }

	 /**
	  * @return the sens
	  */
	 public double getSens() {
		 return sens.get();
	 }

	 /**
	  * @return the x
	  */
	 public void setX(double x) {
		 this.x.set(x);
	 }


	 /**
	  * @return the x
	  */
	 public void setY(double y) {
		 this.y.set(y);
	 }

	 /**
	  * @return the x
	  */
	 public void setZ(double z) {
		 this.z.set(z);
	 }

	 /**
	  * @return the sens
	  */
	 public void setSens(double sens) {
		 this.sens.set(sens);
	 }







}
