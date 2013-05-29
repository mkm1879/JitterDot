package edu.clemson.lph.jitter.structs;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingDataRow;

public class WorkingDataRowTests {
	private WorkingData data = new WorkingData();
	
	@Before
	public void setUp() throws Exception {
		data.setRows(100);  // this is silly just make sure we have enough keys for testing.
		double dLat = 34.1;
		double dLong = -81.9;
		double dDelta = 0.5;
		for( int i = 5; i > 0; i-- ) {
			WorkingDataRow dNew = new WorkingDataRow( "Farm" + i, dLat, dLong, "Animal Type");
			dNew.setIntegrator("A Farms");
			data.add(dNew);
			dLat += dDelta;
			dLong += dDelta;
		}
	}

	@Test
	public void testWorkingData() {
		WorkingDataRow dNew;
		try {
			dNew = new WorkingDataRow( "FarmX", 35.0, -81.0, "Animal Type");
			assertTrue( dNew != null );
		} catch (InvalidCoordinateException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}


	@Test
	public void testEqualsObject() {
		WorkingDataRow dNew;
		try {
			dNew = new WorkingDataRow( "FarmX", 35.0, -81.0, "Animal Type");
			data.add(dNew);
			WorkingDataRow dNew2 = new WorkingDataRow( "FarmX", 35.0, -81.0, "Animal Type");
			data.add(dNew2);
			assertFalse( dNew.equals(dNew2) );
		} catch (InvalidCoordinateException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testGetKey() {
// Now that we assign keys pseudorandomly this test doesn't make sense.
//		assertTrue( data.get(0).getKey() == 1 );
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
	public void testGetAnimalType() {
		assertTrue( data.get(0).getAnimalTypeIn().equals("Animal Type"));
	}

	@Test
	public void testGetIntegrator() {
		assertTrue( data.get(0).getIntegrator().equals("A Farms"));
	}

	@Test
	public void testGetDK() {
		// fail("Not yet implemented");
	}

	@Test
	public void testSetDK() {
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
