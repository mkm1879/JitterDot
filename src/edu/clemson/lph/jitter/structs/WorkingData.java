package edu.clemson.lph.jitter.structs;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import edu.clemson.lph.dialogs.ProgressDialog;
import edu.clemson.lph.jitter.JitterDot;
import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.geometry.Distance;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.geometry.InvalidUTMZoneException;
import edu.clemson.lph.jitter.geometry.StateBounds;
import edu.clemson.lph.jitter.geometry.UTMProjection;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.security.RandomNumbers;


@SuppressWarnings("serial")
public class WorkingData extends ArrayList<WorkingDataRow> {
	public static final int SORT_NONE = -1;
	public static final int SORT_SOUTH_NORTH = 0;
	public static final int SORT_WEST_EAST = 1;
	public static final Double SQRT2 = Math.sqrt(2.0);
	public static final Double MAX_LONGITUDE_DISTANCE = 45.0;
	public static String[] aColumns;

	private int k;
	private static final int altK = 3;
	
	private int iSortDirection = SORT_WEST_EAST;  // More states are wider than taller.
	private int iCurrentSort = SORT_NONE;
	private int iNextKey = 0;
	private ArrayList<Integer> randInts;
	ArrayList<String> aSmallGroups = null;
	private int iRows = 10000;
	private Double dMinLong = null;
	private Double dMaxLong = null;
	private Double dMedianLat = null;
	private Double dMedianLong = null;
	private Double dMinLat = null;
	private Double dMaxLat = null;
	private ProgressDialog prog = null;
	
	private ArrayList<WorkingDataRow> aRemovedRows = new ArrayList<WorkingDataRow>();
	
	public WorkingData() {
		k = ConfigFile.getMinK();
	}
	
	public static void setColumns( String[] aColumns) {
		WorkingData.aColumns = aColumns;
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
	 * Should use enum!
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
		// If already sorted do sorted direction first
		if( iCurrentSort == SORT_WEST_EAST) {
			doLongStats();
			doLatStats();
		}
		else if( iCurrentSort == SORT_SOUTH_NORTH) {
			doLatStats();
			doLongStats();
		}
		// If not already sorted do in order to leave sorted 
		// in correct direction.
		else if( getSortDirection() == SORT_SOUTH_NORTH ) {
			doLongStats();
			doLatStats();			
		}
		else {
			doLatStats();
			doLongStats();			
		}
		// this should be belt and suspenders.
		if( iCurrentSort != getSortDirection() ) {
			System.err.println( "Third sort performed in calcStats" );
			sortMajorAxis();
		}		

	}
	
	private void doLatStats() {
		// Calculate Latitude
		WorkingDataRow row;
		if( iCurrentSort != SORT_SOUTH_NORTH) {
			if( prog != null ) prog.setCurrentTask("Sorting by Latitude");
			Collections.sort(this, new CompareLatitude() );
			iCurrentSort = SORT_SOUTH_NORTH;
			row = get(0);
			dMinLat = row.getLatitudeIn();
			row = get( size() - 1 );
			dMaxLat = row.getLatitudeIn();
		}
		if( size() % 2 != 0 ) {
			WorkingDataRow midRow = get( size() / 2);
			dMedianLat = midRow.getLatitudeIn();
		}
		else {
			WorkingDataRow lowRow = get( (size() -1) / 2 );
			WorkingDataRow highRow = get( ((size() -1) / 2) + 1 );
			dMedianLat = ( lowRow.getLatitudeIn() + highRow.getLatitudeIn() ) / 2.0;
		}	
	}
	
