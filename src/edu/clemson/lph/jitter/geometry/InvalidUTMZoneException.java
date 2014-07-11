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
@SuppressWarnings("serial")
public class InvalidUTMZoneException extends Exception {
	private int iZone = -1;
	private String sVarName;
	
	public InvalidUTMZoneException() {
		
	}
	
	public InvalidUTMZoneException( int iZone, String sVarName ) {
		this.iZone = iZone;
		this.sVarName = sVarName;
	}
	
	@Override
	public String getMessage() {
		if( sVarName != null ) {
			return "Invalid UTM Zone" + iZone + " for variable " + sVarName;
		}
		else {
			return "Invalid UTM Zone " + iZone;
		}			
	}
	
}