package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.StateBounds;
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
public class ConfigFileTests {
	String sDefaultFile = "TestFiles/JitterDotConfigTest.config";

	@Before
	public void setUp() throws Exception {
		// Setup Global ConfigFile instance.  Use local instances if other settings are needed.
		ConfigFile.setConfigFilePath(sDefaultFile);
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
	public void testMapColumn() {
		assertTrue( ConfigFile.mapColumn("OriginalKey").equals("HerdID"));
	}
	
	@Test
	public void testSetFieldMap() {
		ConfigFile.setFieldMap("TestField", "TestValue");
		assertTrue( ConfigFile.mapColumn("TestField").equals("TestValue"));
		ConfigFile.setFieldMap("TestField", "NewValue");
		assertTrue( ConfigFile.mapColumn("TestField").equals("NewValue"));
		ConfigFile.saveConfigAs("TestFiles/TestJitterConfigTestsOut.config");
		ConfigFile.setConfigFilePath(sDefaultFile);
		ConfigFile newConfig = new ConfigFile("TestFiles/TestJitterConfigTestsOut.config");
		assertTrue( newConfig._mapColumn("TestField").equals("NewValue"));	
	
	}
	
	@Test
	public void testSetMinK() {
		ConfigFile.setMinK(10);
		assertTrue(ConfigFile.getMinK() == 10);
	}
	
	@Test
	public void testGetStates() {
		List<String> aStates = ConfigFile.getStates();
		assertTrue( aStates.get(0).equals("South Carolina") );
		ConfigFile newConfig = new ConfigFile("TestFiles/TestMultiState.config");
		aStates = newConfig._getStates();
		assertTrue( aStates.size() == 3);
		assertTrue( aStates.contains("Texas"));

	}
	
	@Test
	public void testConfigStateBounds() {
		StateBounds bounds = new StateBounds(ConfigFile.getStates());
		// TODO replace with valid test of values!!!!
		System.out.println( "Config States: " +  bounds.getMinLat() + ", " + bounds.getMaxLat() + ", " + bounds.getMinLong() + ", " + bounds.getMaxLong());
	}
	
	@Test
	public void testListKeys() {
		List<String> aKeys = ConfigFile.listMapKeys();
		assertTrue(aKeys.get(0).equals("OriginalKey"));
		assertTrue(aKeys.size() > 0 );
	}
	
	@Test
	public void testValidateDataFile() {
		assertTrue(ConfigFile.validateDataFile("TestFiles/Test.csv"));
		assertFalse(ConfigFile.validateDataFile("TestFiles/TestBad.csv"));
	}
	
	@Test
	public void testLockConfig() {
		ConfigFile newConfig = new ConfigFile("TestFiles/JitterDotConfigTest.config");
		assertTrue( newConfig._getMinK() == 5);
		newConfig._lockConfig(true);
		newConfig._setMinK(20);
		// Log file and console should show ConcurrentModificationException
		assertTrue( newConfig._getMinK() == 5);
		// Value not changed.
		newConfig._lockConfig(false);
		newConfig._setMinK(20);
		assertTrue( newConfig._getMinK() == 20);
		// Value changed.
	}

	@Test
	public void testSaveAs() {
		ConfigFile.setMinK(25);
		ConfigFile.saveConfigAs("TestFiles/JitterDotConfigTest25.config");
		// We should now be reading new values
		assertTrue( ConfigFile.getMinK() == 25);
		assertTrue( ConfigFile.getConfigFile().getPath().endsWith("Test25.config"));
		// Original File Should contain original values
		ConfigFile newConfig2 = new ConfigFile("TestFiles/JitterDotConfigTest.config");
		assertTrue( newConfig2._getMinK() == 5);
		// Re-read new file to see that it has new value
		ConfigFile newConfig3 = new ConfigFile("TestFiles/JitterDotConfigTest25.config");
		assertTrue( newConfig3._getMinK() == 25);
	}

}
