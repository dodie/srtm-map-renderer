package hu.awm.srtm.cli;

import java.util.List;

import hu.awm.srtm.data.hgt.Position;
import hu.awm.srtm.data.hgt.TileMap;
import hu.awm.srtm.data.hgt.TileReader;
import hu.awm.srtm.map.contour.ContourCalculator;
import hu.awm.srtm.map.contour.ContourRenderer;
import hu.awm.srtm.map.reliefmap.ReliefMapRenderer;
import hu.awm.srtm.presentation.JFramePresenter;
import hu.awm.srtm.tools.cansee.CanSeeCalculator;

public class CliApplication {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();

		System.out.println("Rendering started...");

		String type = args[0];
		String hgtFiles = args[1];

		if (type.equals("relief")) {
			Position coordinates = new Position(
					Double.parseDouble(args[2]),
					Double.parseDouble(args[3]));
			int fromLat = Integer.parseInt(args[4]);
			int fromLon = Integer.parseInt(args[5]);
			int toLat = Integer.parseInt(args[6]);
			int toLon = Integer.parseInt(args[7]);

			TileReader tileReader = new TileReader(hgtFiles);
			TileMap tileMap = tileReader.readHgtFiles(fromLat, fromLon, toLat, toLon);
			new JFramePresenter("Relief map", ReliefMapRenderer.render(tileMap, coordinates)).display();
		} else if (type.equals("contour")) {
			Position coordinates = new Position(
					Double.parseDouble(args[2]),
					Double.parseDouble(args[3]));
			final double fromDeg = Double.parseDouble(args[4]);
			final double toDeg = Double.parseDouble(args[5]);
			final double resolution = Double.parseDouble(args[6]);

			TileReader tileReader = new TileReader(hgtFiles);

			// Read all nearby HGT files
			TileMap tileMap = tileReader.readHgtFiles(
					coordinates.gridLat - 1, coordinates.gridLon - 1,
					coordinates.gridLat + 1, coordinates.gridLon + 1);

			List<List<ContourCalculator.Contour>> contours =
					ContourCalculator.getContours(tileMap, coordinates, fromDeg, toDeg, resolution);

			new JFramePresenter("Contours", ContourRenderer.renderContours(contours)).display();
		} else if (type.equals("cansee")) {
			int tileFromLat = Integer.parseInt(args[2]);
			int tileFromLon = Integer.parseInt(args[3]);
			int tileToLat = Integer.parseInt(args[4]);
			int tileToLon = Integer.parseInt(args[5]);

			TileReader tileReader = new TileReader(hgtFiles);
			TileMap tileMap = tileReader.readHgtFiles(tileFromLat, tileFromLon, tileToLat, tileToLon);

			Double fromLat = Double.parseDouble(args[6]);
			Double fromLon = Double.parseDouble(args[7]);
			Double fromHeight = Double.parseDouble(args[8]);
			Double toLat = Double.parseDouble(args[9]);
			Double toLon = Double.parseDouble(args[10]);
			Double toHeight = Double.parseDouble(args[11]);

			CanSeeCalculator canSeeCalculator = new CanSeeCalculator();
			canSeeCalculator.setTileMap(tileMap);
			canSeeCalculator.setCoordinates(fromLat, fromLon, fromHeight, toLat, toLon, toHeight);
			System.out.println(
					canSeeCalculator.calculateBoolean()
					? "Hurray! Points are visible to each other. :)"
					: "Points aren't visible to each other. :(");
		}
		else {
			throw new IllegalArgumentException("Unknown map type: " + type + ". Type should be one of the following: relief, contour.");
		}

		System.out.println("Rendering finished in " + (System.currentTimeMillis() - start) + " ms.");
	}

}
