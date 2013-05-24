package edu.clemson.lph.jitter.structs;

import java.util.HashMap;

/**
 * This class provides a skeleton for later implementation of 
 * a tool to allow user to identify which columns in their input file
 * map to which columns in the standard working data.  The translations
 * will be stored in mCols with the standard field name string in the key
 * and the column label name in the value.
 * @author mmarti5
 *
 */

public class ColumnNameMap {
	protected HashMap<String, String> mCols;
	
	public ColumnNameMap() {
		mCols = new HashMap<String, String>();
	}
	
	public void put( String sColumnOut, String sColumnIn ) {
		mCols.put(sColumnOut, sColumnIn);
	}
	
	public String mapColumn( String sColumnOut ) {
		String sColumnIn = mCols.get(sColumnOut);
		if( sColumnIn == null ) 
			sColumnIn = sColumnOut;
		return sColumnIn;
	}
}
