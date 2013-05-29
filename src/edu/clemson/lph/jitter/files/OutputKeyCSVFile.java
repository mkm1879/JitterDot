package edu.clemson.lph.jitter.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.Ostermiller.util.*;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.WorkingData;
import edu.clemson.lph.jitter.structs.WorkingDataRow;

public class OutputKeyCSVFile {
	private CSVPrinter printer;
	private final static String aColNames[] = {"HerdID","Key","HerdSize","Houses","AnimalType","Integrator","LongitudeIn","LatitudeIn","DK",
		                                       "Longitude", "Latitude", "Status","DaysInState","DaysLeftInState"};
	
	public OutputKeyCSVFile( File fOut ) throws FileNotFoundException {
		printer = new CSVPrinter( new FileOutputStream( fOut ) );
	}
	
	public void print( WorkingData aData ) {
		printer.println(aColNames);
		for( WorkingDataRow row : aData ) {
			printRow(row);
		}
		close();
	}
	
	public void printRow( WorkingDataRow row ) {
		put(row.getOriginalKey());
		put(row.getKey());
		put(row.getAnimals());
		put(row.getHouses());
		put(row.getAnimalType());
		put(row.getIntegrator());
		put(row.getLongitudeIn());
		put(row.getLatitudeIn());
		put(row.getDK());
		put(row.getLongitude());
		put(row.getLatitude());
		put(row.getStatus());
		put(row.getDaysInState());
		put(row.getDaysLeftInState());
		printer.println();
	}
	
	private void put( String s ) {
		if( s != null ) 
			printer.print(s);
		else
			printer.print("");
	}
	
	private void put( Integer i ) {
		if( i != null && i != -1 ) 
			printer.print(Integer.toString(i));
		else
			printer.print("");
	}
	
	private void put( Double d ) {
		if( d != null && d != -1 ) 
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
