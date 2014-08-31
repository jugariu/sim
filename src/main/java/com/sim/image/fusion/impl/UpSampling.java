package com.sim.image.fusion.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class UpSampling {
	private String firstImagePath;
	private String secondImagePath;
	private BufferedImage firstImage;
	private BufferedImage secondImage;
	private String workingDirPath;

	private static final String PROCESS = "upSampling";
	public static final String PROCESS_TYPE = "twoImageProcessor";

	public UpSampling(String workingDirPath) {
		this.workingDirPath = workingDirPath;
	}

	public void readImages(String firstImagePath, String secondImagePath) {
		this.firstImagePath = firstImagePath;
		this.secondImagePath = secondImagePath;

		try {
			firstImage = ImageIO.read(new File(this.firstImagePath));
			secondImage = ImageIO.read(new File(this.secondImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String processImages(String firstImagePath, String secondImagePath, String number) {
		readImages(firstImagePath, secondImagePath);
		BufferedImage resultedImage = doUpSampling();
		String resultedPath = exportImage(resultedImage, number);
		
		return resultedPath;
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

	private BufferedImage doUpSampling() {
		for (int y = 0; y < firstImage.getHeight(); y++) {
			for (int x = 0; x < firstImage.getWidth(); x++) {
				int clr1 = firstImage.getRGB(x, y);
				int value1 = (clr1 & 0x00ff0000) >> 16;

				int clr2 = secondImage.getRGB(x, y);
				int value2 = (clr2 & 0x00ff0000) >> 16;

				if(value1 != 0){
					secondImage.setRGB(x, y, clr1);
					
				} else if(value2 != 0){
					secondImage.setRGB(x, y, clr2);
					
				} else {
					Color color = new Color(0, 0,0);
					int rgb = color.getRGB();
					secondImage.setRGB(x, y, rgb);
				}
			}
		}

		return secondImage;
	}
}
