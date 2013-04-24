package edu.clemson.lph.jitter.structs;

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
	private static int iNextKey = 1;
	
	// Fields
	private String sOriginalKey;
	private int iKey = -1;
	private double dLatitudeIn = -1.0;
	private double dLongitudeIn = -1.0;
	private String sAnimalType = null;
	private String sIntegrator = null;
	
	
	// Temporary Fields
	private double dDN = -1.0;
	private double dDLat = -1.0;
	private double dDLong = -1.0;
	
	// Output Fields
	private double dLatitude = -1.0;
	private double dLongitude = -1.0;
	
	public WorkingData( String sOriginalKey, double dLatitude, double dLongitude, String sAnimalType, String sIntegrator ) {
		this.sOriginalKey = sOriginalKey;
		this.iKey = iNextKey++;
		this.dLatitudeIn = dLatitude;
		this.dLongitudeIn = dLongitude;
		this.sAnimalType = sAnimalType;
		this.sIntegrator = sIntegrator;
	}
	
	public static void resetKeyList() {
		iNextKey = 1;
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
	
	@Override
	public boolean equals(Object otherData) {
		if( otherData instanceof WorkingData && iKey == ((WorkingData)otherData).iKey )
			return true;
		else
			return false;
	}

	public int getKey() {
		return iKey;
	}

	public double getLatitudeIn() {
		return dLatitudeIn;
	}

	public double getLongitudeIn() {
		return dLongitudeIn;
	}

	public String getOriginalKey() {
		return sOriginalKey;
	}

	public String getAnimalType() {
		return sAnimalType;
	}

	public String getIntegrator() {
		return sIntegrator;
	}

	public double getDN() {
		return dDN;
	}

	public void setDN(double dN) {
		this.dDN = dN;
	}

	public double getDLat() {
		return dDLat;
	}

	public void setDLat(double dLat) {
		this.dDLat = dLat;
	}

	public double getDLong() {
		return dDLong;
	}

	public void setDLong(double dLong) {
		this.dDLong = dLong;
	}

	public double getLatitude() {
		return dLatitude;
	}

	public void setLatitude(double dLatitude) {
		this.dLatitude = dLatitude;
	}

	public double getLongitude() {
		return dLongitude;
	}

	public void setLongitude(double dLongitude) {
		this.dLongitude = dLongitude;
	}

}
