package com.sim.image.filters;

import java.awt.image.BufferedImage;

public interface Filter {

	public void readImage(String imagePath);

	public String processImage(String imagePath);

	public String exportImage(BufferedImage resultedImage);
}
