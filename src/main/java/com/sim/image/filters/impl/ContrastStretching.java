package com.sim.image.filters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.time.StopWatch;

import com.sim.image.filters.Filter;
import com.sim.ui.components.ScrollableLogArea;

public class ContrastStretching implements Filter {

	private String workingDirPath;
	private String imagePath;
	private BufferedImage image;
	private String imageName;
	
	private int lMax;
	private int lMin;
	private static final int MP=255;

	private static final String PROCESS = "contrastStretchingFilter";
	public static final String PROCESS_TYPE = "oneImageProcessor";
	
	private ScrollableLogArea log;

	public ContrastStretching(String workingDirPath, ScrollableLogArea log) {
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
		log.info("Contrast Stretching processing started.");
		
		readImage(imagePath);
		BufferedImage resultedImage = getContrastStretchingImage();
		String exportedImagePath = exportImage(resultedImage);
		
		cronometer.stop();
		log.appendInfo("Contrast Stretching processing finished in " + cronometer.getTime() + "ms.");
		
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
	
	public BufferedImage getContrastStretchingImage(){
		setLimits();
		log.appendInfo("The limits for Contrast Stretching: Max=" + getlMax() + " Min=" + getlMin() + ".");
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int clr = image.getRGB(x, y);
				int pixelValue = clr & 0x000000ff;
				
				float contrastStretchingValue = ((float)(pixelValue - getlMin()) / (getlMax() - getlMin()))*MP;
				Color color = new Color(Math.round(contrastStretchingValue), Math.round(contrastStretchingValue), Math.round(contrastStretchingValue));
				int rgb = color.getRGB();

				image.setRGB(x, y, rgb);
			}
		}

		return image;
	}
	
	private void setLimits(){
		setlMax(0);
		setlMin(255);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int clr = image.getRGB(x, y);
				int pixelValue = clr & 0x000000ff;

				if(pixelValue>lMax)
					setlMax(pixelValue);
				if(pixelValue<lMin)
					setlMin(pixelValue);
				
			}
		}
	}

	public int getlMax() {
		return lMax;
	}

	public void setlMax(int lMax) {
		this.lMax = lMax;
	}

	public int getlMin() {
		return lMin;
	}

	public void setlMin(int lMin) {
		this.lMin = lMin;
	}
}
