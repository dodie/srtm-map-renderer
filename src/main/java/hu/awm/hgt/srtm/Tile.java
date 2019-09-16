package hu.awm.hgt.srtm;

import java.nio.ShortBuffer;

/**
 * Height data for a one by one degree latitude and longitude tile in high resolution.
 * 
 * @see https://dds.cr.usgs.gov/srtm/version2_1/Documentation/Quickstart.pdf
 */
public class Tile {

    public static final int RESOLUTION = 3601;
    private static final int MAGIC_DATA_VOID = -32768;

    private ShortBuffer data;
    private int lon;
    private int lat;

    public Tile(int lat, int lon, ShortBuffer data) {
	this.lat = lat;
	this.lon = lon;
	this.data = data;
    }

    public double elevation(final int row, final int col) {
	if (row < 0 || 3600 < row || col < 0 || 3600 < col) {
	    throw new IllegalArgumentException("Query: " + row + "/" + col);
	}
	
	int cell = (RESOLUTION * (RESOLUTION - row - 1)) + col;

	if (data.limit() <= cell) {
	    throw new IllegalArgumentException("Query: " + row + "/" + col);
	}

	short elevation;
	try {
	    elevation = data.get(cell);
	} catch (IndexOutOfBoundsException e) {
	    throw new IllegalArgumentException("Query: " + row + "/" + col, e);	    
	}
	if (elevation == MAGIC_DATA_VOID) {
	    return Double.NaN;
	} else {
	    return elevation;
	}
    }
    
    public int lat() {
	return lat;
    }
    
    public int lon() {
	return lon;
    }
    
}
