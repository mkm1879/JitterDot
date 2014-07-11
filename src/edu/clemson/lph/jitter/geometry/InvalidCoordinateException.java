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
public class InvalidCoordinateException extends Exception {
	private Double dCoord = null;
	private String sVarName;
	
	public InvalidCoordinateException() {
		
	}
	public InvalidCoordinateException( double dCoord, String sVarName ) {
		this.dCoord = dCoord;
		this.sVarName = sVarName;
	}
	public InvalidCoordinateException( String sMsg ) {
		this.sVarName = sMsg;
	}
	@Override
	public String getMessage() {
		if( sVarName != null && dCoord != null ) {
			return "Invalid coordinate " + dCoord + " for variable " + sVarName;
		}
		else if( sVarName == null && dCoord != null){
			return "Invalid coordinate " + dCoord;
		}	
		else if( sVarName != null && dCoord == null){
			return "Invalid coordinate " + sVarName;
		}	
		else 
			return "Invalid coordinate";
	}
	
}
