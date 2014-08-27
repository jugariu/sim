package com.sim.image.fusion.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	private AverageImageFusion aif;
	private UpSampling sampling;
	private Map<String, String> resultMap = new HashMap<String, String>();
	private Map<String, String> map1 = new HashMap<String, String>();
	private Map<String, String> map2 = new HashMap<String, String>();
	
	private static final String PROCESS = "discreteWaveletTransformImageFusion";
	public static final String PROCESS_TYPE = "twoImageProcessor";
	
	private String finalResult = null;

	public DiscreteWaveletTransformImageFusion(String workingDirPath) {
		this.workingDirPath = workingDirPath;
		HPFilter = new HighPassFilter(workingDirPath);
		LPFilter = new LowPassFilter(workingDirPath);
		aif = new AverageImageFusion(workingDirPath);
		sampling = new UpSampling(workingDirPath);
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
		subSampling(firstImagePath, map1, "", 3);
		subSampling(secondImagePath, map2, "", 3);
		int i = 0;
		for( String key : map1.keySet() ){
			String img = aif.processImages(map1.get(key), map2.get(key), i+"");
			resultMap.put(key, img);
			i++;
		}
		
		doUpSampling(resultMap, 3, 0);
		
		return "";
	}
	
	private void doUpSampling(Map<String, String> intermediateMap, int n, int j){
		if(n==0){
			String key = intermediateMap.keySet().iterator().next();
			finalResult = intermediateMap.get(key);
			return;
		}else{
			Map<String, String> newMap = new HashMap<String, String>();
			while (intermediateMap.keySet().size()>1) {
				String key1 = intermediateMap.keySet().iterator().next();
				String key2 = "";
				String path1 = intermediateMap.get(key1);
				String newKey = "";
				if(key1.endsWith("_LowPass")){
					key2 = key1.substring(0, key1.length()-8)+"_HighPass";
					newKey = key1.substring(0, key1.length()-8);
				}
				if(key1.endsWith("_HighPass")){
					key2 = key1.substring(0, key1.length()-9)+"_LowPass";	
					newKey = key1.substring(0, key1.length()-9);		
				}
				String path2 = intermediateMap.get(key2);
				String path = sampling.processImages(path1, path2, j+"");
				j++;
				intermediateMap.remove(key1);
				intermediateMap.remove(key2);
				newMap.put(newKey, path);
			}
			doUpSampling(newMap, n--, j);
		}
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
	
	private void subSampling(String path, Map<String, String> map, String process, int n){
		if( n == 0 ){
			map.put(n+process, path);
			return ;
		} 
			String path1 = LPFilter.processImage(path);
			String path2 = HPFilter.processImage(path);
			n--;
			subSampling(path1, map, n+process+"_LowPass", n);
			subSampling(path2, map, n+process+"_HighPass", n);
		
	}
}
