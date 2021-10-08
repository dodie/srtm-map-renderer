package hu.awm.srtm.map.contour;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ContourCalculatorTest {

	@Test
	public void testCalculateTargetHiddenHeight() throws Exception {
		assertEquals(192.433508, ContourCalculator.calculateTargetHiddenHeight(200D, 100000), 0.001);
	}

	@Test
	public void testCalculateVisibilityAngle() throws Exception {
		assertEquals(0, ContourCalculator.calculateVisibilityAngle(200D, 100D, 0), 0.001);
		assertEquals(45D, ContourCalculator.calculateVisibilityAngle(200D, 100D, 100), 0.001);
		assertEquals(135D, ContourCalculator.calculateVisibilityAngle(100D, 200D, 100), 0.001);
		assertEquals(90D, ContourCalculator.calculateVisibilityAngle(100D, 100D, 100), 0.001);
	}

}
