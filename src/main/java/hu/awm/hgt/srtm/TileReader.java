package hu.awm.hgt.srtm;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class TileReader {
    
    private static final String FILE_EXT = ".hgt";
    
    private final String baseDir;
    
    public TileReader(String baseDir) {
	this.baseDir = baseDir;
    }

    public Tile readHgtFile(final int lat, final int lon) {
	String key = "N" + lat + "E0" + lon;
	// https://stackoverflow.com/questions/8194447/nasa-binary-files-in-java
	try (FileInputStream fis = new FileInputStream(baseDir + key + FILE_EXT); FileChannel fc = fis.getChannel()) {
	    ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
	    while (bb.remaining() > 0) {
		fc.read(bb);
	    }

	    bb.flip();
	    return new Tile(lat, lon, bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer());
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }
    
    public Tile[] readHgtFiles(int fromLat, int fromLon, int toLat, int toLon) {
	List<Tile> tiles = new ArrayList<>();
	for (int lat = fromLat; toLat <= lat; lat--) {
	    for (int lon = fromLon; lon <= toLon; lon++) {
		tiles.add(readHgtFile(lat, lon));
	    }
	}
	
	return tiles.toArray(new Tile[tiles.size()]);
    }

}
