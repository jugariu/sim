package com.sim.image.filters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class LowPassFilter {
	private String workingDirPath;
	private String imagePath;
	private BufferedImage image;
	private String imageName;
	
	private static final int LOW_PASS_VALUE = 150;

	private static final String PROCESS = "lowPassFilter";
	public static final String PROCESS_TYPE = "oneImageProcessor";

	public LowPassFilter(String workingDirPath) {
		this.workingDirPath = workingDirPath;
	}
	
	public void readImage(String imagePath) {
		this.imagePath = imagePath;
		
		try {
			File file = new File(this.imagePath);
			imageName = file.getName().substring(0, file.getName().length()-4) + "_";
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String processImage(String imagePath) {
		readImage(imagePath);
		BufferedImage resultedImage = getLowPassImage();
		String exportedImagePath = exportImage(resultedImage);
		
		return exportedImagePath;
		
	}

	public String exportImage(BufferedImage resultedImage) {
		File outputImage = new File(workingDirPath + "/" + imageName + PROCESS + ".jpg");
		try {
			ImageIO.write(resultedImage, "jpg", outputImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputImage.getAbsolutePath();
	}
	
	private BufferedImage getLowPassImage() {
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int clr = image.getRGB(x, y);
				int pixelValue = (clr & 0x00ff0000) >> 16;
				
				if(pixelValue<=LOW_PASS_VALUE){
					image.setRGB(x, y, clr);
				} else {
					Color color = new Color(0, 0, 0);
					int rgb = color.getRGB();

					image.setRGB(x, y, rgb);
				}

				
			}
		}

		return image;
	}
}
