package hu.awm.srtm.cli;

import hu.awm.srtm.data.hgt.TileMap;
import hu.awm.srtm.data.hgt.TileReader;
import hu.awm.srtm.tools.cansee.CanSeeCalculator;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;

@CommandLine.Command(name = "cansee")
public class CanSeeCli implements Runnable {

	@Parameters(index = "0")
	private String hgtFiles;

	@Parameters(index = "1")
	private int tileFromLat;

	@Parameters(index = "2")
	private int tileFromLon;

	@Parameters(index = "3")
	private int tileToLat;

	@Parameters(index = "4")
	private int tileToLon;

	@Parameters(index = "5..6")
	private double[] viewPoint;

	@Parameters(index = "7")
	private double fromHeight;

	@Parameters(index = "8..9")
	private double[] watchedPoint;

	@Parameters(index = "10")
	private double toHeight;

	@Override
	public void run() {
		TileReader tileReader = new TileReader(hgtFiles);
		TileMap tileMap = tileReader.readHgtFiles(tileFromLat, tileFromLon, tileToLat, tileToLon);

		CanSeeCalculator canSeeCalculator = new CanSeeCalculator();
		canSeeCalculator.setTileMap(tileMap);
		canSeeCalculator.setCoordinates(viewPoint[0], viewPoint[1], fromHeight, watchedPoint[0], watchedPoint[1], toHeight);
		System.out.println(
				canSeeCalculator.calculateBoolean()
						? "Hurray! Given target point is visible from the viewpoint. :)"
						: "Given target is not visible from the viewpoint. :(");
	}
}
