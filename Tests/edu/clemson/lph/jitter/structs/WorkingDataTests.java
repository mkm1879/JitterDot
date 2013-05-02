package edu.clemson.lph.jitter.structs;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.files.InvalidInputException;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.geometry.UTMProjection;

public class WorkingDataTests {
	private static final double TOLERANCE = 0.0001;
	SourceCSVFile source;
	WorkingData aData;

	@Before
	public void setUp() throws Exception {
		File fileIn = new File( "Test.csv");
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
	public void testGetMinLong() {
		assertTrue( Math.abs(aData.getMinLong() - (-82.992877) ) < TOLERANCE );
	}

	@Test
	public void testGetMaxLong() {
		assertTrue( Math.abs(aData.getMaxLong() - (-80.647672) ) < TOLERANCE );
	}

	@Test
	public void testGetMinLat() {
		assertTrue( Math.abs(aData.getMinLat() - (33.825771) ) < TOLERANCE );
	}

	@Test
	public void testGetMaxLat() {
		assertTrue( Math.abs(aData.getMaxLat() - (34.787656) ) < TOLERANCE );
	}

	@Test
	public void testGetMedianLongEven() {
		assertTrue( Math.abs(aData.getMedianLong() - (-81.6357) ) < TOLERANCE );
	}
	
	@Test
	public void testGetBestZone() {
		// For zone 16, our test default.
		// test again with real data in WorkingDataTests
		assertTrue( UTMProjection.getBestZone( aData.getMedianLong() ) == 17 );
	}
	
	@Test 
	public void testGetMajorAxis() {
		try {
			aData.setSortDirection();
			assertTrue(aData.getSortDirection() == WorkingData.SORT_WEST_EAST);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	// Also tests CompareTo and CollectionsSort
	@Test
	public void testSetSortDirection() {
		try {
			aData.setSortDirection(WorkingData.SORT_SOUTH_NORTH);
			aData.sortMajorAxis();
			assertTrue( aData.get(0).getOriginalKey().equals("32") );
		} catch (Exception e) {
			fail(e.getMessage());
		}
		try {
			aData.setSortDirection(WorkingData.SORT_WEST_EAST);
			aData.sortMajorAxis();
			assertTrue( aData.get(0).getOriginalKey().equals("17") );
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/** 
	 * This tests both an odd number of rows but also switching sort direction
	 * prior to calculating the median.
	 */
	@Test
	public void testGetMedianLongOdd() {
		File fileIn = new File( "Test2.csv");
		try {
			source = new SourceCSVFile( fileIn );
			aData = source.getData();	
			aData.setSortDirection(WorkingData.SORT_SOUTH_NORTH);
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (NumberFormatException e) {
			fail(e.getMessage());
		} catch (InvalidCoordinateException e) {
			fail(e.getMessage());
		} catch (InvalidInputException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
		assertTrue( Math.abs(aData.getMedianLong() - (-81.6194) ) < TOLERANCE );
	}

	@Test
	public void testCalcDKs() {
		aData.calcDKs(5);
	}
}
