package hu.awm.srtm.tools.cansee;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.util.EGM96;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WGS84Geoid {
    private static final double R_EQUATOR = 6378137;
    private static final double R_POLAR = 6356752.314245;
    private static final double INVERSE_FLATTENING = 298.257223563;

    private static final double R_EQUATOR_SQUARED = Math.pow(R_EQUATOR, 2);
    private static final double R_POLAR_SQUARED = Math.pow(R_POLAR, 2);
    private static final double R_EQUATOR_TIMES_R_POLAR = R_EQUATOR * R_POLAR;

    private static final String EGM96_OFFSET_PATH = "EGM96.dat";

    private String basePath;
    private EGM96 geoidOffset;
    private boolean logging;

    public WGS84Geoid() {
        loadEGM96Module();
    }

    public void loadEGM96Module() {
        copyEGM96Dat();
        try {
            geoidOffset = new EGM96(Path.of(basePath, EGM96_OFFSET_PATH).toString());
        }
        catch (IOException ex) {
            throw new RuntimeException("EGM96.dat file could not be found: ", ex);
        }
    }

    private void copyEGM96Dat() {
        try {
            basePath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            if (basePath.endsWith("jar")) {
                basePath = basePath.substring(0, basePath.lastIndexOf(File.separator));
            }
            if (Files.exists(Path.of(basePath, EGM96_OFFSET_PATH))) {
                return;
            }
            Files.copy(getClass().getClassLoader().getResourceAsStream("config/EGM96.dat"), Path.of(basePath, EGM96_OFFSET_PATH));
        }
        catch (URISyntaxException ex) {
            throw new RuntimeException("Problem querying basepath.", ex);
        }
        catch (IOException ex) {
            throw new RuntimeException("Problem during copying file.", ex);
        }
    }

    public double getEllipsoidRadiusAt(double latitudeDegree) {
        double latitudeRadian = Math.toRadians(latitudeDegree);
        return R_EQUATOR_TIMES_R_POLAR /
                Math.sqrt(
                        R_EQUATOR_SQUARED * Math.pow(Math.sin(latitudeRadian), 2)
                        + R_POLAR_SQUARED * Math.pow(Math.cos(latitudeRadian), 2));
    }

    public double getGeoidRadiusAt(double latitudeDegree, double longitudeDegree) {
        double ellipsoidRadius = getEllipsoidRadiusAt(latitudeDegree);
        double offset = geoidOffset.getOffset(
                Angle.fromDegreesLatitude(latitudeDegree),
                Angle.fromDegreesLongitude(longitudeDegree));
        double geoidRadius = ellipsoidRadius + offset;
        if (logging) {
            System.out.println(String.format("Latitude: %.5f\t\tLongitude: %.5f\t\tEllipsoid radius: %.5f\t\tGeoid offset: %.5f\t\tGeoid radius: %.5f",
                    latitudeDegree, longitudeDegree, ellipsoidRadius, offset, geoidRadius));
        }
        return geoidRadius;
    }

    public void testDataEllipsoid() {
        for (double lat = 0.0; lat <= 90.0; lat += 1.0) {
            System.out.println("Radius at " + lat + " latitude: " + getEllipsoidRadiusAt(lat));
        }
    }

    public void testDataGeoidOffsets() {
        enableLogging();
        double fixedLon = 18.0;
        System.out.println("Longitude: " + fixedLon);
        for (double lat = 45.00000; lat <= 46.00000; lat += 0.0005) {
            getGeoidRadiusAt(lat, fixedLon);
        }
        disableLogging();
    }

    public void enableLogging() {
        this.logging = true;
    }

    public void disableLogging() {
        this.logging = false;
    }
}
