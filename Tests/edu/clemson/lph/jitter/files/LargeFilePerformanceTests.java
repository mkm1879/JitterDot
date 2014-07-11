package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.Distance;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;
import edu.clemson.lph.jitter.structs.WorkingDataRow;
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
public class LargeFilePerformanceTests {
	private static final long MAX_RUN_TIME = 2;
	private static final double TOLERANCE = 1.0;
	SourceCSVFile source;
	WorkingData aData;
	long timeStart;

	@Before
	public void setUp() throws Exception {
	}

	
	@Test
	public void testPerformance() {
		long startTime = System.currentTimeMillis();
		// Invoke private subroutine
		File fileIn = new File( "TestFiles/HerdsPlus.csv");
		ConfigFile.setConfigFilePath("TestFiles/JitterDotConfigTest.config");
		try {
			source = new SourceCSVFile( fileIn );
			aData = source.getData();	
			System.out.println("getData() took " + (System.currentTimeMillis() - startTime ) + " milliseconds");
		} catch (Exception e) {
			e.printStackTrace();
			fail( "Uncaught exception " + e.getMessage() );
		}
		startTime = System.currentTimeMillis();
		try {
			aData.deIdentify();
		} catch (Exception e1) {
			e1.printStackTrace();
			fail("Uncaught Exception " + e1.getMessage());
		} 
		System.out.println("deIdentify took " + (System.currentTimeMillis() - startTime ) + " milliseconds");
		double dSumDiffDistDK = 0.0;
		for( WorkingDataRow row : aData ) {
			try {
				double dDist = Distance.getDistance(row.getLatitudeIn(), row.getLongitudeIn(), row.getLatitude(), row.getLongitude());
				dSumDiffDistDK += dDist  - row.getDK();
			} catch (InvalidCoordinateException e) {
				e.printStackTrace();
				fail(row.getOriginalKey() + ": " + row.getDK() + ": " + e.getMessage());
			}
		}
		assertTrue( Math.abs( dSumDiffDistDK / (1.0 * aData.size() ) ) < TOLERANCE); // Would really like to test distribution of these
		System.out.println( "Average(distance - dK) = " + dSumDiffDistDK / (1.0 * aData.size() ) ); 
		try {
			startTime = System.currentTimeMillis();
			OutputCSVFile fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.KEY );
			fileOut.print(aData);
			fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.NAADSM );
			fileOut.print(aData);
			fileOut = new OutputCSVFile( source, OutputCSVFile.OutputFileType.INTERSPREAD );
			fileOut.print(aData);			
			System.out.println("print took " + (System.currentTimeMillis() - startTime ) + " milliseconds");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testPrint() {
	}


}
