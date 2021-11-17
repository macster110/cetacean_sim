package doug;

import Jama.Matrix;
import utils.CetSimUtils;
import utils.Vector;

public class AnimalAngles {

	public static void main(String[] args) {

//		double[] hphone = {0., 0., 0.};
//		double[] animalpos = {000., -1, 0};
//		double hAng = -180;
//		double vAng = -0;
//		double[] animalAngle = {Math.toRadians(hAng), Math.toRadians(vAng)};
//
//		//		for (int i = 1; i <= 6; i++) {
//		double[] relAngles = getRelativeAngles(hphone, animalpos, animalAngle);
//		System.out.printf("D Rel angles %3.1f, %3.1f\n", relAngles[0]*180./Math.PI, relAngles[1]*180./Math.PI);
//		
//
//		double[] jAngle = {Math.toRadians(hAng), Math.toRadians(vAng)};
//		double[] relAngles2 = CetSimUtils.getRelativeAngle(hphone, animalpos, jAngle);
//		System.out.printf("J Rel angles %3.1f, %3.1f\n", relAngles2[0]*180./Math.PI, relAngles2[1]*180./Math.PI);
		
		
		//TEST 1
		//the output angles should be  horz/vert = 158.1/-11.5 degrees (correspond to MATLAB code.)
		double[] hPhone = new double[] {0,0,0}; 
		double[] animalPos = new double[] {0,10,0}; 
		double[] animalAngles = new double[] {2,0,0.5}; 
				
		double[] relAngles =  getRelativeAngles2(hPhone, animalPos, animalAngles);
		
		System.out.printf("J Rel angles %3.1f, %3.1f\n", relAngles[0]*180./Math.PI, relAngles[1]*180./Math.PI);
		
	}
	
	
	
	/**
	 * Get relative angles between a rotated animal (heading, pitch AND roll) and receiver. 
	 * <p>
	 * <ul>
	 * Including roll in the angle is quite complicated. The way to do this is as follows;
	 * <p>
	 * <li> Create a vector orthogonal to the pointing vector of the porpoise (heading and pitch)</li>
	 * <li> Rotate the orthogonal vector by the roll around the pointing vector</li>
	 * <li> The rotated orthogonal vector and the position of the porpoise define a plane. Calculating the intersection
	 * 	of the vector from the propose to receiver with the plane. is now required </li>
	 * </ul>
	 * @param hPhone - the receiver position in Cartesian 3D [x, y, z] where z is height (not depth)
	 * @param animalPos - the receiver position in Cartesian 3D [x, y, z] where z is height (not depth) 
	 * @param animalAngles - the horizontal and vertical angle [horizontal, vertical, roll] in RADIANS. Note that roll cannot be zero. 
	 * @return the relative horizontal and vertical angles in RADIANS. 
	 */
	public static double[] getRelativeAngles2(double[] hPhone, double[] animalPos, double[] animalAngles) {
				
		//calculate a vector between the animal and hydrophone
		double[] vecHyArrd= new double[3];
		for (int i = 0; i < 3; i++) {
			vecHyArrd[i] = hPhone[i]-animalPos[i];
		}
		
		Vector vecHyd = new Vector(vecHyArrd); 
		//System.out.printf("vecHyd %3.1f, %3.1f, %3.1f\n", vecHyd.getX(), vecHyd.getY(), vecHyd.getZ());

		//work out the pointer vector of the porpoise based on the animal angles. 
		double[] vecPorpArr = animalAngles2Vector(animalAngles[0], animalAngles[1]); 
		Vector vecPorp = new Vector(vecPorpArr); 
		
		//System.out.printf("vecPorp %3.3f, %3.3f, %3.3f\n", vecPorp.getX(), vecPorp.getY(), vecPorp.getZ());

		
		//calculate the plane from the heading pitch and roll. 
		Vector plane = animalAngles2Plane(vecPorp, animalAngles[2]); 
		
		//System.out.printf("plane %3.1f, %3.1f, %3.1f\n", plane.getX(), plane.getY(), plane.getZ());
		
		//project the hydrophone vector onto the plane
		//E = cross(C, cross(vechyd, C)); 
		//E=E/norm(E);
		Vector  E = plane.crossProduct(vecHyd.crossProduct(plane)).normalize();
	
		//now use the projections to calculate the angles
		//Vertical angle
		double vertAng = Math.acos(E.dot(vecHyd)/(E.magnitude()*vecHyd.magnitude()));
	
		Vector vecHydN = vecHyd.normalize(); 
		if (vecHydN.getZ()<E.getZ()) {
			vertAng=-vertAng; 
		}
		
		//horizontal angle
		double horzAng = -Math.atan2(E.crossProduct(vecPorp).dot(plane), E.dot(vecPorp));
		
		//return the horizontal and veetical angle in RADIANS. 
		return new double[] {horzAng, vertAng};		
	}
	
