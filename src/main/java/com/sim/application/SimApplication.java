package com.sim.application;

import com.sim.image.filters.impl.GrayScaleFilter;
import com.sim.image.fusion.impl.AverageImageFusion;
import com.sim.image.lut.SevenRampsLUT;
import com.sim.image.utilities.FolderUtilities;
import com.sim.ui.frame.MainFrame;

public class SimApplication {

	public static void main(String[] args) {
//		FolderUtilities folder = new FolderUtilities();
//		String work = folder.createWorkDirectory();
//
//		GrayScaleFilter filter = new GrayScaleFilter(work);
//		String firstImage = filter.processImage("C:/Users/Vlad/Desktop/L72154006_00620090721_B80.tif");
//		String secondImage = filter.processImage("C:/Users/Vlad/Desktop/L71154006_00620090721_B10.tif");
//		
//		AverageImageFusion fusion = new AverageImageFusion(work);
//		String path = fusion.processImages(firstImage, secondImage);
//
//		SevenRampsLUT lut = new SevenRampsLUT(work);
//		String path1 = lut.processImage(path);
		
		new MainFrame();
	}

}
