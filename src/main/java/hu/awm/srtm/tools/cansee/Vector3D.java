package hu.awm.srtm.tools.cansee;

public class Vector3D {
    private Double lat;
    private Double lon;
    private Double height;

    public Vector3D(Double lat, Double lon, Double height) {
        this.lat = lat;
        this.lon = lon;
        this.height = height;
    }

    public Vector3D newInstance() {
        return new Vector3D(this.lat, this.lon, this.height);
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Double getHeight() {
        return height;
    }

    public Vector3D getInstanceResizedToLength(Double newLength) {
        Double ratio = newLength / length();
        return new Vector3D(lat * ratio, lon * ratio, height * ratio);
    }

    public Double length() {
        return Math.sqrt(Math.pow(lat, 2.0)
                + Math.pow(lon, 2.0)
                + Math.pow(height, 2.0));
    }

    public static Vector3D subtract(Vector3D from, Vector3D what) {
        return from.subtract(what);
    }

    public Vector3D subtract(Vector3D what) {
        return new Vector3D(this.lat - what.getLat(),
                this.lon - what.getLon(),
                this.height - what.getHeight());
    }

    public static Vector3D add(Vector3D to, Vector3D what) {
        return to.add(what);
    }

    public Vector3D add(Vector3D what) {
        return new Vector3D(this.lat + what.getLat(),
                this.lon + what.getLon(),
                this.height + what.getHeight());
    }

    public double lengthTo(Vector3D what) {
        return what.subtract(this).length();
    }
}
