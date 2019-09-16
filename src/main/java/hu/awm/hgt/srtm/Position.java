package hu.awm.hgt.srtm;

public class Position {

    public final int gridLat;
    public final int gridLon;
    public final int row;
    public final int col;

    public Position(double lat, double lon) {
	this.gridLat = integerPart(lat);
	this.gridLon = integerPart(lon);
	this.row = (int) Math.round(fractionalPart(lat) * Tile.RESOLUTION);
	this.col = (int) Math.round(fractionalPart(lon) * Tile.RESOLUTION);
    }

    public Position(int gridLat, int gridLon, int col, int row) {
	this.gridLat = gridLat;
	this.gridLon = gridLon;
	this.row = row;
	this.col = col;
    }

    static Position get(Position coordinates, double deg, int distance) {
	double dx = ((distance * Math.sin(deg)));
	double dy = ((distance * Math.cos(deg)));

	double len = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

	int dxi = (int) (dx / len * distance);
	int dyi = (int) (dy / len * distance);

	return new Position(
		(coordinates.row + dyi) < 0 ? coordinates.gridLat + ((coordinates.row + dyi) / Tile.RESOLUTION) - 1
			: coordinates.gridLat + (coordinates.row + dyi) / Tile.RESOLUTION,
		(coordinates.col + dxi) < 0 ? coordinates.gridLon + ((coordinates.col + dxi) / Tile.RESOLUTION) - 1
			: coordinates.gridLon + (coordinates.col + dxi) / Tile.RESOLUTION,

		mod(coordinates.col + dxi, Tile.RESOLUTION), mod(coordinates.row + dyi, Tile.RESOLUTION));
    }

    static int mod(int a, int b) {
	return (((a) % b) + b) % b;
    }

    static int integerPart(double d) {
	return (int) d;
    }

    static double fractionalPart(double d) {
	return d - (int) d;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + gridLat;
	result = prime * result + gridLon;
	result = prime * result + col;
	result = prime * result + row;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Position other = (Position) obj;
	if (gridLat != other.gridLat)
	    return false;
	if (gridLon != other.gridLon)
	    return false;
	if (col != other.col)
	    return false;
	if (row != other.row)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Offset [gridLat=" + gridLat + ", gridLon=" + gridLon + ", col=" + col + ", row=" + row + "]";
    }

}
