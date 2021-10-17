package hu.awm.srtm.tools.cansee;

import hu.awm.srtm.data.hgt.Tile;
import hu.awm.srtm.data.hgt.TileMap;
import hu.awm.srtm.map.contour.ContourCalculator;

public class CanSeeCalculator {

    private Vector3D from;
    private Vector3D to;
    private TileMap tileMap;

    public CanSeeCalculator() {

    }

    public void setCoordinates(Double fromLat, Double fromLon, Double fromHeight, Double toLat, Double toLon, Double toHeight) {
        this.from = new Vector3D(fromLat, fromLon, fromHeight);
        this.to = new Vector3D(toLat, toLon, toHeight);
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public Sight calculateSight() {
        Vector3D loseSightCoordinate = null;
        Vector3D directionVector = to.subtract(from);
        double stepCount = Math.max(Math.abs(to.getLat() - from.getLat()) * Tile.RESOLUTION,
                Math.abs(to.getLon() - from.getLon()) * Tile.RESOLUTION) * 2;
        Vector3D stepVector = directionVector.getInstanceResizedToLength(directionVector.length() / stepCount);

        for (Vector3D i = from.newInstance(); i.lengthTo(to) > stepVector.length(); i = i.add(stepVector)) {
            if (!isPointVisible(i)) {
                loseSightCoordinate = i.newInstance();
                break;
            }
        }
        if (loseSightCoordinate == null) {
            if (!isPointVisible(to)) {
                loseSightCoordinate = to.newInstance();
            }
        }
        return new Sight(from, to, loseSightCoordinate);
    }

    public boolean calculateBoolean() {
        Sight sight = calculateSight();
        return !sight.hasBlocker();
    }

    private boolean isPointVisible(Vector3D pointToCheck) {
        int tileLat = pointToCheck.getLat().intValue();
        int tileLon = pointToCheck.getLon().intValue();
        Tile tile = tileMap.getByLatLon(tileLat, tileLon);
        double height = tile.elevationByExactCoordinates(pointToCheck.getLat(), pointToCheck.getLon());
        return height <= pointToCheck.getHeight();
    }
}
