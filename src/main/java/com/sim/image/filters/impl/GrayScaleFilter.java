package com.sim.image.filters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sim.image.filters.Filter;
import com.sim.image.fusion.impl.AverageImageFusion;
import com.sim.ui.components.ScrollableLogArea;

public class GrayScaleFilter implements Filter{

	private String workingDirPath;
	private String imagePath;
	private BufferedImage image;
	private String imageName;

	private static final String PROCESS = "grayScaleFilter";
	public static final String PROCESS_TYPE = "oneImageProcessor";
	
	private ScrollableLogArea log;

	public GrayScaleFilter(String workingDirPath, ScrollableLogArea log) {
		this.workingDirPath = workingDirPath;
		this.log = log;
		
		log.setLoggedClass(GrayScaleFilter.class.getName());
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
		readImage(imagePath);
		BufferedImage resultedImage = getGrayScaleImage();
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
	
	private BufferedImage getGrayScaleImage() {
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int clr = image.getRGB(x, y);
				int red = (clr & 0x00ff0000) >> 16;
				int green = (clr & 0x0000ff00) >> 8;
				int blue = clr & 0x000000ff;

				int grayScaleValue = (red + green + blue) / 3;

				Color color = new Color(grayScaleValue, grayScaleValue, grayScaleValue);
				int rgb = color.getRGB();

				image.setRGB(x, y, rgb);
			}
		}

		return image;
	}

}
