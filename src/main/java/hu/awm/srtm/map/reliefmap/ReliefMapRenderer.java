package hu.awm.srtm.map.reliefmap;

import java.awt.*;
import java.awt.image.BufferedImage;

import hu.awm.srtm.data.hgt.Position;
import hu.awm.srtm.data.hgt.Tile;
import hu.awm.srtm.data.hgt.TileMap;
import hu.awm.srtm.helper.Gradient;
import hu.awm.srtm.helper.LinearGradient;

public class ReliefMapRenderer {

	public static BufferedImage render(TileMap tiles, Position highlight) {
		int resx = tiles.getSizeX();
		int resy = tiles.getSizeY();

		final int w = Tile.RESOLUTION * resx;
		final int h = Tile.RESOLUTION * resy;

		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		double maxElevation = 0;

		for (int tileYNum = 0; tileYNum < resy; tileYNum++) {
			for (int tileXNum = 0; tileXNum < resx; tileXNum++) {
				Tile tile = tiles.get(tileYNum, tileXNum);

				if (tile.getMaxElevation() > maxElevation) {
					maxElevation = tile.getMaxElevation();
				}
			}
		}

		maxElevation *= 1.2; // Can be changed to any number greater than 1, to tweak the scale

		Gradient gradient = new LinearGradient(new double[] { 0.0, 0.1, 1.0 },
				new Color(148, 139, 70),
				new Color(34, 79, 34),
				new Color(117, 169, 68));

		for (int tileYNum = 0; tileYNum < resy; tileYNum++) {
			for (int tileXNum = 0; tileXNum < resx; tileXNum++) {
				Tile tile = tiles.get(tileYNum, tileXNum);
				final double[][] data = scale(tile, maxElevation, 1.0);

				for (int x = 0; x < Tile.RESOLUTION; x++) {
					for (int y = 0; y < Tile.RESOLUTION; y++) {

						Color color = gradient.heightToColor(data[x][y]);

						img.setRGB(y + Tile.RESOLUTION * tileXNum, Tile.RESOLUTION - x - 1 + Tile.RESOLUTION * tileYNum,
								color.getRGB());
					}
				}
			}
		}

		return img;
	}

	private static double[][] scale(Tile tile, double unitInput, double unitOutput) {
		int size = Tile.RESOLUTION;
		final double[][] data = new double[size][size];
		for (int lat = 0; lat < size; lat = lat + 1) {
			for (int lon = 0; lon < size; lon = lon + 1) {
				data[lat][lon] = (tile.elevation(lat, lon) / unitInput) * unitOutput;
			}
		}

		return data;
	}

}
