package hu.awm.srtm.map.gradient;

import static org.junit.Assert.assertEquals;

import java.awt.*;

import hu.awm.srtm.helper.ColorScheme;
import hu.awm.srtm.helper.RelativeLinearGradient;
import org.junit.Test;

public class GradientTest {

	@Test
	public void testTwoStepLinearGradient() {
		ColorScheme gradient = new RelativeLinearGradient(1.0, Color.BLACK, Color.WHITE);

		assertEquals(Color.BLACK, gradient.heightToColor(0.0));
		assertEquals(Color.WHITE, gradient.heightToColor(1.0));
		assertEquals(new Color(128, 128, 128), gradient.heightToColor(0.5));
	}

	@Test
	public void testThreeStepLinearGradient() {
		ColorScheme gradient = new RelativeLinearGradient(1.0, Color.BLACK, Color.RED, Color.WHITE);

		assertEquals(Color.BLACK, gradient.heightToColor(0.0));
		assertEquals(Color.WHITE, gradient.heightToColor(1.0));
		assertEquals(Color.RED, gradient.heightToColor(0.5));
	}
}
