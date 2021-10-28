package hu.awm.srtm.cli;

import static picocli.CommandLine.Command;

import hu.awm.srtm.cli.converter.PositionConsumer;
import hu.awm.srtm.data.hgt.Position;
import hu.awm.srtm.data.hgt.TileMap;
import hu.awm.srtm.data.hgt.TileReader;
import hu.awm.srtm.map.reliefmap.ReliefMapRenderer;
import hu.awm.srtm.presentation.JFramePresenter;
import picocli.CommandLine.Parameters;

@Command(name = "relief")
public class ReliefCli implements Runnable {

	@Parameters(index = "0")
	private String hgtFiles;

	@Parameters(index = "1..2", parameterConsumer = PositionConsumer.class)
	private Position positionCoordinates;

	@Parameters(index = "3")
	private int fromLat;

	@Parameters(index = "4")
	private int fromLon;

	@Parameters(index = "5")
	private int toLat;

	@Parameters(index = "6")
	private int toLon;

	@Override
	public void run() {
		TileReader tileReader = new TileReader(hgtFiles);
		TileMap tileMap = tileReader.readHgtFiles(fromLat, fromLon, toLat, toLon);
		new JFramePresenter("Relief map", ReliefMapRenderer.render(tileMap, positionCoordinates)).display();
	}
}
