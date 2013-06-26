package edu.clemson.lph.jitter;

import java.awt.Window;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import edu.clemson.lph.dialogs.ProgressDialog;
import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.files.InvalidInputException;
import edu.clemson.lph.jitter.files.OutputCSVFile;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.WorkingData;

public class JitterThread extends Thread {
	private Window parent;
	private String sDataFile;
	private ProgressDialog prog = null;
	

	public JitterThread(Window parent, String sDataFile) {
		super("RunJitter");
		this.parent = parent;
		this.sDataFile = sDataFile;
	}
	
	/**
	 * Sets up ProgressDialog and then starts thread.
	 */
	public void runJitter() {
		prog = new ProgressDialog(parent, "JitterDot: Progress", "Running De-identification");
		prog.setAuto(true);
		prog.setVisible(true);
		// Do not allow concurrent access to mutator functions in ConfigFile during 
		// the actual deidentification run.
		ConfigFile.lockConfig();
		this.start();
	}
	
	@Override
	public void run() {
		long lStartTime = System.currentTimeMillis();
		
		// TODO Move this to secondary thread, add method for canceling?, add ProgressDialog and updates, fix error output now going to loggers
		SourceCSVFile source = null;
		try {
			prog.setCurrentTask("Reading source data");
			OutputCSVFile fileError = null;
			File fIn = new File( sDataFile );
			source = new SourceCSVFile( fIn );
			// Set the global ErrorOutput file.  Is this the right way of handling this?  Comparable to Loggers.
			fileError = new OutputCSVFile( new File(sDataFile), OutputCSVFile.OutputFileType.ERROR );
			JitterDot.setErrorFile(fileError);
			OutputCSVFile fileOut = null;
			fileOut = new OutputCSVFile( new File(sDataFile), OutputCSVFile.OutputFileType.KEY );
			WorkingData aData = source.getData();
			prog.setCurrentTask("Jittering Data");
			aData.deIdentify(prog);
			prog.setCurrentTask("Writing Key File");

			fileOut.print(aData);
			if( ConfigFile.isNAADSMRequested() ) {
				prog.setCurrentTask("Writing NAADSM File");
				fileOut = new OutputCSVFile( new File(sDataFile), OutputCSVFile.OutputFileType.NAADSM );
				fileOut.print(aData);
			}
			if( ConfigFile.isInterspreadRequested() ) {
				prog.setCurrentTask("Writing InterspreadPlus File");
				fileOut = new OutputCSVFile( new File(sDataFile), OutputCSVFile.OutputFileType.INTERSPREAD );
				fileOut.print(aData);
			}
			fileError.close();
			prog.setCurrentTask("Closing");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Loggers.error(e);
		} catch (IOException e) {
			e.printStackTrace();
			Loggers.error(e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Loggers.error(e);
		} catch (InvalidCoordinateException e) {
			e.printStackTrace();
			Loggers.error(e);
		} catch (InvalidInputException e) {
			e.printStackTrace();
			Loggers.error(e);
		}
		long lEndTime = System.currentTimeMillis();
		Loggers.getLogger().info( "Run took " + Double.toString((lEndTime - lStartTime)/1000.0) + " seconds" );
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					prog.setVisible(false);
				}
			});
		} catch (InvocationTargetException e) {
			Loggers.error(e);
		} catch (InterruptedException e) {
			Loggers.error(e);
		}
	}

}
