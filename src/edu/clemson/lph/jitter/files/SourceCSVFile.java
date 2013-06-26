package edu.clemson.lph.jitter.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.Ostermiller.util.*;

import edu.clemson.lph.controls.GPSTextField;
import edu.clemson.lph.jitter.JitterDot;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.ColumnNameMap;
import edu.clemson.lph.jitter.structs.WorkingData;
import edu.clemson.lph.jitter.structs.WorkingDataRow;

public class SourceCSVFile {
	private static final String[] STANDARD_COLUMNS = {	"OriginalKey","Animals","Houses","AnimalType","Integrator","Longitude","Latitude","Status","DaysInState","DaysLeftInState"};
	private static final String[] ESSENTIAL_COLUMNS = {	"OriginalKey","Animals","AnimalType","Longitude","Latitude"};
	private File fInput = null;
	private LabeledCSVParser parser = null;
	private String aColumns[];
	private int iRows;
	
	public SourceCSVFile( File fIn ) throws FileNotFoundException, IOException {
		fInput = fIn;
		iRows = getRowCount(fIn);
		parser = new LabeledCSVParser( new ExcelCSVParser( new FileInputStream( fInput )));
		aColumns = parser.getLabels();
		WorkingData.setColumns(aColumns);
	}
	
	public static String[] getStandardColumns() {
		return STANDARD_COLUMNS;
	}
	
	
	public static String[] getEssentialColumns() {
		return ESSENTIAL_COLUMNS;
	}
	
	public String[] getColumns() {
		return aColumns;
	}
	
	/**
	 * This is the guts of the Source File implementation.  Take the parser and read all the rows into 
	 * instances of our data structure working data.
	 * This is the longest running method in the program.  Not sure if it can be optimized.
	 * @return ArrayList of Original Data with generated new key value.
	 * @throws IOException Mainly when problems with the source file arise
	 * @throws NumberFormatException When converting fields in CSV to numeric (double or int)
	 * @throws InvalidCoordinateException When converting coordinates from strings in CSV file.
	 * @throws InvalidInputException For any row that lacks valid data for any required field.
	 */
	public WorkingData getData() throws IOException, NumberFormatException, InvalidCoordinateException, InvalidInputException {
		WorkingData aData = new WorkingData();
		aData.setRows(iRows);
		String aLine[] = null;
		while( (aLine = parser.getLine()) != null ) {
			WorkingDataRow dataRow = null;
			String sOriginalKey = null;
			Double dLatitudeIn = null;
			Double dLongitudeIn = null;
			String sAnimalType = null;
			String sIntegrator = null;
			int iHouses = -1;
			int iAnimals = -1;
			String sStatus = null;
			int iDaysInState = -1;
			int iDaysLeftInState = -1;
			
			for( String sColName : aColumns ) {
				int iIndex = parser.getLabelIdx(sColName);
				String sValue = aLine[iIndex];
				
				if( ConfigFile.mapColumn("OriginalKey").equalsIgnoreCase(sColName) ) {
					sOriginalKey = sValue;
				}
				else if( ConfigFile.mapColumn("Animals").equalsIgnoreCase(sColName) ) {
					try {
						iAnimals = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iAnimals = -1;
					}
				}
				else if( ConfigFile.mapColumn("Houses").equalsIgnoreCase(sColName) ) {
					try {
						iHouses = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iHouses = -1;
					}					
				}
				else if( ConfigFile.mapColumn("AnimalType").equalsIgnoreCase(sColName) ) {
					sAnimalType = sValue;
				}
				else if( ConfigFile.mapColumn("Integrator").equalsIgnoreCase(sColName) ) {
					sIntegrator = sValue;
				}
				else if( ConfigFile.mapColumn("Longitude").equalsIgnoreCase(sColName) ) {
					if( sValue != null && sValue.trim().length() > 0 && !"NULL".equalsIgnoreCase(sValue) ) {
						String sLongitude = GPSTextField.convertGPS(sValue);
						dLongitudeIn = Double.parseDouble(sLongitude);
					}
				}
				else if( ConfigFile.mapColumn("Latitude").equalsIgnoreCase(sColName) ) {
					if( sValue != null && sValue.trim().length() > 0 && !"NULL".equalsIgnoreCase(sValue) ) {
						String sLatitude = GPSTextField.convertGPS(sValue);
						dLatitudeIn = Double.parseDouble(sLatitude);	
					}
				}
				else if( ConfigFile.mapColumn("Status").equalsIgnoreCase(sColName) ) {
					sStatus = sValue;
				}
				else if( ConfigFile.mapColumn("DaysInState").equalsIgnoreCase(sColName) ) {
					try {
						iDaysInState = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iDaysInState = -1;
					}					
				}
				else if( ConfigFile.mapColumn("DaysLeftInState").equalsIgnoreCase(sColName) ) {
					try {
						iDaysLeftInState = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iDaysLeftInState = -1;
					}					
				}
			}
			try {
				dataRow = new WorkingDataRow( aLine, sOriginalKey, dLatitudeIn, dLongitudeIn, sAnimalType );
			} catch( InvalidCoordinateException e ) {
				dataRow = null;
			}
			if( dataRow == null || sOriginalKey == null || dLongitudeIn == null || dLatitudeIn == null || sAnimalType == null
					|| ( Math.abs(dLongitudeIn) < 0.0001 && Math.abs(dLatitudeIn) < 0.0001 ) ) {
				Loggers.getLogger().info("Row " + sOriginalKey + " could not be used ");
				JitterDot.getErrorFile().printErrorRow("Row missing required data", aLine);
				aData.setRows(--iRows);
				continue;
			}
			if( iAnimals >= 0 )
				dataRow.setAnimals(iAnimals);
			if( iHouses >= 0 )
				dataRow.setHouses(iHouses);
			if( sIntegrator != null )
				dataRow.setIntegratorIn(sIntegrator);
			if( sStatus != null ) 
				dataRow.setStatus(sStatus);
			if( iDaysInState >= 0 )
				dataRow.setDaysInState(iDaysInState);
			if( iDaysLeftInState >= 0 )
				dataRow.setDaysLeftInState(iDaysLeftInState);
			aData.add(dataRow);
		}
		return aData;
	}
	
	/**
	 * Close the underlying file and flush data.
	 */
	public void close() {
		try {
			parser.close();
		} catch (IOException e) {
			Loggers.error(e);
		}
	}
	
	/**
	 * Get the number of rows read earlier in getRowCount.  Used mainly by test case.
	 * @return
	 */
	public int getRows() { return iRows; }
	
	/**
	 * 
	 * @param fIn file to count rows
	 * @return int number of rows not counting header.
	 */
	private int getRowCount( File fIn ) {
		int iRet = -1;
		try {
			BufferedReader reader = new BufferedReader( new FileReader( fIn ) );
			while( reader.readLine() != null ) {
				iRet++;
			}
			reader.close();
		} catch (IOException e) {
			iRet = -1;
		}
		return iRet;
	}

}
