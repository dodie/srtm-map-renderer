package hu.awm.srtm.tools.cansee;

public class WGS84Geoid {
    private static final double R_EQUATOR = 6378137;
    private static final double R_POLAR = 6356752.314245;
    private static final double INVERSE_FLATTENING = 298.257223563;

    private static final double R_EQUATOR_SQUARED = Math.pow(R_EQUATOR, 2);
    private static final double R_POLAR_SQUARED = Math.pow(R_POLAR, 2);
    private static final double R_EQUATOR_TIMES_R_POLAR = R_EQUATOR * R_POLAR;

    public static double getGeoidRadiusAt(double latitudeDegree) {
        double latitudeRadian = Math.toRadians(latitudeDegree);
        return R_EQUATOR_TIMES_R_POLAR /
                Math.sqrt(
                        R_EQUATOR_SQUARED * Math.pow(Math.sin(latitudeRadian), 2)
                        + R_POLAR_SQUARED * Math.pow(Math.cos(latitudeRadian), 2));
    }

    public static void testData() {
        for (double lat = 0.0; lat <= 90.0; lat += 1.0) {
            System.out.println("Radius at " + lat + " latitude: " + getGeoidRadiusAt(lat));
        }
    }
}
