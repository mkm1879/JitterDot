package edu.clemson.lph.jitter.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import edu.clemson.lph.jitter.logger.Loggers;

public class ConfigFile {
	private static String sFilePath = "./JitterDot.config";
	private static File fFile;
	private static Properties props = null;
	private static ArrayList<String> lines;
	private static boolean bLoggerChanged = false;
	private static boolean bLocked = false;
	
	/**
	 * Block further modification for access in run thread.
	 */
	public static void lockConfig() {
		bLocked = true;
	}
	
	private static boolean testLock() {
		if( bLocked ) {
			Loggers.error( new ConcurrentModificationException("Attempt to modify config during execution or jitter") );
		}
		return bLocked;
	}
	
	/**
	 * Load the Properties object to read values.  We don't read based on section.
	 */
	private static void initRead() {
		if( props == null ) {
			if( testLock() ) return;
			props = new Properties();
			try {
				fFile = new File( sFilePath );
				props.load(new FileInputStream(fFile));
			} catch (FileNotFoundException e) {
				System.err.println( "Cannot find " + sFilePath );
				Loggers.error(e);
			} catch (IOException e) {
				System.err.println( "Cannot load " + sFilePath );
				Loggers.error(e);
			}
		}
	}
	
	private static List<String> getStringList(String sKey) {
		List<String> aRet = new ArrayList<String>();
		initRead();
		String sValue = props.getProperty(sKey);
		if( sValue != null && sValue.contains(",") ) {
			StringTokenizer tok = new StringTokenizer(sValue,",");
			while( tok.hasMoreTokens() ) {
				String sNext = tok.nextToken();
				aRet.add(sNext.trim());
			}
		}
		else if( sValue != null ) 
			aRet.add(sValue);
		return aRet;
	}
	
	private static Integer getInt(String sKey) {
		initRead();
		Integer iRet = null;
		String sValue = props.getProperty(sKey);
		if( sValue == null ) {
			Loggers.error("Cannot get value for " + sKey + " from " + sFilePath);
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
			Loggers.error("Cannot get value for " + sKey + " from " + sFilePath);
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
			Loggers.getLogger().info("Cannot get value for " + sKey + " from " + sFilePath);
		}
		else {
			sRet = sValue;
		}
		return sRet;
	}
	
	// Specific public getters and setters
	
	public static List<String> getStates() {
		return getStringList("States");
	}
	
	public static void setStates( List<String> aStates ) {
		if( testLock() ) return;
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for( String sState : aStates ) {
			sb.append(sState);
			if( ++i < aStates.size() )
				sb.append(',');
		}
		setValue("States", sb.toString(), "Geography");
	}
	
	/**
	 * Get the value of K in our privacy setting.
	 * @return minimum K anonymity required
	 */
	public static Integer getMinK() {
		return getInt("MinK");
	}
	
	/**
	 * Set the value of K in our privacy setting.
	 * @param iMinK Integer minimum K anonymity required
	 */
	public static void setMinK( Integer iMinK ) {
		setValue("MinK", iMinK.toString(), "Jittering");
	}
	
	/**
	 * Get the value of the smallest grouping of animal types for calculation of K distance
	 * @return Integer minimum group size.  Uses K if not otherwise specified.
	 */
	public static Integer getMinGroup() {
		Integer iGroup = getInt("MinGroup");
		if( iGroup == null )
			iGroup = getMinGroup();
		return iGroup;
	}
	
