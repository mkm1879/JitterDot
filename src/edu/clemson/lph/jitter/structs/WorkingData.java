package edu.clemson.lph.jitter.structs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author mmarti5
 * Note: this class has a natural ordering that is inconsistent with equals.  Sort order arranges 
 * points geographically along east west or north south axis so they may fall on the same lat or long
 * but not be the same point. 
 */

public class WorkingData implements Comparable<WorkingData> {
	public static final int SORT_NORTH_SOUTH = 0;
	public static final int SORT_WEST_EAST = 1;
	private static int iSortDirection = SORT_WEST_EAST;
	private static int iNextKey = 0;
	private static ArrayList<Integer> randInts;
	private static int iRows = 10000;
	
	// Fields
	private String sOriginalKey;
	private int iKey = -1;
	private double dLatitudeIn = -1.0;
	private double dLongitudeIn = -1.0;
	private String sAnimalType = null;
	private String sIntegrator = null;
	private int iHouses = -1;
	private int iAnimals = -1;
	private String sStatus = null;
	private int iDaysInState = -1;
	private int iDaysLeftInState = -1;
	
	
	// Temporary Fields
	private double dDN = -1.0;
	private double dDLat = -1.0;
	private double dDLong = -1.0;
	
	// Output Fields
	private double dLatitude = -1.0;
	private double dLongitude = -1.0;
	
	/**
	 * Sets the number of rows to be keyed.  If set too low, constructor will fail
	 * with an indexOutOfBounds error.
	 * @param iNumRows
	 */
	public static void setRows( int iNumRows ) {
		iRows = iNumRows;
		randInts = null; // populate next time an instance is constructed
	}
	
	/**
	 * Constructor with just the essential columns.  Use setters to provide other columns.
	 * @param sOriginalKey
	 * @param dLatitude
	 * @param dLongitude
	 * @param sAnimalType
	 */
	public WorkingData( String sOriginalKey, double dLatitude, double dLongitude, String sAnimalType ) {
		// populate static randInts here so default size will work.
		if( randInts == null ) {
			randInts = randomPick(1, iRows);
		}
		this.sOriginalKey = sOriginalKey;
		// Pull our key from a pseudo random list of integers (10000 or number of rows if calculated.)
		this.iKey = randInts.get(iNextKey++);
		this.dLatitudeIn = dLatitude;
		this.dLongitudeIn = dLongitude;
		this.sAnimalType = sAnimalType;
	}
	
	public static void resetKeyList() {
		iNextKey = 0;
		// don't waste time reshuffling unless necessary.  If so, call setRows again or reshuffle.
	}
	
	public static void reshuffle() {
		randInts = null;
	}
	
	/**
	 * Default and fall back is East West since most states are longer that direction.
	 * @param iSort
	 */
	public static void setSortDirection( int iSort ) throws Exception {
		if( iSort == SORT_NORTH_SOUTH || iSort == SORT_WEST_EAST ) {
			iSortDirection = iSort;
		}
		else {
			throw new Exception("Invalid Sort Direction in WorkingData");
		}
	}
	
	
	@Override
	public int compareTo(WorkingData otherData) {
		if( iSortDirection == SORT_NORTH_SOUTH ) {
			if( dLatitudeIn < otherData.dLatitudeIn )
				return 1;
			else if ( otherData.dLatitudeIn < dLatitudeIn )
				return -1;
			else 
				return 0;
		}
		else {
			if( dLongitudeIn < otherData.dLongitudeIn )
				return -1;
			else if ( otherData.dLongitudeIn < dLongitudeIn )
				return 1;
			else 
				return 0;
		}
	}
	
