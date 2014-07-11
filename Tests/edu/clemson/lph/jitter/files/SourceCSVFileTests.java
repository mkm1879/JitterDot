package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;
import edu.clemson.lph.jitter.structs.WorkingDataRow;
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
public class SourceCSVFileTests {
	private static final double TOLERANCE = 0.0001;
	SourceCSVFile source;

	@Before
	public void setUp() throws Exception {
		File fileIn = new File( "TestFiles/Test.csv");
		try {
			source = new SourceCSVFile( fileIn );
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testSourceCSVFile() {
		File fileIn = new File( "TestFiles/Test.csv");
		try {
			source = new SourceCSVFile( fileIn );
			assertTrue( source != null );
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetColumns() {
		String[] aCols = source.getColumns();
		assertTrue( aCols[0].equals("HerdID") );
		assertTrue( aCols[1].equals("HerdSize") );
		assertTrue( aCols[2].equals("Lon") );
		assertTrue( aCols[3].equals("Lat") );
		assertTrue( aCols[4].equals("AnimalType") );
		assertTrue( aCols[5].equals("Status") );
	}

	@Test
	public void testGetData() {
		try {
			WorkingData aData = source.getData();
			WorkingDataRow row1 = aData.get(0);
			assertTrue( row1.getStatus().equals("Susceptible"));
			WorkingDataRow row2 = aData.get(1);
			assertTrue( row2.getOriginalKey().equals("7"));
			assertTrue( Math.abs(row2.getLongitudeIn() - (-81.70784) ) < TOLERANCE );
			assertTrue( Math.abs(row2.getLatitudeIn() - 34.351887 ) < TOLERANCE );
			assertTrue( row2.getAnimalTypeIn().equals("Turkey grow-out"));
			assertTrue( row2.getStatus().equals("Susceptible"));
			assertTrue( row2.getIntegratorIn().equals("A Farms"));
			assertTrue( row2.getDaysInState() < 0 );
			assertTrue( row2.getDaysLeftInState() < 0 );
			assertTrue( row2.getAnimals() == 15500 );
			// try converting from minutes and seconds
			WorkingDataRow row3 = aData.get(2);
			assertTrue( Math.abs(row3.getLongitudeIn() - (-82.924311) ) < TOLERANCE );
			assertTrue( Math.abs(row3.getLatitudeIn() - 34.550682 ) < TOLERANCE );
			// Test odd characters and quoted field with comma.  Not meant to be a meaningful value.
			assertTrue( row3.getAnimalTypeIn().equals("Broiler's, Pullets"));
			assertTrue( row3.getIntegratorIn().equals("B Farms"));
			int iRows = source.getRows();
			WorkingDataRow rowLast = aData.get(iRows-1);
			assertTrue( rowLast.getOriginalKey().equals("371"));

		} catch (NumberFormatException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (InvalidCoordinateException e) {
			fail(e.getMessage());
		} catch (InvalidInputException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testClose() {
		source.close();
	}
}
