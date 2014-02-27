package edu.clemson.lph.security;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import edu.clemson.lph.jitter.logger.Loggers;

/**
 * Simple wrapper around standard Java SecureRandom class to ensure that I seed using its internal secure seed
 * and reseed every 1000 values to prevent analysis of the sequence.  This is massive overkill in this project.
 * @author mmarti5
 *
 */
public class RandomNumbers {
	private SecureRandom sr;
	private static final int SEED_SIZE = 100;
	private static final int MAX_NUMS = 1000;
	private int iCounter = MAX_NUMS;  // So we set seed the first time
	
	public RandomNumbers() {
		try {
			// Use the Sun SHA1 PRNG, the best available on Windows so we stay consistent
			// Otherwise Unix, Linux, Mac, would use internal PRNG which while better,
			// would make debugging lots harder.
			sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
		} catch (NoSuchAlgorithmException e) {
			Loggers.error(e);
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			Loggers.error(e);
			e.printStackTrace();
		}
	}
	
	private void seed() {
		if( iCounter++ >= MAX_NUMS ) {
			sr.setSeed(sr.generateSeed(SEED_SIZE));
			iCounter = 0;
		}
	}
	
	/**
	 * Get the next double value from the range 0 to 1.0
	 * @return double 
	 */
	public double nextDouble() {
		seed();
		return sr.nextDouble();
	}
	
	/**
	 * Get the next double value from the range 0 inclusive to dMax exclusive
	 * @return double 
	 */
	public double nextDouble( double dMax ) {
		seed();
		return sr.nextDouble() * dMax;
	}
	
	
	/**
	 * Get the next double value from the range 0 inclusive to dMax exclusive
	 * @return double 
	 */
	public double nextDouble( double dMin, double dMax ) {
		seed();
		double dDist = dMax - dMin;
		return (sr.nextDouble() * dDist) + dMin;
	}

	/**
	 * Get the next double value from the range Integer.MIN_VALUE to Integer.MAX_VALUE
	 * @return int 
	 */
	public int nextInt() {
		seed();
		return sr.nextInt();
	}
	
	/**
	 * Get the next int value from the range 0 inclusive to iMax exclusive
	 * @return int 
	 */
	public int nextInt( int iMax ) {
		seed();
		return sr.nextInt( iMax );
	}
	
	/**
	 * Get the next long integer value from the range Long.MIN_VALUE to Long.MAX_VALUE
	 * @return long
	 */
	public long nextLong() {
		seed();
		return sr.nextLong();
	}

	/**
	 * Will pick numbers randomly from the set of numbers between
	 * startNumber (included) and endNumber (included).
	 * Used by WorkingData to generate pseudo random pseudonymized idenfiers for each row.
	 * @param startNumber
	 * @param endNumber
	 * @return
	 */
	public static ArrayList<Integer> randomPick( int startNumber, int endNumber ) {
	    // Generate a list of all numbers from start to endNumber
	    ArrayList<Integer> numbers = new ArrayList<Integer>();
	    for(int i = startNumber; i <= endNumber; i++) {
	        numbers.add(i);
	    }
	
	    // Shuffle them
	    Collections.shuffle(numbers);
	
	    // Pick count items.
	    return numbers;
	}

}
