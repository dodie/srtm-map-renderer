package hu.awm.srtm.tools.cansee;

public class Sight {
    private Xyz startCoordinate;
    private Xyz endCoordinate;
    private Xyz looseSightCoordinate;

    public Sight() {
    }

    public Sight(Xyz startCoordinate, Xyz endCoordinate, Xyz looseSightCoordinate) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.looseSightCoordinate = looseSightCoordinate;
    }

    public Xyz getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(Xyz startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public Xyz getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(Xyz endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    public Xyz getLooseSightCoordinate() {
        return looseSightCoordinate;
    }

    public void setLooseSightCoordinate(Xyz looseSightCoordinate) {
        this.looseSightCoordinate = looseSightCoordinate;
    }

    public boolean hasBlocker() {
        return this.looseSightCoordinate != null;
    }
}
