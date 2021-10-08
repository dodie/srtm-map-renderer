package hu.awm.srtm.map.reliefmap;

import java.awt.*;
import java.awt.image.BufferedImage;

import hu.awm.srtm.data.hgt.Position;
import hu.awm.srtm.data.hgt.Tile;
import hu.awm.srtm.data.hgt.TileMap;

public class ReliefMapRenderer {

	public static BufferedImage render(TileMap tiles, Position highlight) {
		int resx = tiles.getSizeX();
		int resy = tiles.getSizeY();

		final int w = Tile.RESOLUTION * resx;
		final int h = Tile.RESOLUTION * resy;

		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

		double maxElevation = 300d;

		for (int tileYNum = 0; tileYNum < resy; tileYNum++) {
			for (int tileXNum = 0; tileXNum < resx; tileXNum++) {
				Tile tile = tiles.get(tileYNum, tileXNum);
				final int[][] data = scale(tile, maxElevation, 255);

				for (int x = 0; x < Tile.RESOLUTION; x++) {
					for (int y = 0; y < Tile.RESOLUTION; y++) {

						Color color;
						if (data[x][y] < 255) {
							color = new Color(0, data[x][y], 0);
						}
						else if (255 <= data[x][y] && data[x][y] < 510) {
							color = new Color(data[x][y] - 255, 255, 0);
						}
						else if (510 <= data[x][y] && data[x][y] < 765) {
							color = new Color(255, 255, data[x][y] - 510);
						}
						else {
							color = new Color(255, 255, 255);
						}

						if (highlight != null && highlight.gridLat == tile.lat()
								&& highlight.gridLon == tile.lon()
								&& Math.abs(y - highlight.col) < 100 && Math.abs(x - highlight.row) < 100) {
							color = Color.RED;
						}

						img.setRGB(y + Tile.RESOLUTION * tileXNum, Tile.RESOLUTION - x - 1 + Tile.RESOLUTION * tileYNum,
								color.getRGB());
					}
				}
			}
		}

		return img;
	}

	private static int[][] scale(Tile tile, double unitInput, int unitOutput) {
		int size = Tile.RESOLUTION;
		final int[][] data = new int[size][size];
		for (int lat = 0; lat < size; lat = lat + 1) {
			for (int lon = 0; lon < size; lon = lon + 1) {
				data[lat][lon] = (int) ((tile.elevation(lat, lon) / unitInput) * unitOutput);
			}
		}

		return data;
	}

}
