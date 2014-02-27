package edu.clemson.lph.jitter.geometry;

import java.util.HashMap;
import java.util.List;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

import edu.clemson.lph.jitter.files.ConfigFile;

/**
 * This is a very thin wrapper around the Proj4j library to make it consistent with 
 * the other geometry implementations in this program.
 * @author mmarti5
 *
 */

public class UTMProjection {
	private static final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
	private static final String WGS84_PARAM = "+title=long/lat:WGS84 +proj=longlat +datum=WGS84 +units=degrees";
	
	// NOTE! Originally used NAD83 but to support more than North America changed to WGS84.  Change is small enough that 
	//       existing test cases still work.  Also allows support for southern hemisphere if necessary.
	// Proj4js.defs["EPSG:32617"] = "+proj=utm +zone=17 +ellps=WGS84 +datum=WGS84 +units=m +no_defs";
	// See http://spatialreference.org/ref/epsg/326[iZone]/
	private static final String UTM_NORTH_WGS84_EPSG = "EPSG:326"; // Add zone number left padded with 0 to two digits
	private static final String UTM_SOUTH_WGS84_EPSG = "EPSG:327"; // Add zone number left padded with 0 to two digits

	//EPSG Projection 269[iZone] - NAD83 / UTM zone [iZone]N All units in meters.  See http://spatialreference.org/ref/epsg/269[iZone]/
//	private static final String UTM_NORTH_EPSG = "EPSG:269"; // Add zone number left padded with 0 to two digits
	private int iZone;
	private String sHemisphere;
	private static HashMap<Integer, Double> zoneMap;
	
	CRSFactory crsFactory = null;
	CoordinateReferenceSystem WGS84 = null;
	CoordinateReferenceSystem tgtCRS = null;
	CoordinateTransform trans = null;
	CoordinateTransform revtrans = null;
	
	/**
	 * Initialize zoneMap with central meridians.
	 */
	static {
		zoneMap = new HashMap<Integer, Double>();
		zoneMap.put(1, -177.00);
		zoneMap.put(2, -171.00);
		zoneMap.put(3, -165.00);
		zoneMap.put(4, -159.00);
		zoneMap.put(5, -153.00);
		zoneMap.put(6, -147.00);
		zoneMap.put(7, -141.00);
		zoneMap.put(8, -135.00);
		zoneMap.put(9, -129.00);
		zoneMap.put(10, -123.00);
		zoneMap.put(11, -117.00);
		zoneMap.put(12, -111.00);
		zoneMap.put(13, -105.00);
		zoneMap.put(14, -099.00);
		zoneMap.put(15, -093.00);
		zoneMap.put(16, -087.00);
		zoneMap.put(17, -081.00);
		zoneMap.put(18, -075.00);
		zoneMap.put(19, -069.00);
		zoneMap.put(20, -063.00);
		zoneMap.put(21, -057.00);
		zoneMap.put(22, -051.00);		
	}

	public UTMProjection( int iZone, String sHemisphere ) throws InvalidUTMZoneException {
		this.iZone = iZone;
		this.sHemisphere = sHemisphere;
		crsFactory = new CRSFactory();
		WGS84 = crsFactory.createFromParameters("WGS84", WGS84_PARAM);
		String sEPSG = zoneToUTM_EPSG( iZone, sHemisphere );
		tgtCRS = createCRS( sEPSG );
		trans = ctFactory.createTransform(WGS84, tgtCRS);
		revtrans = ctFactory.createTransform(tgtCRS, WGS84);
	}
	
	public void setUTMZone( int iZone, String sHemisphere ) throws InvalidUTMZoneException {
		String sEPSG = zoneToUTM_EPSG( iZone, sHemisphere );
		tgtCRS = createCRS( sEPSG );
		trans = ctFactory.createTransform(WGS84, tgtCRS);		
		revtrans = ctFactory.createTransform(tgtCRS, WGS84);
	}
	
	public double getCentralMeridianDegrees() {
		Double dRet = zoneMap.get(iZone);
		return dRet;
	}
	
	public static String getHemisphere( double dMedianLatitude ) {
		if( dMedianLatitude > 0.0 ) 
			return "N";
		else
			return "S";
	}
	
	public static int getBestZone( double dMedianLongitude ) {
		int iBest = -1;
		double dDelta = 180.00;
		for( int iZone : zoneMap.keySet() ) {
			double dCM = zoneMap.get(iZone);
			double dThisDiff = Math.abs( dCM - dMedianLongitude );
			if( dThisDiff < dDelta ) {
				iBest = iZone;
				dDelta = dThisDiff;
			}
		}
		return iBest;
	}
	
	public Double[] project( Double dLatDegrees, Double dLongDegrees ) throws InvalidCoordinateException {
		if( dLatDegrees < -90.0 || dLatDegrees > 90.0 ) {
			throw new InvalidCoordinateException( dLatDegrees, "Latitude" );
		}
		if( dLongDegrees < -180.0 || dLongDegrees > 180.0 ) {
			throw new InvalidCoordinateException( dLatDegrees, "Longitude" );
		}
		Double[] aCoords = new Double[2];
		Double x, y;
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
	
	public Double[] deProject( Double dNorthing, Double dEasting ) throws InvalidCoordinateException {
		Double[] aCoords = new Double[2];
		Double dLong, dLat;
		
		ProjCoordinate p = new ProjCoordinate();
		p.x = dEasting;
		p.y = dNorthing;

  		ProjCoordinate pout = new ProjCoordinate();
  		revtrans.transform(p, pout);
  		dLong = pout.x;
  		dLat = pout.y;
		aCoords[0] = dLong;
		aCoords[1] = dLat;

		return aCoords;
	}
	
	private String zoneToUTM_EPSG( int iZone, String sHemisphere ) throws InvalidUTMZoneException {
		List<String> aStates = ConfigFile.getStates();
		if( iZone < 1 || ( aStates.size() > 0 && aStates.get(0).trim().length() > 0 && iZone > 22 ) || iZone > 60 ) {
			throw new InvalidUTMZoneException( iZone, "iZone" );
		}
		else {
			String sZone = Integer.toString(iZone);
			if( iZone < 10 ) {
				if( sHemisphere.equals("N") )
					sZone = UTM_NORTH_WGS84_EPSG + "0" + sZone;
				else if( sHemisphere.equals("S") )
					sZone = UTM_SOUTH_WGS84_EPSG + "0" + sZone;
				else 
					throw new InvalidUTMZoneException(iZone, "Unknown hemisphere " + sHemisphere);
			}
			else {
				if( sHemisphere.equals("N") )
					sZone = UTM_NORTH_WGS84_EPSG + sZone;
				else if( sHemisphere.equals("S") )
					sZone = UTM_SOUTH_WGS84_EPSG + sZone;
				else 
					throw new InvalidUTMZoneException(iZone, "Unknown hemisphere " + sHemisphere);
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
