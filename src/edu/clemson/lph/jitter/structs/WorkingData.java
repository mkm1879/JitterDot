package edu.clemson.lph.jitter.structs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.geometry.Distance;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.security.RandomNumbers;


@SuppressWarnings("serial")
public class WorkingData extends ArrayList<WorkingDataRow> {
	public static final int SORT_NONE = -1;
	public static final int SORT_SOUTH_NORTH = 0;
	public static final int SORT_WEST_EAST = 1;
	public static final double SQRT2 = Math.sqrt(2.0);

	private int k;
	private static final int altK = 3;
	
	private int iSortDirection = SORT_WEST_EAST;
	private int iCurrentSort = SORT_NONE;
	private int iNextKey = 0;
	private ArrayList<Integer> randInts;
	ArrayList<String> aSmallGroups = null;
	private int iRows = 10000;
	private Double dMinLong = null;
	private Double dMaxLong = null;
	private Double dMedianLong = null;
	private Double dMinLat = null;
	private Double dMaxLat = null;
	
	public WorkingData() {
		k = ConfigFile.getMinK();
	}
	
	@Override
	public boolean add(WorkingDataRow rowIn) {
		boolean bRet = false;
		// Pull our key from a pseudo random list of integers (10000 or number of rows if calculated.)
		rowIn.setKey(randInts.get(iNextKey++));
		super.add(rowIn);
		return bRet;
	}
	
	/**
	 * Sets the number of rows to be keyed.  If set too low, constructor will fail
	 * with an indexOutOfBounds error.
	 * @param iNumRows
	 */
	public void setRows( int iNumRows ) {
		iRows = iNumRows;
		// populate static randInts here so default size will work.
		if( randInts == null ) {
			randInts = RandomNumbers.randomPick(1, iRows);
		}
	}
	
	/**
	 * Default and fall back is East West since most states are longer that direction.
	 * @param iSort
	 */
	public void setSortDirection() {
		try {
			setSortDirection( getMajorAxis() );
		} catch (Exception e) {
			Loggers.error(e.getMessage());
		}
	}

	/**
	 * Default and fall back is East West since most states are longer that direction.
	 * @param iSort
	 */
	public void setSortDirection( int iSort ) throws Exception {
		if( iSort == SORT_SOUTH_NORTH || iSort == SORT_WEST_EAST ) {
			iSortDirection = iSort;
		}
		else {
			throw new Exception("Invalid Sort Direction in WorkingData");
		}
	}
	
	public int getSortDirection() {
		return iSortDirection;
	}
	
	public Double getMinLong() {
		if( dMinLong == null )
			calcStats();
		return dMinLong;
	}
	public Double getMaxLong() {
		if( dMaxLong == null )
			calcStats();
		return dMaxLong;
	}
	public Double getMinLat() {
		if( dMinLat == null )
			calcStats();
		return dMinLat;
	}
	public Double getMaxLat() {
		if( dMaxLat == null )
			calcStats();
		return dMaxLat;
	}
	
	private void calcStats() {
		double dNextMinLat = 90.0;
		double dNextMaxLat = -90.0;

		double dNextMinLong = 180.0;
		double dNextMaxLong = -180.0;
		
		for( WorkingDataRow row : this ) {
			if( row.getLatitudeIn() < dNextMinLat )
				dNextMinLat = row.getLatitudeIn();
			if( row.getLatitudeIn() > dNextMaxLat )
				dNextMaxLat = row.getLatitudeIn();
			if( row.getLongitudeIn() < dNextMinLong )
				dNextMinLong = row.getLongitudeIn();
			if( row.getLongitudeIn() > dNextMaxLong )
				dNextMaxLong = row.getLongitudeIn();
		}
		dMinLat = dNextMinLat;
		dMaxLat = dNextMaxLat;
		dMinLong = dNextMinLong;
		dMaxLong = dNextMaxLong;
	}
	
