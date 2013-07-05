package edu.clemson.lph.jitter.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StateBounds {
	private static final double TOLERANCE = 0.5;
	private static HashMap<String, Coords> map;
	private String sState = null;
	private List<String> aStates = null;
	private Coords stateCoords = null;
	
	public StateBounds() {
	}
	
	public StateBounds( String sState ) {
		setState( sState );
	}
	
	public StateBounds( List<String> aStates ) {
		setStates( aStates );
	}
	
	public static String[] getStateList() {
		List<String> lStates = new ArrayList<String>();
		lStates.add(" ");
		for( String state : map.keySet() )
			lStates.add(state);
		Collections.sort(lStates);
		String[] aStates = new String[lStates.size()];
		aStates = lStates.toArray(aStates);
		return aStates;
	}
	
	public static Set<String> getStates() {
		if( map == null ) return null;
		return map.keySet();
	}
	
	static {
		map = new HashMap<String, Coords>();
		addState("Alabama",-88.47,-84.89,35.02,30.23);
		addState("Alaska",-178.22,-129.99,71.41,51.58);
		addState("Arizona",-114.82,-109.05,37.00,31.34);
		addState("Arkansas",-94.62,-89.65,36.49,33.01);
		addState("California",-124.39,-114.13,42.00,32.54);
		addState("Colorado",-109.06,-102.04,41.00,36.99);
		addState("Connecticut",-73.73,-71.79,42.05,41.00);
		addState("Delaware",-75.79,-75.05,39.84,38.45);
		addState("District of Columbia",-77.12,-76.91,38.99,38.79);
		addState("Florida",-87.63,-80.05,31.00,24.96);
		addState("Georgia",-85.61,-80.89,35.00,30.36);
		addState("Hawaii",-160.24,-154.79,22.23,18.92);
		addState("Idaho",-117.24,-111.05,49.00,41.99);
		addState("Illinois",-91.52,-87.51,42.51,36.99);
		addState("Indiana",-88.10,-84.79,41.77,37.78);
		addState("Iowa",-96.64,-90.14,43.50,40.37);
		addState("Kansas",-102.05,-94.60,40.00,36.99);
		addState("Kentucky",-89.57,-81.96,39.14,36.50);
		addState("Louisiana",-94.04,-89.02,33.02,28.94);
		addState("Maine",-71.09,-66.97,47.45,43.09);
		addState("Maryland",-79.49,-75.05,39.73,37.97);
		addState("Massachusetts",-73.50,-69.92,42.89,41.24);
		addState("Michigan",-90.41,-82.42,48.17,41.70);
		addState("Minnesota",-97.23,-89.53,49.37,43.50);
		addState("Mississippi",-91.64,-88.09,35.01,30.19);
		addState("Missouri",-95.77,-89.11,40.61,35.99);
		addState("Montana",-116.06,-104.04,49.00,44.35);
		addState("Nebraska",-104.06,-95.31,43.00,39.99);
		addState("Nevada",-120.00,-114.04,42.00,35.00);
		addState("New Hampshire",-72.55,-70.73,45.30,42.70);
		addState("New Jersey",-75.57,-73.90,41.35,38.96);
		addState("New Mexico",-109.05,-103.00,37.00,31.34);
		addState("New York",-79.76,-71.87,45.01,40.51);
		addState("North Carolina",-84.32,-75.46,36.59,33.88);
		addState("North Dakota",-104.06,-96.55,49.00,45.93);
		addState("Ohio",-84.81,-80.52,41.99,38.40);
		addState("Oklahoma",-103.00,-94.43,37.00,33.62);
		addState("Oregon",-124.56,-116.47,46.24,41.99);
		addState("Pennsylvania",-80.53,-74.70,42.27,39.72);
		addState("Rhode Island",-71.87,-71.12,42.01,41.32);
		addState("South Carolina",-83.35,-78.58,35.21,32.07);
		addState("South Dakota",-104.06,-96.44,45.94,42.49);
		addState("Tennessee",-90.31,-81.65,36.68,34.99);
		addState("Texas",-106.65,-93.51,36.49,25.85);
		addState("Utah",-114.05,-109.04,42.00,36.99);
		addState("Vermont",-73.44,-71.51,45.01,42.73);
		addState("Virginia",-83.68,-75.24,39.46,36.54);
		addState("Washington",-124.73,-116.92,49.00,45.54);
		addState("West Virginia",-82.65,-77.73,40.64,37.20);
		addState("Wisconsin",-92.89,-86.97,46.95,42.49);
		addState("Wyoming",-111.05,-104.05,45.00,40.99);
	

//		addState("Alabama",-87.50,-83.13,35.00,30.25);
//		addState("Alaska",-172.50,-130.00,71.50,51.25);
//		addState("Arizona",-113.13,-109.00,37.00,31.33);
//		addState("Arkansas",-93.38,-88.38,36.50,33.00);
//		addState("California",-123.58,-113.88,42.00,32.53);
//		addState("Colorado",-108.88,-102.00,41.00,37.00);
//		addState("Connecticut",-72.25,-70.25,42.05,41.00);
//		addState("Delaware",-74.20,-75.00,39.85,38.45);
//		addState("District of Columbia",-76.88,-75.13,39.00,38.87);
//		addState("Florida",-86.38,-80.00,31.00,24.50);
//		addState("Georgia",-84.38,-79.25,35.00,30.35);
//		addState("Hawaii",-159.75,-153.25,22.23,18.87);
//		addState("Idaho",-116.75,-111.00,49.00,42.00);
//		addState("Illinois",-90.50,-86.50,42.50,37.00);
//		addState("Indiana",-87.88,-83.25,41.75,37.87);
//		addState("Iowa",-95.38,-89.88,43.50,40.37);
//		addState("Kansas",-101.50,-93.42,40.00,37.00);
//		addState("Kentucky",-88.42,-80.05,39.15,36.62);
//		addState("Louisiana",-93.95,-87.18,33.02,28.92);
//		addState("Maine",-70.87,-65.12,47.47,42.97);
//		addState("Maryland",-78.50,-75.00,39.75,37.87);
//		addState("Massachusetts",-72.48,-68.08,42.87,41.22);
//		addState("Michigan",-89.50,-81.63,48.28,41.70);
//		addState("Minnesota",-96.75,-88.50,49.38,43.50);
//		addState("Mississippi",-90.37,-87.88,35.00,30.00);
//		addState("Missouri",-94.22,-88.90,40.62,36.00);
//		addState("Montana",-115.95,-103.97,49.00,44.37);
//		addState("Nebraska",-103.95,-94.68,43.00,40.00);
//		addState("Nevada",-120.00,-113.95,42.00,35.00);
//		addState("New Hampshire",-71.43,-69.42,45.35,42.70);
//		addState("New Jersey",-74.45,-72.13,41.37,38.92);
//		addState("New Mexico",-108.95,-103.00,37.00,31.33);
//		addState("New York",-78.23,-70.13,45.02,40.50);
//		addState("North Carolina",-83.67,-74.58,36.60,33.85);
//		addState("North Dakota",-103.95,-95.45,49.00,45.93);
//		addState("Ohio",-83.18,-79.48,42.00,38.40);
//		addState("Oklahoma",-103.00,-93.57,37.00,33.62);
//		addState("Oregon",-123.42,-115.55,46.27,42.00);
//		addState("Pennsylvania",-79.48,-73.32,42.27,39.72);
//		addState("Puerto Rico",-66.05,-64.78,18.53,17.92);
//		addState("Rhode Island",-70.08,-70.88,42.02,41.13);
//		addState("South Carolina",-83.63,-77.48,35.22,32.00);
//		addState("South Dakota",-103.95,-95.57,45.93,42.48);
//		addState("Tennessee",-89.68,-80.37,36.68,34.97);
//		addState("Texas",-104.35,-92.50,36.50,25.83);
//		addState("Utah",-113.95,-109.00,42.00,37.00);
//		addState("Vermont",-72.40,-70.53,45.00,42.72);
//		addState("U. S. Virgin Islands",-63.20,-63.45,18.42,17.67);
//		addState("Virginia",-82.32,-74.75,39.47,36.53);
//		addState("Washington",-123.23,-115.08,49.00,45.53);
//		addState("West Virginia",-81.35,-76.27,40.63,37.20);
//		addState("Wisconsin",-91.10,-85.25,47.12,42.50);
//		addState("Wyoming",-110.90,-104.00,45.00,41.00);		
	}
	
	public void setState( String sState ) {
		this.sState = sState;
		this.stateCoords = map.get(sState);
	}
	
	public void setStates( List<String> aStates ) {
		this.aStates = aStates;
		Coords overallCoords = null;
		for( String sState : aStates ) {
			Coords cNext = map.get(sState);
			if( overallCoords == null ) {
				overallCoords = cNext;
			}
			else {
				if( cNext.minLat < overallCoords.minLat )
					overallCoords.minLat = cNext.minLat;
				if( cNext.maxLat > overallCoords.maxLat )
					overallCoords.maxLat = cNext.maxLat;
				if( cNext.minLong < overallCoords.minLong )
					overallCoords.minLong = cNext.minLong;
				if( cNext.maxLong > overallCoords.maxLong )
					overallCoords.maxLong = cNext.maxLong;
			}
		}
		this.stateCoords = overallCoords;
	}
	
	// Include some wiggle room
	public Double getMinLat() {
		if( stateCoords == null )
			return null;
		return stateCoords.minLat - TOLERANCE;
	}
	
	public Double getMaxLat() {
		if( stateCoords == null )
			return null;
		return stateCoords.maxLat + TOLERANCE;
	}
	public Double getMinLong() {
		if( stateCoords == null )
			return null;
		// Fudge Factor due to apparent error in above data.
		return stateCoords.minLong - TOLERANCE;
	}
	
	public Double getMaxLong() {
		if( stateCoords == null )
			return null;
		// Don't know why the shift in these numbers.  Fix this if I recalculate bounding boxes.
		return stateCoords.maxLong + TOLERANCE;
	}

	private static void addState( String sState, Double dMinLong, Double dMaxLong, Double dMaxLat, Double dMinLat ) {
		Coords coords = new Coords( dMinLat,dMaxLat, dMinLong, dMaxLong );
		map.put(sState, coords);
	}
	
	static class Coords {
		public Double minLat;
		public Double maxLat;
		public Double minLong;
		public Double maxLong;
		
		public Coords( Double dMinLat, Double dMaxLat, Double dMinLong, Double dMaxLong) {
			minLat = dMinLat;
			maxLat = dMaxLat;
			minLong = dMinLong;
			maxLong = dMaxLong;
		}
	}
}
