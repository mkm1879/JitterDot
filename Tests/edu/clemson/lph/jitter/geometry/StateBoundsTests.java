package edu.clemson.lph.jitter.geometry;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.files.ConfigFile;
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