	/**
	 * This is a VERY crude approximation.  Degrees of longitude will be less distance than
	 * degrees of latitude everywhere except the equator, but if it is close enough to matter,
	 * the performance difference will be tiny.  Not worth trying to more accurately calculate
	 * the true east-west and north-south distances between extreme points.
	 * 1.3 correction factor should be about right for mid latitudes of US
	 * @return int from iSortDirection enumeration.  (When should I switch to new enum classes?)
	 */
	public int getMajorAxis() {
		if( (getMaxLat() - getMinLat()) > (1.30 * (getMaxLong() - getMinLong())) )
			return SORT_SOUTH_NORTH;
		else
			return SORT_WEST_EAST;
	}
	
	public Double getMedianLong() {
		if( dMedianLong == null ) {
			if( iCurrentSort != SORT_WEST_EAST) {
				Collections.sort(this, new CompareLongitude() );
				iCurrentSort = SORT_WEST_EAST;			
			}
			if( size() % 2 != 0 ) {
				WorkingDataRow midRow = get( size() / 2);
				dMedianLong = midRow.getLongitudeIn();
			}
			else {
				WorkingDataRow lowRow = get( (size() -1) / 2 );
				WorkingDataRow highRow = get( ((size() -1) / 2) + 1 );
				dMedianLong = ( lowRow.getLongitudeIn() + highRow.getLongitudeIn() ) / 2.0;
			}
			if( iCurrentSort != getSortDirection() ) {
				sortMajorAxis();
			}
		}
		return dMedianLong;
	}

	/**
	 * Sort using WorkingDataRow's compareTo method.
	 */
	public void sortMajorAxis() {
		if( getSortDirection() == SORT_WEST_EAST ) {
			Collections.sort(this, new CompareLongitude() );
			iCurrentSort = SORT_WEST_EAST;
			WorkingDataRow firstRow = this.get(0);
			dMinLong = firstRow.getLongitudeIn();
			WorkingDataRow lastRow = this.get(this.size()-1);
			dMaxLong = lastRow.getLongitudeIn();
			dMinLat = null;
			dMaxLat = null;			
		}
		else {
			Collections.sort(this, new CompareLatitude() );
			iCurrentSort = SORT_SOUTH_NORTH;
			WorkingDataRow firstRow = this.get(0);
			dMinLat = firstRow.getLongitudeIn();
			WorkingDataRow lastRow = this.get(this.size()-1);
			dMaxLat = lastRow.getLongitudeIn();
			dMinLong = null;
			dMaxLong = null;
		}
	}
	
	public ArrayList<String> getSmallGroups( int k ) {
		ArrayList<String> aSmallGroups = new ArrayList<String>();
		HashMap<String, Integer> hGroupSizes = new HashMap<String, Integer>();
		for( WorkingDataRow currentRow : this ) {
			String sAnimalType = currentRow.getAnimalTypeIn();
			Integer iCount = hGroupSizes.get(sAnimalType);
			if( iCount == null )
				iCount = 1;
			else
				iCount++;
			hGroupSizes.put(sAnimalType, iCount);
		}
		for( String sAnimalType : hGroupSizes.keySet() ) {
			Integer iCount = hGroupSizes.get(sAnimalType);
			if( iCount < k + 1 ) {
				aSmallGroups.add(sAnimalType);
			}
		}
		for( WorkingDataRow currentRow : this ) {
			String sAnimalType = currentRow.getAnimalTypeIn();
			Integer iCount = hGroupSizes.get(sAnimalType);
			if( iCount >= k + 1 )
				currentRow.setAnimalType(sAnimalType);
			else
				currentRow.setAnimalType("Other");
		}
		return aSmallGroups;
	}
	
