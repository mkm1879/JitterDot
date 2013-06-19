package edu.clemson.lph.jitter.geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class StateBounds {
	private static HashMap<String, Coords> map;
	private String sState = null;
	private ArrayList<String> aStates = null;
	private Coords stateCoords = null;
	
	public StateBounds() {
	}
	
	public StateBounds( String sState ) {
		setState( sState );
	}
	
	public StateBounds( ArrayList<String> aStates ) {
		setStates( aStates );
	}
	
	public static String[] getStateList() {
		ArrayList<String> lStates = new ArrayList<String>();
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
		addState("Alabama",-87.50,-83.13,35.00,30.25);
		addState("Alaska",-172.50,-130.00,71.50,51.25);
		addState("Arizona",-113.13,-109.00,37.00,31.33);
		addState("Arkansas",-93.38,-88.38,36.50,33.00);
		addState("California",-123.58,-113.88,42.00,32.53);
		addState("Colorado",-108.88,-102.00,41.00,37.00);
		addState("Connecticut",-72.25,-70.25,42.05,41.00);
		addState("Delaware",-74.20,-75.00,39.85,38.45);
		addState("District of Columbia",-76.88,-75.13,39.00,38.87);
		addState("Florida",-86.38,-80.00,31.00,24.50);
		addState("Georgia",-84.38,-79.25,35.00,30.35);
		addState("Hawaii",-159.75,-153.25,22.23,18.87);
		addState("Idaho",-116.75,-111.00,49.00,42.00);
		addState("Illinois",-90.50,-86.50,42.50,37.00);
		addState("Indiana",-87.88,-83.25,41.75,37.87);
		addState("Iowa",-95.38,-89.88,43.50,40.37);
		addState("Kansas",-101.50,-93.42,40.00,37.00);
		addState("Kentucky",-88.42,-80.05,39.15,36.62);
		addState("Louisiana",-93.95,-87.18,33.02,28.92);
		addState("Maine",-70.87,-65.12,47.47,42.97);
		addState("Maryland",-78.50,-75.00,39.75,37.87);
		addState("Massachusetts",-72.48,-68.08,42.87,41.22);
		addState("Michigan",-89.50,-81.63,48.28,41.70);
		addState("Minnesota",-96.75,-88.50,49.38,43.50);
		addState("Mississippi",-90.37,-87.88,35.00,30.00);
		addState("Missouri",-94.22,-88.90,40.62,36.00);
		addState("Montana",-115.95,-103.97,49.00,44.37);
		addState("Nebraska",-103.95,-94.68,43.00,40.00);
		addState("Nevada",-120.00,-113.95,42.00,35.00);
		addState("New Hampshire",-71.43,-69.42,45.35,42.70);
		addState("New Jersey",-74.45,-72.13,41.37,38.92);
		addState("New Mexico",-108.95,-103.00,37.00,31.33);
		addState("New York",-78.23,-70.13,45.02,40.50);
		addState("North Carolina",-83.67,-74.58,36.60,33.85);
		addState("North Dakota",-103.95,-95.45,49.00,45.93);
		addState("Ohio",-83.18,-79.48,42.00,38.40);
		addState("Oklahoma",-103.00,-93.57,37.00,33.62);
		addState("Oregon",-123.42,-115.55,46.27,42.00);
		addState("Pennsylvania",-79.48,-73.32,42.27,39.72);
		addState("Puerto Rico",-66.05,-64.78,18.53,17.92);
		addState("Rhode Island",-70.08,-70.88,42.02,41.13);
		addState("South Carolina",-82.63,-77.48,35.22,32.00);
		addState("South Dakota",-103.95,-95.57,45.93,42.48);
		addState("Tennessee",-89.68,-80.37,36.68,34.97);
		addState("Texas",-104.35,-92.50,36.50,25.83);
		addState("Utah",-113.95,-109.00,42.00,37.00);
		addState("Vermont",-72.40,-70.53,45.00,42.72);
		addState("U. S. Virgin Islands",-63.20,-63.45,18.42,17.67);
		addState("Virginia",-82.32,-74.75,39.47,36.53);
		addState("Washington",-123.23,-115.08,49.00,45.53);
		addState("West Virginia",-81.35,-76.27,40.63,37.20);
		addState("Wisconsin",-91.10,-85.25,47.12,42.50);
		addState("Wyoming",-110.90,-104.00,45.00,41.00);		
	}
	
	public void setState( String sState ) {
		this.sState = sState;
		this.stateCoords = map.get(sState);
	}
	
	public void setStates( ArrayList<String> aStates ) {
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
	
	public Double getMinLat() {
		if( stateCoords == null )
			return null;
		return stateCoords.minLat;
	}
	
	public Double getMaxLat() {
		if( stateCoords == null )
			return null;
		return stateCoords.maxLat;
	}
	public Double getMinLong() {
		if( stateCoords == null )
			return null;
		return stateCoords.minLong;
	}
	
	public Double getMaxLong() {
		if( stateCoords == null )
			return null;
		return stateCoords.maxLong;
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
