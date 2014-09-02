package com.sim.image.filters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.time.StopWatch;

import com.sim.ui.components.ScrollableLogArea;

public class SaltAndPepper {

	private String workingDirPath;
	private String imagePath;
	private BufferedImage image;
	private String imageName;

	private static final String PROCESS = "saltAndPepperFilter";
	public static final String PROCESS_TYPE = "oneImageProcessor";
	
	private ScrollableLogArea log;

	public SaltAndPepper(String workingDirPath, ScrollableLogArea log) {
		this.workingDirPath = workingDirPath;
		this.log = log;
		
		log.setLoggedClass(HighPassFilter.class.getName());
	}
	
	public void readImage(String imagePath) {
		this.imagePath = imagePath;
		
		try {
			File file = new File(this.imagePath);
			imageName = file.getName().substring(0, file.getName().length()-4) + "_";
			image = ImageIO.read(file);
		} catch (IOException e) {
			log.error("Could not read image.", e.getMessage());
			e.printStackTrace();
		}
	}

	public String processImage(String imagePath) {
		StopWatch cronometer = new StopWatch();
		cronometer.start();
		log.info("Salt&Pepper Filter processing started.");
		
		readImage(imagePath);
		BufferedImage resultedImage = removeSaltAndPepperNoise();
		String exportedImagePath = exportImage(resultedImage);
		
		cronometer.stop();
		log.appendInfo("Salt&Pepper Filter processing finished in " + cronometer.getTime() + "ms.");
		
		return exportedImagePath;
		
	}
	
	public BufferedImage removeSaltAndPepperNoise() {
		for (int y = 1; y < image.getHeight() - 1; y++) {
			for (int x = 1; x < image.getWidth() - 1; x++) {
				int center = image.getRGB(x, y);
				int centerColor = (center & 0x00ff0000) >> 16;

				if (centerColor > 200 || centerColor < 50) {
					int left = image.getRGB(x - 1, y);
					int right = image.getRGB(x + 1, y);
					int down = image.getRGB(x, y + 1);
					int up = image.getRGB(x, y - 1);

					int leftColor = (left & 0x00ff0000) >> 16;
					int rightColor = (right & 0x00ff0000) >> 16;
					int downColor = (down & 0x00ff0000) >> 16;
					int upColor = (up & 0x00ff0000) >> 16;

					int value = (leftColor + rightColor + upColor + downColor) / 4;
					Color color = new Color(value, value, value);
					int rgb = color.getRGB();

					image.setRGB(x, y, rgb);
				} else {
					image.setRGB(x, y, center);
				}

			}
		}

		return image;
	}

	public String exportImage(BufferedImage resultedImage) {
		File outputImage = new File(workingDirPath + "/" + imageName + PROCESS + ".jpg");
		try {
			ImageIO.write(resultedImage, "jpg", outputImage);
		} catch (IOException e) {
			log.error("Could not save intermediate image.", e.getMessage());
			e.printStackTrace();
		}
		
		return outputImage.getAbsolutePath();
	}
}
