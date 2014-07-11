package edu.clemson.lph.jitter.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.logger.Loggers;
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
public class UTMProjectionTests {
	private static final double TOLERANCE = 0.1;
	

	@Test(expected=InvalidUTMZoneException.class)
	public void testUTMProjectionBadZone() throws InvalidUTMZoneException {
		new UTMProjection( 28, "N" );
	}

	@Test(expected=InvalidUTMZoneException.class)
	public void testUTMProjectionBadHemisphere() throws InvalidUTMZoneException {
		new UTMProjection( 17, "W" );
	}
	
	@Test
	public void testGetCentralMeridianDegrees() {
		UTMProjection projection;
		try {
			projection = new UTMProjection( 16, "N" );
			// For zone 16, our test default.
			assertTrue( Math.abs( projection.getCentralMeridianDegrees() - (-87.00)) < TOLERANCE  );
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetBestZone() {
		UTMProjection projection;
		try {
			projection = new UTMProjection( 16, "N" );
			// For zone 16, our test default.
			// test again with real data in WorkingDataTests
			assertTrue( UTMProjection.getBestZone( projection.getCentralMeridianDegrees() ) == 16 );
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testProject() {
		UTMProjection projection;
		try {
			projection = new UTMProjection( 16, "N" );
			// Just test setting zone in constructor
			// Normal usage sets the zone in constructor and uses for all points
			testProjectRow(projection,34.579, -86.6056, 3826428.04, 536173.11);
			// Remaining tests test specific coordinates from various zones across the US.
			projection.setUTMZone(20, "N");
			testProjectRow(projection,41.2318601, -77.03975372, 4660664.89377, -678090.725901);
			testProjectRow(projection,45.27658, -69.605286, 5034940.73076, -18075.367296);
			projection.setUTMZone(17, "N");
			testProjectRow(projection,33.11023292, -86.17330374, 3675436.69099, 17067.086245);
			testProjectRow(projection,38.68177308, -85.78775375, 4292354.74463, 83462.52924);
			testProjectRow(projection,33.48331911, -82.20029071, 3705514.29522, 388485.950812);
			projection.setUTMZone(14, "N");
			testProjectRow(projection,43.00215555, -91.15228927, 4791034.156, 1139769.407);
			testProjectRow(projection,37.7709911, -92.11738172, 4202780.328, 1106515.307);
			testProjectRow(projection,31.0895711, -97.39004972, 3440642.973, 653561.2669);
			testProjectRow(projection,32.33757409, -89.49892171, 3617811.851, 1395884.757);
			projection.setUTMZone(12, "N");
			testProjectRow(projection,44.89701413, -99.67484647, 5034298.97574, 1394188.90818);
			testProjectRow(projection,34.95464622, -109.0466408, 3869755.81155, 678357.523273);
			testProjectRow(projection,43.13828955, -108.1789843, 4780035.31868, 729426.167439);
			testProjectRow(projection,47.8251026, -107.2680564, 5303607.198, 779306.5346045);
			testProjectRow(projection,36.48947911, -100.5498393, 4089411.74887, 1437510.22072);
			projection.setUTMZone(10, "N");
			testProjectRow(projection,43.79825018 ,-118.7501754, 4858249.4553, 841891.820637);
			testProjectRow(projection,38.04480634, -115.8841053, 4234765.43982, 1124763.51893);
			testProjectRow(projection,47.98348223, -112.952873, 5363513.21396, 1249296.61747);
			projection.setUTMZone(4, "N");
			testProjectRow(projection,62.02674, -142.762511, 6983714.91955, 1343262.29862);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSouth() {
		UTMProjection projection;
		try {
			projection = new UTMProjection( 16, "N" );
			projection.setUTMZone(12, "S");
			// Just some arbitrary points in the southern pacific!
			testProjectRow(projection,-44.89701413, -99.67484647, 4965701.0241755, 1394188.90818);
			testProjectRow(projection,-34.95464622, -109.0466408, 6130244.188860544, 678357.523273);
			testProjectRow(projection,-43.13828955, -108.1789843, 5219964.680774526, 729426.167439);
			testProjectRow(projection,-47.8251026, -107.2680564, 4696392.801778319, 779306.5346045);
			testProjectRow(projection,-36.48947911, -100.5498393, 5910588.250326522, 1437510.22072);
		} catch (InvalidUTMZoneException e) {
			fail(e.getMessage());
		}
	}
	
	private void testProjectRow( UTMProjection projection, Double dLat, Double dLong, Double dNorth, Double dEast ) {
		Double[] aCoords;
		Double[] aLongLat;
		try {
			aCoords = projection.project(dLat, dLong);
//			if( dLat < 0.0 ) {
//				System.out.println(dLong +", " + dLat + ", " + aCoords[0] + ", " + aCoords[1]);
//			}
			assertTrue( Math.abs(aCoords[0] - dEast) < TOLERANCE  );
			assertTrue( Math.abs(aCoords[1] - dNorth) < TOLERANCE  );	
			
			// Test whether I get back the same coordinates I put in.
			aLongLat = projection.deProject(aCoords[1], aCoords[0]);
			System.out.println(dLong +", " + dLat + ", " + aLongLat[0] + ", " + aLongLat[1]);
			
			assertTrue( Math.abs(dLong - aLongLat[0]) < TOLERANCE  );
			assertTrue( Math.abs(dLat - aLongLat[1]) < TOLERANCE  );	

		} catch (InvalidCoordinateException e) {
			Loggers.error(e);
			fail( e.getMessage() );
		}
	}


}
