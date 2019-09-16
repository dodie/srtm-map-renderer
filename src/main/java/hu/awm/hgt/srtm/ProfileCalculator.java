package hu.awm.hgt.srtm;

import java.util.ArrayList;
import java.util.List;

public class ProfileCalculator {
    
    final static int STEP_SIZE_IN_M = 30; // TODO: pass in as an argument. In HU it's ~30 when we look north, and ~22 when we look east.
    final static int EYE_HEIGHT_FROM_GROUND_IN_M = 5;
    
    public static List<DistancePoint> getDistances(double[] heightData) {
	double eyeHeight = heightData[0] + EYE_HEIGHT_FROM_GROUND_IN_M;
	List<DistancePoint> distances = new ArrayList<>();

	double topDeg = 0D;
	for (int i = 1; i < heightData.length; i++) {
	    int targetDistance = STEP_SIZE_IN_M * i;
	    double visibilityAngle = calculateVisibilityAngle(eyeHeight, heightData[i], targetDistance);

	    if (topDeg < visibilityAngle) {
		distances.add(new DistancePoint(visibilityAngle, targetDistance));
		topDeg = visibilityAngle;
	    }
	}

	return distances;
    }

    public static List<Contour> getContour(double[] heightData) {
	double eyeHeight = heightData[0] + EYE_HEIGHT_FROM_GROUND_IN_M;
	List<Contour> contours = new ArrayList<>();

	int currentStrength = 0;
	double topDeg = 0D;

	for (int i = 1; i < heightData.length; i++) {
	    int targetDistance = STEP_SIZE_IN_M * i;
	    double visibilityAngle = calculateVisibilityAngle(eyeHeight, heightData[i], targetDistance);

	    if (topDeg < visibilityAngle) {
		currentStrength = 0;
		topDeg = visibilityAngle;
	    } else {
		if (currentStrength == 0) {
		    contours.add(new Contour(visibilityAngle, currentStrength));
		}
		currentStrength++;
	    }
	}

	return contours;
    }
    
    public static double[] applyEarthCurvitude(double[] heightData) {
	double[] result = new double[heightData.length];
	
	double eyeHeight = heightData[0];
	for (int i = 0; i < heightData.length; i++) {
	    int targetDistance = STEP_SIZE_IN_M * i;
	    double targetHeight = heightData[i];
	    double targetHiddenHeight = calculateTargetHiddenHeight(eyeHeight, targetDistance);
	    double targetVisibleHeight = targetHeight - targetHiddenHeight;
	    
	    result[i] = targetVisibleHeight;
	}
	
	return result;
    }

    public static class Contour {
	public final double deg;
	public final int strength;

	public Contour(double deg, int strength) {
	    this.deg = deg;
	    this.strength = strength;
	}
    }
    
    public static class DistancePoint {
	public final double deg;
	public final int distance;

	public DistancePoint(double deg, int distance) {
	    this.deg = deg;
	    this.distance = distance;
	}
    }

    static double calculateVisibilityAngle(double eyeHeight, double targetVisibleHeight, int targetDistance) {
	if (eyeHeight == targetVisibleHeight) {
	    return 90D;
	} else if (eyeHeight < targetVisibleHeight) {
	    return Math.atan((Math.abs(eyeHeight - targetVisibleHeight)) / targetDistance) * 180 / Math.PI + 90;
	} else {
	    return Math.atan(targetDistance / (eyeHeight - targetVisibleHeight)) * 180 / Math.PI;
	}
    }

    //Based on https://github.com/dizzib/earthcalc.
    static double calculateTargetHiddenHeight(double eyeHeight, int targetDistance) {
	final int EARTH_RADIUS_KM = 6371;
	final double eyeHeightKm = eyeHeight * 0.001;
	final double targetDistanceKm = targetDistance * 0.001;
	final double horizonDistance = Math.sqrt(Math.pow(eyeHeightKm, 2) + 2 * EARTH_RADIUS_KM * eyeHeightKm);

	if (targetDistanceKm < horizonDistance) {
	    return 0;
	}

	return (Math.sqrt(Math.pow(targetDistanceKm - horizonDistance, 2) + Math.pow(EARTH_RADIUS_KM, 2))
		- EARTH_RADIUS_KM) * 1000;
    }

}