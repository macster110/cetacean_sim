package utils;


import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;



/**
 * Class for handling and formatting latitude and longitude data.
 * Only contains two variables, which are assumed to be in decimal degrees. 
 * Also contains a number of other static variables for formatting
 * other lat long data. 
 * @author Doug Gillespie
 *
 */
public class LatLong implements Serializable, Cloneable, Transferable {

	static final long serialVersionUID = 1;
	
	transient public static final String deg="\u00B0";
	transient private static final String min="'";
	transient private static final String sec="\"";
	transient private static final String pt=".";
	transient private static DataFlavor latLongFlavor = createDataFlavor();//this needs to be moved to pam controller or something that will DEFINITELY only create it once
	
//	transient public static final int latDegMax=90,latDegMin=-90;
//	transient public static final int lonDegMax=180,lonDegMin=-180;
//	transient public static final int minNsecMax=60,minNsecMin=0;
	
	public transient static final double MetersPerMile = 1852.;
	/**
	 *  latitude in decimal degrees (North is positive; -90 <= latitude <= +90)
	 */
	protected double latitude;
	/**
	 *  longitude in decimal degrees (East is positive; -180 < longitude <= +180)
	 */
	protected double longitude;
	
	/**
	 * Using height rather than depth since it's less ambiguous, <p>
	 * i.e. height is ALWAYS the distance up from the sea surface, so real 
	 * depth values will always be a negative height. 
	 */
	protected double height;
	
	
	static public final int FORMAT_DECIMALMINUTES = 1;
	static public final int FORMAT_MINUTESSECONDS = 2;
	static private int formatStyle = FORMAT_DECIMALMINUTES;
	
	
	/**
	 * @return Returns the formatStyle.
	 */
	public static int getFormatStyle() {
		return formatStyle;
	}

