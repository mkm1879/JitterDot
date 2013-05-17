package edu.clemson.lph.security;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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
	public void testNextDoubleDouble() {
		double d = rn.nextDouble( 5.50 );
		System.out.println( "Double(5.50) = " + d );
		assertTrue(d >= 0.0 && d < 5.50 );
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
