package edu.clemson.lph.jitter.geometry;

public class Bounds {
	protected static final double TOLERANCE = 0.5;
	protected Double minLong;
	protected Double minLat;
	protected Double maxLong;
	protected Double maxLat;

	public Bounds() {
		
	}
	
	public Bounds(Double minLat, Double maxLat, Double minLong, Double maxLong ) {
		this.minLong = minLong;
		this.minLat = minLat;
		this.maxLong = maxLong;
		this.maxLat = maxLat;
	}
	
	// Include some wiggle room
	public Double getMinLat() {
		return minLat - TOLERANCE;
	}
	
	public Double getMaxLat() {
		return maxLat + TOLERANCE;
	}
	public Double getMinLong() {
		return minLong - TOLERANCE;
	}
	
	public Double getMaxLong() {
		return maxLong + TOLERANCE;
	}

}