	/**
	 * NOTE: Equality and Compare are NOT equivalent in this case.  Equality is based on key
	 * while comparison is based on Longitude for spatial sorting.
	 * @param otherData is another instance of WorkingData
	 */
	@Override
	public boolean equals(Object otherData) {
		if( otherData instanceof WorkingData && iKey == ((WorkingData)otherData).iKey )
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @return int
	 */
	public int getKey() {
		return iKey;
	}

	/**
	 * 
	 * @return double Original Latitude
	 */
	public double getLatitudeIn() {
		return dLatitudeIn;
	}

	/**
	 * 
	 * @return double Original Longitude
	 */
	public double getLongitudeIn() {
		return dLongitudeIn;
	}

	/**
	 * 
	 * @return String original Key
	 */
	public String getOriginalKey() {
		return sOriginalKey;
	}

	/**
	 * 
	 * @return String Animal Type
	 */
	public String getAnimalType() {
		return sAnimalType;
	}

	/**
	 * 
	 * @return String integrator
	 */
	public String getIntegrator() {
		return sIntegrator;
	}

	/**
	 * 
	 * @param sIntegrator String integrator
	 */
	public void setIntegrator(String sIntegrator) {
		this.sIntegrator = sIntegrator;
	}

	/**
	 * 
	 * @return double Distance to Nth nearest similar neighbor.
	 */
	public double getDN() {
		return dDN;
	}

	/**
	 * 
	 * @param dN double Distance to Nth nearest similar neighbor.
	 */
	public void setDN(double dN) {
		this.dDN = dN;
	}

	/**
	 * 
	 * @return double Delta Latitude needed
	 */
	public double getDLat() {
		return dDLat;
	}

	/**
	 * 
	 * @param dLat double Delta Latitude needed
	 */
	public void setDLat(double dLat) {
		this.dDLat = dLat;
	}

	/**
	 * 
	 * @return double Delta Longitude needed
	 */
	public double getDLong() {
		return dDLong;
	}

	/**
	 * 
	 * @param dLong double Delta Latitude needed
	 */
	public void setDLong(double dLong) {
		this.dDLong = dLong;
	}

	/**
	 * 
	 * @return double Jittered Latitude
	 */
	public double getLatitude() {
		return dLatitude;
	}

	/**
	 * 
	 * @param dLatitude double Jittered Latitude
	 */
	public void setLatitude(double dLatitude) {
		this.dLatitude = dLatitude;
	}

	/**
	 * 
	 * @return double Jittered Longitude
	 */
	public double getLongitude() {
		return dLongitude;
	}

	/**
	 * 
	 * @param dLongitude double Jittered Longitude
	 */
	public void setLongitude(double dLongitude) {
		this.dLongitude = dLongitude;
	}

	/**
	 * 
	 * @return int Number of Houses
	 */
	public int getHouses() {
		return iHouses;
	}

	/**
	 * 
	 * @param iHouses int Number of Houses
	 */
	public void setHouses(int iHouses)
	{
		this.iHouses = iHouses;
	}

	/**
	 * 
	 * @return int Number of Animals
	 */
	public int getAnimals() {
		return iAnimals;
	}

	/**
	 * 
	 * @param iAnimals int Number of Animals
	 */
	public void setAnimals(int iAnimals) {
		this.iAnimals = iAnimals;
	}

	/**
	 * 
	 * @return String Status
	 */
	public String getStatus() {
		return sStatus;
	}

	/**
	 * 
	 * @param sStatus String Status
	 */
	public void setStatus(String sStatus) {
		this.sStatus = sStatus;
	}

	/**
	 * 
	 * @return int Days in this Status
	 */
	public int getDaysInState() {
		return iDaysInState;
	}

	/**
	 * 
	 * @param iDaysInState int Days in this Status
	 */
	public void setDaysInState(int iDaysInState) {
		this.iDaysInState = iDaysInState;
	}

	/**
	 * 
	 * @return int Days left in this Status 
	 */
	public int getDaysLeftInState() {
		return iDaysLeftInState;
	}

	/**
	 * 
	 * @param iDaysLeftInState int Days left in this Status
	 */
	public void setDaysLeftInState(int iDaysLeftInState) {
		this.iDaysLeftInState = iDaysLeftInState;
	}
	

	/**
	 * Will pick numbers randomly from the set of numbers between
	 * startNumber (included) and endNumber (included).
	 * @param startNumber
	 * @param endNumber
	 * @return
	 */
	private static ArrayList<Integer> randomPick( int startNumber, int endNumber ) {
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
