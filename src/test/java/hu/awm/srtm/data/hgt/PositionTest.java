package hu.awm.srtm.data.hgt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PositionTest {

	@Test
	public void offset_in_one_tile() throws Exception {
		assertEquals(
				new Position(47, 18, 10, 15),
				Position.get(new Position(47, 18, 10, 10), 0 * Math.PI / 180, 5));
		assertEquals(
				new Position(47, 18, 15, 10),
				Position.get(new Position(47, 18, 10, 10), 90 * Math.PI / 180, 5));
		assertEquals(
				new Position(47, 18, 10, 5),
				Position.get(new Position(47, 18, 10, 10), 180 * Math.PI / 180, 5));
		assertEquals(
				new Position(47, 18, 5, 10),
				Position.get(new Position(47, 18, 10, 10), 270 * Math.PI / 180, 5));
		assertEquals(
				new Position(47, 18, 10, 15),
				Position.get(new Position(47, 18, 10, 10), 360 * Math.PI / 180, 5));
	}

	@Test
	public void offset_between_tiles() throws Exception {
		assertEquals(
				new Position(48, 18, 10, 9),
				Position.get(new Position(47, 18, 10, 3590), 0 * Math.PI / 180, 20));
		assertEquals(
				new Position(47, 19, 9, 10),
				Position.get(new Position(47, 18, 3590, 10), 90 * Math.PI / 180, 20));
		assertEquals(
				new Position(46, 18, 10, 3591),
				Position.get(new Position(47, 18, 10, 10), 180 * Math.PI / 180, 20));
		assertEquals(
				new Position(45, 18, 10, 3592),
				Position.get(new Position(47, 18, 10, 10), 180 * Math.PI / 180, 3620));
		assertEquals(
				new Position(47, 17, 3591, 10),
				Position.get(new Position(47, 18, 10, 10), 270 * Math.PI / 180, 20));
		assertEquals(
				new Position(47, 16, 3592, 10),
				Position.get(new Position(47, 18, 10, 10), 270 * Math.PI / 180, 3620));
	}

	@Test
	public void testFractionalPart() throws Exception {
		assertEquals(0.456d, Position.fractionalPart(123.456), 0.01);
		assertEquals(123.0d, Position.integerPart(123.456), 0.01);
	}

	@Test
	public void coordinates_from_lat_lon() throws Exception {
		assertEquals(
				new Position(48, 18, 3601, 3601),
				new Position(48.999999, 18.999999));
		assertEquals(
				new Position(48, 18, 0, 0),
				new Position(48.000000, 18.000000));
		assertEquals(
				new Position(48, 18, 1801, 1801),
				new Position(48.500000, 18.500000));
	}

}
