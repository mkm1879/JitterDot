package edu.clemson.lph.jitter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.clemson.lph.jitter.files.InvalidInputException;
import edu.clemson.lph.jitter.files.OutputCSVFile;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.jitter.structs.WorkingData;

public class JitterDot {
	public static OutputCSVFile fileError = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setup(args);
	}
	
	public static void setup(String[] args) {
		long lStartTime = System.currentTimeMillis();
		String sInFile = null;
		String sOutType = null;
		File fIn = null;

		if( args.length > 0 ) {
			sInFile = args[0];
			fIn = new File(sInFile);
		}
		else {
		    try {
		        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } catch (Exception e) {
		        Loggers.error( "Error setting look and feel\n" + e.getMessage() );
		    }
			File fDir = new File(".");
			JFileChooser open = new JFileChooser(fDir);
			open.setDialogTitle("Select Population File to Deidentify");
			open.setFileSelectionMode(JFileChooser.FILES_ONLY);
			open.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
			int resultOfFileSelect = JFileChooser.ERROR_OPTION;
			resultOfFileSelect = open.showOpenDialog(null);
			if(resultOfFileSelect==JFileChooser.APPROVE_OPTION) {
				fIn = open.getSelectedFile();
				sInFile = fIn.getName();
			}
			else {
				Loggers.error("No file selected");
				System.exit(1);
			}
		}
		if( args.length > 1 ) {
			sOutType = args[1];
		}
		SourceCSVFile source = null;
		try {
			source = new SourceCSVFile( fIn );
			fileError = new OutputCSVFile( new File(sInFile), OutputCSVFile.OutputFileType.ERROR );
			OutputCSVFile fileOut = null;
			fileOut = new OutputCSVFile( new File(sInFile), OutputCSVFile.OutputFileType.KEY );
			WorkingData aData = source.getData();
			aData.deIdentify();
			fileOut.print(aData);
			if( sOutType == null || "NAADSM".equalsIgnoreCase(sOutType) ) {
				fileOut = new OutputCSVFile( new File(sInFile), OutputCSVFile.OutputFileType.NAADSM );
				fileOut.print(aData);
			}
			if( sOutType == null || "INTERSPREAD".equalsIgnoreCase(sOutType) ) {
				fileOut = new OutputCSVFile( new File(sInFile), OutputCSVFile.OutputFileType.INTERSPREAD );
				fileOut.print(aData);
			}
			fileError.close();

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
		
	}

}
