/**
 * 
 */
package edu.clemson.lph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.clemson.lph.jitter.logger.Loggers;

/**
 * @author mmarti5
 * NOTE:  Missing features:  "" to escape the " character is not implemented as used by Excel
 *                           New line character in side " quotes is not supported as used by Excel
 *
 */
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

public class CSVParser {
	private static final Logger logger = Loggers.getLogger();
	private char cSepChar = ',';
	private char cQuoteChar = '"';
	private boolean bStrict = false;
	
	private BufferedReader reader = null;
	private ArrayList<List<String>> aRows = new ArrayList<List<String>>();
	private int iRows = -1;
	private int iCurrent = 0;
	
	public CSVParser(String sFileName) throws IOException {
		reader = new BufferedReader( new FileReader( sFileName ) );
		readLines();
	}
	public CSVParser(File file) throws IOException {
		reader = new BufferedReader( new FileReader( file ) );
		readLines();
	}
	public CSVParser(InputStream isIn) throws IOException {
		reader = new BufferedReader( new InputStreamReader( isIn ) );
		readLines();
	}
	public CSVParser(Reader rIn) throws IOException {
		reader = new BufferedReader( rIn );
		readLines();
	}
	
	public void setSepChar( char c ) {
		cSepChar = c;
	}
	
	public void setQuoteChar( char c ) {
		cQuoteChar = c;
	}
	
	public void setStrictLineLength( boolean bStrict ) {
		this.bStrict = bStrict;
	}
	
	public List<String> getHeader() {
		if( aRows != null && iRows > 0 ) 
			return aRows.get(0);
		else
			return null;
	}
	
	/**
	 * Better version of getting next row
	 * @return
	 */
	public List<String> getNext() {
		if( aRows != null && iRows > iCurrent ) 
			return aRows.get(iCurrent++);
		else
			return null;
	}	
	
	public void close() {
		// Ignore only resources are Java objects to G.C.
	}
	
	/**
	 * Legacy version returns array of strings to mimic older library.
	 * @return
	 */
	public String[] getLine() {
		if( aRows != null && iRows > iCurrent ) {
			List<String> aLine = aRows.get(iCurrent++);
			if( aLine == null  ) return null;
			return aLine.toArray(new String[0]);
		}
		else
			return null;
	}
	
	private void readLines() throws IOException {
		String sLine = reader.readLine();
		while( sLine != null ) {
			logger.info( sLine );
			ArrayList<String> aFields = readLine( sLine );
			if( bStrict && aRows.size() > 0 && aFields.size() != aRows.get(0).size() ) {
				logger.error(sLine + '\n' + aFields.size() + " != " + aRows.get(0).size());
			}
			aRows.add(aFields);
			sLine = reader.readLine();
		}
		iRows = aRows.size();
		iCurrent = 1;
		reader.close();
	}
		
	private ArrayList<String> readLine( String sLine ) throws IOException {
		StringBuffer sb = new StringBuffer();
		ArrayList<String> aFields = new ArrayList<String>();
		boolean bInQuote = false;
		boolean bAfterQuote = false;
		for( int i = 0; i < sLine.trim().length(); i++ ) {
			char c = sLine.charAt(i);
			if( c == cQuoteChar && !bInQuote ) {
				bInQuote = true;
			}
			else if( bAfterQuote && c != cSepChar ) {
				// Ignore
			}
			else if( c == cQuoteChar && bInQuote ) {
				bInQuote = false;
				bAfterQuote = true;
			}
			else if( !bInQuote && c == cSepChar ) {
				bInQuote = false;
				bAfterQuote = false;
				aFields.add(sb.toString());	
				sb.setLength(0);
			}
			else {
				sb.append(c);
			}
		}
		if( sb.length() > 0 ) {
			aFields.add(sb.toString());
		}
		return aFields;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CSVParser p = new CSVParser("../CSVParser/Test.csv");
			CSVPrinter printer = new CSVPrinter( "../CSVParser/TestOut.csv" );
			List<String> aHead = p.getHeader();
			for( String s : aHead ) {
				System.out.print( s + "|" );
			}
			System.out.println();
			printer.println(p.getHeader());
			List<String> aRow = p.getNext();
			while( aRow != null ) {
				for( String s : aRow ) {
					System.out.print( s + "|" );
					printer.print(s);
				}
				System.out.println();
				printer.println();
				aRow = p.getNext();
			}
			printer.flush();
			printer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
