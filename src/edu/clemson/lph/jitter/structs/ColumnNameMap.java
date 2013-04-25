package edu.clemson.lph.jitter.structs;

import java.util.HashMap;

/**
 * This class provides a skeleton for later implementation of 
 * a tool to allow user to identify which columns in their input file
 * map to which columns in the standard working data.  The translations
 * will be stored in mCols with the original string in the key
 * and the standard field name in the value.
 * @author mmarti5
 *
 */

public class ColumnNameMap {
	protected HashMap<String, String> mCols;
	
	public ColumnNameMap() {
		mCols = new HashMap<String, String>();
	}
	
	public String mapColumn( String sColumnIn ) {
		String sColumnOut = mCols.get(sColumnIn);
		if( sColumnOut == null ) sColumnOut = sColumnIn;
		return sColumnOut;
	}
}