	/**
	 * Calculate a plane from the heading, pitch and roll. 
	 * @param animalAngles - the heading pitch and roll in RADIANS. 
	 * @return a vector orthogonal to the plane (represents the plane)
	 */
	public static Vector animalAngles2Plane(Vector vecPorp, double roll) {

		//calculate an orthogonal vector to the propose pointing vector.
		Vector vecPorpOrth;
		if (vecPorp.getY()!=0) {
			 vecPorpOrth = new Vector(1,-vecPorp.getX()/vecPorp.getY(), 0);
		}
		else {
			 vecPorpOrth = new Vector(1,0,0); //is this right?
		}

//		System.out.printf("vecPorp %3.1f, %3.1f, %3.1f\n", vecPorp.getX(), vecPorp.getY(), vecPorp.getZ());
		//System.out.printf("vecPorpOrth %3.3f, %3.3f, %3.3f\n", vecPorpOrth.getX(), vecPorpOrth.getY(), vecPorpOrth.getZ());

		//rotate the orthogonal vector about the porpoise pointing vector
		Vector vRot = rotateVectorCC(vecPorpOrth,  vecPorp, roll); 
		
		//System.out.printf("vRot %3.3f, %3.3f, %3.3f\n", vRot.getX(), vRot.getY(), vRot.getZ());

		Vector plane = vRot.crossProduct(vecPorp).normalize();

		return plane; 
	}
	
	/**
	 * Rotate one vector vec around a unit vector axis by an angle theta. Similar to Rodrigues formula. 
	 * @param vec - the vector to rotate (3D).
	 * @param axis - the axis to roate around (3D).
	 * @param theta - the angle to rotate by in RADIANS
	 * @return the rotated vector. 
	 */
	public static Vector rotateVectorCC(Vector vec, Vector axis, double theta){
	    double x, y, z;
	    double u, v, w;
	    
	    x=vec.getX();y=vec.getY();z=vec.getZ();
	    u=axis.getX();v=axis.getY();w=axis.getZ();
	    
//	    System.out.println(String.format("x %.3f y %.3f z %.3f u %.3f v %.3f w %.3f", x,y,z,u,v,w));
	    
	    double xPrime = u*(u*x + v*y + w*z)*(1d - Math.cos(theta)) 
	            + x*Math.cos(theta)
	            + (-w*y + v*z)*Math.sin(theta);
	    double yPrime = v*(u*x + v*y + w*z)*(1d - Math.cos(theta))
	            + y*Math.cos(theta)
	            + (w*x - u*z)*Math.sin(theta);
	    double zPrime = w*(u*x + v*y + w*z)*(1d - Math.cos(theta))
	            + z*Math.cos(theta)
	            + (-v*x + u*y)*Math.sin(theta);
	    
//	    System.out.println(String.format("xPrime %.3f yPrime %.3f zPrime %.3f ", xPrime,yPrime,zPrime));

	    return new Vector(xPrime, yPrime, zPrime);
	}
	
	
	/**
	 * Get relative angles between a rotated animal (heading and pitch with NO roll) and receiver. 
	 * @param hPhone - the receiver position in Cartesian 3D [x, y, z] where z is height (not depth)
	 * @param animalPos - the receiver position in Cartesian 3D [x, y, z] where z is height (not depth) 
	 * @param animalAngles - the horizontal and vertical angle [horz, vert] in RADIANS
	 * @return the are relative horizontal and vertical angle in RADIANS. 
	 */
	public static double[] getRelativeAngles(double[] hPhone, double[] animalPos, double[] animalAngles) {
		
		Matrix m = getRotationMatrixQ(animalAngles);
		
		double animalToPhone[] = new double[3];
		for (int i = 0; i < 3; i++) {
			animalToPhone[i] = hPhone[i]-animalPos[i];
		}
		
//		m.print(new DecimalFormat("#.##"),6);
		
		Matrix pointer = new Matrix(animalToPhone, 3);
		Matrix rotMat = m.times(pointer);
		double[] rotPos = rotMat.getRowPackedCopy(); 		

		double vecMag = 0;
		for (int i = 0; i < 3; i++) {
			vecMag += Math.pow(rotPos[i], 2);
		}
		vecMag = Math.sqrt(vecMag);
		double rYZ = Math.sqrt(Math.pow(rotPos[1], 2) + Math.pow(rotPos[2], 2)); 
		double ang1 = Math.atan2(rotPos[1],rotPos[0]);
		//		if (rotPos[1] < 0) ang1 = -ang1;
		double ang2 = Math.asin(rotPos[2]/vecMag);
		if (ang2 > Math.PI/2) ang2 -= Math.PI;
		else if (ang2 < -Math.PI/2) ang2 += Math.PI; 
		double[] rotatedAngles = {ang1, ang2};
		// now we have a rotated vector from the animal to the hydrophone in animal coordinates. 
//		System.out.printf(" Un rot vector = %3.2f,%3.2f,%3.2f\n", animalToPhone[0], animalToPhone[1], animalToPhone[2]);
//		System.out.printf("Rotated vector = %3.2f,%3.2f,%3.2f\n", rotPos[0], rotPos[1], rotPos[2]);
		return rotatedAngles;

	}
	
	
	
