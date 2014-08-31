package com.sim.image.fusion;

import java.awt.image.BufferedImage;

public interface ImageFusion {
	
	public void readImages(String firstImagePath, String secondImagePath);
	
	public String processImages(String firstImagePath, String secondImagePath);
	
	public String exportImage(BufferedImage resultedImage);
}
