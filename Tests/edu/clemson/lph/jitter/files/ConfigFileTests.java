package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConfigFileTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetMinK() {
		assertTrue( ConfigFile.getMinK() == 5 );
	}

	@Test
	public void testGetMinGroup() {
		assertTrue( ConfigFile.getMinGroup() == 5 );
	}

	@Test
	public void testGetUTMZoneNum() {
		assertTrue( ConfigFile.getUTMZoneNum() == 17 );
	}

	@Test
	public void testGetZoneHemisphere() {
		assertTrue( ConfigFile.getZoneHemisphere().equals("N"));
	}

}
