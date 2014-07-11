package edu.clemson.lph.jitter.geometry;
/*
Copyright 2014 Michael K Martin

This file is part of JitterDot.

JitterDot is free software: you can redistribute it and/or modify
it under the terms of the Lesser GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JitterDot is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the Lesser GNU General Public License
along with JitterDot.  If not, see <http://www.gnu.org/licenses/>.
*/
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
