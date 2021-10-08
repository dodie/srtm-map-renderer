package hu.awm.srtm.presentation;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class JFramePresenter {

	String title;

	BufferedImage img;

	public JFramePresenter(String title, BufferedImage img) {
		this.title = title;
		this.img = Util.fit(img, 1200, 600);
	}

	public void display() {
		EventQueue.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}

			JFrame frame = new JFrame(title);
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());
			frame.add(new Pane(img));
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.pack();
			frame.setSize(img.getWidth(), img.getHeight());
		});
	}

	public static class Pane extends JPanel {

		private static final long serialVersionUID = 1L;

		private final BufferedImage img;

		public Pane(BufferedImage img) {
			this.img = img;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.drawImage(img, 0, 0, this);
			g2d.dispose();
		}
	}

}
