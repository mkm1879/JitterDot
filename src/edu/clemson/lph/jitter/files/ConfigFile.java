package edu.clemson.lph.jitter.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.ColumnNameMap;

public class ConfigFile {
	private static Properties props = null;
//	private static ColumnNameMap colMap = null;
	private static ArrayList<String> lines;
	
	/**
	 * Load the Properties object to read values.  We don't read based on section.
	 */

	private static void initRead() {
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
	
	/**
	 * Find the column name in the input file that matches the output column
	 * @param sColumnOut output column.
	 * @return String input column name.
	 */
	public static String mapColumn( String sColumnOut ) {
		initRead();
		String sColumnIn = props.getProperty(sColumnOut);
		if( sColumnIn == null ) 
			sColumnIn = sColumnOut;
		return sColumnIn;
	}
	
	private static ArrayList<String> getStringList(String sKey) {
		ArrayList<String> aRet = new ArrayList<String>();
		initRead();
		String sValue = props.getProperty(sKey);
		StringTokenizer tok = new StringTokenizer(sValue,",");
		while( tok.hasMoreTokens() ) {
			String sNext = tok.nextToken();
			aRet.add(sNext.trim());
		}
		return aRet;
	}
	
	private static Integer getInt(String sKey) {
		initRead();
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
		initRead();
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
		initRead();
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
	
	public static ArrayList<String> getStates() {
		return getStringList("States");
	}
	
	/**
	 * Get the value of K in our privacy setting.
	 * @return minimum K annonymity required
	 */
	public static Integer getMinK() {
		return getInt("MinK");
	}
	
	/**
	 * Get the value of the smallest grouping of animal types for calculation of K distance
	 * @return Integer minimum group size.  Uses K if not otherwise specified.
	 */
	public static Integer getMinGroup() {
		Integer iGroup = getInt("MinGroup");
		if( iGroup == null )
			iGroup = getMinK();
		return iGroup;
	}
	
	
	// Currently not using user configured UTMZones but calculating from median longitude.
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

	// Currently not using user configured UTMZones but calculating from median longitude.
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
	
	/**
	 * Convenience method for setting field mappings.
	 * @param sField
	 * @param sMappedTo
	 */
	public static void setFieldMap( String sField, String sMappedTo ) {
		setValue( sField, sMappedTo, "Field Mappings");
	}
	
	/**
	 * Set the value of a property in a section of the file
	 * @param sProp String property left of the =
	 * @param sValue String value to the right of the =
	 * @param sSection String that follows # in section header comment.  Requires just enough to be unique.
	 */
	public static void setValue( String sProp, String sValue, String sSection ) {
		initRead();
		initWrite();
		
		String sOldMap = props.getProperty(sProp);
		props.put(sProp, sValue);
		if( sOldMap != null ) {
			if( sOldMap.equals(sValue) )
				return;
			else {
				for( int i = 0; i < lines.size(); i++ ) {
					String sLine = lines.get(i);
					if( sLine.startsWith(sProp + "=") ) {
						lines.remove(i);
						lines.add(i, sProp + "=" + sValue);
					}
				}
			}
		}
		else {
			boolean bSection = false;
			for( int i = 0; i < lines.size(); i++ ) {
				String sLine = lines.get(i);
				if( sLine.startsWith("#" + sSection))
					bSection = true;
				if( bSection && sLine.trim().length() == 0 ) {
					lines.add(i, sProp + "=" + sValue);
					break;
				}
			}

		}
	}
	
	/**
	 * We don't use props.store because it loses sections and comments.
	 * The save commands edit a literal representation of the lines in the file.
	 * This saves the config settings back to the config file.
	 */	
	public static void saveConfig() {
		if( lines == null ) return;
		try {
			PrintWriter pw = new PrintWriter( new FileWriter( new File( "JitterDot.config.txt") ) );
			for( String sLine : lines ) {
				pw.println(sLine);
			}
			pw.close();
		} catch (IOException e) {
			Loggers.error(e);
		}
	}
	
	/**
	 * We don't use props.store because it loses sections and comments.
	 * The save commands edit a literal representation of the lines in the file.
	 * This creates the array of lines for use in all set commands.
	 */
	private static void initWrite() {
		if( lines == null ) {
			lines = new ArrayList<String>();
			try {
				BufferedReader br = new BufferedReader( new FileReader( new File("JitterDot.config")));
				String sLine = null;
				while( (sLine = br.readLine()) != null ) {
					lines.add(sLine);
				}
				br.close();
			} catch (IOException e) {
				Loggers.error(e);
			}
		}
	}

}
