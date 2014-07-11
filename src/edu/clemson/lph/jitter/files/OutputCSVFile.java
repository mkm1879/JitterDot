package edu.clemson.lph.jitter.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import edu.clemson.lph.CSVPrinter;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.WorkingData;
import edu.clemson.lph.jitter.structs.WorkingDataRow;
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
	private SourceCSVFile fInput = null;
	private String sFilePath;
	
	
	public OutputCSVFile( SourceCSVFile fIn, OutputFileType type ) throws FileNotFoundException {
		this.type = type;
		fInput = fIn;
		sFilePath = fInput.getPath();
		initCols();
	}
	
	private void initCols() {
		if( aColNames == null && fInput.getColumns() != null ) {
			sFilePath = sFilePath.substring(0, sFilePath.lastIndexOf('.'));
			switch( type ) {
			case KEY:
				sFilePath = sFilePath + "KEY.csv";
				aColNames = aColNamesKey;
				break;
			case NAADSM:
				sFilePath = sFilePath + "NAADSM.csv";
				aColNames = aColNamesNAADSM;
				break;
			case INTERSPREAD:
				sFilePath = sFilePath + "INTERSPREAD.csv";
				aColNames = aColNamesINTERSPREAD;
				break;
			case ERROR:
				sFilePath = sFilePath + "ERRORS.csv";
				// Because some errors are bad enough we never construct a WorkingDataRow
				// we use the column structure from the source with the addition of 
				// a column for the type of error.
				aColNames = new String[fInput.getColumns().length + 1];
				aColNames[0] = "Error Type";
				for( int i = 1; i <= fInput.getColumns().length; i++ )
					aColNames[i] = fInput.getColumns()[i-1];
				break;
			} 
			try {
			printer = new CSVPrinter( new FileOutputStream( sFilePath ) );
			printer.println(aColNames);
			} catch( FileNotFoundException e ) {
				Loggers.error(e);
			}
		}
	}
	
	public void print( WorkingData aData ) {
		initCols();
		for( WorkingDataRow row : aData ) {
			printRow(row);
		}
		close();
	}
	
	private void printRow( WorkingDataRow row ) {
		initCols();
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
			print(row.getUTMZone() + row.getUTMHemisphere() );
		}
		print(row.getStatus());
		print(row.getDaysInState());
		print(row.getDaysLeftInState());
		printer.println();
	}
	
	
	public void printErrorRow( String sError, String aLine[] ) {
		initCols();
		print(sError);
		for( String sValue : aLine ) {
			print( sValue );
		}
		printer.println();
	}
	
	public String getFilePath() {
		return sFilePath;
	}
	
	public void printErrorRow( String sError, WorkingDataRow row ) {
		initCols();
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
		printer.flush();
		printer.close();
	}


}
