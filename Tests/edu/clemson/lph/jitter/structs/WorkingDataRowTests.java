package edu.clemson.lph.jitter.structs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.structs.WorkingDataRow;

public class WorkingDataRowTests {
	private WorkingData data = new WorkingData();
	
	@Before
	public void setUp() throws Exception {
		data.setRows(100);
		double dLat = 34.1;
		double dLong = -81.9;
		double dDelta = 0.5;
		for( int i = 5; i > 0; i-- ) {
			WorkingDataRow dNew = new WorkingDataRow( "Farm" + i, dLat, dLong, "Animal Type");
			data.add(dNew);
			dLat += dDelta;
			dLong += dDelta;
		}
	}

	@Test
	public void testWorkingData() {
		WorkingDataRow dNew = new WorkingDataRow( "FarmX", 35.0, -81.0, "Animal Type");
		assertTrue( dNew != null );
	}


	@Test
	public void testEqualsObject() {
		WorkingDataRow dNew = new WorkingDataRow( "FarmX", 35.0, -81.0, "Animal Type");
		data.add(dNew);
		WorkingDataRow dNew2 = new WorkingDataRow( "FarmX", 35.0, -81.0, "Animal Type");
		data.add(dNew2);
		assertFalse( dNew.equals(dNew2) );
	}

	@Test
	public void testGetKey() {
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
