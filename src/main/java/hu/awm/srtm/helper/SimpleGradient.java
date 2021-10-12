package hu.awm.srtm.helper;

import java.awt.*;

public class SimpleGradient implements Gradient {

	private static final int SCALE = 255;

	private Color start;
	private double startFactor = 1.0;

	private Color end;
	private double endFactor = 1.0;

	public SimpleGradient(Color start, Color end) {
		this.start = start;
		this.end = end;
	}

	public SimpleGradient(Color start, double startFactor, Color end, double endFactor) {
		this.start = start;
		this.startFactor = startFactor;
		this.end = end;
		this.endFactor = endFactor;
	}

	@Override
	public Color heightToColor(int height) {
		return new Color(averageChannel(height, start.getRed(), end.getRed()),
				averageChannel(height, start.getGreen(), end.getGreen()),
				averageChannel(height, start.getBlue(), end.getBlue()));
	}

	private int averageChannel(int height, int a, int b) {
		final double aFact = constrainTo(SCALE - height, 0, SCALE) * startFactor;
		final double bFact = constrainTo(height, 0, SCALE) * endFactor;
		return (int) Math.round((aFact * a + bFact * b) / (aFact + bFact));
	}

	private int constrainTo(int n, int min, int max) {
		return Math.max(Math.min(n, max), min);
	}
}