	/**
	 * Determines the point of intersection between a plane defined by a point and a normal vector and a line defined by a point and a direction vector.
	 *
	 * @param planePoint    A point on the plane.
	 * @param planeNormal   The normal vector of the plane.
	 * @param linePoint     A point on the line.
	 * @param lineDirection The direction vector of the line.
	 * @return The point of intersection between the line and the plane, null if the line is parallel to the plane.
	 */
	public static Vector lineIntersection(Vector planePoint, Vector planeNormal, Vector linePoint, Vector lineDirection) {
	    if (planeNormal.dot(lineDirection.normalize()) == 0) {
	        return null;
	    }

	    double t = (planeNormal.dot(planePoint) - planeNormal.dot(linePoint)) / planeNormal.dot(lineDirection.normalize());
	    return linePoint.plus(lineDirection.normalize().scale(t));
	}


	
	/**
	 * Convert a pitch and yaw to a pointing vector
	 * @param yaw - the yaw
	 * @param pitch - the pitch
	 * @return the 
	 */
	public static double[] animalAngles2Vector(double yaw, double pitch) {
		double[] vec = new double[3]; 
		vec [0]= Math.cos(yaw)*Math.cos(pitch);
		vec[1] = Math.sin(yaw)*Math.cos(pitch);
		vec[2] = Math.sin(pitch);
		return vec; 
	}
	
	/**
	 * Get the rotation matrix based on creating a quaternion and then converting it to a Matrix. 
	 * @param animalAngles - the animal angles,heading and pitch in RADIANS. 
	 * @return the rotation matrix corresponding to the animalAngles. 
	 */
	private static Matrix getRotationMatrixQ(double[] animalAngles) {
		PamQuaternion pq = new PamQuaternion(animalAngles[0], 0, -animalAngles[1]);

		Matrix m = pq.toRotation();
		m = m.inverse();
		
		return m;
	}
	
	/**
	 * Get a rotation matrix from heading and pitch. 
	 * @param animalAngles - heading and pitch. 
	 * @return the rotation matrix. 
	 */
	public static Matrix getRotationMatrixD(double[] animalAngles) {
		double ch = Math.cos(animalAngles[0]);
		double sh = Math.sin(animalAngles[0]);
		double cp = Math.cos(animalAngles[1]);
		double sp = Math.sin(animalAngles[1]);
		double[][] rHP = {{ch, sh*cp, -sh*sp}, {-sh, ch*cp,-ch*sp}, {0, sp, cp}}; // head then pitch
		double[][] rPH = {{ch, sh, 0},{-sh*cp, ch*cp, -sp}, {-sh*sp, ch*sp, cp}}; // pitch then head
		double[][] rH = {{ch, sh, 0},{-sh, ch, 0}, {1, 0, 0}}; // head only
		double[][] rP = {{1., 0, 0}, {0, cp, -sp}, {0, sp, cp}}; // pitch only


		Matrix m = new Matrix(rPH);
		return m;
	}
	
	


}
