package hu.awm.srtm.data.hgt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Read HGT files into {@link Tile} objects to form a {@link TileMap}.
 */
public class TileReader {

	private static final String FILE_EXT = ".hgt";

	private final String baseDir;

	public TileReader(String baseDir) {
		this.baseDir = baseDir;
	}

	public TileMap readHgtFiles(int fromLat, int fromLon, int toLat, int toLon) {
		List<Tile> tiles = new ArrayList<>();
		for (int lat = fromLat; lat <= toLat; lat++) {
			for (int lon = fromLon; lon <= toLon; lon++) {
				tiles.add(readHgtFile(lat, lon));
			}
		}

		return new TileMap(tiles.toArray(new Tile[0]));
	}

	private Tile readHgtFile(final int lat, final int lon) {
		// https://stackoverflow.com/questions/8194447/nasa-binary-files-in-java
		try (FileInputStream fis = new FileInputStream(hgtFilePath(lat, lon)); FileChannel fc = fis.getChannel()) {
			ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
			while (bb.remaining() > 0) {
				fc.read(bb);
			}

			bb.flip();
			return new Tile(lat, lon, bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer());
		}
		catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private String hgtFilePath(int lat, int lon) {
		return baseDir + "N" + lat + "E0" + lon + FILE_EXT;
	}

}
