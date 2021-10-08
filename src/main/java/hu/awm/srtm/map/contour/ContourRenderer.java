package hu.awm.srtm.map.contour;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import hu.awm.srtm.map.contour.ContourCalculator.Contour;
import hu.awm.srtm.map.contour.ContourCalculator.DistancePoint;

public class ContourRenderer {

	public static BufferedImage renderDistancePoints(final List<List<DistancePoint>> distancePoints) {
		int height = 500;

		double minDeg = 180;
		double maxDeg = 0;
		int maxDst = 0;

		for (List<DistancePoint> distancePoint : distancePoints) {
			for (DistancePoint contourPoint : distancePoint) {
				if (maxDst < contourPoint.distance) {
					maxDst = contourPoint.distance;
				}
				if (contourPoint.deg < minDeg) {
					minDeg = contourPoint.deg;
				}
				if (maxDeg < contourPoint.deg) {
					maxDeg = contourPoint.deg;
				}
			}
		}
		double degDelta = maxDeg - minDeg;

		BufferedImage img = new BufferedImage(distancePoints.size(), height + 20, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();

		for (int i = 0; i < distancePoints.size(); i++) {
			List<DistancePoint> line = distancePoints.get(i);
			int lastY = height;
			for (int j = 0; j < line.size(); j++) {
				double pos = (line.get(j).deg - minDeg) * (height / degDelta);
				int y = (int) (Math.max(0, height - pos - 1) + 10);

				int strength = (int) Math.min(254d, ((double) (line.get(j).distance) / (double) 1000) * 255);
				g.setColor(new Color(0, 0, 255 - strength));
				g.drawLine(i, y, i, lastY);

				lastY = y;
			}
		}

		return img;
	}

	public static BufferedImage renderContours(final List<List<Contour>> contours) {
		int height = 500;

		double minDeg = 180;
		double maxDeg = 0;
		int maxStr = 0;

		for (List<Contour> contour : contours) {
			for (Contour contourPoint : contour) {
				if (maxStr < contourPoint.strength) {
					maxStr = contourPoint.strength;
				}
				if (contourPoint.deg < minDeg) {
					minDeg = contourPoint.deg;
				}
				if (maxDeg < contourPoint.deg) {
					maxDeg = contourPoint.deg;
				}
			}
		}
		double degDelta = maxDeg - minDeg;

		BufferedImage img = new BufferedImage(contours.size(), height + 20, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < contours.size(); i++) {
			List<Contour> line = contours.get(i);
			for (int j = 0; j < line.size(); j++) {
				double pos = (line.get(j).deg - minDeg) * (height / degDelta);
				int y = (int) (Math.max(0, height - pos - 1) + 10);

				img.setRGB(i, y, Color.white.getRGB());
			}
		}

		return img;
	}

}
