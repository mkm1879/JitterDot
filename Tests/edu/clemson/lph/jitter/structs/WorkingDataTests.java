package edu.clemson.lph.jitter.structs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.structs.WorkingData;

public class WorkingDataTests {
	private ArrayList<WorkingData> data = new ArrayList<WorkingData>();
	
	@Before
	public void setUp() throws Exception {
		double dLat = 34.1;
		double dLong = -81.9;
		double dDelta = 0.5;
		WorkingData.resetKeyList();
		for( int i = 5; i > 0; i-- ) {
			WorkingData dNew = new WorkingData( "Farm" + i, dLat, dLong, "Animal Type");
			data.add(dNew);
			dLat += dDelta;
			dLong += dDelta;
		}
	}

	@Test
	public void testWorkingData() {
		WorkingData dNew = new WorkingData( "FarmX", 35.0, -81.0, "Animal Type");
		assertTrue( dNew != null );
	}

	// Also tests CompareTo and CollectionsSort
	@Test
	public void testSetSortDirection() {
		try {
			WorkingData.setSortDirection(WorkingData.SORT_NORTH_SOUTH);
			Collections.sort(data);
			System.out.println( data.get(0).getKey() );
			assertTrue( data.get(0).getKey() == 5 );
		} catch (Exception e) {
			fail(e.getMessage());
		}
		try {
			WorkingData.setSortDirection(WorkingData.SORT_WEST_EAST);
			Collections.sort(data);
			System.out.println( data.get(0).getKey() );
			assertTrue( data.get(0).getKey() == 1 );
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testEqualsObject() {
		WorkingData dNew = new WorkingData( "FarmX", 35.0, -81.0, "Animal Type");
		WorkingData dNew2 = new WorkingData( "FarmX", 35.0, -81.0, "Animal Type");
		assertFalse( dNew.equals(dNew2) );
	}

	@Test
	public void testGetKey() {
		assertTrue( data.get(0).getKey() == 1 );
	}

	@Test
	public void testGetLatitudeIn() {
		assertTrue( data.get(0).getLatitudeIn() == 34.1 );
	}

	@Test
	public void testGetLongitudeIn() {
		assertTrue( data.get(0).getLongitudeIn() == -81.9 );
	}

	@Test
	public void testGetOriginalKey() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetAnimalType() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetIntegrator() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetDN() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetDN() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetDLat() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetDLat() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetDLong() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetDLong() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetLatitude() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetLatitude() {
		// fail("Not yet implemented");
	}

	@Test
	public void testGetLongitude() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetLongitude() {
		// fail("Not yet implemented");
	}

}
