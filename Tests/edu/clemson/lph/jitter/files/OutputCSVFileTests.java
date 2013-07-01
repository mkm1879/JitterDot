package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.JitterDot;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;

public class OutputCSVFileTests {
//	private static final double TOLERANCE = 0.0001;
	SourceCSVFile source;

	@Before
	public void setUp() throws Exception {
		try {
			File fileIn = new File( "TestFiles/Test.csv");
			source = new SourceCSVFile( fileIn );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testPrint() {
		try {
			new File( "TestFiles/TestOutKEY.csv" ).delete();
			new File( "TestFiles/TestOutINTERSPREAD.csv" ).delete();
			new File( "TestFiles/TestOutNAADSM.csv" ).delete();
			
			WorkingData aData = source.getData();
			aData.deIdentify();
			
			OutputCSVFile fileOut = new OutputCSVFile( new File( "TestFiles/TestOut.csv"), OutputCSVFile.OutputFileType.KEY );
			fileOut.print(aData);
			assertTrue( new File( "TestFiles/TestOutKEY.csv" ).exists() );
			
			fileOut = new OutputCSVFile( new File( "TestFiles/TestOut.csv"), OutputCSVFile.OutputFileType.NAADSM );
			fileOut.print(aData);
			assertTrue( new File( "TestFiles/TestOutNAADSM.csv" ).exists() );
			
			fileOut = new OutputCSVFile( new File( "TestFiles/TestOut.csv"), OutputCSVFile.OutputFileType.INTERSPREAD );
			fileOut.print(aData);
			assertTrue( new File( "TestFiles/TestOutINTERSPREAD.csv" ).exists() );
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidCoordinateException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidInputException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testPrintError() {
		try {
			new File( "TestFiles/TestOutERRORS.csv" ).delete();

			WorkingData aData = source.getData();
			aData.deIdentify();
			OutputCSVFile fileOut = new OutputCSVFile( new File( "TestFiles/TestOut.csv"), OutputCSVFile.OutputFileType.ERROR );
			fileOut.printErrorRow("Test Error", new String[] {"Col1","Col2","col3","Col4"});
			fileOut.close();
			assertTrue( new File( "TestFiles/TestOutERRORS.csv" ).exists() );

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidCoordinateException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (InvalidInputException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
