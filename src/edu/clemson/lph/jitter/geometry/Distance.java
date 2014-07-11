package edu.clemson.lph.jitter.geometry;

import edu.clemson.lph.jitter.structs.WorkingData;
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
public class Distance {
	public static final Double DEG_PER_RAD = 180.0/Math.PI;
	public static final Double EARTH_RADIUS = 3963.1;
	public static final Double MILES_PER_DEGREE_LAT = 69.172;
	
	public static Double getDistance( Double dLat1Degrees, Double dLong1Degrees, Double dLat2Degrees, Double dLong2Degrees )
			 throws InvalidCoordinateException {
		Double dRet = null;
		if( dLat1Degrees == null ) {
			throw new InvalidCoordinateException( "null Latitude 1" );
		}
		if(	dLat1Degrees < -90.0 || dLat1Degrees > 90.0 ) {
			throw new InvalidCoordinateException( dLat1Degrees, "Latitude 1" );
		}
		if( dLong1Degrees == null ) {
			throw new InvalidCoordinateException( "null Longitude 1" );
		}
		if(dLong1Degrees < -180.0 || dLong1Degrees > 180.0 ) {
			throw new InvalidCoordinateException( dLong1Degrees, "Longitude 1" );
		}
		if( dLat2Degrees == null ) {
			throw new InvalidCoordinateException( "null Latitude 2" );
		}
		if( dLat2Degrees < -90.0 || dLat2Degrees > 90.0 ) {
			throw new InvalidCoordinateException( dLat2Degrees, "Latitude 2" );
		}
		if( dLong2Degrees == null ) {
			throw new InvalidCoordinateException( "null Longitude 2" );
		}
		if( dLong2Degrees < -180.0 || dLong2Degrees > 180.0 ) {
			throw new InvalidCoordinateException( dLong2Degrees, "Longitude 2" );
		}

		if( dLat1Degrees.equals(dLat2Degrees) && dLong1Degrees.equals(dLong2Degrees) )
			dRet = 0.0;
		else {
			Double dLat1 = dLat1Degrees / DEG_PER_RAD;
			Double dLong1 = dLong1Degrees / DEG_PER_RAD;
			Double dLat2 = dLat2Degrees / DEG_PER_RAD;
			Double dLong2 = dLong2Degrees / DEG_PER_RAD;
			if ( Math.sin(dLat1) * Math.sin(dLat2) + Math.cos(dLat1) * Math.cos(dLat2) * Math.cos(dLong1 - dLong2) > 1 ) {
				dRet = EARTH_RADIUS * Math.acos(1); 
			}
			else {
				dRet = EARTH_RADIUS * Math.acos(Math.sin(dLat1) * Math.sin(dLat2) + Math.cos(dLat1) * Math.cos(dLat2) * Math.cos(dLong1 - dLong2));
			}
		}
		return dRet;
	}
	
	public static Double milesPerDegreeLongitude( Double dLatitude ) {
		return (Distance.MILES_PER_DEGREE_LAT * Math.abs(Math.cos(dLatitude/Distance.DEG_PER_RAD)));
	}
	
	public static Double getMajorDistance( Double dLat1Degrees, Double dLong1Degrees, 
												Double dLat2Degrees, Double dLong2Degrees, int iSortDirection )
				 throws InvalidCoordinateException {
		Double dRet = null;
		if( iSortDirection == WorkingData.SORT_WEST_EAST ) 
			dRet = getDistance( dLat1Degrees, dLong1Degrees, dLat1Degrees, dLong2Degrees);
		else if( iSortDirection == WorkingData.SORT_SOUTH_NORTH ) 
			dRet = getDistance( dLat1Degrees, dLong1Degrees, dLat2Degrees, dLong1Degrees);
		return dRet;
		
	}

}
