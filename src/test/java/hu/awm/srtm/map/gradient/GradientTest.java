package hu.awm.srtm.map.gradient;

import static org.junit.Assert.assertEquals;

import java.awt.*;

import hu.awm.srtm.helper.Gradient;
import hu.awm.srtm.helper.LinearGradient;
import org.junit.Test;

public class GradientTest {

	@Test
	public void testTwoStepLinearGradient() {
		Gradient gradient = new LinearGradient(Color.BLACK, Color.WHITE);

		assertEquals(Color.BLACK, gradient.heightToColor(0.0));
		assertEquals(Color.WHITE, gradient.heightToColor(1.0));
		assertEquals(new Color(128, 128, 128), gradient.heightToColor(0.5));
	}

	@Test
	public void testThreeStepLinearGradient() {
		Gradient gradient = new LinearGradient(Color.BLACK, Color.RED, Color.WHITE);

		assertEquals(Color.BLACK, gradient.heightToColor(0.0));
		assertEquals(Color.WHITE, gradient.heightToColor(1.0));
		assertEquals(Color.RED, gradient.heightToColor(0.5));
	}
}
