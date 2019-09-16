package hu.awm.hgt;

import java.util.ArrayList;
import java.util.List;

import hu.awm.hgt.presentation.JFramePresenter;
import hu.awm.hgt.render.ContourRenderer;
import hu.awm.hgt.render.HeightLineRenderer;
import hu.awm.hgt.render.ReliefMapRenderer;
import hu.awm.hgt.srtm.Position;
import hu.awm.hgt.srtm.ProfileCalculator;
import hu.awm.hgt.srtm.ProfileCalculator.Contour;
import hu.awm.hgt.srtm.ProfileCalculator.DistancePoint;
import hu.awm.hgt.srtm.TileMap;
import hu.awm.hgt.srtm.TileReader;

public class Application {

    public static void main(String[] args) throws Exception {
	long start = System.currentTimeMillis();
	
	if (args.length != 1) {
	    System.err.println("Exactly one argument is required: please supply the path of the directory where the HGT files are located.");
	    System.exit(1);
	}
	
	TileReader tileReader = new TileReader(args[0]);
	// Read HGT tiles for Hungary.
	TileMap tileMap = new TileMap(tileReader.readHgtFiles(48, 16, 44, 22));
	
	// Specify a coordinate pair in Börzsöny
	Position coordinates = new Position(47.7708946, 18.9205236);
	
	List<List<Contour>> contours = new ArrayList<>();
	List<List<DistancePoint>> distancePoints = new ArrayList<>();
	
	// Calculate contours covering 90 degrees, looking South.
	final double fromDeg = 90D;
	final double toDeg = 180D;
	final double resolution = 0.1D;
	for (double i = fromDeg; i < toDeg; i = i + resolution) {
	    double[] data = tileMap.getHeightLine(coordinates, i * Math.PI / 180, 5000);
	    double[] data2 = ProfileCalculator.applyEarthCurvitude(data);
	    contours.add(ProfileCalculator.getContour(data2));
	    distancePoints.add(ProfileCalculator.getDistances(data2));
	}
	
	new JFramePresenter("Contours", ContourRenderer.renderContours(contours)).display();
	//new JFramePresenter("Distance points", ContourRenderer.renderDistancePoints(distancePoints)).display();
	new JFramePresenter("Relief map", ReliefMapRenderer.render(tileMap, coordinates)).display();
	System.out.println("Rendering finished in " + (System.currentTimeMillis() - start) + " ms.");
    }
    
    public static void main_debug(String[] args) throws Exception {
	if (args.length != 1) {
	    System.err.println("Exactly one argument is required: please supply the path of the directory where the HGT files are located.");
	    System.exit(1);
	}
	
	TileReader tileReader = new TileReader(args[0]);
	TileMap tileMap = new TileMap(tileReader.readHgtFiles(48, 17, 46, 19));
	
	double[] data = tileMap.getHeightLine(new Position(47, 18, 3600, 0), -90 * Math.PI / 180, 3600);
	double[] data2 = ProfileCalculator.applyEarthCurvitude(data);
	new JFramePresenter("Height data", HeightLineRenderer.render(data)).display();
	new JFramePresenter("Height data + earth curvitude", HeightLineRenderer.render(data2)).display();
	
	new JFramePresenter("Relief map", ReliefMapRenderer.render(tileMap, null)).display();

	List<List<Contour>> contours = new ArrayList<>();
	List<Contour> data4 = ProfileCalculator.getContour(data2);
	contours.add(data4);
	
	new JFramePresenter("Contours", ContourRenderer.renderContours(contours)).display();
    }
    
}
