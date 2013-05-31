package edu.clemson.lph.jitter.structs;

import edu.clemson.lph.controls.GPSTextField;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;


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
	private Double dLatitudeIn = null;
	private Double dLongitudeIn = null;
	private String sAnimalTypeIn = null;
	private String sIntegratorIn = null;
	private int iHouses = -1;
	private int iAnimals = -1;
	private String sStatus = null;
	private int iDaysInState = -1;
	private int iDaysLeftInState = -1;
	
	
	// Temporary Fields
	private Double dDK = null;
	private Double dDLat = null;
	private Double dDLong = null;
	
	// Output Fields
	private String sAnimalType = null;
	private String sIntegrator = null;
	private Double dLatitude = null;
	private Double dLongitude = null;
	private Double dEasting = null;
	private Double dNorthing = null;
	private Integer iUTMZone = null;
	
	
	/**
	 * Constructor with just the essential columns.  Use setters to provide other columns.
	 * @param sOriginalKey
	 * @param dLatitude
	 * @param dLongitude
	 * @param sAnimalType
	 */
	public WorkingDataRow( String sOriginalKey, Double dLatitude, Double dLongitude, String sAnimalTypeIn ) throws InvalidCoordinateException {
		if( !GPSTextField.isValidLatitude(dLatitude) )
			throw new InvalidCoordinateException(dLatitude, "dLatitude");
		if( !GPSTextField.isValidLongitude(dLongitude) )
			throw new InvalidCoordinateException(dLongitude, "dLongitude");
		this.sOriginalKey = sOriginalKey;
		this.dLatitudeIn = dLatitude;
		this.dLongitudeIn = dLongitude;
		this.sAnimalTypeIn = sAnimalTypeIn;
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
	 * Compare rows to see if they count in dK.  Initially, only Animal Type is considered
	 * as potentially differentiating and thus requiring different types in different K pools.
	 * This is in a method to simplify maintenance if other attributes are determined to 
	 * need grouping in K pools.
	 * @param otherRow WorkingDataRow to compare with
	 * @return true if the two rows match on key attributes and can therefore be pooled to make K
	 * distinct entries.
	 */
	public boolean similarTo( WorkingDataRow otherRow ) {
		boolean bRet = false;
		if( getAnimalType().equals(otherRow.getAnimalType()) ) 
			bRet = true;
		return bRet;
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
	 * @return Double Original Latitude
	 */
	public Double getLatitudeIn() {
		return dLatitudeIn;
	}

	/**
	 * 
	 * @return Double Original Longitude
	 */
	public Double getLongitudeIn() {
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
	public String getAnimalTypeIn() {
		return sAnimalTypeIn;
	}

	/**
	 * 
	 * @return String Animal Type
	 */
	public void setAnimalType( String sAnimalType ) {
		this.sAnimalType = sAnimalType;
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
	public String getIntegratorIn() {
		return sIntegratorIn;
	}

	/**
	 * 
	 * @param sIntegrator String integrator
	 */
	public void setIntegratorIn(String sIntegrator) {
		this.sIntegratorIn = sIntegrator;
	}

	/**
	 * 
	 * @return String integrator
	 */
	public String getIntegrator() {
		return sIntegratorIn;
	}

	/**
	 * 
	 * @param sIntegrator String integrator
	 */
	public void setIntegrator(String sIntegrator) {
		this.sIntegratorIn = sIntegrator;
	}

	/**
	 * 
	 * @return Double Distance to Nth nearest similar neighbor.
	 */
	public Double getDK() {
		return dDK;
	}

	/**
	 * 
	 * @param dN Double Distance to Nth nearest similar neighbor.
	 */
	public void setDK(Double dK) {
		this.dDK = dK;
	}

	/**
	 * 
	 * @return Double Delta Latitude needed
	 */
	public Double getDLat() {
		return dDLat;
	}

	/**
	 * 
	 * @param dLat Double Delta Latitude needed
	 */
	public void setDLat(Double dLat) {
		this.dDLat = dLat;
	}

	/**
	 * 
	 * @return Double Delta Longitude needed
	 */
	public Double getDLong() {
		return dDLong;
	}

	/**
	 * 
	 * @param dLong Double Delta Latitude needed
	 */
	public void setDLong(Double dLong) {
		this.dDLong = dLong;
	}

	/**
	 * 
	 * @return Double Jittered Latitude
	 */
	public Double getLatitude() {
		return dLatitude;
	}

	/**
	 * 
	 * @param dLatitude Double Jittered Latitude
	 */
	public void setLatitude(Double dLatitude) throws InvalidCoordinateException {
		if( !GPSTextField.isValidLatitude(dLatitude) )
			throw new InvalidCoordinateException(dLatitude, "setLatitude()");
		this.dLatitude = dLatitude;
	}

	/**
	 * 
	 * @return Double Jittered Longitude
	 */
	public Double getLongitude() {
		return dLongitude;
	}

	/**
	 * 
	 * @param dLongitude Double Jittered Longitude
	 */
	public void setLongitude(Double dLongitude) throws InvalidCoordinateException {
		if( !GPSTextField.isValidLongitude(dLongitude) )
			throw new InvalidCoordinateException(dLongitude, "setLongitude()");
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

	public Double getEasting() {
		return dEasting;
	}

	public void setEasting(Double dEasting) {
		this.dEasting = dEasting;
	}

	public Double getNorthing() {
		return dNorthing;
	}

	public void setNorthing(Double dNorthing) {
		this.dNorthing = dNorthing;
	}

	public Integer getUTMZone() {
		return iUTMZone;
	}

	public void setUTMZone(Integer iUTMZone) {
		this.iUTMZone = iUTMZone;
	}
	

}
