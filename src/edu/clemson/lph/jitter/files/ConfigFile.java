package edu.clemson.lph.jitter.files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import edu.clemson.lph.jitter.logger.Loggers;

public class ConfigFile {
	private static Properties props = null;
	
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
		}
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
			Loggers.error("Cannot get value for " + sKey + " from JitterDot.config");
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
