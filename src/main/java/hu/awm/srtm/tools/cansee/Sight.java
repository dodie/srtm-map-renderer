package hu.awm.srtm.tools.cansee;

import org.apache.commons.math3.geometry.euclidean.threed.SphericalCoordinates;

public class Sight {
    private SphericalCoordinates startCoordinate;
    private SphericalCoordinates endCoordinate;
    private SphericalCoordinates looseSightCoordinate;

    public Sight() {
    }

    public Sight(SphericalCoordinates startCoordinate, SphericalCoordinates endCoordinate, SphericalCoordinates looseSightCoordinate) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.looseSightCoordinate = looseSightCoordinate;
    }

    public SphericalCoordinates getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(SphericalCoordinates startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public SphericalCoordinates getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(SphericalCoordinates endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    public SphericalCoordinates getLooseSightCoordinate() {
        return looseSightCoordinate;
    }

    public void setLooseSightCoordinate(SphericalCoordinates looseSightCoordinate) {
        this.looseSightCoordinate = looseSightCoordinate;
    }

    public boolean hasBlocker() {
        return this.looseSightCoordinate != null;
    }
}
