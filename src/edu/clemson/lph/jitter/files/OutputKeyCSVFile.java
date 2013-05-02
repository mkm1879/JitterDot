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
		printer.print(row.getOriginalKey());
		printer.print(Integer.toString(row.getKey()));
		printer.print(Integer.toString(row.getAnimals()));
		printer.print(Integer.toString(row.getHouses()));
		printer.print(row.getAnimalType());
		printer.print(row.getIntegrator());
		printer.print(Double.toString(row.getLongitudeIn()));
		printer.print(Double.toString(row.getLatitudeIn()));
		printer.print(Double.toString(row.getDK()));
		printer.print(Double.toString(row.getLongitude()));
		printer.print(Double.toString(row.getLatitude()));
		printer.print(row.getStatus());
		printer.print(Integer.toString(row.getDaysInState()));
		printer.print(Integer.toString(row.getDaysLeftInState()));
		printer.println();
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
