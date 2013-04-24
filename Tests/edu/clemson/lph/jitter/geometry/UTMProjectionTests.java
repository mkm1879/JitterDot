package edu.clemson.lph.jitter.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class UTMProjectionTests {
	private static final double TOLERANCE = 0.1;
	private UTMProjection projection;
	
	@Before
	public void setUp() throws Exception {
		projection = new UTMProjection( 16 );
	}

	@Test(expected=InvalidUTMZoneException.class)
	public void testUTMProjection() throws InvalidUTMZoneException {
		new UTMProjection( 28 );
	}

	@Test
	public void testProject() {
		testProjectRow(34.579, -86.6056, 536173.11, 3826428.04);
	}
	
	
	private void testProjectRow( double dLat, double dLong, double dNorth, double dEast ) {
		double[] aCoords;
		try {
			aCoords = projection.project(dLat, dLong);
			System.out.println( dNorth + ", " + dEast + " =? " + aCoords[0] + ", " + aCoords[1]);
			assertTrue( Math.abs(aCoords[0] - dNorth) < TOLERANCE  );
			assertTrue( Math.abs(aCoords[1] - dEast) < TOLERANCE  );	
		} catch (InvalidCoordinateException e) {
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}


}
