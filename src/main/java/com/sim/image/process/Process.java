package com.sim.image.process;

import com.sim.image.filters.impl.GrayScaleFilter;
import com.sim.image.fusion.impl.AverageImageFusion;
import com.sim.image.fusion.impl.SelectMaximumImageFusion;
import com.sim.image.fusion.impl.SelectMinimumImageFusion;
import com.sim.image.lut.SevenRampsLUT;

public enum Process {
	AVERAGE_IMAGE_FUSION("Average Image Fusion", AverageImageFusion.class),
	MAXIMUM_IMAGE_FUSION("Select Maximum Image Fusion", SelectMaximumImageFusion.class),
	MINIMUM_IMAGE_FUSION("Select Minimum Image Fusion", SelectMinimumImageFusion.class),
	SEVEN_RAMPS_LUT("7 Ramps LUT", SevenRampsLUT.class),
	GRAY_SCALE_FILTER("Grayscale Filter", GrayScaleFilter.class);
	
	private String name;
	private Class<?> process;
	
	private Process(String name, Class<?> process) {
		this.name = name;
		this.process = process;
	}
	
	public String getName(){
		return name;
	}
	
	public Class<?> getProcess(){
		return process;
	}
}
