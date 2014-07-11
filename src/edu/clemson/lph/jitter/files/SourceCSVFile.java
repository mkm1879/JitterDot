package edu.clemson.lph.jitter.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.clemson.lph.LabeledCSVParser;
import edu.clemson.lph.controls.GPSTextField;
import edu.clemson.lph.jitter.JitterDot;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.geometry.InvalidUTMZoneException;
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
public class SourceCSVFile {
	private static final String[] STANDARD_COLUMNS = {	"OriginalKey","Animals","Houses","AnimalType","Integrator","Longitude","Latitude","Northing","Easting","Status","DaysInState","DaysLeftInState"};
	private static final String[] ESSENTIAL_COLUMNS = {	"OriginalKey","Animals","AnimalType" };
	private File fInput = null;
	private LabeledCSVParser parser = null;
	private String aColumns[];
	private int iRows;
	private OutputCSVFile fileError = null;
	
	public SourceCSVFile( File fIn ) throws FileNotFoundException, IOException {
		fInput = fIn;
		iRows = getRowCount(fIn);
		parser = new LabeledCSVParser( new FileInputStream( fInput ) );
		aColumns = parser.getLabels();
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
	
	public String getPath() {
		if( fInput == null ) return null;
		return fInput.getAbsolutePath();
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
		WorkingData aData = new WorkingData( this );
		fileError = aData.getErrorFile();
		aData.setRows(iRows);
		String aLine[] = null;
		while( (aLine = parser.getLine()) != null ) {
			WorkingDataRow dataRow = null;
			String sOriginalKey = null;
			Double dLatitudeIn = null;
			Double dLongitudeIn = null;
			Double dNorthingIn = null;
			Double dEastingIn = null;
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
				else if( ConfigFile.mapColumn("Northing").equalsIgnoreCase(sColName) ) {
					if( sValue != null && sValue.trim().length() > 0 && !"NULL".equalsIgnoreCase(sValue) ) {
						dNorthingIn = Double.parseDouble(sValue);	
					}
				}
				else if( ConfigFile.mapColumn("Easting").equalsIgnoreCase(sColName) ) {
					if( sValue != null && sValue.trim().length() > 0 && !"NULL".equalsIgnoreCase(sValue) ) {
						dEastingIn = Double.parseDouble(sValue);	
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
				if( dLatitudeIn != null && dLongitudeIn != null )
					dataRow = new WorkingDataRow( aLine, sOriginalKey, dLatitudeIn, dLongitudeIn, sAnimalType );
				else if ( dNorthingIn != null && dEastingIn != null && ConfigFile.getUTMZoneNum() != null )
					dataRow = new WorkingDataRow( aLine, sOriginalKey, dNorthingIn, dEastingIn, ConfigFile.getUTMZone(), sAnimalType );
				else {
					Loggers.getLogger().info("Row " + sOriginalKey + " could not be used. Missing Coordinates");
					fileError.printErrorRow("Row has no coordinates", aLine);
					aData.setRows(--iRows);
					continue;
				}
				if( sOriginalKey == null ) {
					Loggers.getLogger().info("Row could not be used. Missing Original Key");
					fileError.printErrorRow("Row missing Original Key", aLine);
					aData.setRows(--iRows);
					continue;
				}
				if( sAnimalType == null ) {
					Loggers.getLogger().info("Row " + sOriginalKey + " could not be used. Missing Animal Type");
					fileError.printErrorRow("Row missing Animal Type", aLine);
					aData.setRows(--iRows);
					continue;
				}
					
			} catch( InvalidCoordinateException e ) {
				String sCoordinates = null;
				if( dLatitudeIn != null && dLongitudeIn != null )
					sCoordinates = dLatitudeIn.toString() + ", " + dLongitudeIn.toString();
				else if ( dNorthingIn != null && dEastingIn != null )
					sCoordinates = dEastingIn.toString() + ", " + dNorthingIn.toString() + ", " + ConfigFile.getUTMZoneNum();
				else
					sCoordinates = "No coordinates";
				Loggers.getLogger().info("Row " + sOriginalKey + " could not be used. Invalid Coordinates " + sCoordinates
						+ ": " + e.getMessage() );
				fileError.printErrorRow("Row missing or bad coordinates", aLine);
				aData.setRows(--iRows);		
				continue;
			} catch (InvalidUTMZoneException e) {
				Loggers.getLogger().info("Row " + sOriginalKey + " could not be used. Bad UTMZone " +  ConfigFile.getUTMZoneNum()
						+ ": " + e.getMessage() );
				fileError.printErrorRow("Row had bad UTM Zone", aLine);
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
		if( fileError != null )
			fileError.close();
		parser.close();
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