	/**
	 * This method is much too complicated.  It involves iteration through all the rows 
	 * then iterating through as many rows forward and backward to be sure we have found 
	 * the K closest.  We convert this from O(n^2) to O(n) by looking at distance on the 
	 * sorted axis (Major distance .  If we find a distance on that axis > the Kth closest 
	 * so far, we know there can be no closer points further away on this axis.  Because we
	 * are searching both directions, we need to do this test forward and backward.  Finally,
	 * or really first, we have to see if there really are j nodes in each direction before 
	 * trying to test that direction.  And if we haven't found K points at all then we add
	 * them as in the K closest, of course.
	 * @param k this is the integer confidentiality level.
	 */
	private void calcDKs() {
		int iSize = size();
		// Wrap this in a test so we don't do over and over.
		sortMajorAxis();
		ArrayList<WorkingDataRow> dClosestRows = null;
		ArrayList<Double> dClosest = null;
		aSmallGroups = getSmallGroups(k);
		WorkingDataRow currentRow = null;
		// Could potentially simplify things by extracting this loop, but it is a very small part of the complexity.
		for( int i = 0; i < iSize; i++ ) {
			boolean bDone[] = {false,false};
			dClosestRows = new ArrayList<WorkingDataRow>();
			dClosest = new ArrayList<Double>();
			currentRow = get(i);
			for( int j = 1; j < iSize; j++ ) {
				try {
					WorkingDataRow nextRow = null;
					if( ( i - j ) >= 0 && !bDone[0] ) {
						nextRow = get( i - j );
						if( nextRow != null && currentRow.similarTo(nextRow) ) {
							if( dClosest.size() >= k ) {
								Double dKCurrent =  dClosest.get(k-1);
								if( getMajorDistance( currentRow, nextRow, iSortDirection ) > dKCurrent ) {
									bDone[0] = true;
									if( bDone[0] && bDone[1] )
										break;
								}
								// Didn't short circuit backward
								else {
									checkRow( currentRow, nextRow, dClosestRows, dClosest, k );
								}
							}
							// Didn't have enough to test 
							else
								checkRow( currentRow, nextRow, dClosestRows, dClosest, k );
						}
					}
					else 
						bDone[0] = true;
					if( ( i + j ) < iSize && !bDone[1] ) {
						nextRow = get( i + j );
						if( nextRow != null && currentRow.similarTo(nextRow) ) {
							if( dClosest.size() >= k ) {
								Double dKCurrent =  dClosest.get(k-1);
								if( getMajorDistance( currentRow, nextRow, iSortDirection ) > dKCurrent ) {
									bDone[1] = true;
									if( bDone[0] && bDone[1] )
										break;
								}
								// Didn't short circuit forward
								else {
									checkRow( currentRow, nextRow, dClosestRows, dClosest, k );
								}
							}
							// Didn't have enough to test
							else
								checkRow( currentRow, nextRow, dClosestRows, dClosest, k );
						}
					}
					else 
						bDone[1] = true;
				} catch (InvalidCoordinateException e) {
					Loggers.error(e);
				}
			}
			if( dClosest.size() >= k ) {
				Double dK = dClosest.get(k-1);
				currentRow.setDK(dK);
			}
			else if ( dClosest != null && dClosest.size() > altK - 1 && currentRow.getAnimalType().equals("Other") ) {
				Double dK =  dClosest.get(dClosest.size()-1);
				currentRow.setDK(dK);
				Loggers.getLogger().info(currentRow.getAnimalTypeIn() + " animal type using " + dClosest.size() + " closest 'Other' premises.");
			}
			else {
				Double dK = 0.0;
				currentRow.setDK(dK);
				Loggers.error("Not enough premises of type " + currentRow.getAnimalTypeIn() + " to get " + k + " closest " + currentRow.getAnimalType());
			}
			// TODO Calculate gold standard values and write test cases!
		}
	}
	
	private void checkRow( WorkingDataRow currentRow, WorkingDataRow nextRow, 
							ArrayList<WorkingDataRow> dClosestRows, ArrayList<Double> dClosest, int k ) {
		if( currentRow == null ) {
			Loggers.error("null currentRow passed to checkRow");
			return;
		}
		if( nextRow == null ) {
			Loggers.error("null nextRow passed to checkRow");
			return;
		}
		try {
			boolean bAdded = false;
			Double distance = getDistance(currentRow, nextRow);
			for( int i = 0; i < dClosest.size() ; i++ ) {
				if( distance < dClosest.get(i) ) {
					dClosestRows.add(i,currentRow);
					dClosest.add(i,distance);
					bAdded = true;
					break;
				}
			}
			if( !bAdded && dClosest.size() < k ) {
				dClosestRows.add(currentRow);
				dClosest.add( distance );
			}
		} catch (InvalidCoordinateException e) {
			Loggers.error(e);
		}
		
	}
	
