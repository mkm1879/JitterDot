package edu.clemson.lph.jitter.structs;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.files.InvalidInputException;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.geometry.Distance;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.geometry.UTMProjection;
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
public class WorkingDataTests {
	private static final double TOLERANCE = 0.0001;
	SourceCSVFile source;
	WorkingData aData;

	@Before
	public void setUp() throws Exception {
		ConfigFile.setConfigFilePath("./JitterDot.config");
		try {
			System.out.println( ConfigFile.getConfigFile().getAbsolutePath() );
			File fileIn = new File( "TestFiles/Test.csv");
			source = new SourceCSVFile( fileIn );
			aData = source.getData();		
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMinLong() {
		assertTrue( Math.abs(aData.getMinLong() - (-82.992877) ) < TOLERANCE );
	}

	@Test
	public void testGetMaxLong() {
		assertTrue( Math.abs(aData.getMaxLong() - (-79.611257) ) < TOLERANCE );
	}

	@Test
	public void testGetMinLat() {
		assertTrue( Math.abs(aData.getMinLat() - (32.986462) ) < TOLERANCE );
	}

	@Test
	public void testGetMaxLat() {
		assertTrue( Math.abs(aData.getMaxLat() - (34.81479) ) < TOLERANCE );
	}

	@Test
	public void testGetMedianLongEven() {
		assertTrue( Math.abs(aData.getMedianLongitude() - (-81.088185) ) < TOLERANCE );
	}
	
	@Test
	public void testGetBestZone() {
		// For zone 16, our test default.
		// test again with real data in WorkingDataTests
		assertTrue( UTMProjection.getBestZone( aData.getMedianLongitude() ) == 17 );
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
			assertTrue( aData.get(0).getOriginalKey().equals("187") );
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
		File fileIn = new File( "TestFiles/Test2.csv");
		WorkingData aData2 = null;
		try {
			SourceCSVFile source2 = new SourceCSVFile( fileIn );
			aData2 = source2.getData();	
			aData2.setSortDirection(WorkingData.SORT_SOUTH_NORTH);
			System.out.println("aData2.medianLong = " + aData2.getMedianLongitude());
			assertTrue( Math.abs(aData2.getMedianLongitude() - (-81.6194) ) < TOLERANCE );
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
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	// Does not work in isolation
//	@Test
//	public void testCalcDKs() {
//		// Invoke private subroutine
//		try {
//			Method method;
//			method = aData.getClass().getDeclaredMethod("calcDKs");
//			method.setAccessible(true);
//			method.invoke(aData);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			fail(e1.getMessage());
//		} 
//	}
	
	@Test
	public void testJitter() {
		aData.deIdentify();
		double dSumDistance = 0.0;
		for( WorkingDataRow row : aData ) {
			try {
				double dDist = Distance.getDistance(row.getLatitudeIn(), row.getLongitudeIn(), row.getLatitude(), row.getLongitude());
//				System.out.println( row.getDK() + ", " + dDist + ", " + row.getDLat() + ", " + row.getDLong() );
				dSumDistance += dDist  - row.getDK();
			} catch (InvalidCoordinateException e) {
				fail(e.getMessage());
			}
		}
		System.out.println( "Avg dist - dK = " + dSumDistance / (1.0 * aData.size() ) ); 
	}
}
