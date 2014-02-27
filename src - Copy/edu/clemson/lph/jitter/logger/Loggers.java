package edu.clemson.lph.jitter.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.clemson.lph.jitter.files.ConfigFile;

public class Loggers {
	static Logger logger = null;
	
	public static Logger getLogger() {
		if( logger == null ) {
			refresh();
		}
		return logger;
	}
	
	public static Level getLevel() {
		return logger.getLevel();
	}
	
	public static void refresh() {
		PropertyConfigurator.configure(ConfigFile.getConfigFile().getAbsolutePath());	
		logger = Logger.getLogger(Loggers.class.getName());
	    // I don't know why the above does not seem to set the level in logger
	    logger.setLevel(ConfigFile.getLogLevel());  // Defaults to ERROR
	}
	
	public static void error( Object oMsg ) {
		if( oMsg instanceof Exception ) {
			ByteArrayOutputStream bos =  new ByteArrayOutputStream();
			PrintStream ps = new PrintStream( bos );
			((Exception)oMsg).printStackTrace( ps );
			ps.flush();
			getLogger().error(bos.toString());
			ps.close();
		}
		else {
			getLogger().error(oMsg);
		}
	}
	
	public static void error( String sMsg, Object oMsg ) {
		if( oMsg instanceof Exception ) {
			ByteArrayOutputStream bos =  new ByteArrayOutputStream();
			PrintStream ps = new PrintStream( bos );
			ps.println(sMsg);
			((Exception)oMsg).printStackTrace( ps );
			ps.flush();
			getLogger().error(bos.toString());
			ps.close();
		}
		else {
			getLogger().error(oMsg);
		}
	}
	
	public static void info( Object oMsg ) {
		if( oMsg instanceof Exception ) {
			ByteArrayOutputStream bos =  new ByteArrayOutputStream();
			PrintStream ps = new PrintStream( bos );
			((Exception)oMsg).printStackTrace( ps );
			ps.flush();
			getLogger().info(bos.toString());
			ps.close();
		}
		else {
			getLogger().info(oMsg);
		}
	}
	
	public static void info( String sMsg, Object oMsg ) {
		if( oMsg instanceof Exception ) {
			ByteArrayOutputStream bos =  new ByteArrayOutputStream();
			PrintStream ps = new PrintStream( bos );
			ps.println(sMsg);
			((Exception)oMsg).printStackTrace( ps );
			ps.flush();
			getLogger().info(bos.toString());
			ps.close();
		}
		else {
			getLogger().info(oMsg);
		}
	}

	
	public static void debug( Object oMsg ) {
		if( oMsg instanceof Exception ) {
			ByteArrayOutputStream bos =  new ByteArrayOutputStream();
			PrintStream ps = new PrintStream( bos );
			((Exception)oMsg).printStackTrace( ps );
			ps.flush();
			getLogger().info(bos.toString());
			ps.close();
		}
		else {
			getLogger().info(oMsg);
		}
	}
	
	public static void debug( String sMsg, Object oMsg ) {
		if( oMsg instanceof Exception ) {
			ByteArrayOutputStream bos =  new ByteArrayOutputStream();
			PrintStream ps = new PrintStream( bos );
			ps.println(sMsg);
			((Exception)oMsg).printStackTrace( ps );
			ps.flush();
			getLogger().info(bos.toString());
			ps.close();
		}
		else {
			getLogger().info(oMsg);
		}
	}
	
}
