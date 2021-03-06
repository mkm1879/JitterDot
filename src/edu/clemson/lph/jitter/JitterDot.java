package edu.clemson.lph.jitter;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.UIManager;

import edu.clemson.lph.dialogs.MessageDialog;
import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.ui.ConfigFrame;

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
public class JitterDot {
	// Make args available in runnable without having to subclass Thread.
	private static String[] args;
	private static boolean bCmd;
	private static boolean bQuiet;

	/**
	 * @param args
	 */
	public static void main(String[] argv) {
		args = argv;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			    try {
			        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			        if( UIManager.getSystemLookAndFeelClassName().contains("Aqua") ) {
			        	setUIFontSizeChange( -2 );
			        }
			    } catch (Exception e) {
			        Loggers.error( "Error setting look and feel\n" + e.getMessage() );
			    }
				try {
					setup(args);
				} catch (Exception e) {
					Loggers.error(e);
				}
			}
		});
	}
	
	public static void setup(String[] args) {
		String sInFile = null;
		boolean bRunNow = false;

		if( args.length > 0 ) {
			for( String sArg : args ) {
				if( sArg.toLowerCase().endsWith(".csv")) {
					bCmd = true;
					sInFile = sArg;
				}
				else if( sArg.toLowerCase().endsWith(".config")) {
					bCmd = true;
					ConfigFile.setConfigFilePath(sArg);
				}
				else if( sArg.toLowerCase().equals("naadsm")) {
					bCmd = true;
					ConfigFile.setInterspreadRequested(false);
					ConfigFile.setNAADSMRequested(true);
				}
				else if( sArg.toLowerCase().startsWith("interspread")) {
					bCmd = true;
					ConfigFile.setNAADSMRequested(false);
					ConfigFile.setInterspreadRequested(true);
				}
				else if( sArg.startsWith("-")) {
					if( sArg.toLowerCase().contains("r")) {
						bCmd = true;
						bRunNow = true;
					}
					if( sArg.toLowerCase().contains("q")) {
						bQuiet = true;
					}
				}
				else {
					Loggers.error("Unexpected argument '" + sArg + "' on command line");
					System.exit(1);
				}
			}
		}
		if( bRunNow && sInFile != null ) {
			runJitter( sInFile );
		}
		else {
			ConfigFrame frame = null;
			try {
				frame = new ConfigFrame();
				if( sInFile != null )
					frame.setDataFile( sInFile );
				frame.setVisible(true);
			} catch (IOException e) {
				MessageDialog.messageWait(frame, "JitterDot Error", e.getMessage() );
				System.exit(1);
			} catch (Exception e) {
				Loggers.error(e);
			}		
		}	
	}
	
	public static boolean isCmd() {
		return bCmd;
	}
	
	public static boolean isQuiet() {
		return bQuiet;
	}
	
	public static void runJitter( String sDataFile ) {
		JitterThread thread = new JitterThread( null, sDataFile );
		thread.runJitter();
	}
	
	/**
	 * Adjust the size on some OS's to fit.  Mac OS X needs smaller.
	 * @param iDelta
	 */
	private static void setUIFontSizeChange( int iDelta )
	{
	    java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements())
	    {
	        Object key = keys.nextElement();
	        Object value = UIManager.get(key);
	        if (value instanceof javax.swing.plaf.FontUIResource)
	        {
	        	javax.swing.plaf.FontUIResource f = (javax.swing.plaf.FontUIResource)value;
	        	String sName = f.getName();
	        	int iStyle = f.getStyle();
	        	int iSize = f.getSize();
	        	iSize += iDelta;
	        	javax.swing.plaf.FontUIResource f1 = new javax.swing.plaf.FontUIResource(sName, iStyle, iSize);
	            UIManager.put(key, f1);
	        }
	    }
	}

}
