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
	
	@Test
	public void testSetFieldMap() {
		ConfigFile.setFieldMap("TestField", "TestValue");
		assertTrue( ConfigFile.mapColumn("TestField").equals("TestValue"));
		//ConfigFile.setFieldMap("TestField", "NewValue");
		//assertTrue( ConfigFile.mapColumn("TestField").equals("NewValue"));
		ConfigFile.saveConfig();
	}
	
	@Test
	public void testSetValue() {
		ConfigFile.setValue("MinK", "10", "Configuration of Jittering");
		ConfigFile.setValue("MinLat", "30", "Configuration of Coordinate");
	}

}
