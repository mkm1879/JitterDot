package edu.clemson.lph.jitter.structs;

import edu.clemson.lph.controls.GPSTextField;


/**
 * 
 * @author mmarti5
 * Note: this class has a natural ordering that is inconsistent with equals.  Sort order arranges 
 * points geographically along east west or north south axis so they may fall on the same lat or long
 * but not be the same point. 
 */

public class WorkingDataRow {
	
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
	private double dDK = -1.0;
	private double dDLat = -1.0;
	private double dDLong = -1.0;
	
	// Output Fields
	private double dLatitude = -1.0;
	private double dLongitude = -1.0;
	
	
	/**
	 * Constructor with just the essential columns.  Use setters to provide other columns.
	 * @param sOriginalKey
	 * @param dLatitude
	 * @param dLongitude
	 * @param sAnimalType
	 */
	public WorkingDataRow( String sOriginalKey, double dLatitude, double dLongitude, String sAnimalType ) {
		this.sOriginalKey = sOriginalKey;
		this.dLatitudeIn = dLatitude;
		this.dLongitudeIn = dLongitude;
		this.sAnimalType = sAnimalType;
	}
	
	/**
	 * @param otherData is another instance of WorkingData
	 */
	@Override
	public boolean equals(Object otherData) {
		if( otherData instanceof WorkingDataRow && iKey == ((WorkingDataRow)otherData).iKey )
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
	 * @param iKey
	 */
	public void setKey( int iKey ) {
		this.iKey = iKey;
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
	public double getDK() {
		return dDK;
	}

	/**
	 * 
	 * @param dN double Distance to Nth nearest similar neighbor.
	 */
	public void setDK(double dK) {
		this.dDK = dK;
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
	

}
