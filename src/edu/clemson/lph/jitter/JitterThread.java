package edu.clemson.lph.jitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.LogManager;

import edu.clemson.lph.dialogs.MessageDialog;
import edu.clemson.lph.dialogs.ProgressDialog;
import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.files.InvalidInputException;
import edu.clemson.lph.jitter.files.OutputCSVFile;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.WorkingData;
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
public class JitterThread extends Thread {
	private ConfigFrame frame;
	private String sDataFile;
	private ProgressDialog prog = null;
	

	public JitterThread(ConfigFrame parent, String sDataFile) {
		super("RunJitter");
		this.frame = parent;
		this.sDataFile = sDataFile;
	}
	
	/**
	 * Sets up ProgressDialog and then starts thread.
	 */
	public void runJitter() {
		if( !JitterDot.isQuiet() ) {
			prog = new ProgressDialog(frame, "JitterDot: Progress", "Running De-identification");
			prog.setAuto(true);
			prog.setVisible(true);
		}
		// Do not allow concurrent access to mutator functions in ConfigFile during 
		// the actual deidentification run.
		ConfigFile.lockConfig( true );
		this.start();
	}
	
	@Override
	public void run() {
		long lStartTime = System.currentTimeMillis();
		long lRows = -1;
		// TODO Move this to secondary thread, add method for canceling?, add ProgressDialog and updates, fix error output now going to loggers
		SourceCSVFile source = null;
		try {
			if( prog != null ) prog.setCurrentTask("Reading source data");
			File fIn = new File( sDataFile );
			source = new SourceCSVFile( fIn );
			OutputCSVFile fileOut = null;
			WorkingData aData = source.getData();
			if( aData == null ) {
				MessageDialog.messageLater(frame, "JitterDot Error", "Failed to read data file. See log file for reason, possibly out of memory.");
				return; // via finally to clean up
			}
			lRows = aData.size();
			if( lRows == 0 ) {
				Loggers.error("Empty Data File: " + sDataFile);
				return;  // return via finally block.
			}
			fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.KEY );
			if( prog != null ) prog.setCurrentTask("Jittering Data");
			aData.deIdentify(prog);
			if( prog != null ) prog.setCurrentTask("Writing Key File");
			fileOut.print(aData);		
			if( ConfigFile.isNAADSMRequested() ) {
				if( prog != null ) prog.setCurrentTask("Writing NAADSM File");
				fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.NAADSM );
				fileOut.print(aData);
			}
			if( ConfigFile.isInterspreadRequested() ) {
				if( prog != null ) prog.setCurrentTask("Writing InterspreadPlus File");
				fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.INTERSPREAD );
				fileOut.print(aData);
			}
			if( prog != null ) prog.setCurrentTask("Closing");
			// Also closes the error file it tracks.
			source.close();

		} catch (FileNotFoundException e) {
			if( JitterDot.isCmd() ) {
				System.err.println( "Cannot find file " + sDataFile);
			}
			else {
				e.printStackTrace();
			}
			Loggers.error(e);
		} catch (IOException e) {
			if( JitterDot.isCmd() ) {
				System.err.println( "Cannot open file " + sDataFile);
			}
			else {
				e.printStackTrace();
			}
			Loggers.error(e);
		} catch (NumberFormatException e) {
			if( JitterDot.isCmd() ) {
				System.err.println( "Error reading file " + sDataFile);
			}
			else {
				e.printStackTrace();
			}
			Loggers.error(e);
		} catch (InvalidCoordinateException e) {
			if( JitterDot.isCmd() ) {
				System.err.println( "Invalid coordinates in file " + sDataFile);
			}
			else {
				e.printStackTrace();
			}
			Loggers.error(e);
		} catch (InvalidInputException e) {
			if( JitterDot.isCmd() ) {
				System.err.println( "Invalid input in file " + sDataFile);
			}
			else {
				e.printStackTrace();
			}
			Loggers.error(e);
		} catch( OutOfMemoryError ee ) {
			MessageDialog.messageLater(frame, "JitterDot Error", "Out of memory reading dataset");
			Loggers.error("Ran out of memory reading data.  Add memory or use smaller dataset.");
//			LogManager.shutdown();
		}
		finally {
			if( lRows > 0 ) {
				long lEndTime = System.currentTimeMillis();
				Loggers.getLogger().info( "Run of " + lRows + " rows took "+ Double.toString((lEndTime - lStartTime)/1000.0) + " seconds" );
				System.out.println( "Run of " + lRows + " rows took "+ Double.toString((lEndTime - lStartTime)/1000.0) + " seconds" );
			}
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						if( prog != null ) prog.setVisible(false);
						if( frame != null ) {
							frame.setEditEnabled(true);
							ConfigFile.lockConfig(false);
						}
						else {
							// We are running from command line so just exit
							System.exit(0);
						}
					}
				});
			} catch (InvocationTargetException e) {
				Loggers.error(e);
			} catch (InterruptedException e) {
				Loggers.error(e);
			}
		}
	}

}
