package com.sim.image.lut;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang.time.StopWatch;

import com.sim.image.fusion.impl.AverageImageFusion;
import com.sim.ui.components.ScrollableLogArea;

public class SevenRampsLUT {

	private String imagePath;
	private BufferedImage image;
	private String workingDirPath;

	private static final String PROCESS = "sevenRampsLUT";
	public static final String PROCESS_TYPE = "oneImageProcessor";
	
	private ScrollableLogArea log;
	
	public SevenRampsLUT(String workingDirPath, ScrollableLogArea log) {
		this.workingDirPath = workingDirPath;
		this.log = log;
		
		log.setLoggedClass(SevenRampsLUT.class.getName());
	}
	
	public void readImage(String imagePath){
		this.imagePath = imagePath;
		
		try {
			image = ImageIO.read(new File(this.imagePath));
		} catch (IOException e) {
			log.error("Could not read image.", e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String processImage(String imagePath){
		StopWatch cronometer = new StopWatch();
		cronometer.start();
		log.info("Pseudo-coloring processing started.");
		
		readImage(imagePath);
		BufferedImage resultedImage = getSevenRampsLUTImage();
		String exportedImagePath = exportImage(resultedImage);
		
		cronometer.stop();
		log.appendInfo("Pseudo-coloring processing finished in " + cronometer.getTime() + "ms.");
		
		return exportedImagePath;
	}
	
	public String exportImage(BufferedImage resultedImage) {
		File outputImage = new File(workingDirPath + "/" + PROCESS + ".jpg");
		try {
			ImageIO.write(resultedImage, "jpg", outputImage);
		} catch (IOException e) {
			log.error("Could not save intermediate image.", e.getMessage());
			e.printStackTrace();
		}
		
		return outputImage.getAbsolutePath();
	}
	
	private BufferedImage getSevenRampsLUTImage() {		
		BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int clr = image.getRGB(x, y);
				int grayScaleColor = (clr & 0x00ff0000) >> 16;
				
				if(grayScaleColor!=0){
					if (grayScaleColor <= 72) {
						Color color = new Color(255, (grayScaleColor * 3), 0);
						int rgb = color.getRGB();
						outputImage.setRGB(x, y, rgb);
					}
					if ((grayScaleColor > 72) && (grayScaleColor <= 108)) {
						int val = grayScaleColor - 72;
						Color color = new Color(255 - (val * 7), 255, 0);
						int rgb = color.getRGB();
						outputImage.setRGB(x, y, rgb);
					}
					if ((grayScaleColor > 108) && (grayScaleColor <= 144)) {
						int val = grayScaleColor - 108;
						Color color = new Color(0, 255, val * 7);
						int rgb = color.getRGB();
						outputImage.setRGB(x, y, rgb);
					}
					if ((grayScaleColor > 144) && (grayScaleColor <= 180)) {
						int val = grayScaleColor - 144;
						Color color = new Color(0, 255 - (val * 7), 255);
						int rgb = color.getRGB();
						outputImage.setRGB(x, y, rgb);
					}
					if ((grayScaleColor > 180) && (grayScaleColor <= 216)) {
						int val = grayScaleColor - 180;
						Color color = new Color(val * 7, 0, 255);
						int rgb = color.getRGB();
						outputImage.setRGB(x, y, rgb);
					}
					if ((grayScaleColor > 216) && (grayScaleColor <= 252)) {
						int val = grayScaleColor - 216;
						Color color = new Color(255, 0, 255 - (val * 7));
						int rgb = color.getRGB();
						outputImage.setRGB(x, y, rgb);
					}
				}
			}
		}
		
		return outputImage;
	}
}
