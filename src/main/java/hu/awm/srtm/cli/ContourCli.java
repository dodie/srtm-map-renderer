package hu.awm.srtm.cli;

import java.util.List;

import hu.awm.srtm.cli.converter.PositionConsumer;
import hu.awm.srtm.data.hgt.Position;
import hu.awm.srtm.data.hgt.TileMap;
import hu.awm.srtm.data.hgt.TileReader;
import hu.awm.srtm.map.contour.ContourCalculator;
import hu.awm.srtm.presentation.JFramePresenter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "contour")
public class ContourCli implements Runnable {

	@Parameters(index = "0")
	private String hgtFiles;

	@Parameters(index = "1..2", parameterConsumer = PositionConsumer.class)
	private Position coordinates;

	@Parameters(index = "3")
	private double fromDeg;

	@Parameters(index = "4")
	private double toDeg;

	@Parameters(index = "5")
	private double resolution;

	@Override
	public void run() {
		TileReader tileReader = new TileReader(hgtFiles);

		// Read all nearby HGT files
		TileMap tileMap = tileReader.readHgtFiles(
				coordinates.gridLat - 1, coordinates.gridLon - 1,
				coordinates.gridLat + 1, coordinates.gridLon + 1);

		List<List<ContourCalculator.Contour>> contours =
				ContourCalculator.getContours(tileMap, coordinates, fromDeg, toDeg, resolution);

		new JFramePresenter("Contours", hu.awm.srtm.map.contour.ContourRenderer.renderContours(contours)).display();
	}
}
