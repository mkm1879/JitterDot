package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.Distance;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;
import edu.clemson.lph.jitter.structs.WorkingDataRow;

public class LargeFilePerformanceTests {
	private static final long MAX_RUN_TIME = 2;
	private static final double TOLERANCE = 1.0;
	SourceCSVFile source;
	WorkingData aData;
	long timeStart;

	@Before
	public void setUp() throws Exception {
		File fileIn = new File( "HerdsPlus.csv");
		try {
			source = new SourceCSVFile( fileIn );
			aData = source.getData();	
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSourceCSVFile() {
		try {
			aData.getMajorAxis();
			aData.setSortDirection();
			aData.setSortDirection( WorkingData.SORT_SOUTH_NORTH );
			aData.setSortDirection( WorkingData.SORT_WEST_EAST );
			aData.getMinLat();
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	
	@Test
	public void testPerformance() {
		long startTime = System.currentTimeMillis();
		// Invoke private subroutine
		try {
			Method method;
			method = aData.getClass().getDeclaredMethod("calcDKs");
			method.setAccessible(true);
			method.invoke(aData);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		} 
		System.out.println("calcDKs took " + (System.currentTimeMillis() - startTime ) + " milliseconds");
		// Invoke private subroutine
		try {
			Method method;
			method = aData.getClass().getDeclaredMethod("jitter");
			method.setAccessible(true);
			method.invoke(aData);
		} catch (Exception e1) {
			e1.printStackTrace();
			fail(e1.getMessage());
		} 
		System.out.println("calcDKs and Jitter took " + (System.currentTimeMillis() - startTime ) + " milliseconds");
		double dSumDiffDistDK = 0.0;
		for( WorkingDataRow row : aData ) {
			try {
				double dDist = Distance.getDistance(row.getLatitudeIn(), row.getLongitudeIn(), row.getLatitude(), row.getLongitude());
				dSumDiffDistDK += dDist  - row.getDK();
			} catch (InvalidCoordinateException e) {
				fail(e.getMessage());
			}
		}
		assertTrue( Math.abs( dSumDiffDistDK / (1.0 * aData.size() ) ) < TOLERANCE); // Would really like to test distribution of these
		System.out.println( dSumDiffDistDK / (1.0 * aData.size() ) ); 
	}

	@Test
	public void testPrint() {
		try {
			long startTime = System.currentTimeMillis();
			aData.deIdentify();
			System.out.println("deIdentify took " + (System.currentTimeMillis() - startTime ) + " milliseconds");
			startTime = System.currentTimeMillis();
			OutputCSVFile fileOut = new OutputCSVFile( new File( "TestOutLarge.csv"), OutputCSVFile.OutputFileType.KEY );
			fileOut.print(aData);
			fileOut = new OutputCSVFile( new File( "TestOutLarge.csv"), OutputCSVFile.OutputFileType.NAADSM );
			fileOut.print(aData);
			fileOut = new OutputCSVFile( new File( "TestOutLarge.csv"), OutputCSVFile.OutputFileType.INTERSPREAD );
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


}
