package edu.clemson.lph.jitter.structs;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;

import edu.clemson.lph.dialogs.ProgressDialog;
import edu.clemson.lph.jitter.files.ConfigFile;
import edu.clemson.lph.jitter.files.OutputCSVFile;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.geometry.Bounds;
import edu.clemson.lph.jitter.geometry.Distance;
import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.geometry.InvalidUTMZoneException;
import edu.clemson.lph.jitter.geometry.StateBounds;
import edu.clemson.lph.jitter.geometry.UTMProjection;
import edu.clemson.lph.jitter.logger.Loggers;
import edu.clemson.lph.security.RandomNumbers;

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
@SuppressWarnings("serial")
public class WorkingData extends ArrayList<WorkingDataRow> {
	public static final int SORT_NONE = -1;
	public static final int SORT_SOUTH_NORTH = 0;
	public static final int SORT_WEST_EAST = 1;
	public static final Double SQRT2 = Math.sqrt(2.0);
	public static final Double MAX_LONGITUDE_DISTANCE = 45.0;
	
	public String[] aColumns;

	private int k;  // This is THE K in K-Anonymity.  Normally don't use single letter variable names here
	private int iRowsPerLog = 100;
	
	private String sFilePath;
	private OutputCSVFile fileError = null;

	private int iSortDirection = SORT_WEST_EAST;  // More states are wider than tall.
	private int iCurrentSort = SORT_NONE;
	private int iNextKey = 0;
	private ArrayList<Integer> randInts;
	private ArrayList<String> aSmallGroups = null;
	private int iRows = 10000;
	private Double dMinLong = null;
	private Double dMaxLong = null;
	private Double dMedianLat = null;
	private Double dMedianLong = null;
	private Double dMinLat = null;
	private Double dMaxLat = null;
	private ProgressDialog prog = null;
	
	private ArrayList<WorkingDataRow> aRemovedRows = new ArrayList<WorkingDataRow>();
	
	/**
	 * Construct an empty WorkingData from source.  Only called by directly by unit tests.
	 * Correct usage is WorkingData data = source.getData();
	 * @param source SourceCSVFile from which to initialize the data structures.
	 * @throws FileNotFoundException
	 */
	public WorkingData( SourceCSVFile source ) throws FileNotFoundException {
		this.fileError = new OutputCSVFile( source, OutputCSVFile.OutputFileType.ERROR );
		this.sFilePath = source.getPath();
		this.aColumns = source.getColumns();
		k = ConfigFile.getMinK();
	}
	
