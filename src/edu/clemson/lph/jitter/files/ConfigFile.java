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

import org.apache.log4j.Level;

import edu.clemson.lph.jitter.logger.Loggers;

/**
 * Usually used as global settings for the application.
 * Globals == BAD.  I know, I know!  But in 99% of cases this realistically reflects what
 * we are trying to do.  Simply read a bunch of constants from a config file.
 * 
 * For the 1% we allow a saveConfigAs method that saves a different named config file.  An open 
 * config file allows changing all the globals to those values.  Values are locked (turned into
 * true constants before actual processing begins.)
 * 
 * The odd structure of this class uses public instance methods exclusively for testing.  Most calls by the 
 * application are facilitated by static wrapper methods that simply call the instance methods against
 * the static singleton instance.
 * Tests can test against specific configurations with:
 * ConfigFile testConfig = new ConfigFile( "filepath" );
 * testConfig._[Static Wrapper Method Name]
 * In this way, a whole suite of configurations vs. data files can be tested in parallel while
 * the application code can still use the simple static calls to the global configuration.
 * @author mmarti5
 *
 */
public class ConfigFile {
	private static final String DEFAULT_CONFIG_FILE = "./JitterDot.config";
	private static ConfigFile singleton = new ConfigFile( DEFAULT_CONFIG_FILE );
	private String sFilePath = "./JitterDot.config";
	private File fFile;
	private Properties props = null;
	private ArrayList<String> lines;
	private boolean bLoggerChanged = false;
	private boolean bLocked = false;
	
	/**
	 * Construct a non-global instance of configuration.  Normally only for testing.
	 * @param sFilePath
	 */
	public ConfigFile( String sFilePath ) {
		this.sFilePath = sFilePath;
	}
	
	/**
	 * Block further modification for access in run thread.
	 */
	public void _lockConfig( boolean bLocked ) {
		this.bLocked = bLocked;
	}
	
	private boolean testLock() {
		if( bLocked ) {
			Loggers.error( new ConcurrentModificationException("Attempt to modify config during execution or jitter") );
		}
		return bLocked;
	}
	
	/**
	 * Load the Properties object to read values.  We don't read based on section.
	 */
	private void initRead() {
		if( props == null ) {
			if( testLock() ) return;
			props = new Properties();
			try {
				fFile = new File( sFilePath );
				FileInputStream fIs = new FileInputStream(fFile);
				props.load(fIs);
				fIs.close();
			} catch (FileNotFoundException e) {
				System.err.println( "Cannot find " + sFilePath );
				Loggers.error(e);
			} catch (IOException e) {
				System.err.println( "Cannot load " + sFilePath );
				Loggers.error(e);
			}
		}
	}
	
