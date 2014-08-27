package com.sim.image.fusion.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.sim.image.filters.impl.HighPassFilter;
import com.sim.image.filters.impl.LowPassFilter;

public class DiscreteWaveletTransformImageFusion {
	private String firstImagePath;
	private String secondImagePath;
	private BufferedImage firstImage;
	private BufferedImage secondImage;
	private String workingDirPath;
	
	private static final int DWT_VALUE = 4;
	private HighPassFilter HPFilter;
	private LowPassFilter LPFilter;

	private static final String PROCESS = "discreteWaveletTransformImageFusion";
	public static final String PROCESS_TYPE = "twoImageProcessor";

	public DiscreteWaveletTransformImageFusion(String workingDirPath) {
		this.workingDirPath = workingDirPath;
		HPFilter = new HighPassFilter(workingDirPath);
		LPFilter = new LowPassFilter(workingDirPath);
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

	public String processImages(String firstImagePath, String secondImagePath) {
		readImages(firstImagePath, secondImagePath);
		BufferedImage resultedImage = null;

		//TODO Create a recursive function to do the high-low filter decomposition.
		
		return "";//exportedImagePath;
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
}
