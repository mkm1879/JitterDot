package edu.clemson.lph.jitter.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Loggers {
	static Logger logger = null;
	
	public static Logger getLogger() {
		if( logger == null ) {
			logger = Logger.getLogger("edu.clemson.lph.jitter.logger");
		    PropertyConfigurator.configure("JitterDot.config");
		    logger.info("Entering application.");
		}
		return logger;
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
	
	public static void Info( String sMsg, Object oMsg ) {
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
