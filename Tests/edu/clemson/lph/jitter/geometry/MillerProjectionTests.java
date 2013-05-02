package edu.clemson.lph.jitter.geometry;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.geometry.MillerProjection;
import edu.clemson.lph.jitter.logger.Loggers;

public class MillerProjectionTests {
	private static final double TOLERANCE = 0.01;  // Meters in this case!
	private MillerProjection projection;

	@Before
	public void setUp() throws Exception {
		setProjection(new MillerProjection( -81.00 ));
	}

	@Test
	public void testProject() {
		testProjectRow(34.19593, -79.63991, 151404.5262, 3960141.67);
		testProjectRow(34.19593,-79.63991,151404.5262,3960141.67);
		testProjectRow(34.83195,-82.48727,-165562.1391,4040043.163);
		testProjectRow(34.83195,-82.48727,-165562.1391,4040043.163);
		testProjectRow(32.91618,-80.6189,42423.8579,3800470.329);
		testProjectRow(34.04362,-81.72763,-80999.4011,3941062.096);
		testProjectRow(34.61481,-79.66183,148964.403,4012722.663);
		testProjectRow(34.67859,-83.06,-229318.151,4020742.924);
		testProjectRow(33.78957,-82.12861,-125636.2905,3909284.131);
		testProjectRow(34.7697,-81.29167,-32468.5559,4032206.42);
		testProjectRow(33.97899,-81.02359,-2626.0268,3932972.339);
		testProjectRow(34.06314,-78.88422,235527.5522,3943506.16);
		testProjectRow(34.01499,-81.78475,-87357.9704,3937478.007);
		testProjectRow(34.50953,-82.63039,-181494.1846,3999492.021);
		testProjectRow(34.49048,-82.48502,-165311.6702,3997099.076);
		testProjectRow(33.65599,-81.1542,-17165.4655,3892598.302);
		testProjectRow(34.07119,-79.28475,190940.7566,3944514.185);
		testProjectRow(34.4252,-80.04917,105845.9114,3988901.521);
		testProjectRow(32.90673,-80.63603,40516.9551,3799296.598);
		testProjectRow(34.16028,-82.41377,-157380.1565,3955673.987);
		testProjectRow(34.72869,-81.63808,-71030.7407,4027045.575);
		testProjectRow(32.99704,-80.11168,98887.3301,3810516.63);
		testProjectRow(33.99698,-82.01661,-113168.5075,3935223.776);
		testProjectRow(35.0087,-82.21764,-135547.0648,4062314.143);
		testProjectRow(34.64393,-82.41524,-157543.7962,4016384.004);
		testProjectRow(34.56922,-82.70878,-190220.5195,4006992.072);
		testProjectRow(33.28744,-80.51155,54374.0053,3846643.408);
		testProjectRow(32.9753,-80.85362,16294.9471,3807815.031);
		testProjectRow(34.7402,-82.24931,-139072.553,4028493.877);
		testProjectRow(32.86709,-81.11057,-12308.5961,3794373.972);
		testProjectRow(34.95956,-81.95222,-106000.6455,4056119.443);
		testProjectRow(34.49486,-81.98587,-109746.5464,3997649.235);
		testProjectRow(34.48473,-81.81927,-91200.7192,3996376.861);
		testProjectRow(33.41119,-81.34424,-38320.6215,3862060.631);
		testProjectRow(34.23693,-80.60458,44017.953,3965281.242);
		testProjectRow(34.90738,-81.12205,-13586.5439,4049543.984);
		testProjectRow(34.13001,-79.94283,117683.6261,3951881.431);
		testProjectRow(34.27027,-81.59837,-66610.2437,3969461.716);
		testProjectRow(33.79354,-81.92846,-103355.6944,3909780.277);
		testProjectRow(35.07318,-82.2994,-144648.5463,4070446.07);
		testProjectRow(35.04219,-82.26372,-140676.6669,4066537.264);
		testProjectRow(34.93173,-81.25516,-28404.2813,4052612.131);
	}
	
	private void testProjectRow( double dLat, double dLong, double dNorth, double dEast ) {
		double[] aCoords;
		try {
			aCoords = projection.project(dLat, dLong);
			assertTrue( Math.abs(aCoords[0] - dNorth) < TOLERANCE  );
			assertTrue( Math.abs(aCoords[1] - dEast) < TOLERANCE  );	
		} catch (InvalidCoordinateException e) {
			Loggers.error(e);
			fail( e.getMessage() );
		}
	}
	

	@Test(expected=InvalidCoordinateException.class)
	public void projectLowLat() throws InvalidCoordinateException {
		projection.project(-95.00, -81.00);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void projectHighLat() throws InvalidCoordinateException {
		projection.project(95.00, -81.00);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void projectLowLong() throws InvalidCoordinateException {
		projection.project(34.00, -181.00);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void projectHighLong() throws InvalidCoordinateException {
		projection.project(34.00, 181.00);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void projectHighBoth() throws InvalidCoordinateException {
		projection.project(94.00, 181.00);
	}
	@Test(expected=InvalidCoordinateException.class)
	public void constructLow() throws InvalidCoordinateException {
		@SuppressWarnings("unused")
		MillerProjection project2 = new MillerProjection( -181.00 );
	}
	@Test(expected=InvalidCoordinateException.class)
	public void constructHigh() throws InvalidCoordinateException {
		@SuppressWarnings("unused")
		MillerProjection project2 = new MillerProjection( 181.00 );
	}

	public MillerProjection getProjection() {
		return projection;
	}

	public void setProjection(MillerProjection projection) {
		this.projection = projection;
	}

}
