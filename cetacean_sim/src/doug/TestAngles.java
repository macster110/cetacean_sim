package doug;

import Jama.Matrix;
import utils.CetSimUtils;

public class TestAngles {

	public static void main(String[] args) {

		double[] hphone = {0., 0., 0.};
		double[] animalpos = {000., -1, 0};
		double hAng = -180;
		double vAng = -0;
		double[] animalAngle = {Math.toRadians(hAng), Math.toRadians(vAng)};

		//		for (int i = 1; i <= 6; i++) {
		double[] relAngles = getRelativeAngles(hphone, animalpos, animalAngle);
		System.out.printf("D Rel angles %3.1f, %3.1f\n", relAngles[0]*180./Math.PI, relAngles[1]*180./Math.PI);
		

		double[] jAngle = {Math.toRadians(hAng), Math.toRadians(vAng)};
		double[] relAngles2 = CetSimUtils.getRelativeAngle(hphone, animalpos, jAngle);
		System.out.printf("J Rel angles %3.1f, %3.1f\n", relAngles2[0]*180./Math.PI, relAngles2[1]*180./Math.PI);

	}
	
	/**
	 * Get relative angles between a rotated animal and receiver. 
	 * @param hPhone - the receiver position in Cartesian 3D [x, y, z] where z is height (not depth)
	 * @param animalPos - the receiver position in Cartesian 3D [x, y, z] where z is height (not depth) 
	 * @param animalAngles - the horizontal and vertical angle [horz, vert] in RADIANS
	 * @return the arelative horz and vertical angle in RADIANS. 
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
	private static Matrix getRotationMatrixQ(double[] animalAngles) {
		PamQuaternion pq = new PamQuaternion(animalAngles[0], 0, -animalAngles[1]);

		Matrix m = pq.toRotation();
		m = m.inverse();
		
		return m;
	}
	static Matrix getRotationMatrixD(double[] animalAngles) {
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
