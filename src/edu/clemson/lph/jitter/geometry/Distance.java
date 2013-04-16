package edu.clemson.lph.jitter.geometry;

public class Distance {
	private static final double DEG_PER_RAD = 57.29577951;
	private static final double EARTH_RADIUS = 3963.1;
	
	public static double getDistance( double dLat1Degrees, double dLong1Degrees, double dLat2Degrees, double dLong2Degrees ) {
		double dRet = -1.0;
		if(dLat1Degrees == 0.0 || dLong1Degrees == 0.0 || dLat2Degrees == 0.0 || dLong2Degrees == 0.0 )
			dRet = -1.0;
		else if( dLat1Degrees ==  dLat2Degrees && dLong1Degrees == dLong2Degrees )
			dRet = 0.0;
		else {
			double dLat1 = dLat1Degrees / DEG_PER_RAD;
			double dLong1 = dLong1Degrees / DEG_PER_RAD;
			double dLat2 = dLat2Degrees / DEG_PER_RAD;
			double dLong2 = dLong2Degrees / DEG_PER_RAD;
			if ( Math.sin(dLat1) * Math.sin(dLat2) + Math.cos(dLat1) * Math.cos(dLat2) * Math.cos(dLong1 - dLong2) > 1 ) {
				dRet = EARTH_RADIUS * Math.acos(1); 
			}
			else {
				dRet = EARTH_RADIUS * Math.acos(Math.sin(dLat1) * Math.sin(dLat2) + Math.cos(dLat1) * Math.cos(dLat2) * Math.cos(dLong1 - dLong2));
			}
		}
		return dRet;
	}

}
