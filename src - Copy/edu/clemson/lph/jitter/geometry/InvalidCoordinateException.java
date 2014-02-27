package edu.clemson.lph.jitter.geometry;

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
