package hu.awm.hgt.presentation;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class JFramePresenter {
    
    String title;
    BufferedImage img;
    
    public JFramePresenter(String title, BufferedImage img) {
	this.title = title;
	this.img = Util.fit(img, 1200, 600);
    }
    
    public void display() {
	EventQueue.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    throw new RuntimeException(e);
		}

		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(new Pane(img));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	    }
	});
    }

    public class Pane extends JPanel {
	private static final long serialVersionUID = 1L;
	private BufferedImage img;

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
