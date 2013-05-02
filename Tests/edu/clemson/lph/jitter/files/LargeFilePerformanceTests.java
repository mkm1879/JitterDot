package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;

public class LargeFilePerformanceTests {
	private static final long MAX_RUN_TIME = 2;
	private static final double TOLERANCE = 0.0001;
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
		aData.calcDKs(5);
		System.out.println("calcDKs took " + (System.currentTimeMillis() - startTime ) + " milliseconds");
	}

	@Test
	public void testPrint() {
		try {
			OutputKeyCSVFile fileOut = new OutputKeyCSVFile( new File( "TestOutLarge.csv") );
			aData.setSortDirection();
			aData.calcDKs(5);
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

	
	@After
	public void TearDown() throws Exception {
		long timeEnd = System.currentTimeMillis();
		System.out.println( "Runtime = " + ((timeEnd - timeStart)) + " milliseconds" );
		assertTrue( timeEnd - timeStart < (MAX_RUN_TIME * 1000) );
	}

}
