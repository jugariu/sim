package com.sim.image.fusion.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang.time.StopWatch;

import com.sim.ui.components.ScrollableLogArea;

public class AverageImageFusion {

	private String firstImagePath;
	private String secondImagePath;
	private BufferedImage firstImage;
	private BufferedImage secondImage;
	private String workingDirPath;

	private static final String PROCESS = "averageImageFusion";
	public static final String PROCESS_TYPE = "twoImageProcessor";
	
	private ScrollableLogArea log;

	public AverageImageFusion(String workingDirPath, ScrollableLogArea log) {
		this.workingDirPath = workingDirPath;
		this.log = log;
		
		log.setLoggedClass(AverageImageFusion.class.getName());
	}

	public void readImages(String firstImagePath, String secondImagePath) {
		this.firstImagePath = firstImagePath;
		this.secondImagePath = secondImagePath;

		try {
			firstImage = ImageIO.read(new File(this.firstImagePath));
			log.info("First image was loaded.");
			secondImage = ImageIO.read(new File(this.secondImagePath));
			log.info("Second image was loaded.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String processImages(String firstImagePath, String secondImagePath) {
		StopWatch cronometer = new StopWatch();
		cronometer.start();
		log.info("Average Image Fusion processing started.");
		
		readImages(firstImagePath, secondImagePath);
		BufferedImage resultedImage = null;

		int firstImageSize = firstImage.getWidth() * firstImage.getHeight();
		int secondImageSize = secondImage.getWidth() * secondImage.getHeight();

		if (firstImageSize != secondImageSize) {
			if (firstImageSize > secondImageSize) {
				int xDiff = (int) Math.round((double)firstImage.getWidth() / (double)secondImage.getWidth());
				int yDiff = (int) Math.round((double)firstImage.getHeight() / (double)secondImage.getHeight());				
				resultedImage = getAverageImageFusionDiff(firstImage, secondImage, xDiff, yDiff);
			} else {
				int xDiff =  (int) Math.round((double)secondImage.getWidth() / (double)firstImage.getWidth());
				int yDiff = (int) Math.round((double)secondImage.getHeight() / (double)firstImage.getHeight());
				resultedImage = getAverageImageFusionDiff(secondImage, firstImage, xDiff, yDiff);
			}
		} else {
			resultedImage = getAverageImageFusion();
		}

		String exportedImagePath = exportImage(resultedImage);
		
		cronometer.stop();
		log.info("Average Image Fusion processing finished in " + cronometer.getTime() + "ms.");
		
		return exportedImagePath;
	}
	
	public String processImages(String firstImagePath, String secondImagePath, String number) {
		StopWatch cronometer = new StopWatch();
		cronometer.start();
		log.info("Average Image Fusion processing started.");
		
		readImages(firstImagePath, secondImagePath);
		BufferedImage resultedImage = null;

		int firstImageSize = firstImage.getWidth() * firstImage.getHeight();
		int secondImageSize = secondImage.getWidth() * secondImage.getHeight();

		if (firstImageSize != secondImageSize) {
			if (firstImageSize > secondImageSize) {
				int xDiff = (int) Math.round((double)firstImage.getWidth() / (double)secondImage.getWidth());
				int yDiff = (int) Math.round((double)firstImage.getHeight() / (double)secondImage.getHeight());				
				resultedImage = getAverageImageFusionDiff(firstImage, secondImage, xDiff, yDiff);
			} else {
				int xDiff =  (int) Math.round((double)secondImage.getWidth() / (double)firstImage.getWidth());
				int yDiff = (int) Math.round((double)secondImage.getHeight() / (double)firstImage.getHeight());
				resultedImage = getAverageImageFusionDiff(secondImage, firstImage, xDiff, yDiff);
			}
		} else {
			resultedImage = getAverageImageFusion();
		}

		String exportedImagePath = exportImage(resultedImage, number);
		
		cronometer.stop();
		log.info("Average Image Fusion processing finished in " + cronometer.getTime() + "ms.");
		
		return exportedImagePath;
	}

	public String exportImage(BufferedImage resultedImage) {
		File outputImage = new File(workingDirPath + "/" + PROCESS + ".jpg");
		try {
			ImageIO.write(resultedImage, "jpg", outputImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputImage.getAbsolutePath();
	}
	
	public String exportImage(BufferedImage resultedImage, String number) {
		File outputImage = new File(workingDirPath + "/" + PROCESS + number + ".jpg");
		try {
			ImageIO.write(resultedImage, "jpg", outputImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputImage.getAbsolutePath();
	}

	private BufferedImage getAverageImageFusion() {
		for (int y = 0; y < firstImage.getHeight(); y++) {
			for (int x = 0; x < firstImage.getWidth(); x++) {
				int clr1 = firstImage.getRGB(x, y);
				int red1 = (clr1 & 0x00ff0000) >> 16;
				int green1 = (clr1 & 0x0000ff00) >> 8;
				int blue1 = clr1 & 0x000000ff;

				int clr2 = secondImage.getRGB(x, y);
				int red2 = (clr2 & 0x00ff0000) >> 16;
				int green2 = (clr2 & 0x0000ff00) >> 8;
				int blue2 = clr2 & 0x000000ff;

				int redFusionedValue = (red1 + red2) / 2;
				int greenFusionedValue = (green1 + green2) / 2;
				int blueFusionedValue = (blue1 + blue2) / 2;

				Color color = new Color(redFusionedValue, greenFusionedValue,
						blueFusionedValue);
				int rgb = color.getRGB();

				secondImage.setRGB(x, y, rgb);
			}
		}

		return secondImage;
	}

	private BufferedImage getAverageImageFusionDiff(BufferedImage first, BufferedImage second, int xDiff, int yDiff) {
		for (int y = 0; y < second.getHeight(); y++) {
			for (int x = 0; x < second.getWidth(); x++) {
				try{
					List<Integer> redList = new ArrayList<Integer>();
					List<Integer> greenList = new ArrayList<Integer>();
					List<Integer> blueList = new ArrayList<Integer>();
					
					int clr2 = second.getRGB(x, y);
					int red2 = (clr2 & 0x00ff0000) >> 16;
					redList.add(red2);
					int green2 = (clr2 & 0x0000ff00) >> 8;
					greenList.add(green2);
					int blue2 = clr2 & 0x000000ff;
					blueList.add(blue2);
					
					getImageValues(redList, greenList, blueList, x, y, first, second, xDiff, yDiff);
					
					int redFusionedValue = getAverage(redList);
					int greenFusionedValue = getAverage(greenList);
					int blueFusionedValue = getAverage(blueList);

					Color color = new Color(redFusionedValue, greenFusionedValue, blueFusionedValue);
					int rgb = color.getRGB();

					second.setRGB(x, y, rgb);
				}catch(Exception e){
					
				}
			}
		}
		
		return second;
	}
	
	private void getImageValues(List<Integer> redList, List<Integer> greenList, List<Integer> blueList, int x, int y, BufferedImage first, BufferedImage second, int xDiff, int yDiff){
		
		for(int i=0;i<xDiff;i++){
			for(int j=0;j<yDiff;j++){

				int clr = first.getRGB(((x*xDiff))+i, ((y*yDiff))+j);
				int red = (clr & 0x00ff0000) >> 16;
				redList.add(red);
				int green = (clr & 0x0000ff00) >> 8;
				greenList.add(green);
				int blue = clr & 0x000000ff;
				blueList.add(blue);
			}
		}
	}
	
	private int getAverage(List<Integer> a){
		int average = 0;
		for(Integer i : a){
			average += i;
		}
		return average/a.size();
	}
}
