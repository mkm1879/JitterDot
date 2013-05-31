package edu.clemson.lph.jitter.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.logger.Loggers;

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
	public void testGetCentralMeridianDegrees() {
		// For zone 16, our test default.
		assertTrue( Math.abs( projection.getCentralMeridianDegrees() - (-87.00)) < TOLERANCE  );
	}
	
	@Test
	public void testGetBestZone() {
		// For zone 16, our test default.
		// test again with real data in WorkingDataTests
		assertTrue( UTMProjection.getBestZone( projection.getCentralMeridianDegrees() ) == 16 );
	}

	@Test
	public void testProject() {
		// Just test setting zone in constructor
		// Normal usage sets the zone in constructor and uses for all points
		testProjectRow(34.579, -86.6056, 3826428.04, 536173.11);
		// Remaining tests test specific coordinates from various zones across the US.
		try {
			projection.setUTMZone(20);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
		testProjectRow(41.2318601, -77.03975372, 4660664.89377, -678090.725901);
		testProjectRow(45.27658, -69.605286, 5034940.73076, -18075.367296);
		try {
			projection.setUTMZone(17);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
		testProjectRow(33.11023292, -86.17330374, 3675436.69099, 17067.086245);
		testProjectRow(38.68177308, -85.78775375, 4292354.74463, 83462.52924);
		testProjectRow(33.48331911, -82.20029071, 3705514.29522, 388485.950812);
		try {
			projection.setUTMZone(14);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
		testProjectRow(43.00215555, -91.15228927, 4791034.156, 1139769.407);
		testProjectRow(37.7709911, -92.11738172, 4202780.328, 1106515.307);
		testProjectRow(31.0895711, -97.39004972, 3440642.973, 653561.2669);
		testProjectRow(32.33757409, -89.49892171, 3617811.851, 1395884.757);
		try {
			projection.setUTMZone(12);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
		testProjectRow(44.89701413, -99.67484647, 5034298.97574, 1394188.90818);
		testProjectRow(34.95464622, -109.0466408, 3869755.81155, 678357.523273);
		testProjectRow(43.13828955, -108.1789843, 4780035.31868, 729426.167439);
		testProjectRow(47.8251026, -107.2680564, 5303607.198, 779306.5346045);
		testProjectRow(36.48947911, -100.5498393, 4089411.74887, 1437510.22072);
		try {
			projection.setUTMZone(10);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
		testProjectRow(43.79825018 ,-118.7501754, 4858249.4553, 841891.820637);
		testProjectRow(38.04480634, -115.8841053, 4234765.43982, 1124763.51893);
		testProjectRow(47.98348223, -112.952873, 5363513.21396, 1249296.61747);
		try {
			projection.setUTMZone(4);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
		testProjectRow(62.02674, -142.762511, 6983714.91955, 1343262.29862);
	}
	
	
	private void testProjectRow( Double dLat, Double dLong, Double dNorth, Double dEast ) {
		Double[] aCoords;
		try {
			aCoords = projection.project(dLat, dLong);
			assertTrue( Math.abs(aCoords[0] - dEast) < TOLERANCE  );
			assertTrue( Math.abs(aCoords[1] - dNorth) < TOLERANCE  );	
		} catch (InvalidCoordinateException e) {
			Loggers.error(e);
			fail( e.getMessage() );
		}
	}


}
