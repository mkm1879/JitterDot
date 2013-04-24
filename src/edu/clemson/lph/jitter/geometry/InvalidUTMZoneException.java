package edu.clemson.lph.jitter.geometry;

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