	/**
	 * We don't use props.store because it loses sections and comments.
	 * The save commands edit a literal representation of the lines in the file.
	 * This creates the array of lines for use in all set commands.
	 */
	private void initWrite() {
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

	private List<String> getStringList(String sKey) {
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
		else if( sValue != null && sValue.trim().length() > 0 ) 
			aRet.add(sValue);
		return aRet;
	}
	
	private Integer getInt(String sKey) {
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
	private Double getDouble(String sKey) {
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
	
	private String getString(String sKey) {
		initRead();
		String sRet = null;
		String sValue = props.getProperty(sKey);
		if( sValue == null && !sKey.equalsIgnoreCase("UTMZone") ) {
			Loggers.getLogger().info("Cannot get value for " + sKey + " from " + sFilePath);
		}
		else {
			sRet = sValue;
		}
		return sRet;
	}
	
	// Specific public getters and setters
	
	public List<String> _getStates() {
		return getStringList("States");
	}
	
	public void _setStates( List<String> aStates ) {
		if( testLock() ) return;
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for( String sState : aStates ) {
			sb.append(sState);
			if( ++i < aStates.size() )
				sb.append(',');
		}
		_setValue("States", sb.toString(), "Geography");
	}
	
	/**
	 * Get the minimum value of Longitude.
	 * @return minimum Longitude in bounds
	 */
	public Double _getMinLongitude() {
		return getDouble("MinLongitude");
	}
	
	/**
	 * Get the maximum value of Longitude.
	 * @return maximum Longitude in bounds
	 */
	public Double _getMaxLongitude() {
		return getDouble("MaxLongitude");
	}

	
	/**
	 * Get the minimum value of Latitude.
	 * @return minimum Latitude in bounds
	 */
	public Double _getMinLatitude() {
		return getDouble("MinLatitude");
	}
	
	/**
	 * Get the maximum value of Latitude.
	 * @return maximum Latitude in bounds
	 */
	public Double _getMaxLatitude() {
		return getDouble("MaxLatitude");
	}

	/**
	 * Get the value of K in our privacy setting.
	 * @return minimum K anonymity required
	 */
	public Integer _getMinK() {
		return getInt("MinK");
	}
	
	/**
	 * Set the value of K in our privacy setting.
	 * @param iMinK Integer minimum K anonymity required
	 */
	public void _setMinK( Integer iMinK ) {
		_setValue("MinK", iMinK.toString(), "Jittering");
	}
	
	/**
	 * Get the value of the smallest grouping of animal types for calculation of K distance
	 * @return Integer minimum group size.  Uses K if not otherwise specified.
	 */
	public Integer _getMinGroup() {
		Integer iGroup = getInt("MinGroup");
		Integer iK = getMinK();
		if( iGroup == null || iGroup < iK )
			iGroup = iK;
		return iGroup;
	}
	
	/**
	 * Set the value of the smallest grouping of animal types for calculation of K distance.
	 * @param iMinGroup Integer minimum group size.  Uses K if not otherwise specified.
	 */
	public void _setMinGroup( Integer iMinGroup ) {
		_setValue("MinGroup", iMinGroup.toString(), "Jittering");
	}
	
	public boolean _isUTMSet() {
		String sValue = props.getProperty("UTMZone");
		return ( sValue != null );
	}
	
	public String _getUTMZone() {
		return getString("UTMZone");
	}
	
	// Currently not using user configured UTMZones but calculating from median longitude.
	public Integer _getUTMZoneNum() {
		Integer iRet = null;
		String sZone = getString("UTMZone");
		if( sZone != null ) {
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
	public String _getZoneHemisphere() {
		String sRet = null;
		String sZone = getString("UTMZone");
		if( sZone != null ) {
			if( sZone.trim().endsWith("S") ) {
				sRet = "S";
			}
			else if( sZone.trim().endsWith("N") ) {
				sRet = "N";
			}
			else {
				Loggers.error("Cannot parse hemisphere from " + sZone + " for UTMZone" );				
			}
		}
		return sRet;
	}
	
	public void _setUTMZone( String sZone ) {
		_setValue("UTMZone", sZone, "Geography");
	}
	
	public boolean _isNAADSMRequested() {
		String sNAADSM = getString("NAADSM");
		return ( sNAADSM != null && sNAADSM.equalsIgnoreCase( "True" ) );
	}
	
	public void _setNAADSMRequested( boolean bNAADSM ) {
		if( testLock() ) return;
		String sRequested = null;
		if( bNAADSM ) 
			sRequested = "True";
		else 
			sRequested = "False";
		_setValue("NAADSM", sRequested, "Output");
	}
	
	public boolean _isInterspreadRequested() {
		String sInterspread = getString("InterspreadPlus");
		return ( sInterspread != null && sInterspread.equalsIgnoreCase( "True" ) );
	}
	
	public void _setInterspreadRequested( boolean bInterspread ) {
		if( testLock() ) return;
		String sRequested = null;
		if( bInterspread ) 
			sRequested = "True";
		else 
			sRequested = "False";
		_setValue("InterspreadPlus", sRequested, "Output");
	}
	
	/**
	 * Clear a list of field mappings by setting their values to null
	 * Used to remove mappings to fields that do not exist in new data file
	 * when opened.  We don't want to lose any that do match but 
	 * have no GUI option to remove those without a column to display in.
	 * @param aFields
	 */
	public void _clearFieldMappings( List<String> aFields ) {
		for( String sField : aFields ) {
			_setValue( sField, null, "Field Mappings");		
		}
	}

	/**
	 * Convenience method for setting field mappings.
	 * @param sField
	 * @param sMappedTo
	 */
	public void _setFieldMap( String sField, String sMappedTo ) {
		_setValue( sField, sMappedTo, "Field Mappings");
	}
	
	/**
	 * Find the column name in the input file that matches the output column
	 * @param sColumnOut output column.
	 * @return String input column name.
	 */
	public String _mapColumn( String sColumnOut ) {
		initRead();
		String sColumnIn = props.getProperty(sColumnOut);
		if( sColumnIn == null ) 
			sColumnIn = sColumnOut;
		return sColumnIn;
	}
	
	public boolean _isDetailedLoggingRequested() {
		boolean bRet = false;
		String sRootLogger = getString( "log4j.rootLogger" );
		if( sRootLogger != null && sRootLogger.contains("info")) 
			bRet = true;
		return bRet;
	}
	
	public void _setDetailedLoggingRequested( boolean bDetailed ) {
		if( testLock() ) return;
		boolean bPrior = isDetailedLoggingRequested();
		if( bPrior != bDetailed ) {
			String sRootLogger = null;
			if( bDetailed )
				sRootLogger = "info,stdout,R";
			else
				sRootLogger = "error,R";
			_setValue("log4j.rootLogger", sRootLogger, "Logging");
			bLoggerChanged = true;
		}
	}
	
	
	/**
	 * Set the value of a property in a section of the file
	 * @param sProp String property left of the =
	 * @param sValue String value to the right of the =
	 * @param sSection String that follows # in section header comment.  Requires just enough to be unique.
	 */
	public void _setValue( String sProp, String sValue, String sSection ) {
		if( testLock() ) return;
		initRead();
		initWrite();
		
		String sOldMap = props.getProperty(sProp);
		if( sValue != null )
			props.put(sProp, sValue);
		else
			props.remove(sProp);
		if( sOldMap != null ) {
			if( sOldMap.equals(sValue) )
				return;
			else {
				for( int i = 0; i < lines.size(); i++ ) {
					String sLine = lines.get(i);
					if( sLine.startsWith(sProp + "=") ) {
						lines.remove(i);
						if( sValue != null )
							lines.add(i, sProp + "=" + sValue);
					}
				}
			}
		}
		else if( sValue != null ) {
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
				_setValue( sProp, sValue, sSection);
			}
		}
		// else doesn't exist and setting to null so do nothing.
	}
	
	private void makeSection( String sSection ) {
		lines.add(1, "#" + sSection);
		lines.add(2,"");
	}
	
	public List<String> _listMapKeys() {
		initRead();
		initWrite();
		List<String> aKeys = new ArrayList<String>();
		boolean bInSection = false;
		for( String sLine : lines ) {
			if( sLine == null ) continue;
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
	public void _saveConfig() {
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
	public void _saveConfigAs( String sNewFilePath) {
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
	public void _setConfigFilePath( String sNewFilePath ) {
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
	public File _getConfigFile() {
		return fFile;
	}
	
	
	/**
	 * Get the current log level
	 * Only ERROR and INFO are actually supported by GUI others can be set by text editor.
	 * @return String relative or absolute path to config file.
	 */
	public Level _getLogLevel() {
		String sLevel = getString("log4j.rootLogger");
		if( sLevel != null ) {
			if( sLevel.toUpperCase().contains(Level.ALL.toString())) return Level.ALL;
			if( sLevel.toUpperCase().contains(Level.TRACE.toString())) return Level.TRACE;
			if( sLevel.toUpperCase().contains(Level.DEBUG.toString())) return Level.DEBUG;
			if( sLevel.toUpperCase().contains(Level.INFO.toString())) return Level.INFO;
			if( sLevel.toUpperCase().contains(Level.WARN.toString())) return Level.WARN;
			if( sLevel.toUpperCase().contains(Level.ERROR.toString())) return Level.ERROR;
			if( sLevel.toUpperCase().contains(Level.FATAL.toString())) return Level.FATAL;
			if( sLevel.toUpperCase().contains(Level.OFF.toString())) return Level.OFF;
			return Level.ERROR;
		}
		return Level.ERROR;
	}
	
	
	public boolean _validateDataFile( String sDataFile ) {
		boolean bRet = true;
		try {
			SourceCSVFile source = new SourceCSVFile( new File( sDataFile) );
			String[] aCols = source.getColumns();
			for( String sKey : SourceCSVFile.getEssentialColumns() ) {
				String sMappedCol = this._mapColumn(sKey);
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

	
	public ConfigFile getConfig() { return singleton; }
	
	// Static methods simply call same method on Singleton instance
	public static void lockConfig( boolean bLocked ) { singleton._lockConfig( bLocked ); }
	public static List<String> getStates() { return singleton._getStates(); }
	public static void setStates( List<String> aStates ) { singleton._setStates(aStates); }
	public static Double getMinLongitude() { return singleton._getMinLongitude(); }
	public static Double getMaxLongitude() { return singleton._getMaxLongitude(); }
	public static Double getMinLatitude() { return singleton._getMinLatitude(); }
	public static Double getMaxLatitude() { return singleton._getMaxLatitude(); }
	public static Integer getMinK() { return singleton._getMinK(); }
	public static void setMinK( Integer iMinK ) { singleton._setMinK(iMinK); }
	public static Integer getMinGroup() { return singleton._getMinGroup(); }
	public static void setMinGroup( Integer iMinGroup ) { singleton._setMinGroup(iMinGroup); }
	public static boolean isUTMSet() { return singleton._isUTMSet(); }
	public static String getUTMZone() { return singleton._getUTMZone(); }
	public static Integer getUTMZoneNum() { return singleton._getUTMZoneNum(); }
	public static String getZoneHemisphere() { return singleton._getZoneHemisphere(); }
	public static void setUTMZone( String sZone ) { singleton._setUTMZone(sZone); }
	public static boolean isNAADSMRequested() { return singleton._isNAADSMRequested(); }
	public static void setNAADSMRequested( boolean bNAADSM ) { singleton._setNAADSMRequested(bNAADSM); }
	public static boolean isInterspreadRequested() { return singleton._isInterspreadRequested(); }
	public static void setInterspreadRequested( boolean bInterspread ) { singleton._setInterspreadRequested(bInterspread); }
	public static void clearFieldMappings( List<String> aFields ) { singleton._clearFieldMappings( aFields ); }
	public static void setFieldMap( String sField, String sMappedTo ) { singleton._setFieldMap(sField, sMappedTo); }
	public static String mapColumn( String sColumnOut ) { return singleton._mapColumn(sColumnOut); }
	public static boolean isDetailedLoggingRequested() { return singleton._isDetailedLoggingRequested(); }
	public static void setDetailedLoggingRequested( boolean bDetailed ) { singleton._setDetailedLoggingRequested(bDetailed); }
//	public static void setValue( String sProp, String sValue, String sSection ) { singleton._setValue(sProp, sValue, sSection); }
	public static List<String> listMapKeys() { return singleton._listMapKeys(); }
	public static void saveConfig() { singleton._saveConfig(); }
	public static void saveConfigAs( String sNewFilePath) { singleton._saveConfigAs(sNewFilePath); }
	public static void setConfigFilePath( String sNewFilePath ) { singleton._setConfigFilePath(sNewFilePath); }
	public static File getConfigFile() { return singleton._getConfigFile(); }
	public static Level getLogLevel() { return singleton._getLogLevel(); }
	public static boolean validateDataFile( String sDataFile ) { return singleton._validateDataFile(sDataFile); }

}
