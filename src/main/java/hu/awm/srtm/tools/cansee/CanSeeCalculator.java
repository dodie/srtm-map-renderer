package hu.awm.srtm.tools.cansee;

import hu.awm.srtm.data.hgt.Tile;
import hu.awm.srtm.data.hgt.TileMap;
import org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class CanSeeCalculator {

    public static final int EXAMINATION_NUMBER_IN_ARCSEC = 2;
    private SphericalCoordinates from;
    private SphericalCoordinates to;
    private TileMap tileMap;

    public CanSeeCalculator() {

    }

    public void setCoordinates(Double fromLat, Double fromLon, Double fromHeight, Double toLat, Double toLon, Double toHeight) {
        double fromGeoidOffset = WGS84Geoid.getGeoidRadiusAt(fromLat);
        this.from = new SphericalCoordinates(fromHeight + fromGeoidOffset, Math.toRadians(fromLon), Math.toRadians(90.0 - fromLat));
        double toGeoidOffset = WGS84Geoid.getGeoidRadiusAt(toLat);
        this.to = new SphericalCoordinates(toHeight + toGeoidOffset, Math.toRadians(toLon), Math.toRadians(90.0 - toLat));
    }

    public void setTileMap(TileMap tileMap) {
        this.tileMap = tileMap;
    }

    public Sight calculateSight() {
        SphericalCoordinates loseSightCoordinate = null;
        Vector3D directionVector = this.to.getCartesian().subtract(this.from.getCartesian());
        System.out.println("Direction vector length: " + directionVector.distance(Vector3D.ZERO));
        double stepCount = Math.max(Math.abs(to.getTheta() - from.getTheta()),
                Math.abs(to.getPhi() - from.getPhi())) * Tile.RESOLUTION * EXAMINATION_NUMBER_IN_ARCSEC;
        Vector3D stepVector = directionVector.scalarMultiply(1 / stepCount);
        double stepVectorLength = stepVector.distance(Vector3D.ZERO);

        for (Vector3D i = new Vector3D(1, from.getCartesian());
                to.getCartesian().distance(i) > stepVectorLength;
                i = i.add(stepVector)) {
            if (!isPointVisible(new SphericalCoordinates(i))) {
                loseSightCoordinate = new SphericalCoordinates(new Vector3D(1, i));
                break;
            }
        }
        if (loseSightCoordinate == null) {
            if (!isPointVisible(to)) {
                loseSightCoordinate = new SphericalCoordinates(to.getCartesian());
            }
        }
        if (loseSightCoordinate != null) {
            double loseSightLat = 90.0 - Math.toDegrees(loseSightCoordinate.getPhi());
            double loseSightLon = Math.toDegrees(loseSightCoordinate.getTheta());
            System.out.println("Sight is lost at: latitude: " + loseSightLat + " longitude: " + loseSightLon
                    + " height: " + (loseSightCoordinate.getR() - WGS84Geoid.getGeoidRadiusAt(loseSightLat)));
        }
        return new Sight(from, to, loseSightCoordinate);
    }

    public boolean calculateBoolean() {
        Sight sight = calculateSight();
        return !sight.hasBlocker();
    }

    private boolean isPointVisible(SphericalCoordinates pointToCheck) {
        double lat = 90 - Math.toDegrees(pointToCheck.getPhi());
        double lon = Math.toDegrees(pointToCheck.getTheta());
        int tileLat = (int)lat;
        int tileLon = (int)lon;
        Tile tile = tileMap.getByLatLon(tileLat, tileLon);
        double height = tile.elevationByExactCoordinates(lat, lon);
        return height + WGS84Geoid.getGeoidRadiusAt(lat) <= pointToCheck.getR();
    }
}
