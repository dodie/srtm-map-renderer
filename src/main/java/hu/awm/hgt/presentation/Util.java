package hu.awm.hgt.presentation;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Util {

    public static void toFile(BufferedImage img, String path, String formatName) {
	File file = new File(path);
	try {
	    ImageIO.write(img, formatName, file);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
	System.out.println(file.getPath());
    }
    
    public static BufferedImage resize(BufferedImage img, int width, int height) {
	int originalWidth = img.getWidth();
	int originalHeight = img.getHeight();
	
	BufferedImage resizedImg = new BufferedImage(width, height, img.getType());
	Graphics2D g = resizedImg.createGraphics();
	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.drawImage(img, 0, 0, width, height, 0, 0, originalWidth, originalHeight, null);
	g.dispose();
	return resizedImg;
    }
    
    public static BufferedImage fit(BufferedImage img, int maxWidth, int maxHeight) {
	int originalWidth = img.getWidth();
	int originalHeight = img.getHeight();
	int newWidth = originalWidth;
	int newHeight = originalHeight;
	if (originalWidth > maxWidth) {
	    newWidth = maxWidth;
	    newHeight = (newWidth * originalHeight) / originalWidth;
	}
	if (newHeight > maxHeight) {
	    newHeight = maxHeight;
	    newWidth = (newHeight * originalWidth) / originalHeight;
	}
	
	return resize(img, newWidth, newHeight);
    }
    
}
