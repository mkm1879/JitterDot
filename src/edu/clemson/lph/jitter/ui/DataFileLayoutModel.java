package edu.clemson.lph.jitter.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;



import edu.clemson.lph.LabeledCSVParser;
import edu.clemson.lph.jitter.files.SourceCSVFile;
import edu.clemson.lph.jitter.logger.Loggers;
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
public class DataFileLayoutModel extends AbstractTableModel {
	private static final int MAXROWS = 50;
	private LabeledCSVParser parser = null;
	private String aColumns[];
	private ArrayList<ArrayList<String>> aData = new ArrayList<ArrayList<String>>();
	private File fData;
	
	public DataFileLayoutModel() {
		super();
	}
	
	public DataFileLayoutModel( File fData ) throws IOException {
		super();
		setDataFile(fData);
	}
	
	/**
	 * Set the data file for example data to map
	 * @param fData
	 * @throws IOException
	 */
	public void setDataFile( File fData ) throws IOException {
		this.fData = fData;
		aData.clear();
		parser = new LabeledCSVParser( new FileInputStream( fData ) );
		aColumns = parser.getLabels();
		ArrayList<String> aRow = new ArrayList<String>();
		// First row holds selected fields from our list
		String[] aStdCols = SourceCSVFile.getStandardColumns();
		ArrayList<String> lStdCols = new ArrayList<String>();
		for( int i = 0; i < aStdCols.length; i++ ) 
			lStdCols.add(aStdCols[i]);
		for( int iCol = 0; iCol < aColumns.length; iCol++ ) {
			if( lStdCols.contains(aColumns[iCol]) )
				aRow.add(aColumns[iCol]);
			else
				aRow.add("");
		}
		aData.add(aRow);
		aRow = new ArrayList<String>();
		// Second row holds headers from data file
		for( String sLabel : aColumns ) {
			aRow.add(sLabel);
		}
		aData.add(aRow);
		aRow = new ArrayList<String>();
		// Subsequent MAXROWS hold actual data from the top of the data file
		String aLine[] = null;
		int iRow = 0;
		while( (aLine = parser.getLine()) != null && iRow < MAXROWS ) {
			for( String sLabel : aLine ) {
				aRow.add(sLabel);
			}
			aData.add(aRow);
			aRow = new ArrayList<String>();
		}
		this.fireTableStructureChanged();
	}
	
	public File getDataFile() { return fData; }
	
	@Override 
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if( rowIndex == 0 )
			return true;
		else
			return false;
	}

	@Override
	public int getColumnCount() {
		if( aData == null || aData.size() < 1 )
			return 0;
		return aData.get(0).size();
	}

	@Override
	public int getRowCount() {
		if( aData == null || aData.size() < 1 )
			return 0;
		return aData.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if( aData == null || aData.size() < 1 || rowIndex < 0 || rowIndex >= aData.size() || columnIndex < 0 || columnIndex >= aData.get(rowIndex).size())
			return null;
		return aData.get(rowIndex).get(columnIndex);
	}
	
	/**
	 * Only the first row is editable in this environment.
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if( aData == null || aData.size() < 1 || rowIndex < 0 || rowIndex >= aData.size() || columnIndex < 0 || columnIndex >= aData.get(0).size()) {
			Loggers.error("Invalid cell coordinates: " + rowIndex +", " + columnIndex);
			return;
		}
		if( rowIndex != 0 ) {
			Loggers.error("Attempt to set value of read-only data row " + rowIndex );
			return;
		}
		if( !(aValue instanceof String) ) {
			Loggers.error("Non-String passed to setValueAt(): " + aValue.getClass().getName());
			return;
		}
		aData.get(rowIndex).set(columnIndex, (String)aValue);
		this.fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	@Override
	public String getColumnName( int columnIndex ) {
		if( aColumns == null ) {
			Loggers.error(new Exception("getColumnName called before Data File Loaded"));
			return null;
		}
		if( columnIndex < 0 || columnIndex >= aColumns.length ) {
			Loggers.error(new Exception("Invalid columnIndex " + columnIndex));
			return null;
		}
		return aColumns[columnIndex];
	}

}