	/**
	 * Set the value of the smallest grouping of animal types for calculation of K distance.
	 * @param iMinGroup Integer minimum group size.  Uses K if not otherwise specified.
	 */
	public static void setMinGroup( Integer iMinGroup ) {
		setValue("MinGroup", iMinGroup.toString(), "Jittering");
	}
	
	
	// Currently not using user configured UTMZones but calculating from median longitude.
	public static Integer getUTMZoneNum() {
		Integer iRet = null;
		String sZone = getString("UTMZone");
		if( sZone == null ) {
			Loggers.error("Cannot get value for UTMZone from " + sFilePath);
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
			Loggers.error("Cannot get value for UTMZone from " + sFilePath);
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
	
	public static void setUTMZone( String sZone ) {
		setValue("UTMZone", sZone, "Geography");
	}
	
	public static boolean isNAADSMRequested() {
		String sNAADSM = getString("NAADSM");
		return ( sNAADSM != null && sNAADSM.equalsIgnoreCase( "True" ) );
	}
	
	public static void setNAADSMRequested( boolean bNAADSM ) {
		if( testLock() ) return;
		String sRequested = null;
		if( bNAADSM ) 
			sRequested = "True";
		else 
			sRequested = "False";
		setValue("NAADSM", sRequested, "Output");
	}
	
	public static boolean isInterspreadRequested() {
		String sInterspread = getString("InterspreadPlus");
		return ( sInterspread != null && sInterspread.equalsIgnoreCase( "True" ) );
	}
	
	public static void setInterspreadRequested( boolean bInterspread ) {
		if( testLock() ) return;
		String sRequested = null;
		if( bInterspread ) 
			sRequested = "True";
		else 
			sRequested = "False";
		setValue("InterspreadPlus", sRequested, "Output");
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
	 * Find the column name in the input file that matches the output column
	 * @param sColumnOut output column.
	 * @return String input column name.
	 */
	public static String mapColumn( String sColumnOut ) {
		String sColumnIn = props.getProperty(sColumnOut);
		if( sColumnIn == null ) 
			sColumnIn = sColumnOut;
		return sColumnIn;
	}
	
	public static boolean isDetailedLoggingRequested() {
		boolean bRet = false;
		String sRootLogger = getString( "log4j.rootLogger" );
		if( sRootLogger != null && sRootLogger.contains("info")) 
			bRet = true;
		return bRet;
	}
	
	public static void setDetailedLoggingRequested( boolean bDetailed ) {
		if( testLock() ) return;
		boolean bPrior = isDetailedLoggingRequested();
		if( bPrior != bDetailed ) {
			String sRootLogger = null;
			if( bDetailed )
				sRootLogger = "info,stdout,R";
			else
				sRootLogger = "error,R";
			setValue("log4j.rootLogger", sRootLogger, "Logging");
			bLoggerChanged = true;
		}
	}
	
	
	/**
	 * Set the value of a property in a section of the file
	 * @param sProp String property left of the =
	 * @param sValue String value to the right of the =
	 * @param sSection String that follows # in section header comment.  Requires just enough to be unique.
	 */
	public static void setValue( String sProp, String sValue, String sSection ) {
		if( testLock() ) return;
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
			if( !bSection ) {
				makeSection( sSection );
				setValue( sProp, sValue, sSection);
			}
		}
	}
	
	private static void makeSection( String sSection ) {
		lines.add(1, "#" + sSection);
		lines.add(2,"");
	}
	
	public static List<String> listMapKeys() {
		initRead();
		initWrite();
		List<String> aKeys = new ArrayList<String>();
		boolean bInSection = false;
		for( String sLine : lines ) {
			if( sLine.startsWith("#Field Mappings")) {
				bInSection = true;
			}
			else if( sLine.startsWith("#")) {
				bInSection = false;
			}
			else if( bInSection && sLine != null && sLine.contains("=") ) {
				aKeys.add(sLine.substring(0, sLine.indexOf('=')));
			}			
		}
		return aKeys;
	}
	
	/**
	 * We don't use props.store because it loses sections and comments.
	 * The save commands edit a literal representation of the lines in the file.
	 * This saves the config settings back to the config file.
	 */	
	public static void saveConfig() {
		if( testLock() ) return;
		if( lines == null ) return;
		try {
			PrintWriter pw = new PrintWriter( new FileWriter( fFile ) );
			for( String sLine : lines ) {
				pw.println(sLine);
			}
			pw.close();
		} catch (IOException e) {
			Loggers.error(e);
		}
		if( bLoggerChanged ) {
			Loggers.refresh();
		}
	}
	
	/**
	 * We don't use props.store because it loses sections and comments.
	 * The save commands edit a literal representation of the lines in the file.
	 * This saves the config settings back to a new Config File.
	 * @param Stirng with new path
	 */	
	public static void saveConfigAs( String sNewFilePath) {
		if( testLock() ) return;
		if( lines == null ) return;
		try {
			fFile = new File( sNewFilePath );
			PrintWriter pw = new PrintWriter( new FileWriter( fFile ) );
			for( String sLine : lines ) {
				pw.println(sLine);
			}
			pw.close();
		} catch (IOException e) {
			Loggers.error(e);
		}
		setConfigFilePath(sNewFilePath);
		if( bLoggerChanged ) {
			Loggers.refresh();
		}
	}
	
	/**
	 * Change config files for this session.
	 * Because this is a global settings (static) class it affects all config actions.
	 * Calling method must refresh any in memory or GUI representation of the current 
	 * configuration.
	 * NOTE: Logging will continue with existing settings.
	 * @param sNewFilePath Relative or absolute filename for config file.
	 */
	public static void setConfigFilePath( String sNewFilePath ) {
		if( testLock() ) return;
		sFilePath = sNewFilePath;
		fFile = new File( sNewFilePath );
		props = null;
		initRead();
		lines = null;
		initWrite();
	}
	
	/**
	 * Get the current config file
	 * @return String relative or absolute path to config file.
	 */
	public static File getConfigFile() {
		return fFile;
	}
	
	
	public static boolean validateDataFile( String sDataFile ) {
		boolean bRet = true;
		try {
			SourceCSVFile source = new SourceCSVFile( new File( sDataFile) );
			String[] aCols = source.getColumns();
			for( String sKey : SourceCSVFile.getEssentialColumns() ) {
				String sMappedCol = ConfigFile.mapColumn(sKey);
				boolean bFound = false;
				for( String sCol : aCols ) {
					if( sCol.equalsIgnoreCase(sMappedCol) ) {
						bFound = true;
						break;
					}
				}
				if( !bFound ) {
					bRet = false;
					break;
				}
			}
			
		} catch (FileNotFoundException e) {
			Loggers.error(e);
			return false;
		} catch (IOException e) {
			Loggers.error(e);
			return false;
		}
		return bRet;
	}

	
	/**
	 * We don't use props.store because it loses sections and comments.
	 * The save commands edit a literal representation of the lines in the file.
	 * This creates the array of lines for use in all set commands.
	 */
	private static void initWrite() {
		if( lines == null ) {
			if( testLock() ) return;
			lines = new ArrayList<String>();
			try {
				fFile = new File(sFilePath);
				BufferedReader br = new BufferedReader( new FileReader( fFile ));
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
