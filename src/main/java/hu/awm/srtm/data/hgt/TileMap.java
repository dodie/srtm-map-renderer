package hu.awm.srtm.data.hgt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A rectangle consisting of multiple {@link Tile} objects to model larger areas.
 * E.g. the Tiles from N44E016 to N48E022 encode Hungary's elevation data.
 *
 * +---------+--+--+--+--+--+---------+
 * |         |  |  |  |  |  | N48E022 |
 * +---------+--+--+--+--+--+---------+
 * |         |  |  |  |  |  |         |
 * +---------+--+--+--+--+--+---------+
 * |         |  |  |  |  |  |         |
 * +---------+--+--+--+--+--+---------+
 * |         |  |  |  |  |  |         |
 * +---------+--+--+--+--+--+---------+
 * | N44E016 |  |  |  |  |  |         |
 * +---------+--+--+--+--+--+---------+
 *
 */
public class TileMap {

	private final List<List<Tile>> grid = new ArrayList<>();

	private final Map<Integer, Map<Integer, Tile>> tileByLatLon = new HashMap<>();

	TileMap(Tile... tiles) {
		for (Tile tile : tiles) {
			add(tile);
		}
	}

	private void add(Tile tile) {
		insertIntoGrid(tile);
		insertIntoCache(tile);
	}

	private void insertIntoCache(Tile tile) {
		tileByLatLon.putIfAbsent(tile.lat(), new HashMap<>());
		tileByLatLon.get(tile.lat()).put(tile.lon(), tile);
	}

	private void insertIntoGrid(Tile tile) {
		for (List<Tile> row : grid) {
			if (row.get(0).lat() == tile.lat()) {
				row.add(tile);
				return;
			}
		}
		List<Tile> newList = new ArrayList<>();
		newList.add(tile);
		grid.add(0, newList);
	}

	public Tile get(int y, int x) {
		return grid.get(y).get(x);
	}

	public Tile getByLatLon(int lat, int lon) {
		if (!tileByLatLon.containsKey(lat) || !tileByLatLon.get(lat).containsKey(lon)) {
			add(new Tile.EmptyTile(lat, lon, null));
		}
		return tileByLatLon.get(lat).get(lon);
	}

	public int getSizeX() {
		return grid.get(0).size();
	}

	public int getSizeY() {
		return grid.size();
	}

}
