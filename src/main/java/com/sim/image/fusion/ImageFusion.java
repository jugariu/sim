package com.sim.image.fusion;

import java.awt.image.BufferedImage;

public interface ImageFusion {
	
	public void readImages(String firstImagePath, String secondImagePath);
	
	public void processImages(String firstImagePath, String secondImagePath);
	
	public void exportImage(BufferedImage resultedImage);
}
