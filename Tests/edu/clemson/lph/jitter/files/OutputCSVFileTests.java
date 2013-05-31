package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;

public class OutputCSVFileTests {
	private static final double TOLERANCE = 0.0001;
	SourceCSVFile source;

	@Before
	public void setUp() throws Exception {
		File fileIn = new File( "Test.csv");
		try {
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
			WorkingData aData = source.getData();
			aData.deIdentify();
			OutputCSVFile fileOut = new OutputCSVFile( new File( "TestOut.csv"), OutputCSVFile.OutputFileType.KEY );
			fileOut.print(aData);
			fileOut = new OutputCSVFile( new File( "TestOut.csv"), OutputCSVFile.OutputFileType.NAADSM );
			fileOut.print(aData);
			fileOut = new OutputCSVFile( new File( "TestOut.csv"), OutputCSVFile.OutputFileType.INTERSPREAD );
			fileOut.print(aData);
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
