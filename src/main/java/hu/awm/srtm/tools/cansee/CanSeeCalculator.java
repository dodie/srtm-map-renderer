package hu.awm.srtm.tools.cansee;

import hu.awm.srtm.data.hgt.TileMap;

public class CanSeeCalculator {

    private Xyz from;
    private Xyz to;
    private TileMap tileMap;

    public CanSeeCalculator() {

    }

    public void setCoordinates(Double fromLat, Double fromLon, Double fromHeight, Double toLat, Double toLon, Double toHeight) {
        this.from = new Xyz(fromLat, fromLon, fromHeight);
        this.to = new Xyz(toLat, toLon, toHeight);
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public Sight calculateSight() {
        // TODO: Replace this with the correct math.
        return new Sight(null, null, null);
    }

    public boolean calculateBoolean() {
        Sight sight = calculateSight();
        return !sight.hasBlocker();
    }
}
