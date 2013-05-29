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
		timeStart = System.currentTimeMillis();
		File fileIn = new File( "BigTest.csv");
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
//		aData.calcDKs();
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
				System.out.println( row.getDK() + ", " + dDist + ", " + row.getDLat() + ", " + row.getDLong() );
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
			OutputKeyCSVFile fileOut = new OutputKeyCSVFile( new File( "TestOutLarge.csv") );
			aData.setSortDirection();
			try {
				Method method;
				method = aData.getClass().getDeclaredMethod("calcDKs");
				method.setAccessible(true);
				method.invoke(aData);
			} catch (Exception e1) {
				e1.printStackTrace();
				fail(e1.getMessage());
			} 
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	
	@After
	public void TearDown() throws Exception {
		long timeEnd = System.currentTimeMillis();
		System.out.println( "Runtime = " + ((timeEnd - timeStart)) + " milliseconds" );
		assertTrue( timeEnd - timeStart < (MAX_RUN_TIME * 1000) );
	}

}
