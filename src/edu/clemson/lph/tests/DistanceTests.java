package edu.clemson.lph.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.Distance;

public class DistanceTests {
	private static final double TOLERANCE = 0.0001;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetDistance() {
		System.out.println(Distance.getDistance(34.301147, -81.625323, 34.334553, -81.645930));
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
	}

}
