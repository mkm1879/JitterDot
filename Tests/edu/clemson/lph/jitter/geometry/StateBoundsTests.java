package edu.clemson.lph.jitter.geometry;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.files.ConfigFile;

public class StateBoundsTests {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSetState() {
		StateBounds bounds = new StateBounds();
		bounds.setState("South Carolina");
		System.out.println( "SC: " + bounds.getMinLat() + ", " + bounds.getMaxLat() + ", " + bounds.getMinLong() + ", " + bounds.getMaxLong());
	}

	@Test
	public void testSetStates() {
		StateBounds bounds = new StateBounds();
		ArrayList<String> aStates = new ArrayList<String>();
		aStates.add("South Carolina");
		aStates.add("North Carolina");
		bounds.setStates(aStates);
		System.out.println( "SC/NC: " +  bounds.getMinLat() + ", " + bounds.getMaxLat() + ", " + bounds.getMinLong() + ", " + bounds.getMaxLong());
	}
	
	@Test
	public void testConfigStates() {
		StateBounds bounds = new StateBounds();
		bounds.setStates(ConfigFile.getStates());
		System.out.println( "Config States: " +  bounds.getMinLat() + ", " + bounds.getMaxLat() + ", " + bounds.getMinLong() + ", " + bounds.getMaxLong());
	
	}

}
