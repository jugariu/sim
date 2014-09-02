package com.sim.ui.frame;

import javax.swing.JFrame;

import com.sim.ui.panel.ImagePanel;

public class ImageFrame extends JFrame{
	private static final long serialVersionUID = -8573263945618101691L;

	public ImageFrame(String imagePath) {
		this.setSize(750, 790);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
//		this.setResizable(false);
		this.setLayout(null);
		
		this.add(new ImagePanel(imagePath));
		
		this.setVisible(true);
	}
}