	public OutputCSVFile getErrorFile() {
		return fileError;
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
		Bounds bounds = null;
		if( selectedStates != null && selectedStates.size() > 0 ) {
			bHasStates = true;
			Set<String> states = StateBounds.getStates();
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
		}
		else if ( ConfigFile.getMinLatitude() != null && ConfigFile.getMaxLatitude() != null 
				&& ConfigFile.getMinLongitude() != null && ConfigFile.getMaxLatitude() != null ) {
			bounds = new Bounds( ConfigFile.getMinLatitude(), 
								 ConfigFile.getMaxLatitude(), 
								 ConfigFile.getMinLongitude(), 
								 ConfigFile.getMaxLongitude() );
			System.out.println( "MinLat = " + ConfigFile.getMinLatitude() + " MaxLat = "+ ConfigFile.getMaxLatitude() 
					+ " MinLong = "+ ConfigFile.getMinLongitude() + " MaxLong = "+ConfigFile.getMaxLongitude() );

		}
		for( WorkingDataRow row : this ) {
			if( bounds != null ) {
				if( row.getLongitudeIn() < bounds.getMinLong() || row.getLongitudeIn() > bounds.getMaxLong() ||
						 row.getLatitudeIn() < bounds.getMinLat() || row.getLatitudeIn() > bounds.getMaxLat() ) {
					if( bHasStates )
						Loggers.error( "Coordinate outside state bounds (" + row.getLongitudeIn() + ", " + row.getLatitudeIn() + " in row " + row.getOriginalKey() );
					else	
						Loggers.error( "Coordinate outside bounds (" + row.getLongitudeIn() + ", " + row.getLatitudeIn() + " in row " + row.getOriginalKey() );
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
	 * sorted axis (Major distance).  If we find a distance on that axis > the Kth closest 
	 * so far, we know there can be no closer points further away on this axis.  Because we
	 * are searching both directions, we need to do this test forward and backward.  Finally,
	 * or really first, we have to see if there really are j nodes in each direction before 
	 * trying to test that direction.  And if we haven't found K points at all then we add
	 * them as in the K closest, of course.
	 * With the profiling counters uncommented we can see the number of comparisons needed
	 * to find the Kth closest point.  In a large, roughly square data set near the middle of 
	 * the set, this came to almost half the points having to compare to each point.  Making
	 * the complexity of this step O(n^2) BY FAR the slowest step for large sets.  I had
	 * expected the major distance short circuit to save more than that.
	 * @param k this is the integer confidentiality level.
	 */
	private void calcDKs() {
		int iSize = size();
		// Wrap this in a test so we don't do over and over.
		sortMajorAxis();
		ArrayList<WorkingDataRow> dClosestRows = null;
		ArrayList<Double> dClosestDistances = null;
		WorkingDataRow currentRow = null;
		// Could potentially simplify things by extracting this loop, but it is a very small part of the complexity.
		if( prog != null ) prog.setCurrentTask("Calculating DK for each point.");
		// Used only for some profiling but incrementing a long is faster than testing to see if we need to.
		long lTimeLast = System.currentTimeMillis();
		long lRowsSeen = 0;
		for( int i = 0; i < iSize; i++ ) {			
			currentRow = get(i);
			if( aRemovedRows.contains( currentRow ) )
				continue;
			if( ( i % iRowsPerLog == 0 ) && prog != null) {
				String sRows = String.format("%,d", i);
				prog.setCurrentTask("Calculating dK for each point. (" + sRows + " points complete.)");
				// Only spend time on IO if we are asked.
				if( ConfigFile.isDetailedLoggingRequested() ) {
					long lTimeNow = System.currentTimeMillis();
					System.out.println( "Row " + i + "; " + (lTimeNow - lTimeLast) + " milliseconds; " + (lRowsSeen/iRowsPerLog) + " Avg Rows seen");
					lTimeLast = lTimeNow;
				}
				lRowsSeen = 0;
			}
			boolean bDone[] = {false,false};
			dClosestRows = new ArrayList<WorkingDataRow>();
			dClosestDistances = new ArrayList<Double>();
			for( int j = 1; j < iSize; j++ ) {
				try {
					WorkingDataRow nextRow = null;
					if( ( i - j ) >= 0 && !bDone[0] ) {
						nextRow = get( i - j );
						if( aRemovedRows.contains( nextRow ) )
							continue;
						if( nextRow != null && currentRow.similarQuasiIdentifiers(nextRow) && currentRow.variesOnSensitive(nextRow) ) {
							if( dClosestDistances.size() >= k ) {
								Double dKCurrent =  dClosestDistances.get(k-1);
								if( getMajorDistance( currentRow, nextRow, iSortDirection ) > dKCurrent ) {
									bDone[0] = true;
									if( bDone[0] && bDone[1] )
										break;
								}
								// Didn't short circuit backward so add if in closest K
								else {
									addNextRowDistance( currentRow, nextRow, dClosestRows, dClosestDistances, k );
									lRowsSeen++;
								}
							}
							// Didn't have enough to test for short circuit so just add
							else {
								addNextRowDistance( currentRow, nextRow, dClosestRows, dClosestDistances, k );
								lRowsSeen++;
							}
						}
					}
					else 
						bDone[0] = true;
					if( ( i + j ) < iSize && !bDone[1] ) {
						nextRow = get( i + j );
						if( aRemovedRows.contains( nextRow ) )
							continue;
						if( nextRow != null && currentRow.similarQuasiIdentifiers(nextRow) && currentRow.variesOnSensitive(nextRow)  ) {
							if( dClosestDistances.size() >= k ) {
								Double dKCurrent =  dClosestDistances.get(k-1);
								if( getMajorDistance( currentRow, nextRow, iSortDirection ) > dKCurrent ) {
									bDone[1] = true;
									if( bDone[0] && bDone[1] )
										break;
								}
								// Didn't short circuit forward so add if in closest K
								else {
									addNextRowDistance( currentRow, nextRow, dClosestRows, dClosestDistances, k );
									lRowsSeen++;
								}
							}
							// Didn't have enough to test for short circuit so just add
							else {
								addNextRowDistance( currentRow, nextRow, dClosestRows, dClosestDistances, k );
								lRowsSeen++;
							}
						}
					}
					else 
						bDone[1] = true;
				} catch (InvalidCoordinateException e) {
					Loggers.error("Unable to calculate dK", e);
					removeRow("Nonsense Jitter", currentRow);  
				}
			}
			if( dClosestDistances.size() >= k ) {
				Double dK = dClosestDistances.get(k-1);
				currentRow.setDK(dK);
				// Create log file appropriate for studying dK calculation
				if( Loggers.getLevel() == Level.DEBUG || Loggers.getLevel() == Level.ALL ) {
					String sClosest = "";
					int ithClosest = 0;
					for( WorkingDataRow r : dClosestRows ) {
						if( ithClosest++ >= 5 ) break;
						sClosest += "," + r.getOriginalKey();
					}
					Loggers.debug(i + "," +currentRow.getOriginalKey() +","+currentRow.getLatitudeIn()+","+currentRow.getLongitudeIn()+sClosest);
				}
				
			}
			// Because we removed all groups of less than K previously, the only way we failed is not enough variation in size
			else {
				Double dK = 0.0;
				currentRow.setDK(dK);
				Loggers.info("Not enough size variation in premises of other types to get " + k + " closest for type " + currentRow.getAnimalTypeIn());
				removeType("Not enough size variation in premises of type " + currentRow.getAnimalTypeIn(), currentRow.getAnimalType());
			}
		} // End for each row.
	}
	
	/**
	 * Internal method to to test pair of rows to add to closest if appropriate.
	 * @param currentRow
	 * @param nextRow
	 * @param dClosestRows
	 * @param dClosestDistances
	 * @param k
	 * @throws InvalidCoordinateException 
	 */
	private void addNextRowDistance( WorkingDataRow currentRow, WorkingDataRow nextRow, 
							ArrayList<WorkingDataRow> dClosestRows, ArrayList<Double> dClosestDistances, int k ) throws InvalidCoordinateException {
		if( currentRow == null ) {
			Loggers.error("null currentRow passed to checkRow");
			return;
		}
		if( nextRow == null ) {
			Loggers.error("null nextRow passed to checkRow");
			return;
		}
		if( aRemovedRows.contains(nextRow) ) {
			Loggers.error("Compare to removed row " + nextRow.getOriginalKey() );
			return;
		}
		boolean bAdded = false;
		Double distance = getDistance(currentRow, nextRow);
		for( int i = 0; i < dClosestDistances.size() ; i++ ) {
			if( distance < dClosestDistances.get(i) ) {
				dClosestRows.add(i,nextRow);
				dClosestDistances.add(i,distance);
				bAdded = true;
				break;
			}
		}
		if( !bAdded && dClosestDistances.size() < k ) {
			dClosestRows.add(nextRow);
			dClosestDistances.add( distance );
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
	
	/**
	 * The methods that make up this simple sequence are NOT actually loosely coupled.
	 * Some of these fail without the previous steps having run.
	 * @param prog Progress dialog to update as we go along.
	 */
	public void deIdentify( ProgressDialog prog ) {
		this.prog = prog;
		if( this.size() > 100000 )
			iRowsPerLog = 1000;
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
		Integer iZone = null;
		String sHemisphere = null;
		if( ConfigFile.isUTMSet() ) {
			iZone = ConfigFile.getUTMZoneNum();
			sHemisphere = ConfigFile.getZoneHemisphere();
		}
		else {
			iZone = UTMProjection.getBestZone(getMedianLongitude());
			sHemisphere = UTMProjection.getHemisphere(getMedianLatitude());
		}
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
			fileError.printErrorRow( sMsg, row );
			aRemovedRows.add(row);
		}
	}
	
	private void removeType( String sMsg, String sAnimalType ) {
		for( WorkingDataRow row : this ) {
			if( row.getAnimalType().equals(sAnimalType) ) {
				if( !aRemovedRows.contains(row) ) {
					fileError.printErrorRow( sMsg, row );
					aRemovedRows.add(row);
				}
			}
		}
	}
	
	private void saveSmallGroups( ArrayList<String> aSmallGroups ) {
		String sSmallGroupsPath = sFilePath.substring(0, sFilePath.lastIndexOf(".")) + "SmallGroups.txt";
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

//	class CompareDistance implements Comparator<WorkingDataRow> {
//		Double dLat;
//		Double dLong;
//		
//		public CompareDistance( WorkingDataRow dStart ) {
//			dLat = dStart.getLatitudeIn();
//			dLong = dStart.getLongitudeIn();
//		}
//		@Override
//		public int compare(WorkingDataRow arg0, WorkingDataRow arg1) {
//			try {
//				Double dDist0 = Distance.getDistance(dLat, dLong, arg0.getLatitudeIn(), arg0.getLongitudeIn() );
//				Double dDist1 = Distance.getDistance(dLat, dLong, arg1.getLatitudeIn(), arg1.getLongitudeIn() );
//				if( dDist0 < dDist1 )
//					return -1;
//				else if (dDist0 > dDist1 )
//					return 1;
//				else 
//					return 0;
//			} catch (InvalidCoordinateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return 0;
//		}
//	}
	
	private class CompareLatitude implements Comparator<WorkingDataRow> {
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

	private class CompareLongitude implements Comparator<WorkingDataRow> {
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
