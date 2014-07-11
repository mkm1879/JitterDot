package edu.clemson.lph.security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
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
public class RandomNumberTests {
	private RandomNumbers rn;

	@Before
	public void setUp() throws Exception {
		rn = new RandomNumbers();
	}

	@Test
	public void testNextDouble() {
		double d = rn.nextDouble();
		System.out.println( "Double = " + d );
		assertTrue(d >= 0.0 && d < 1.0 );
	}

	@Test
	public void testNextDoubleMax() {
		double d = rn.nextDouble( 5.50 );
		System.out.println( "Double(5.50) = " + d );
		assertTrue(d >= 0.0 && d < 5.50 );
	}

	@Test
	public void testNextDoubleMinMax() {
		double d = rn.nextDouble( -5.50, 5.50 );
		System.out.println( "Double(-5.50,5.50) = " + d );
		assertTrue(d >= -5.50 && d < 5.50 );
	}


	@Test
	public void testNextInt() {
		int i = rn.nextInt();
		System.out.println( "Int = " + i );
		assertTrue(i >= Integer.MIN_VALUE && i <= Integer.MAX_VALUE );
	}

	@Test
	public void testNextIntInt() {
		int i = rn.nextInt( 250 );
		System.out.println( "Int 250 = " + i );
		assertTrue(i >= 0 && i < 250 );
	}

	@Test
	public void testNextLong() {
		long l = rn.nextLong();
		System.out.println( "Long = " + l );
		assertTrue(l >= Long.MIN_VALUE && l < Long.MAX_VALUE );
	}

}
