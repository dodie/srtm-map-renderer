package hu.awm.hgt.srtm;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProfileCalculatorTest {

    @Test
    public void testCalculateTargetHiddenHeight() throws Exception {
	assertEquals(192.433508, ProfileCalculator.calculateTargetHiddenHeight(200D, 100000), 0.001);
    }

    @Test
    public void testCalculateVisibilityAngle() throws Exception {
	assertEquals(0, ProfileCalculator.calculateVisibilityAngle(200D, 100D, 0), 0.001);
	assertEquals(45D, ProfileCalculator.calculateVisibilityAngle(200D, 100D, 100), 0.001);
	assertEquals(135D, ProfileCalculator.calculateVisibilityAngle(100D, 200D, 100), 0.001);
	assertEquals(90D, ProfileCalculator.calculateVisibilityAngle(100D, 100D, 100), 0.001);
    }

}
