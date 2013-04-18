package edu.clemson.lph.jitter.geometry;

@SuppressWarnings("serial")
public class InvalidCoordinateException extends Exception {
	private double dCoord = -1;
	private String sVarName;
	
	public InvalidCoordinateException() {
		
	}
	public InvalidCoordinateException( double dCoord, String sVarName ) {
		this.dCoord = dCoord;
		this.sVarName = sVarName;
	}
	@Override
	public String getMessage() {
		if( sVarName != null ) {
			return "Invalid coordinate " + dCoord + " for variable " + sVarName;
		}
		else {
			return "Invalid coordinate";
		}
			
	}
	
}
