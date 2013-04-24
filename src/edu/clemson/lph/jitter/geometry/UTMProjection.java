package edu.clemson.lph.jitter.geometry;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

/**
 * This is a very thin wrapper around the Proj4j library to make it consistent with 
 * the other geometry implementations in this program.
 * @author mmarti5
 *
 */

public class UTMProjection {
	private static final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
	private static final String WGS84_PARAM = "+title=long/lat:WGS84 +proj=longlat +datum=WGS84 +units=degrees";
	private static final String UTM_NORTH_EPSG = "EPSG:269"; // Add zone number left padded with 0 to two digits
	
	CRSFactory crsFactory = null;
	CoordinateReferenceSystem WGS84 = null;
	CoordinateReferenceSystem tgtCRS = null;
	CoordinateTransform trans = null;

	public UTMProjection( int iZone ) throws InvalidUTMZoneException {
		crsFactory = new CRSFactory();
		WGS84 = crsFactory.createFromParameters("WGS84", WGS84_PARAM);
		String sEPSG = zoneToUTM_EPSG( iZone );
		tgtCRS = createCRS( sEPSG );
		trans = ctFactory.createTransform(WGS84, tgtCRS);
	}
	
	public void setUTMZone( int iZone ) throws InvalidUTMZoneException {
		String sEPSG = zoneToUTM_EPSG( iZone );
		tgtCRS = createCRS( sEPSG );
		trans = ctFactory.createTransform(WGS84, tgtCRS);		
	}
	
	public double[] project( double dLatDegrees, double dLongDegrees ) throws InvalidCoordinateException {
		if( dLatDegrees < -90.0 || dLatDegrees > 90.0 ) {
			throw new InvalidCoordinateException( dLatDegrees, "Latitude" );
		}
		if( dLongDegrees < -180.0 || dLongDegrees > 180.0 ) {
			throw new InvalidCoordinateException( dLatDegrees, "Longitude" );
		}
		double[] aCoords = new double[2];
		double x, y;
		// TODO Implement Proj4j calls as needed
		ProjCoordinate p = new ProjCoordinate();
		p.x = dLongDegrees;
		p.y = dLatDegrees;

  		ProjCoordinate pout = new ProjCoordinate();
  		trans.transform(p, pout);
  		x = pout.x;
  		y = pout.y;
		aCoords[0] = x;
		aCoords[1] = y;

		return aCoords;
	}
	
	private String zoneToUTM_EPSG( int iZone ) throws InvalidUTMZoneException {
		if( iZone < 1 || iZone > 22 ) {
			throw new InvalidUTMZoneException( iZone, "iZone" );
		}
		else {
			String sZone = Integer.toString(iZone);
			if( iZone < 10 ) {
				sZone = UTM_NORTH_EPSG + "0" + sZone;
			}
			else {
				sZone = UTM_NORTH_EPSG + sZone;
			}
			return sZone;
		}
	}

	private CoordinateReferenceSystem createCRS(String crsSpec)
	{
		CoordinateReferenceSystem crs = null;
		// test if name is a PROJ4 spec
		if (crsSpec.indexOf("+") >= 0 || crsSpec.indexOf("=") >= 0) {
			crs = crsFactory.createFromParameters("Anon", crsSpec);
		} 
		else {
			crs = crsFactory.createFromName(crsSpec);
		}
		return crs;
	}

}