	public static DataFlavor getDataFlavor(){
		return latLongFlavor;
	}
	private static DataFlavor createDataFlavor(){
		String latLongType = DataFlavor.javaJVMLocalObjectMimeType +
                "; class=utils.LatLong";
		
//		DataFlavor latLongFlavor=new 
//				DataFlavor(LatLong.class, "LatLong");
//		latLongFlavor=new DataFlavor("text/plain; charset=ASCII", "Plain ASCII text");
//	    latLongFlavor = new DataFlavor(java.awt.TextField.class, "AWT TextField");
	    
		try {
			latLongFlavor = new DataFlavor(latLongType);
			latLongFlavor.setHumanPresentableName("LatLongClass");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return latLongFlavor;
	}
	
	
	/**
	 * @param formatStyle The formatStyle to set.
	 */
	public static void setFormatStyle(int formatStyle) {
		LatLong.formatStyle = formatStyle;
	}
	
	

	public LatLong(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public LatLong(double latitude, double longitude, double height) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.height = height;
	}
	
	public LatLong() {
		this.latitude = 0;
		this.longitude = 0;
	}
	
	/**
	 * USE WITH CAUTION uses
	 * LatLong.valueOfSubstring
	 * @param LatLong
	 */
	public LatLong(String string) throws NullPointerException{
		
		String lat=string.substring(0,string.indexOf(","));
		String lon=string.substring(string.indexOf(",")+1);
		
		if (string.indexOf(",")==-1){
			throw new NullPointerException("The Lat and Long are not separated by a comma");
		}
		
		Double Lat = valueOfSubstring(lat);
		Double Lon = valueOfSubstring(lon);
		if (Lat==null|Lon==null){
			throw new NullPointerException("Either the Latitude or Longitude cannot be converted to double");
		}
		
		this.latitude=Lat;
		this.longitude=Lon;
		
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public LatLong clone() {
		try {
			return (LatLong) super.clone();
		}
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @return Returns the latitude.
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude The latitude to set.
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
	/**
	 * @return Returns the longitude.
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude The longitude to set.
	 */
	public void setLongitude(double longitude) {
		if (Double.isInfinite(longitude)) longitude = Double.NaN; 
		while (longitude > 180) longitude -= 360.;
		while (longitude <= -180) longitude += 360.; 
		this.longitude = longitude;
	}
	
	/**
	 * formats the latitude string
	 * @return formated latitude string
	 */
	public String formatLatitude()
	{
		return formatLatitude(latitude);
	}
	/**
	 * convert a double decimal degrees into a formatted latlong string
	 * @param latlong
	 * @return a string representation of a Latitude
	 */
	public static String formatLatitude(double latlong) {
		if (latlong < 0) {
			return formatLatLong(latlong) + " S";
		}
		else  {
			return formatLatLong(latlong) + " N";
		}		
	}
	
	/**
	 * must be separated by comma, then see valueOfSubstring
	 * 
	 * @param string
	 * @return LatLong
	 */
	public static LatLong valueOfString(String string){
		
		String lat=string.substring(0,string.indexOf(","));
		String lon=string.substring(string.indexOf(",")+1);
		
		return new LatLong(valueOfSubstring(lat), valueOfSubstring(lon));
	}
	
	/**
	 * must contain degree sign �
	 * 
	 * -5� S will produce a north value
	 * if neither N, S or - will assume positive
	 * 
	 * 57�19.800' N or  10�48.000' W
	 * 
	 * @param string
	 * @return the angle
	 */
	public static Double valueOfSubstring(String angleString){
		
		double value = 0;
		boolean negate=false;
		if (angleString==null) return null;
//		57�19.800' N, 10�48.000' W
		String string=angleString.replaceAll(" ", "").toUpperCase();

		if (string.length()==0) return null;
//		57�19.800'N
//		System.out.println("a:"+string+":Val:"+value);
		if (string.endsWith("S")||string.endsWith("W")){
			negate=true;
		}
//		System.out.println("b:"+string+":Val:"+value);
		try{
			value=Double.valueOf(string.substring(0,string.indexOf(deg)));
			string = string.substring(string.indexOf(deg)+1);
//			System.out.println("c:"+string+":Val:"+value);
			
			value+=Double.valueOf(string.substring(0,string.indexOf(min)))/60;
			string = string.substring(string.indexOf(min)+1);
//			System.out.println("d:"+string+":Val:"+value);
			
			value+=Double.valueOf(string.substring(0,string.indexOf(sec)))/60;
			string = string.substring(string.indexOf(sec)+1);
//			System.out.println("e:"+string+":Val:"+value);
			
		}catch (IndexOutOfBoundsException e){
//			return Double.NaN;
		}catch (ClassCastException c){
//			return Double.NaN;
		}catch (NumberFormatException nf) {
		}finally{
					
		}
		
		if (negate){
			value=-value;
		}
		return value;
	}
	
	/**
	 * formats the longitude as a string
	 * @return formatted longitude string
	 */
	public String formatLongitude()
	{
		return formatLongitude(longitude);
	}
	/**
	 * convert a double decimal degrees into a formatted latlong string
	 * @param latlong
	 * @return a string representation of a Longitude
	 */
	public static String formatLongitude(double latlong) {
		if (latlong < 0) {
			return formatLatLong(latlong) + " W";
		}
		else  {
			return formatLatLong(latlong) + " E";
		}
	}
	
	private static String formatLatLong(double latLong, int style) {
		switch (style) {
		case FORMAT_DECIMALMINUTES:
			return formatLatLongDDM(latLong);
		case FORMAT_MINUTESSECONDS:
			return formatLatLongDMS(latLong);
		}
		return null;
	}
	
	private static String formatLatLongDMS(double latLong) {
		int d = Math.abs(getSignedDegrees(latLong));
		int m = getIntegerMinutes(latLong);
		double s = getSeconds(latLong);
		return String.format("%2d\u00B0%02d\'%04.1f\"", d, m, s);
	}
	
	private static String formatLatLongDDM(double latlong) {
		latlong = Math.abs(latlong);
		int degrees = (int) Math.floor(latlong);
		double minutes = (latlong - degrees) * 60;
		return String.format("%2d\u00B0%06.3f\'", degrees, minutes);
	}
	
	private static String formatLatLong(double latlong) {
		return formatLatLong(latlong, formatStyle);
	}
	
	public static String doubleDegrees2StringDegs(double latlong){
		double degrees =  Math.signum(latlong)*Math.floor(Math.abs(latlong));
		NumberFormat formatter = new DecimalFormat("0");
		return formatter.format(degrees);
	}
	
	public static String doubleDegrees2StringMins(double latlong){
		double degrees =  Math.signum(latlong)*Math.floor(Math.abs(latlong));
		double minutes = Math.abs((latlong - degrees) * 60);
		NumberFormat formatter = new DecimalFormat(".0");
		return formatter.format(minutes);
	}
	
	public static int getSignedDegrees(double latLong) {
		return (int) (Math.signum(latLong)*Math.floor(Math.abs(latLong)));
	}
	
	public static double getDecimalMinutes(double latLong) {
		double degrees = getSignedDegrees(latLong);
		return Math.abs((latLong - degrees) * 60);
	}
	
	/*
	 * Get some slightly weird rounding errors in al this which tends
	 * to make minutes 0 and seconds = 60, when we really 
	 * want 1 minute and 0 seconds. So need to slightly bump up the
	 * minutes before flooring them, then in the seconds calculation
	 * assume that they can never be negative, so set a minumium
	 * value for them
	 */
	public static int getIntegerMinutes(double latLong) {
		return (int) Math.floor(getDecimalMinutes(latLong)+.00000001);
	}
	
	public static double getSeconds(double latLong) {
		double decMinutes = getDecimalMinutes(latLong);
		double intMinutes = getIntegerMinutes(latLong);
		return Math.max((decMinutes - intMinutes) * 60., 0.);
	}

	/**
	 * @param trueHeading
	 *            Heading relative to True North -degrees
	 * @param distance
	 *            Distance to travel in meters.
	 * @return New Latlong in decimal degrees
	 */
	public LatLong travelDistanceMeters(double trueHeading,
			double distance) {
		return TravelDistanceMiles(trueHeading, distance / MetersPerMile);
	}

	/**
	 * @param trueHeading
	 *            Heading relative to True North- degrees
	 * @param distance
	 *            Distance to travel in nautical miles.
	 * @return New Latlong in decimal degrees
	 */
	public LatLong TravelDistanceMiles(double trueHeading,
			double distance) {
		LatLong newLatLong = new LatLong(this.latitude,
				this.longitude);
		double angle = Math.PI / 180. * (90. - trueHeading); // easier to
																// think like
																// this !
		double longDistortion = Math.cos(this.latitude * Math.PI / 180.);
		newLatLong.longitude += Math.cos(angle) * distance / 60.
				/ longDistortion;
		newLatLong.latitude += Math.sin(angle) * distance / 60.;

		// System.out.println("LatLongDegrees, latDegs: " + newLatLong.latDegs +
		// " longDegs: " + newLatLong.longDegs);

		return newLatLong;
	}
	
	double metersToDegreeLatitude(double meters) {
		return meters / 60. / LatLong.MetersPerMile;
	}
	
	double metersToDegreeLongitude(double meters, double latitudeDegrees) {
		double fac = Math.cos(latitudeDegrees * Math.PI / 180.);
		return meters / 60. / LatLong.MetersPerMile / fac;
	}
	
	/**
	 * Add distances in metres East and metres North to a LatLong
	 * <br> Note that this assumes a flat earth model away from poles, so 
	 * it's possible to create stupid values. It's intended for small 
	 * localised shifts in the vicinity of a single detection. 
	 * @param addX distance to travel East in metres
	 * @param addY distance to travel North in metres
	 * @return new LatLong
	 */
	public LatLong addDistanceMeters(double addX, double addY) {
		LatLong newLatLong = this.clone();
		newLatLong.latitude += metersToDegreeLatitude(addY);
		newLatLong.longitude += metersToDegreeLongitude(addX, (this.latitude + newLatLong.latitude)/2.);
		return newLatLong;
	}
	/**
	 * Calculate the distance from this positions to some other
	 * position in miles.
	 * @param destination destiantion position
	 * @return distance in miles
	 */
	public double distanceToMiles(LatLong destination) {
		double dist = Math.pow(60 * (latitude - destination.latitude), 2);
		double meanLat = (latitude + destination.latitude) / 2 * Math.PI / 180;
		double dist2 = Math.pow(60 * Math.cos(meanLat) * (longitude - destination.longitude), 2);
		return Math.sqrt(dist + dist2);
	}
	/**
	 * Calculate the distance from this positions to some other
	 * position in metres.
	 * @param destination destination position
	 * @return distance in metres
	 */
	public double distanceToMetres(LatLong destination) {
		return distanceToMiles(destination) * CetSimConstants.METERS_PER_MILE;
	}
	
	/**
	 * Calculate the distance from this positions to some other
	 * position in three dimensions (i.e. including any height or depth changes) in metres.
	 * @param destination destination position
	 * @return distance in metres
	 */
	public double distanceToMetres3D(LatLong destination) {
		double a = distanceToMetres(destination);
		double b = this.height - destination.height;
		if (b == 0) return a;
		else return Math.sqrt(a*a + b*b);
	}
	
	/**
	 * Calculate the distance in metres along the X coordinate from the lat long to 
	 * some other lat long.  
	 * @param destination destination coordinate
	 * @return distance in metres
	 */
	public double distanceToMetresX(LatLong destination) {
		double longDist = (destination.getLongitude() - getLongitude()) * 
			getLongDistanceCorr(destination.getLatitude(), getLatitude());
		return longDist * CetSimConstants.METERS_PER_MILE * 60;
	}	
	/**
	 * Calculate the distance in metres along the Y coordinate from the lat long to 
	 * some other lat long.  
	 * @param destination destination coordinate
	 * @return distance in metres
	 */
	public double distanceToMetresY(LatLong destination) {
		double latDist = destination.getLatitude() - getLatitude();
		return latDist * CetSimConstants.METERS_PER_MILE * 60;
	}

	public boolean equals(LatLong ll) {
		return (ll.latitude == latitude && ll.longitude == longitude);
	}
	
	public static LatLong getMean(LatLong[] lls){
		double lat=0;
		double lon=0;
		double height=0;
		int no=lls.length;
		for (int i=0;i<no;i++){
			lat+=lls[i].latitude;
			lon+=lls[i].longitude;
			height+=lls[i].height;
		}
		
		
		return new LatLong(lat/no, lon/no, height/no);
		
	}
	
	/**
	 * Get the bearing in degree to this latlong to another latlong. 
	 * <br>Does not do a full great circle calculation, but uses rectangular trigonometry.   
	 * @param destination destination lat long
	 * @return angle to destination in degrees. 
	 */
	public double bearingTo(LatLong destination) {
		double latDist = destination.getLatitude() - getLatitude();
		double longDist = (destination.getLongitude() - getLongitude()) * 
		getLongDistanceCorr(destination.getLatitude(), getLatitude());
		double angle = Math.atan2(latDist, longDist) * 180. / Math.PI;
		angle = 90-angle;
		if (angle < 0) angle += 360;
		return angle;
	}
	
	private double getLongDistanceCorr(double latitude) {
		return Math.cos(latitude * Math.PI / 180.);
	}
	private double getLongDistanceCorr(double latitude1, double latitude2) {
		return getLongDistanceCorr((latitude1+latitude2)/2);
	}
	
	/**
	 * Clculate the perpendicular distance from this LatLong
	 * to a line joining latLong1 and latLong2
	 * @return cross track error in metres
	 */
	public double getCrossTrackError(LatLong latLong1, LatLong latLong2) {
		double lineBearing = latLong1.bearingTo(latLong2);
		double hereBearing = latLong1.bearingTo(this);
		double angDiff = lineBearing - hereBearing;
		double distHere = latLong1.distanceToMetres(this);
		return Math.abs(distHere * Math.sin(angDiff * Math.PI / 180));
	}

	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return formatLatitude() + ", " + formatLongitude();
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
//		return this;
		if (flavor==getDataFlavor()){
			return this;
		}else if(flavor.equals(DataFlavor.stringFlavor)){
			return this.toString();
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		DataFlavor[] flavors = new DataFlavor[2];
		flavors[0] =getDataFlavor();
		flavors[1] =DataFlavor.stringFlavor;
		return flavors;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
//		System.out.println("flavors equal: "+flavor.equals(getDataFlavor()));
		if(flavor.equals(getDataFlavor())){
//			System.out.println("flavors good");
			return true;
//		}else if(flavor==DataFlavor.plainTextFlavor){
//			return true;
//		}else if (flavor==DataFlavor.stringFlavor){
//			return true;
		}
		
		if (flavor.equals(DataFlavor.stringFlavor)){
//			System.out.println("String flav good");
			return true;
		}
		return false;
	}
	
	public static LatLong getPastedValue() {
		
		Clipboard clipboard =Toolkit.getDefaultToolkit().getSystemClipboard();
		

//		DataFlavor[] a = clipboard.getContents(null).getTransferDataFlavors();
		boolean ans = clipboard.getContents(null).isDataFlavorSupported(LatLong.getDataFlavor());
		if (ans == false) {
			return null;
		}
		
		//		clipboard.
		Object data =null;
		LatLong dataVal=null;
		Transferable data2=null;

		data2= clipboard.getContents(null);
		try {
			//				System.out.println("d2Cls:"+data2.getTransferData(LatLong.getDataFlavor()).getClass());
			data= data2.getTransferData(LatLong.getDataFlavor());
			dataVal=(LatLong)data;
			return dataVal;
		} catch (UnsupportedFlavorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("CBLLdatV:"+dataVal);

		try {
			data = clipboard.getData(DataFlavor.stringFlavor);
			System.out.println("CBStdata:"+data);
			dataVal= new LatLong((String)data);
			System.out.println("CBStdatV:"+dataVal);
		} catch (UnsupportedFlavorException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassCastException e3) {
			// TODO: handle exception
			e3.printStackTrace();
		}
		
		return dataVal;
	}
	
}
