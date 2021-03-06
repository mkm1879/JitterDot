package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;
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
public class OutputCSVFileTests {
//	private static final double TOLERANCE = 0.0001;
	SourceCSVFile source;

	@Before
	public void setUp() throws Exception {
		try {
			ConfigFile.setConfigFilePath("./JitterDot.config");
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
			new File( "TestFiles/TestKEY.csv" ).delete();
			new File( "TestFiles/TestINTERSPREAD.csv" ).delete();
			new File( "TestFiles/TestNAADSM.csv" ).delete();
			
			WorkingData aData = source.getData();
			aData.deIdentify();
			
			OutputCSVFile fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.KEY );
			fileOut.print(aData);
			assertTrue( new File( "TestFiles/TestKEY.csv" ).exists() );
			
			fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.NAADSM );
			fileOut.print(aData);
			assertTrue( new File( "TestFiles/TestNAADSM.csv" ).exists() );
			
			fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.INTERSPREAD );
			fileOut.print(aData);
			assertTrue( new File( "TestFiles/TestINTERSPREAD.csv" ).exists() );
			
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
			new File( "TestFiles/TestERRORS.csv" ).delete();

			WorkingData aData = source.getData();
			aData.deIdentify();
			OutputCSVFile fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.ERROR );
			fileOut.printErrorRow("Test Error", new String[] {"Col1","Col2","col3","Col4"});
			fileOut.close();
			assertTrue( new File( "TestFiles/TestERRORS.csv" ).exists() );

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
