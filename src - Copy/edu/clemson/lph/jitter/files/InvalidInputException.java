package edu.clemson.lph.jitter.files;

@SuppressWarnings("serial")
public class InvalidInputException extends Exception {
	public InvalidInputException( String sError ) {
		super( sError );
	}
}