	private void doLongStats() {
		// Calculate Longitude
		WorkingDataRow row;
		if( iCurrentSort != SORT_WEST_EAST) {
			if( prog != null ) prog.setCurrentTask("Sorting by Longitude");
			Collections.sort(this, new CompareLongitude() );
			iCurrentSort = SORT_WEST_EAST;		
			row = get(0);
			dMinLong = row.getLongitudeIn();
			row = get( size() - 1 );
			dMaxLong = row.getLongitudeIn();
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
	
	public Double getMedianLongitude() {
		if( dMedianLong == null ) {
			calcStats();
		}
		return dMedianLong;
	}

	
	public Double getMedianLatitude() {
		if( dMedianLat == null ) {
			calcStats();
		}
		return dMedianLat;
	}

	/**
	 * Sort based on Sort Direction.  If we guessed right, will already be sorted
	 * from call to calcStats.
	 */
	public void sortMajorAxis() {
		if( getSortDirection() == SORT_WEST_EAST ) {
			if( iCurrentSort != SORT_WEST_EAST) {
				if( prog != null ) prog.setCurrentTask("Sorting by Longitude");
				Collections.sort(this, new CompareLongitude() );
				iCurrentSort = SORT_WEST_EAST;
//				WorkingDataRow firstRow = this.get(0);
//				dMinLong = firstRow.getLongitudeIn();
//				WorkingDataRow lastRow = this.get(this.size()-1);
//				dMaxLong = lastRow.getLongitudeIn();
//				dMinLat = null;
//				dMaxLat = null;		
			}
		}
		else {
			if( iCurrentSort != SORT_SOUTH_NORTH) {
				if( prog != null ) prog.setCurrentTask("Sorting by Latitude");
				Collections.sort(this, new CompareLatitude() );
				iCurrentSort = SORT_SOUTH_NORTH;
//				WorkingDataRow firstRow = this.get(0);
//				dMinLat = firstRow.getLongitudeIn();
//				WorkingDataRow lastRow = this.get(this.size()-1);
//				dMaxLat = lastRow.getLongitudeIn();
//				dMinLong = null;
//				dMaxLong = null;
			}
		}
	}
	
	/**
	 * Look for all groups too small to identify individually and convert to Other.
	 * @param minGroup Smallest group size for purposes of computing DK.
	 * @return the list of small groups so we can make note of that.
	 */
	public ArrayList<String> getSmallGroups( int minGroup ) {
		if( prog != null ) prog.setCurrentTask("Finding Small Animal Type Groups");

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
			if( iCount < minGroup + 1 ) {
				aSmallGroups.add(sAnimalType);
			}
		}
		if( prog != null ) prog.setCurrentTask("Converting Small Animal Type Groups to 'Other'");
		for( WorkingDataRow currentRow : this ) {
			String sAnimalType = currentRow.getAnimalTypeIn();
			Integer iCount = hGroupSizes.get(sAnimalType);
			if( iCount >= minGroup + 1 )
				currentRow.setAnimalType(sAnimalType);
			else
				currentRow.setAnimalType("Other");
		}
		return aSmallGroups;
	}
	
	private void sanityCheck() {
		if( prog != null ) prog.setCurrentTask("Performing coordinate sanity checks based on State Bounds");

		boolean bHasStates = false;
		List<String> selectedStates = ConfigFile.getStates();
		if( selectedStates != null )
			bHasStates = true;
		Set<String> states = StateBounds.getStates();
		StateBounds bounds = null;
		for( String sState : selectedStates ) {
			if( !states.contains(sState) ) {
				Loggers.error( "State " + sState + " not found");
				bHasStates = false;
				break;
			}
		}
		if( bHasStates ) {
			bounds = new StateBounds( selectedStates );
		}
		for( WorkingDataRow row : this ) {
			if( bounds != null ) {
				// Why did I have to hack this????
				if( row.getLongitudeIn() < bounds.getMinLong() || row.getLongitudeIn() > bounds.getMaxLong() ||
						 row.getLatitudeIn() < bounds.getMinLat() || row.getLatitudeIn() > bounds.getMaxLat() ) {
					Loggers.error( "Coordinate outside state bounds (" + row.getLongitudeIn() + ", " + row.getLatitudeIn() + " in row " + row.getOriginalKey() );
					removeRow("Coordinate Outside State Bounds", row);			
				}
			}
			else {
				if( Math.abs( row.getLongitudeIn() - getMedianLongitude() ) > MAX_LONGITUDE_DISTANCE ) {
					Loggers.error( "Wild Longitude (" + row.getLongitudeIn() + " in row " + row.getOriginalKey() );
					removeRow("Wild Longitude", row);			
				}
			}
		}
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
		WorkingDataRow currentRow = null;
		// Could potentially simplify things by extracting this loop, but it is a very small part of the complexity.
		if( prog != null ) prog.setCurrentTask("Calculating DK for each point.");
		for( int i = 0; i < iSize; i++ ) {
			
			if( ( i % 100 == 0 ) && prog != null ) {
				String sRows = String.format("%,d", i);
				prog.setCurrentTask("Calculating DK for each point. (" + sRows + " points complete.)");
			}
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
									checkNextRowDistance( currentRow, nextRow, dClosestRows, dClosest, k );
								}
							}
							// Didn't have enough to test 
							else
								checkNextRowDistance( currentRow, nextRow, dClosestRows, dClosest, k );
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
									checkNextRowDistance( currentRow, nextRow, dClosestRows, dClosest, k );
								}
							}
							// Didn't have enough to test
							else
								checkNextRowDistance( currentRow, nextRow, dClosestRows, dClosest, k );
						}
					}
					else 
						bDone[1] = true;
				} catch (InvalidCoordinateException e) {
					Loggers.error("Unable to calculate dK", e);
					removeRow("Nonsense Jitter", currentRow);
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
				removeRow("Not enough premises of type " + currentRow.getAnimalTypeIn(), currentRow);
			}
			// TODO Calculate gold standard values and write test cases!
		}
	}
	
	/**
	 * Internal method to to test pair of rows to add to closest if appropriate.
	 * @param currentRow
	 * @param nextRow
	 * @param dClosestRows
	 * @param dClosest
	 * @param k
	 * @throws InvalidCoordinateException 
	 */
	private void checkNextRowDistance( WorkingDataRow currentRow, WorkingDataRow nextRow, 
							ArrayList<WorkingDataRow> dClosestRows, ArrayList<Double> dClosest, int k ) throws InvalidCoordinateException {
		if( currentRow == null ) {
			Loggers.error("null currentRow passed to checkRow");
			return;
		}
		if( nextRow == null ) {
			Loggers.error("null nextRow passed to checkRow");
			return;
		}
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
	}
	
	private Double getDistance( WorkingDataRow Row1, WorkingDataRow Row2 )
				 throws InvalidCoordinateException {
		return Distance.getDistance(Row1.getLatitudeIn(), Row1.getLongitudeIn(), Row2.getLatitudeIn(), Row2.getLongitudeIn());
	}
	
	private Double getMajorDistance( WorkingDataRow Row1, WorkingDataRow Row2, int iSortDirection )
				 throws InvalidCoordinateException {
		return Distance.getMajorDistance(Row1.getLatitudeIn(), Row1.getLongitudeIn(), Row2.getLatitudeIn(), Row2.getLongitudeIn(), iSortDirection);
	}
	
	/**
	 * Process the whole structure ready for output.
	 */
	public void deIdentify() {
		deIdentify( (ProgressDialog) null );
	}
	
	public void deIdentify( ProgressDialog prog ) {
		this.prog = prog;
		setSortDirection();
		annonymizeIntegrator();
		aSmallGroups = getSmallGroups(k);
		saveSmallGroups( aSmallGroups );
		sanityCheck();
		cleanup();		
		calcDKs();
		cleanup();
		jitter();
		cleanup();
		addUTMCoordinates();
		cleanup();
	}

	private void jitter() {
		if( prog != null ) prog.setCurrentTask("Performing random jitter on each row");
		RandomNumbers rn = new RandomNumbers();
		WorkingDataRow aRow = get(0);
		Double dK = aRow.getDK();
		if( dK < 0.0 ) {
			Loggers.error("jitter() called before calcDKs");
			return;
		}
		for( WorkingDataRow currentRow : this ) {
			jitter( currentRow, rn );
		}
	}
	
	private void jitter( WorkingDataRow currentRow, RandomNumbers rn ) {
		Double dK = currentRow.getDK();
		Double dLatIn = currentRow.getLatitudeIn();
		Double dLongIn = currentRow.getLongitudeIn();
		Double dLat;
		Double dLong;
		Double dDeltaLat;
		Double dDeltaLong;
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
			Loggers.error("Latitude " + dLatIn + ", Longitude " + dLongIn + ", dK " + dK + " produced nonsense output", e);
			removeRow("Nonsense Jitter", currentRow);
		}
	}
	
	private void annonymizeIntegrator() {
		if( prog != null ) prog.setCurrentTask("Converting integrator to anonymous 'Co'+ X format");
		HashMap<String, String> map = new HashMap<String, String>();
		int iCo = 1;
		for( WorkingDataRow row : this ) {
			String sIntegrator = row.getIntegratorIn();
			if( sIntegrator != null && sIntegrator.trim().length() > 0 && !map.containsKey(sIntegrator) && !"NULL".equalsIgnoreCase(sIntegrator) ) {
				map.put( sIntegrator, "Co" + Integer.toString(iCo++) );
			}
		}
		for( WorkingDataRow row : this ) {
			String sIntegrator = row.getIntegratorIn();
			if( sIntegrator != null && sIntegrator.trim().length() > 0 && !"NULL".equalsIgnoreCase(sIntegrator) ) {
				row.setIntegrator( map.get(sIntegrator) );
			}
			else {
				row.setIntegrator(null);
			}
			
		}
	}
	
	private void addUTMCoordinates() {
		if( prog != null ) prog.setCurrentTask("Calculating UTM Coordinates for each row");
		int iZone = UTMProjection.getBestZone(getMedianLongitude());
		String sHemisphere = UTMProjection.getHemisphere(getMedianLatitude());
		UTMProjection utm = null;
		try {
			utm = new UTMProjection( iZone, sHemisphere );
		} catch (InvalidUTMZoneException e) {
			Loggers.error("Invalid UTM Zone", e);
			return;
		}
		for( WorkingDataRow row : this ) {
			// Work with already jittered coordinates
			Double dLat = row.getLatitude();
			Double dLong = row.getLongitude();
			if( dLat != null && dLong != null ) {
				Double aCoords[];
				try {
					aCoords = utm.project(dLat, dLong);
					row.setEasting(aCoords[0]);
					row.setNorthing(aCoords[1]);
					row.setUTMZone(iZone);
					row.setUTMHemisphere(sHemisphere);
				} catch (InvalidCoordinateException e) {
					Loggers.error("Cannot project coordinates", e);
					removeRow( "Cannot project coordinates", row );
				}
			}
		}
	}
	
	private void removeRow( String sMsg, WorkingDataRow row ) {
		if( !aRemovedRows.contains(row) ) {
			JitterDot.getErrorFile().printErrorRow( sMsg, row );
			aRemovedRows.add(row);
		}
	}
	
	private void saveSmallGroups( ArrayList<String> aSmallGroups ) {
		String sErrorFilePath = JitterDot.getErrorFile().getFilePath();
		String sSmallGroupsPath = sErrorFilePath.substring(0, sErrorFilePath.indexOf("ERROR")) + "SmallGroups.txt";
		try {
			PrintWriter pw = new PrintWriter( new FileWriter( sSmallGroupsPath ) );
			for( String sGroup : aSmallGroups ) {
				pw.println(sGroup);
			}
			pw.close();
		} catch (IOException e) {
			Loggers.error(e);
		}
	}
	
	private void cleanup() {
		for( WorkingDataRow row : aRemovedRows ) {
			this.remove(row);
			iRows--;
		}
		aRemovedRows.clear();
	}

	class CompareDistance implements Comparator<WorkingDataRow> {
		Double dLat;
		Double dLong;
		
		public CompareDistance( WorkingDataRow dStart ) {
			dLat = dStart.getLatitudeIn();
			dLong = dStart.getLongitudeIn();
		}
		@Override
		public int compare(WorkingDataRow arg0, WorkingDataRow arg1) {
			try {
				Double dDist0 = Distance.getDistance(dLat, dLong, arg0.getLatitudeIn(), arg0.getLongitudeIn() );
				Double dDist1 = Distance.getDistance(dLat, dLong, arg1.getLatitudeIn(), arg1.getLongitudeIn() );
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
