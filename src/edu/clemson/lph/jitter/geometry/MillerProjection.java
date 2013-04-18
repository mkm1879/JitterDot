package edu.clemson.lph.jitter.geometry;

public class MillerProjection {
	private static final double DEG_PER_RAD = 180.0/Math.PI;
	private double dCentralMeridian;
//	private static final double EARTH_RADIUS = 6371007.1810824;
	private static final double EARTH_RADIUS = 6378136.999;
	
	public MillerProjection( double dCentralMeridian ) throws InvalidCoordinateException {
		if( dCentralMeridian < -180.0 || dCentralMeridian > 180.0 ) {
			throw new InvalidCoordinateException( dCentralMeridian, "Central Meridian" );
		}
		this.dCentralMeridian = dCentralMeridian / DEG_PER_RAD;
	}
	
	public double[] project( double dLatDegrees, double dLongDegrees ) throws InvalidCoordinateException {
		if( dLatDegrees < -90.0 || dLatDegrees > 90.0 ) {
			throw new InvalidCoordinateException( dLatDegrees, "Latitude" );
		}
		if( dLongDegrees < -180.0 || dLongDegrees > 180.0 ) {
			throw new InvalidCoordinateException( dLatDegrees, "Longitude" );
		}
		double[] aCoords = new double[2];
		double x, y;
		double dLat = dLatDegrees / DEG_PER_RAD;
		double dLong = dLongDegrees / DEG_PER_RAD;
		
		x = dLong - dCentralMeridian;
		y = 1.25 * Math.log(Math.tan(0.25*Math.PI + 0.40*dLat));
		
		aCoords[0] = x * EARTH_RADIUS;
		aCoords[1] = y * EARTH_RADIUS;
		
		return aCoords;
	}

}
