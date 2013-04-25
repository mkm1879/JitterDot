package edu.clemson.lph.jitter.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.Ostermiller.util.*;

import edu.clemson.lph.controls.GPSTextField;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.ColumnNameMap;
import edu.clemson.lph.jitter.structs.WorkingData;

public class SourceCSVFile {
	private ColumnNameMap map = new ColumnNameMap();
	private File fInput = null;
	private LabeledCSVParser parser = null;
	private String aColumns[];
	private int iRows;
	
	public SourceCSVFile( File fIn ) throws FileNotFoundException, IOException {
		fInput = fIn;
		iRows = getRowCount(fIn);
		WorkingData.setRows(iRows);  // populate list of keys with right number of values.
		parser = new LabeledCSVParser( new ExcelCSVParser( new FileInputStream( fInput )));
		aColumns = parser.getLabels();
	}
	
	public String[] getColumns() {
		return aColumns;
	}
	
	/**
	 * This is the guts of the Source File implementation.  Take the parser and read all the rows into 
	 * instances of our data structure working data.
	 * @return ArrayList of Original Data with generated new key value.
	 * @throws IOException Mainly when problems with the source file arise
	 * @throws NumberFormatException When converting fields in CSV to numberic (double or int)
	 * @throws InvalidCoordinateException When converting coordinates from strings in CSV file.
	 * @throws InvalidInputException For any row that lacks valid data for any required field.
	 */
	public ArrayList<WorkingData> getData() throws IOException, NumberFormatException, InvalidCoordinateException, InvalidInputException {
		ArrayList<WorkingData> aData = new ArrayList<WorkingData>();
		String aLine[] = null;
		while( (aLine = parser.getLine()) != null ) {
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
			
			for( String sLabel : aColumns ) {
				int iIndex = parser.getLabelIdx(sLabel);
				String sColName = map.mapColumn(sLabel);				
				String sValue = aLine[iIndex];
				
				// slog through all the expected keys saving to appropriate variables in a working data struct.
				//HerdID,HerdSize,Lon,Lat,ProductionType,Status
				if( "HerdID".equalsIgnoreCase(sColName) ) {
					sOriginalKey = sValue;
				}
				else if( "HerdSize".equalsIgnoreCase(sColName) ) {
					try {
						iAnimals = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iAnimals = -1;
					}
				}
				else if( "Houses".equalsIgnoreCase(sColName) ) {
					try {
						iHouses = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iHouses = -1;
					}					
				}
				else if( "Integrator".equalsIgnoreCase(sColName) ) {
					sIntegrator = sValue;
				}
				else if( "Lon".equalsIgnoreCase(sColName) ) {
					String sLongitude = GPSTextField.convertGPS(sValue);
					dLongitudeIn = Double.parseDouble(sLongitude);
				}
				else if( "Lat".equalsIgnoreCase(sColName) ) {
					String sLatitude = GPSTextField.convertGPS(sValue);
					dLatitudeIn = Double.parseDouble(sLatitude);					
				}
				else if( "ProductionType".equalsIgnoreCase(sColName) ) {
					sAnimalType = sValue;
				}
				else if( "Status".equalsIgnoreCase(sColName) ) {
					sStatus = sValue;
				}
				else if( "DaysInState".equalsIgnoreCase(sColName) ) {
					try {
						iDaysInState = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iDaysInState = -1;
					}					
				}
				else if( "DaysLeftInState".equalsIgnoreCase(sColName) ) {
					try {
						iDaysLeftInState = Integer.parseInt(sValue);
					} catch( NumberFormatException e ) {
						iDaysLeftInState = -1;
					}					
				}
			}
			if( sOriginalKey == null || dLongitudeIn == null || dLatitudeIn == null || sAnimalType == null ) {
				throw new InvalidInputException( "Invalid data on line " + parser.getLastLineNumber() );
			}
			WorkingData dataRow = new WorkingData( sOriginalKey, dLatitudeIn, dLongitudeIn, sAnimalType );
			if( iAnimals >= 0 )
				dataRow.setAnimals(iAnimals);
			if( iHouses >= 0 )
				dataRow.setHouses(iHouses);
			if( sIntegrator != null )
				dataRow.setIntegrator(sIntegrator);
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
