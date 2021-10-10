package hu.awm.srtm.map.contour;

import java.util.ArrayList;
import java.util.List;

import hu.awm.srtm.data.hgt.Position;
import hu.awm.srtm.data.hgt.Tile;
import hu.awm.srtm.data.hgt.TileMap;

public class ContourCalculator {

	static final int EYE_HEIGHT_FROM_GROUND_IN_M = 5;

	public static List<List<Contour>> getContours(TileMap tileMap, Position coordinates, double fromDeg, double toDeg,
			double resolution) {
		List<List<Contour>> contours = new ArrayList<>();
		for (double i = fromDeg; i < toDeg; i = i + resolution) {
			double[] data = ContourCalculator.getHeightLine(tileMap, coordinates, i * Math.PI / 180, 5000);
			double[] data2 = ContourCalculator.applyEarthCurvitude(data);
			contours.add(ContourCalculator.getContour(data2));
		}
		return contours;
	}

	static List<DistancePoint> getDistances(double[] heightData) {
		double eyeHeight = heightData[0] + EYE_HEIGHT_FROM_GROUND_IN_M;
		List<DistancePoint> distances = new ArrayList<>();

		double topDeg = 0D;
		for (int i = 1; i < heightData.length; i++) {
			int targetDistance = Tile.PIXEL_SIZE_IN_M * i;
			double visibilityAngle = calculateVisibilityAngle(eyeHeight, heightData[i], targetDistance);

			if (topDeg < visibilityAngle) {
				distances.add(new DistancePoint(visibilityAngle, targetDistance));
				topDeg = visibilityAngle;
			}
		}

		return distances;
	}

	static List<Contour> getContour(double[] heightData) {
		double eyeHeight = heightData[0] + EYE_HEIGHT_FROM_GROUND_IN_M;
		List<Contour> contours = new ArrayList<>();

		int currentStrength = 0;
		double topDeg = 0D;

		for (int i = 1; i < heightData.length; i++) {
			int targetDistance = Tile.PIXEL_SIZE_IN_M * i;
			double visibilityAngle = calculateVisibilityAngle(eyeHeight, heightData[i], targetDistance);

			if (topDeg < visibilityAngle) {
				currentStrength = 0;
				topDeg = visibilityAngle;
			}
			else {
				if (currentStrength == 0) {
					contours.add(new Contour(visibilityAngle, currentStrength));
				}
				currentStrength++;
			}
		}

		return contours;
	}

	static double[] getHeightLine(TileMap tileMap, Position start, double deg, int distance) {
		double[] heightLine = new double[distance];

		for (int i = 0; i < distance; i++) {
			Position offset = Position.get(start, deg, i);

			heightLine[i] = tileMap.getByLatLon(offset.gridLat, offset.gridLon).elevation(offset.row, offset.col);
		}

		return heightLine;
	}

	static double[] applyEarthCurvitude(double[] heightData) {
		double[] result = new double[heightData.length];

		double eyeHeight = heightData[0];
		for (int i = 0; i < heightData.length; i++) {
			int targetDistance = Tile.PIXEL_SIZE_IN_M * i;
			double targetHeight = heightData[i];
			double targetHiddenHeight = calculateTargetHiddenHeight(eyeHeight, targetDistance);
			double targetVisibleHeight = targetHeight - targetHiddenHeight;

			result[i] = targetVisibleHeight;
		}

		return result;
	}

	static double calculateVisibilityAngle(double eyeHeight, double targetVisibleHeight, int targetDistance) {
		if (eyeHeight == targetVisibleHeight) {
			return 90D;
		}
		else if (eyeHeight < targetVisibleHeight) {
			return Math.atan((Math.abs(eyeHeight - targetVisibleHeight)) / targetDistance) * 180 / Math.PI + 90;
		}
		else {
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

}