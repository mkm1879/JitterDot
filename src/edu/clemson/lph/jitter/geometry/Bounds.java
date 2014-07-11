package edu.clemson.lph.jitter.geometry;
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
