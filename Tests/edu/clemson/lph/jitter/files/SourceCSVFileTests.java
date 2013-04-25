package edu.clemson.lph.jitter.files;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import edu.clemson.lph.jitter.geometry.InvalidCoordinateException;
import edu.clemson.lph.jitter.structs.WorkingData;

public class SourceCSVFileTests {
	private static final double TOLERANCE = 0.0001;
	SourceCSVFile source;

	@Before
	public void setUp() throws Exception {
		File fileIn = new File( "Test.csv");
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
	public void testSourceCSVFile() {
		File fileIn = new File( "Test.csv");
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
		assertTrue( aCols[4].equals("ProductionType") );
		assertTrue( aCols[5].equals("Status") );
	}

	@Test
	public void testGetData() {
		try {
			ArrayList<WorkingData> aData = source.getData();
			WorkingData row1 = aData.get(0);
			assertTrue( row1.getStatus().equals("Susceptible"));
			WorkingData row2 = aData.get(1);
			assertTrue( row2.getOriginalKey().equals("7"));
			assertTrue( Math.abs(row2.getLongitudeIn() - (-81.70784) ) < TOLERANCE );
			assertTrue( Math.abs(row2.getLatitudeIn() - 34.351887 ) < TOLERANCE );
			assertTrue( row2.getAnimalType().equals("Turkey grow-out"));
			assertTrue( row2.getStatus().equals("Susceptible"));
			assertTrue( row2.getDaysInState() < 0 );
			assertTrue( row2.getDaysLeftInState() < 0 );
			assertTrue( row2.getAnimals() == 15500 );
			System.out.println(row2.getKey() );
			System.out.println(row2.getOriginalKey() );
			// try converting from minutes and seconds
			WorkingData row3 = aData.get(2);
			assertTrue( Math.abs(row3.getLongitudeIn() - (-82.924311) ) < TOLERANCE );
			assertTrue( Math.abs(row3.getLatitudeIn() - 34.550682 ) < TOLERANCE );
			// Test odd characters and quoted field with comma.  Not meant to be a meaningful value.
			assertTrue( row3.getAnimalType().equals("Broiler's, Pullets"));
			int iRows = source.getRows();
			WorkingData rowLast = aData.get(iRows-1);
			assertTrue( rowLast.getOriginalKey().equals("41"));

		} catch (NumberFormatException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} catch (InvalidCoordinateException e) {
			fail(e.getMessage());
		} catch (InvalidInputException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testClose() {
		source.close();
	}
}
