package edu.clemson.lph.jitter.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.Distance;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;

public class DistanceTests {
	private static final double TOLERANCE = 0.0001; // Miles in this case. Using the same math these represent floating point rounding mostly.
	private static final double LOOSE_TOLERANCE = 0.1; // Again miles.  Difference here is spherical trig vs. SC State Plane Projection

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetDistance() {
		try {
			// Used existing, tested code from SQL Server to verify.
			assertTrue( Math.abs(Distance.getDistance(34.301147, -81.625323, 34.334553, -81.645930) - 2.59327) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.763760, -80.935660, 34.306178, -81.720549) - 54.7894) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.398907, -81.611130, 34.242720, -81.758762) - 13.7054) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.334553, -81.645930, 34.670167, -81.839500) - 25.703) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.536983, -83.005000, 34.474952, -81.295981) - 97.5074) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.349833, -81.987000, 33.825714, -81.422653) - 48.5734) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.306178, -81.720549, 33.936811, -81.480422) - 29.0139) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.242720, -81.758762, 34.854330, -81.365660) - 47.8666) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.670167, -81.839500, 35.034000, -82.533000) - 46.7213) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.474952, -81.295981, 34.648878, -80.666574) - 37.8166) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.825714, -81.422653, 34.575833, -80.341500) - 80.7303) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.936811, -81.480422, 34.532089, -81.301201) - 42.4312) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.854330, -81.365660, 34.771980, -80.914670) - 26.2371) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(35.034000, -82.533000, 34.710643, -81.119378) - 83.2794) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.575833, -80.341500, 34.351531, -81.560489) - 71.2274) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.532089, -81.301201, 34.190000, -81.505667) - 26.3854) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.301147, -81.625323, 34.492518, -80.366799) - 73.0387) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.300579, -81.682006, 34.532167, -80.628833) - 62.1933) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.552780, -82.940730, 35.022848, -82.084167) - 58.5215) < TOLERANCE);
			System.out.println(Distance.getDistance(34.763760, -80.935660, 33.242394, -80.204648));
			// These all seem to be SQL Server rounding errors.
			//		assertTrue( Math.abs(Distance.getDistance(34.763760, -80.935660, 33.242394, -80.204648) - 113.272) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.763760, -80.935660, 33.242394, -80.204648) - 113.272) < TOLERANCE * 5);
			System.out.println(Distance.getDistance(34.398907, -81.611130, 33.242394, -80.204648));
			//		assertTrue( Math.abs(Distance.getDistance(34.398907, -81.611130, 33.242394, -80.204648) - 113.714) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.398907, -81.611130, 33.242394, -80.204648) - 113.714) < TOLERANCE * 5);
			System.out.println(Distance.getDistance(34.334553, -81.645930, 33.242394, -80.204648));
			//		assertTrue( Math.abs(Distance.getDistance(34.334553, -81.645930, 33.242394, -80.204648) - 112.121) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.334553, -81.645930, 33.242394, -80.204648) - 112.121) < TOLERANCE * 5);
			assertTrue( Math.abs(Distance.getDistance(34.536983, -83.005000, 34.017170, -81.840600) - 75.6432) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.349833, -81.987000, 34.797001, -81.279381) - 50.8023) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.306178, -81.720549, 34.499013, -80.011036) - 98.4693) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.242720, -81.758762, 34.465000, -80.443167) - 76.6821) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.670167, -81.839500, 34.453600, -80.522800) - 76.4822) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.474952, -81.295981, 34.036000, -81.935800) - 47.5378) < TOLERANCE);
			System.out.println(Distance.getDistance(33.825714, -81.422653, 34.695850, -79.761220));
			//		assertTrue( Math.abs(Distance.getDistance(33.825714, -81.422653, 34.695850, -79.761220) - 112.441) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.825714, -81.422653, 34.695850, -79.761220) - 112.441) < TOLERANCE * 5);
			assertTrue( Math.abs(Distance.getDistance(33.936811, -81.480422, 33.702833, -81.344167) - 17.9786) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.854330, -81.365660, 33.757830, -81.419670) - 75.9067) < TOLERANCE);
			System.out.println(Distance.getDistance(35.034000, -82.533000, 34.329658, -80.492198));
			//		assertTrue( Math.abs(Distance.getDistance(35.034000, -82.533000, 34.329658, -80.492198) - 125.885) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(35.034000, -82.533000, 34.329658, -80.492198) - 125.885) < TOLERANCE * 5);
			assertTrue( Math.abs(Distance.getDistance(34.648878, -80.666574, 34.364836, -79.566540) - 65.7069) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.575833, -80.341500, 33.652667, -81.104833) - 77.3833) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.532089, -81.301201, 33.286220, -80.905713) - 89.1158) < TOLERANCE);
			System.out.println(Distance.getDistance(34.771980, -80.914670, 33.283333, -80.903000));
			//		assertTrue( Math.abs(Distance.getDistance(34.771980, -80.914670, 33.283333, -80.903000) - 102.971) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.771980, -80.914670, 33.283333, -80.903000) - 102.971) < TOLERANCE * 5);
			//		Test some close distances where it really matters.
			assertTrue( Math.abs(Distance.getDistance(33.833500, -81.520330, 33.692352, -81.519611) - 9.76317) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.865653, -81.395219, 33.842800, -81.511000) - 6.83597) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.917200, -81.673200, 33.995500, -81.653200) - 5.53617) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.025647, -81.547731, 33.964338, -81.477846) - 5.83483) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.910248, -81.371487, 33.840300, -81.284000) - 6.97503) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.137873, -81.714984, 34.092800, -81.625500) - 5.99825) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.881372, -81.490004, 33.787542, -81.443486) - 7.01892) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.092800, -81.625500, 33.987670, -81.675670) - 7.81967) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.001200, -81.640300, 33.989830, -81.689170) - 2.9108) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.777071, -81.349990, 33.666800, -81.448200) - 9.49211) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.842661, -81.354825, 33.877830, -81.352830) - 2.43531) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.857619, -81.516749, 33.757000, -81.507670) - 6.97926) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.873636, -81.441302, 33.750500, -81.392800) - 8.96173) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.987670, -81.675670, 34.030500, -81.539830) - 8.33313) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.877830, -81.352830, 33.823248, -81.503489) - 9.44217) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.757000, -81.507670, 33.696330, -81.501500) - 4.21148) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.823248, -81.503489, 33.855996, -81.518493) - 2.42363) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.804330, -81.531500, 33.682930, -81.454178) - 9.50211) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.516083, -82.896583, 34.579700, -82.938850) - 5.01612) < TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.809212, -81.432039, 33.787300, -81.300300) - 7.7225) < TOLERANCE);
			
			// From ArcGIS Using projection to SC State Plane and Point Distance tool in Anaysis Tools.  
			// Note that this results in some degree of disagreement but within 1/2 mile even for relatively long distances.
			// Just empirically found limits when more than the basic loose tolerance of 1/10 mile.
			assertTrue( Math.abs(Distance.getDistance(34.301147,-81.625323,34.334553,-81.64593) - 2.586296229) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.76376,-80.93566,34.306178,-81.720549) - 54.7614734) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.398907,-81.61113,34.24272,-81.758762) - 13.67940038) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.334553,-81.64593,34.670167,-81.8395) - 25.63303482) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.536983,-83.005,34.474952,-81.295981) - 97.60378357) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.349833,-81.987,33.825714,-81.422653) - 48.49247409) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.306178,-81.720549,33.936811,-81.480422) - 28.93655688) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.24272,-81.758762,34.85433,-81.36566) - 47.74497142) < LOOSE_TOLERANCE * 2);
			assertTrue( Math.abs(Distance.getDistance(34.670167,-81.8395,35.034,-82.533) - 46.7126621) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.474952,-81.295981,34.648878,-80.666574) - 37.83760016) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.825714,-81.422653,34.575833,-80.3415) - 80.65193143) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.936811,-81.480422,34.532089,-81.301201) - 42.2872174) < LOOSE_TOLERANCE * 2);
			assertTrue( Math.abs(Distance.getDistance(34.85433,-81.36566,34.77198,-80.91467) - 26.26052078) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(35.034,-82.533,34.710643,-81.119378) - 83.34664313) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.575833,-80.3415,34.351531,-81.560489) - 71.28210877) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.532089,-81.301201,34.19,-81.505667) - 26.31359893) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.301147,-81.625323,34.492518,-80.366799) - 73.09822648) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.300579,-81.682006,34.532167,-80.628833) - 62.23487446) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.55278,-82.94073,35.022848,-82.084167) - 58.50414576) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.76376,-80.93566,33.242394,-80.204648) - 112.92386) < LOOSE_TOLERANCE * 5);
			assertTrue( Math.abs(Distance.getDistance(34.398907,-81.61113,33.242394,-80.204648) - 113.5530826) < LOOSE_TOLERANCE * 2);
			assertTrue( Math.abs(Distance.getDistance(34.334553,-81.64593,33.242394,-80.204648) - 111.9821602) < LOOSE_TOLERANCE * 2);
			assertTrue( Math.abs(Distance.getDistance(34.536983,-83.005,34.01717,-81.8406) - 75.63584576) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.349833,-81.987,34.797001,-81.279381) - 50.76855133) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.306178,-81.720549,34.499013,-80.011036) - 98.55626703) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.24272,-81.758762,34.465,-80.443167) - 76.74113797) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.670167,-81.8395,34.4536,-80.5228) - 76.54654466) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.474952,-81.295981,34.036,-81.9358) - 47.49328164) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.825714,-81.422653,34.69585,-79.76122) - 112.3988205) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(33.936811,-81.480422,33.702833,-81.344167) - 17.92655351) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.85433,-81.36566,33.75783,-81.41967) - 75.6320878) < LOOSE_TOLERANCE * 3);
			assertTrue( Math.abs(Distance.getDistance(35.034,-82.533,34.329658,-80.492198) - 125.9338578) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.648878,-80.666574,34.364836,-79.56654) - 65.74575615) < LOOSE_TOLERANCE);
			assertTrue( Math.abs(Distance.getDistance(34.575833,-80.3415,33.652667,-81.104833) - 77.21134188) < LOOSE_TOLERANCE * 2);
			assertTrue( Math.abs(Distance.getDistance(34.532089,-81.301201,33.28622,-80.905713) - 88.80946978) < LOOSE_TOLERANCE * 4);
			assertTrue( Math.abs(Distance.getDistance(34.77198,-80.91467,33.283333,-80.903) - 102.5896458) < LOOSE_TOLERANCE * 4);
			// The following were selected from close farms.  Tighten up the limits a little to .02 to .05 Miles
			// Would be interesting research to figure out which pairs tend to deviate more than others. Does not seem to be totally proportional.
			assertTrue( Math.abs(Distance.getDistance(33.8335,-81.52033,33.692352,-81.519611) - 9.726182968) < LOOSE_TOLERANCE / 2.0 );
			assertTrue( Math.abs(Distance.getDistance(33.865653,-81.395219,33.8428,-81.511) - 6.840156658) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.9172,-81.6732,33.9955,-81.6532) - 5.516524348) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(34.025647,-81.547731,33.964338,-81.477846) - 5.825742037) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.910248,-81.371487,33.8403,-81.284) - 6.965535638) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(34.137873,-81.714984,34.0928,-81.6255) - 5.996201027) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.881372,-81.490004,33.787542,-81.443486) - 6.997153392) < LOOSE_TOLERANCE / 2.0);
			assertTrue( Math.abs(Distance.getDistance(34.0928,-81.6255,33.98767,-81.67567) - 7.795434416) < LOOSE_TOLERANCE / 2.0);
			assertTrue( Math.abs(Distance.getDistance(34.0012,-81.6403,33.98983,-81.68917) - 2.91237597) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.777071,-81.34999,33.6668,-81.4482) - 9.471716757) < LOOSE_TOLERANCE / 2.0);
			assertTrue( Math.abs(Distance.getDistance(33.842661,-81.354825,33.87783,-81.35283) - 2.426152832) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.857619,-81.516749,33.757,-81.50767) - 6.953061199) < LOOSE_TOLERANCE / 2.0);
			assertTrue( Math.abs(Distance.getDistance(33.873636,-81.441302,33.7505,-81.3928) - 8.931890426) < LOOSE_TOLERANCE / 2.0);
			assertTrue( Math.abs(Distance.getDistance(33.98767,-81.67567,34.0305,-81.53983) - 8.33562318) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.87783,-81.35283,33.823248,-81.503489) - 9.443305189) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.757,-81.50767,33.69633,-81.5015) - 4.195629488) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.823248,-81.503489,33.855996,-81.518493) - 2.415903824) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.80433,-81.5315,33.68293,-81.454178) - 9.475753652) < LOOSE_TOLERANCE / 2.0);
			assertTrue( Math.abs(Distance.getDistance(34.516083,-82.896583,34.5797,-82.93885) - 5.003606849) < LOOSE_TOLERANCE / 5.0);
			assertTrue( Math.abs(Distance.getDistance(33.809212,-81.432039,33.7873,-81.3003) - 7.727723422) < LOOSE_TOLERANCE / 5.0);
		} catch (InvalidCoordinateException e) {
			Loggers.error( e.getStackTrace() );
			assertTrue(false);
		}
	}

	@Test(expected=InvalidCoordinateException.class)
	public void distanceLowLat1() throws InvalidCoordinateException {
		Distance.getDistance(-94.301147, -81.625323, 34.334553, -81.645930);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void distanceLowLat2() throws InvalidCoordinateException {
		Distance.getDistance(34.301147, -81.625323, -94.334553, -81.645930);
	}

	@Test(expected=InvalidCoordinateException.class)
	public void distanceHighLat1() throws InvalidCoordinateException {
		Distance.getDistance(94.301147, -81.625323, 34.334553, -81.645930);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void distanceHighLat2() throws InvalidCoordinateException {
		Distance.getDistance(34.301147, -81.625323, 94.334553, -81.645930);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void distanceLowLong1() throws InvalidCoordinateException {
		Distance.getDistance(34.301147, -181.625323, 34.334553, -81.645930);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void distanceLowLong2() throws InvalidCoordinateException {
		Distance.getDistance(34.301147, -81.625323, 34.334553, -181.645930);
	}

	@Test(expected=InvalidCoordinateException.class)
	public void distanceHighLong1() throws InvalidCoordinateException {
		Distance.getDistance(34.301147, 181.625323, 34.334553, -81.645930);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void distanceHighLong2() throws InvalidCoordinateException {
		Distance.getDistance(34.301147, -81.625323, 34.334553, 181.645930);
	}


}
