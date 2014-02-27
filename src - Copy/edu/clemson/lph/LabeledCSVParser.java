package edu.clemson.lph;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;


/**
 * @author mmarti5
 * CSVReader is from http://opencsv.sourceforge.net/ and used by permission under its Apache 2 license.
 *
 */
public class LabeledCSVParser extends CSVParser {

	/**
	 * @param reader
	 * @throws IOException 
	 */
	public LabeledCSVParser(Reader reader) throws IOException {
		super(reader);
	}
		public LabeledCSVParser(String sFileName) throws IOException {
		super(sFileName);
	}
	public LabeledCSVParser(File file) throws IOException {
		super(file);
	}
	public LabeledCSVParser(InputStream isIn) throws IOException {
		super(isIn);
	}

	public int getLabelIdx( String sLabel ) throws IOException {
		if( getHeader() == null ) return -1;
		return getHeader().indexOf(sLabel);
	}
	
	public String[] getLabels() {
		List<String> aHeader = getHeader();
		if( aHeader == null ) return null;
		return aHeader.toArray(new String[0]);
	}

}
