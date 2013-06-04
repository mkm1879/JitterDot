package edu.clemson.lph.jitter.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.Ostermiller.util.*;

import edu.clemson.lph.controls.GPSTextField;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.WorkingData;
import edu.clemson.lph.jitter.structs.WorkingDataRow;

public class OutputCSVFile {
	public enum OutputFileType { KEY, NAADSM, INTERSPREAD, ERROR };
	
	private OutputFileType type;
	private CSVPrinter printer;
	private final static String aColNamesKey[] = {"OriginalKey","HerdID","HerdSize","Houses","AnimalType","IntegratorIn","Integrator","LongitudeIn","LatitudeIn","DK",
        "Longitude", "Latitude", "Easting", "Northing", "UTMZone", "Status","DaysInState","DaysLeftInState"};
	private final static String aColNamesNAADSM[] = {"HerdID","HerdSize","Houses","AnimalType","Integrator",
        "Longitude", "Latitude", "Status","DaysInState","DaysLeftInState"};
	private final static String aColNamesINTERSPREAD[] = {"HerdID","HerdSize","Houses","AnimalType","Integrator",
        "Easting", "Northing", "UTMZone", "Status","DaysInState","DaysLeftInState"};
	private String aColNames[];
	
	
	public OutputCSVFile( File fIn, OutputFileType type ) throws FileNotFoundException {
		this.type = type;
		String sFileName = fIn.getName();
		sFileName = sFileName.substring(0, sFileName.lastIndexOf('.'));
		File fOut;
		switch( type ) {
		case KEY:
			sFileName = sFileName + "KEY.csv";
			aColNames = aColNamesKey;
			break;
		case NAADSM:
			sFileName = sFileName + "NAADSM.csv";
			aColNames = aColNamesNAADSM;
			break;
		case INTERSPREAD:
			sFileName = sFileName + "INTERSPREAD.csv";
			aColNames = aColNamesINTERSPREAD;
			break;
		case ERROR:
			sFileName = sFileName + "ERRORS.csv";
			// Because some errors are bad enough we never construct a WorkingDataRow
			// we use the column structure from the source with the addition of 
			// a column for the type of error.
			aColNames = new String[WorkingData.aColumns.length + 1];
			aColNames[0] = "Error Type";
			for( int i = 1; i <= WorkingData.aColumns.length; i++ )
				aColNames[i] = WorkingData.aColumns[i-1];
			break;
		}
		fOut = new File( sFileName );
		printer = new CSVPrinter( new FileOutputStream( fOut ) );
		printer.println(aColNames);
	}
	
	public void print( WorkingData aData ) {
		for( WorkingDataRow row : aData ) {
			printRow(row);
		}
		close();
	}
	
	public void printRow( WorkingDataRow row ) {
		if( type.equals(OutputFileType.KEY) )
			print(row.getOriginalKey());
		print(row.getKey());
		print(row.getAnimals());
		print(row.getHouses());
		print(row.getAnimalType());
		if( type.equals(OutputFileType.KEY) )
			print(row.getIntegratorIn());
		print(row.getIntegrator());
		if( type.equals(OutputFileType.KEY) ) {
			print(row.getLongitudeIn());
			print(row.getLatitudeIn());
			print(row.getDK());
		}
		if( type.equals(OutputFileType.NAADSM) || type.equals(OutputFileType.KEY) ) {
			print(row.getLongitude());
			print(row.getLatitude());
		}
		if( type.equals(OutputFileType.INTERSPREAD) || type.equals(OutputFileType.KEY) ) {
			print(row.getEasting());
			print(row.getNorthing());
			print(row.getUTMZone());
		}
		print(row.getStatus());
		print(row.getDaysInState());
		print(row.getDaysLeftInState());
		printer.println();
	}
	
	
	public void printErrorRow( String sError, String aLine[] ) {
		print(sError);
		for( String sValue : aLine ) {
			print( sValue );
		}
		printer.println();
	}
	
	public void printErrorRow( String sError, WorkingDataRow row ) {
		print(sError);
		for( String sValue : row.getLine() ) {
			print(sValue);
		}
		printer.println();
	}

	
	private void print( String s ) {
		if( s != null ) 
			printer.print(s);
		else
			printer.print("");
	}
	
	private void print( Integer i ) {
		if( i != null && i != -1 ) 
			printer.print(Integer.toString(i));
		else
			printer.print("");
	}
	
	private void print( Double d ) {
		if( d != null ) 
			printer.print(Double.toString(d));
		else
			printer.print("");
	}
	
	public void close() {
		try {
			printer.flush();
			printer.close();
		} catch (IOException e) {
			Loggers.error(e);
		}
	}


}
