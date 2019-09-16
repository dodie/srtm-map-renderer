package hu.awm.hgt.render;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HeightLineRenderer {
    
    public static BufferedImage render(final double[] ds) {
	double max = 0;
	for (int i = 0; i < ds.length; i++) {
	    if (ds[i] > max) {
		max = ds[i];
	    }
	}

	BufferedImage img = new BufferedImage(ds.length + 10, (int) max + 10, BufferedImage.TYPE_INT_RGB);

	Graphics2D g2d = img.createGraphics();
	g2d.setBackground(Color.WHITE);
	g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
	g2d.setColor(Color.BLACK);
	BasicStroke bs = new BasicStroke(2);
	g2d.setStroke(bs);

	for (int i = 0; i < ds.length; i++) {
	    g2d.drawLine(i, (int) (max - ds[i]), i, img.getHeight());
	}
	
	return img;
    }

}
