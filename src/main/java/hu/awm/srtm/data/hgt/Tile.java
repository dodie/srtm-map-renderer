package hu.awm.srtm.data.hgt;

import java.nio.ShortBuffer;

/**
 * Height data for a one by one degree latitude and longitude tile in high resolution (E.g. N44E016),
 * read from a HGT file.
 *
 * The tile is divided to 3600 rows and 3600 columns. The real-world size of such pixel depends on its latitude and longitude.
 * - Degrees of latitude are parallel, so the distance between each degree is mostly constant, 1 degree ~= 111km,
 *   so the height of each pixel is roughly 30.8 m.
 * - Longitude varies based on the actual location. It is also ~= 111km at the equador, but shrinks to 0 as it reaches the poles.
 *
 * @see https://lpdaac.usgs.gov/documents/179/SRTM_User_Guide_V3.pdf
 */
public class Tile {

	public static final int RESOLUTION = 3601;

	private static final int MAGIC_DATA_VOID = -32768;

	private final ShortBuffer data;

	private final int lon;

	private final int lat;

	Tile(int lat, int lon, ShortBuffer data) {
		this.lat = lat;
		this.lon = lon;
		this.data = data;
	}

	public double elevation(final int row, final int col) {
		if (row < 0 || (RESOLUTION - 1) < row || col < 0 || (RESOLUTION - 1) < col) {
			throw new IllegalArgumentException(createExceptionMessage(row, col));
		}

		int cell = (RESOLUTION * (RESOLUTION - row - 1)) + col;

		if (data.limit() <= cell) {
			throw new IllegalArgumentException(createExceptionMessage(row, col));
		}

		short elevation;
		try {
			elevation = data.get(cell);
		}
		catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(createExceptionMessage(row, col), e);
		}
		if (elevation == MAGIC_DATA_VOID) {
			return Double.NaN;
		}
		else {
			return elevation;
		}
	}

	private static String createExceptionMessage(int row, int col) {
		return "Query: " + row + "/" + col;
	}

	public int lat() {
		return lat;
	}

	public int lon() {
		return lon;
	}

	static class EmptyTile extends Tile {

		EmptyTile(int lat, int lon, ShortBuffer data) {
			super(lat, lon, data);

			System.out.println("Failed to access tile at lat/lon " + lat + "/" + lon + ". Using zero elevations in this region.");
		}

		@Override
		public double elevation(int row, int col) {
			return 0;
		}
	}

}
