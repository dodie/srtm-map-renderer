package hu.awm.srtm.tools.cansee;

public class Sight {
    private Vector3D startCoordinate;
    private Vector3D endCoordinate;
    private Vector3D looseSightCoordinate;

    public Sight() {
    }

    public Sight(Vector3D startCoordinate, Vector3D endCoordinate, Vector3D looseSightCoordinate) {
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.looseSightCoordinate = looseSightCoordinate;
    }

    public Vector3D getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(Vector3D startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public Vector3D getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(Vector3D endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    public Vector3D getLooseSightCoordinate() {
        return looseSightCoordinate;
    }

    public void setLooseSightCoordinate(Vector3D looseSightCoordinate) {
        this.looseSightCoordinate = looseSightCoordinate;
    }

    public boolean hasBlocker() {
        return this.looseSightCoordinate != null;
    }
}