	private double getDistance( WorkingDataRow Row1, WorkingDataRow Row2 )
				 throws InvalidCoordinateException {
		return Distance.getDistance(Row1.getLatitudeIn(), Row1.getLongitudeIn(), Row2.getLatitudeIn(), Row2.getLongitudeIn());
	}
	
	private double getMajorDistance( WorkingDataRow Row1, WorkingDataRow Row2, int iSortDirection )
				 throws InvalidCoordinateException {
		return Distance.getMajorDistance(Row1.getLatitudeIn(), Row1.getLongitudeIn(), Row2.getLatitudeIn(), Row2.getLongitudeIn(), iSortDirection);
	}
	
	/**
	 * Process the whole structure ready for output.
	 */
	public void deIdentify() {
		setSortDirection();
		calcDKs();
		jitter();
	}

	private void jitter() {
		RandomNumbers rn = new RandomNumbers();
		WorkingDataRow aRow = get(0);
		double dK = aRow.getDK();
		if( dK < 0.0 ) {
			Loggers.error("jitter() called before calcDKs");
			return;
		}
		for( WorkingDataRow currentRow : this ) {
			jitter( currentRow, rn );
		}
	}
	
	private void jitter( WorkingDataRow currentRow, RandomNumbers rn ) {
		double dK = currentRow.getDK();
		double dLatIn = currentRow.getLatitudeIn();
		double dLongIn = currentRow.getLongitudeIn();
		double dLat;
		double dLong;
		double dDeltaLat;
		double dDeltaLong;
		// Calculate average degrees latitude and longitude needed to move dK
		dDeltaLat = (dK / Distance.MILES_PER_DEGREE_LAT)/SQRT2;
		dDeltaLong = (dK / Distance.milesPerDegreeLongitude(dLatIn))/SQRT2;
		currentRow.setDLat(dDeltaLat);
		currentRow.setDLong(dDeltaLong);
		// Move from twice the distance needed back to twice forward on each axis
		// These will combine to create a distance sqrt( dLat^2 + dLong^2 )
		// resulting in a left skewed (conservative) normal distribution of distance
		// with a mean approximately dK and a median slightly higher.
		dLat = dLatIn + rn.nextDouble((-2.0*dDeltaLat),(2.0*dDeltaLat));
		dLong = dLongIn + rn.nextDouble((-2.0*dDeltaLong),(2.0*dDeltaLong));
		try {
			currentRow.setLatitude(dLat);
			currentRow.setLongitude(dLong);
		} catch (InvalidCoordinateException e) {
			Loggers.error(e);
		}
	}
	

	class CompareDistance implements Comparator<WorkingDataRow> {
		double dLat;
		double dLong;
		
		public CompareDistance( WorkingDataRow dStart ) {
			dLat = dStart.getLatitudeIn();
			dLong = dStart.getLongitudeIn();
		}
		@Override
		public int compare(WorkingDataRow arg0, WorkingDataRow arg1) {
			try {
				double dDist0 = Distance.getDistance(dLat, dLong, arg0.getLatitudeIn(), arg0.getLongitudeIn() );
				double dDist1 = Distance.getDistance(dLat, dLong, arg1.getLatitudeIn(), arg1.getLongitudeIn() );
				if( dDist0 < dDist1 )
					return -1;
				else if (dDist0 > dDist1 )
					return 1;
				else 
					return 0;
			} catch (InvalidCoordinateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
	}
	
	class CompareLatitude implements Comparator<WorkingDataRow> {
		@Override
		public int compare(WorkingDataRow arg0, WorkingDataRow arg1) {
			if( arg0.getLatitudeIn() < arg1.getLatitudeIn() )
				return -1;
			else if ( arg0.getLatitudeIn() > arg1.getLatitudeIn() )
				return 1;
			else 
				return 0;
		}
	}

	class CompareLongitude implements Comparator<WorkingDataRow> {
		@Override
		public int compare(WorkingDataRow arg0, WorkingDataRow arg1) {
			if( arg0.getLongitudeIn() < arg1.getLongitudeIn() )
				return -1;
			else if ( arg0.getLongitudeIn() > arg1.getLongitudeIn() )
				return 1;
			else 
				return 0;
		}

	}

}
