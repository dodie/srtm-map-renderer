package hu.awm.srtm.helper;

import java.awt.*;

/**
 * Generates colors based on a multi-color linear gradient.
 * The gradient is configured with values relative to the maximum height.
 */
public class RelativeLinearGradient implements ColorScheme {

	private final Color[] colors;
	private final double[] stops;
	private final double max;

	public RelativeLinearGradient(double max, Color start, Color end) {
		this.max = max;
		this.colors = new Color[] { start, end };
		this.stops = new double[] { 0.0, 1.0 };
	}

	public RelativeLinearGradient(double max, Color... colors) {
		this.max = max;
		this.colors = colors;
		this.stops = new double[colors.length];
		for (int i = 0; i < colors.length; i++) {
			this.stops[i] = i * 1.0 / (colors.length - 1);
		}
	}

	public RelativeLinearGradient(double max, double[] stops, Color... colors) {
		this.max = max;
		if (stops.length != colors.length) {
			throw new IllegalArgumentException("The number of stops should equal the number of colors!");
		}

		this.colors = colors;
		this.stops = stops;
	}

	@Override
	public Color heightToColor(double height) {
		double relativeHeight = height / max;
		for (int i = 1; i < stops.length; i++) {
			if (relativeHeight <= stops[i]) {
				Color start = colors[i-1];
				Color end = colors[i];
				return new Color(averageChannel(relativeHeight, start.getRed(), end.getRed(), stops[i-1], stops[i]),
						averageChannel(relativeHeight, start.getGreen(), end.getGreen(), stops[i-1], stops[i]),
						averageChannel(relativeHeight, start.getBlue(), end.getBlue(), stops[i-1], stops[i]));
			}
		}
		return Color.BLACK;
	}

	private int averageChannel(double height, int a, int b, double stopStart, double stopEnd) {
		final double aFact = constrainTo(stopEnd - height - stopStart, 0.0, 1.0);
		final double bFact = constrainTo(height - stopStart, 0.0, 1.0);
		return (int) Math.round((aFact * a + bFact * b) / (aFact + bFact));
	}

	private double constrainTo(double n, double min, double max) {
		return Math.max(Math.min(n, max), min);
	}
}
