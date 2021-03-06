package com.sim.image.filters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.time.StopWatch;

import com.sim.image.filters.Filter;
import com.sim.image.fusion.impl.AverageImageFusion;
import com.sim.ui.components.ScrollableLogArea;

public class HighPassFilter implements Filter {
	private String workingDirPath;
	private String imagePath;
	private BufferedImage image;
	private String imageName;
	
	private static final int HIGH_PASS_VALUE = 128;

	private static final String PROCESS = "highPassFilter";
	public static final String PROCESS_TYPE = "oneImageProcessor";
	
	private ScrollableLogArea log;

	public HighPassFilter(String workingDirPath, ScrollableLogArea log) {
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
		log.info("HighPass Filter processing started.");
		
		readImage(imagePath);
		BufferedImage resultedImage = getHighPassImage();
		String exportedImagePath = exportImage(resultedImage);
		
		cronometer.stop();
		log.appendInfo("HighPass Filter processing finished in " + cronometer.getTime() + "ms.");
		
		return exportedImagePath;
		
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
	
	private BufferedImage getHighPassImage() {
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int clr = image.getRGB(x, y);
				int pixelValue = (clr & 0x00ff0000) >> 16;
				
				if(pixelValue>=HIGH_PASS_VALUE){
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
