package com.sim.ui.panel;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 3465949775024714720L;
	
	private JLabel imageLabel;

	public ImagePanel(String imagePath) {
		imageLabel = new JLabel();
		this.add(imageLabel);
		this.setBounds(0, 0, 750, 750);
		
		ImageIcon icon = new ImageIcon(imagePath);
		imageLabel.setIcon(createImageIcon(icon));
		
		this.setVisible(true);
	}
	

	private ImageIcon createImageIcon(ImageIcon icon){
		Image img = icon.getImage() ;  
		Image newimg = img.getScaledInstance( 750, 750,  java.awt.Image.SCALE_SMOOTH ) ;  
		icon = new ImageIcon( newimg );
		
		return icon;
	}

}
