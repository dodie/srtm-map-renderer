package hu.awm.hgt.srtm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileMap {

    List<List<Tile>> grid = new ArrayList<>();
    Map<Integer, Map<Integer, Tile>> tileByLatLon = new HashMap<>();

    public TileMap(Tile... tiles) {
	for (Tile tile : tiles) {
	    insertIntoGrid(tile);
	    insertIntoCache(tile);
	}
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
	grid.add(newList);
	return;
    }

    public List<List<Tile>> grid() {
	return grid;
    }

    public double[] getHeightLine(Position coordinates, double deg, int size) {
	double[] heightLine = new double[size];

	for (int i = 0; i < size; i++) {
	    Position offset = Position.get(coordinates, deg, i);

	    heightLine[i] = tileByLatLon.get(offset.gridLat).get(offset.gridLon).elevation(offset.row, offset.col);
	}

	return heightLine;
    }

}
