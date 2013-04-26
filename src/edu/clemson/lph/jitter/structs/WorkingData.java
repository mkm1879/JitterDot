package edu.clemson.lph.jitter.structs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


@SuppressWarnings("serial")
public class WorkingData extends ArrayList<WorkingDataRow> {
	public static final int SORT_NONE = -1;
	public static final int SORT_SOUTH_NORTH = 0;
	public static final int SORT_WEST_EAST = 1;
	
	private int iSortDirection = SORT_WEST_EAST;
	private int iCurrentSort = SORT_NONE;
	private int iNextKey = 0;
	private ArrayList<Integer> randInts;
	private int iRows = 10000;
	private Double dMinLong = null;
	private Double dMaxLong = null;
	private Double dMinLat = null;
	private Double dMaxLat = null;
	
	public WorkingData() {
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
			randInts = randomPick(1, iRows+1);
		}
	}
	
	public void reshuffle() {
		randInts = null;
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
		if( dMinLong == null && dMinLat == null )
			sortMajorAxis();
		return dMinLong;
	}
	public Double getMaxLong() {
		if( dMinLong == null && dMinLat == null )
			sortMajorAxis();
		return dMaxLong;
	}
	public Double getMinLat() {
		if( dMinLong == null && dMinLat == null )
			sortMajorAxis();
		return dMinLat;
	}
	public Double getMaxLat() {
		if( dMinLong == null && dMinLat == null )
			sortMajorAxis();
		return dMaxLat;
	}
	
	public Double getMedianLong() {
		Double dMedian = null;
		if( iCurrentSort != SORT_WEST_EAST) {
			Collections.sort(this, new CompareLongitude() );
			iCurrentSort = SORT_WEST_EAST;			
		}
		if( size() % 2 != 0 ) {
			WorkingDataRow midRow = get( size() / 2);
			dMedian = midRow.getLongitudeIn();
		}
		else {
			WorkingDataRow lowRow = get( (size() -1) / 2 );
			WorkingDataRow highRow = get( ((size() -1) / 2) + 1 );
			dMedian = ( lowRow.getLongitudeIn() + highRow.getLongitudeIn() ) / 2.0;
		}
		if( iCurrentSort != getSortDirection() ) {
			sortMajorAxis();
		}
		return dMedian;
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
