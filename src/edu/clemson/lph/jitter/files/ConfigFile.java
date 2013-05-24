package edu.clemson.lph.jitter.files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.ColumnNameMap;

public class ConfigFile {
	private static Properties props = null;
	private static ColumnNameMap colMap = null;
	
	private static void init() {
		if( props == null ) {
			props = new Properties();
			try {
				props.load(new FileInputStream("JitterDot.config"));
			} catch (FileNotFoundException e) {
				System.err.println( "Cannot find JitterDot.config" );
				Loggers.error(e);
			} catch (IOException e) {
				System.err.println( "Cannot load JitterDot.config" );
				Loggers.error(e);
			}
			colMap = new ColumnNameMap();
			// List all common column names and add to map if translations are found
			String sOriginalKey = getString("OriginalKey");
			if( sOriginalKey != null )
				colMap.put( "OriginalKey", sOriginalKey );
			String sLatitude = getString("Latitude");
			if( sLatitude!= null )
				colMap.put( "Latitude", sLatitude );
			String sLongitude = getString("Longitude");
			if( sLongitude!= null )
				colMap.put( "Longitude", sLongitude );
			String sAnimalType = getString("AnimalType");
			if( sAnimalType != null )
				colMap.put("AnimalType", sAnimalType );
			String sIntegrator = getString("Integrator");
			if( sIntegrator != null )
				colMap.put( "Integrator", sIntegrator );
			String sHouses = getString("Houses");
			if( sHouses != null )
				colMap.put( "Houses", sHouses );
			String sAnimals = getString("Animals");
			if( sAnimals != null )
				colMap.put( "Animals", sAnimals );
			String sStatus = getString("Status");
			if( sStatus != null )
				colMap.put( "Status", sStatus );
			String sDaysInState = getString("DaysInState");
			if( sDaysInState != null )
				colMap.put( "DaysInState", sDaysInState );
			String sDaysLeftInState = getString("DaysLeftInState");
			if( sDaysLeftInState != null )
				colMap.put( "DaysLeftInState", sDaysLeftInState );
		}
	}
	
	public static String mapColumn( String sColumnOut ) {
		init();
		return colMap.mapColumn( sColumnOut );
	}
	
	private static Integer getInt(String sKey) {
		init();
		Integer iRet = null;
		String sValue = props.getProperty(sKey);
		if( sValue == null ) {
			Loggers.error("Cannot get value for " + sKey + " from JitterDot.config");
		}
		else {
			try {
				iRet = Integer.parseInt(sValue);
			}
			catch( NumberFormatException nfe ) {
				Loggers.error("Cannot parse " + sValue + " as an integer value for " + sKey );
			}
		}
		return iRet;
	}
	
	@SuppressWarnings("unused")
	private static Double getDouble(String sKey) {
		init();
		Double dRet = null;
		String sValue = props.getProperty(sKey);
		if( sValue == null ) {
			Loggers.error("Cannot get value for " + sKey + " from JitterDot.config");
		}
		else {
			try {
				dRet = Double.parseDouble(sValue);
			}
			catch( NumberFormatException nfe ) {
				Loggers.error("Cannot parse " + sValue + " as an number value for " + sKey );
			}
		}
		return dRet;
	}
	
	private static String getString(String sKey) {
		init();
		String sRet = null;
		String sValue = props.getProperty(sKey);
		if( sValue == null ) {
			Loggers.getLogger().info("Cannot get value for " + sKey + " from JitterDot.config");
		}
		else {
			sRet = sValue;
		}
		return sRet;
	}
	
	public static Integer getMinK() {
		return getInt("MinK");
	}
	
	public static Integer getMinGroup() {
		return getInt("MinGroup");
	}
	
	public static Integer getUTMZoneNum() {
		Integer iRet = null;
		String sZone = getString("UTMZone");
		if( sZone == null ) {
			Loggers.error("Cannot get value for UTMZone from JitterDot.config");
		}
		else {
			String sZoneNum = sZone;
			if( sZone.endsWith("S") || sZone.endsWith("N") )
				sZoneNum = sZone.substring(0, sZone.length() - 1);
			try {
				iRet = Integer.parseInt(sZoneNum);
			}
			catch( NumberFormatException nfe ) {
				Loggers.error("Cannot parse integer from " + sZone + " for UTMZone" );
			}
		}
		return iRet;
	}
	
	public static String getZoneHemisphere() {
		String sRet = null;
		String sZone = getString("UTMZone");
		if( sZone == null ) {
			Loggers.error("Cannot get value for UTMZone from JitterDot.config");
		}
		else {
			if( sZone.trim().endsWith("S") ) {
				sRet = "S";
			}
			else if( sZone.trim().endsWith("N") ) {
				sRet = "N";
			}
		}
		return sRet;
	}

}
