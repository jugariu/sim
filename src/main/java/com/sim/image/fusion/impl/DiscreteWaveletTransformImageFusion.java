package com.sim.image.fusion.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.time.StopWatch;

import com.sim.image.filters.impl.HighPassFilter;
import com.sim.image.filters.impl.LowPassFilter;
import com.sim.ui.components.ScrollableLogArea;

public class DiscreteWaveletTransformImageFusion {
	private String firstImagePath;
	private String secondImagePath;
	private BufferedImage firstImage;
	private BufferedImage secondImage;
	private String workingDirPath;
	
	private Integer decompositionFactor;
	private Class<?> fusionType;
	
	private HighPassFilter HPFilter;
	private LowPassFilter LPFilter;
	private UpSampling sampling;
	private Map<String, String> resultMap = new HashMap<String, String>();
	private Map<String, String> map1 = new HashMap<String, String>();
	private Map<String, String> map2 = new HashMap<String, String>();
	
	private static final String PROCESS = "discreteWaveletTransformImageFusion";
	public static final String PROCESS_TYPE = "twoImageProcessor";
	
	private String finalResult = null;
	
	private ScrollableLogArea log;

	public DiscreteWaveletTransformImageFusion(String workingDirPath, ScrollableLogArea log) {
		this.workingDirPath = workingDirPath;
		this.log = log;
		
		log.setLoggedClass(DiscreteWaveletTransformImageFusion.class.getName());
		HPFilter = new HighPassFilter(workingDirPath, log);
		LPFilter = new LowPassFilter(workingDirPath, log);
		sampling = new UpSampling(workingDirPath);
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
		log.setVisible(false);
		log.info("Discrete Wavelet Transform processing started.");
		log.setVisible(true);
		
		subSampling(firstImagePath, map1, "", getDecompositionFactor());
		subSampling(secondImagePath, map2, "", getDecompositionFactor());
		int i = 0;
		for( String key : map1.keySet() ){
			String img = "";
			Method processMethod;
			try {
				Constructor<?> processConstructor = getFusionType().getConstructor(String.class, ScrollableLogArea.class);
				Object processObject = processConstructor.newInstance(workingDirPath, log);
				processMethod = processObject.getClass().getDeclaredMethod("processImages", String.class, String.class, String.class);
				img = (String) processMethod.invoke(processObject, map1.get(key), map2.get(key), i+"");
			} catch (Exception e) {
				e.printStackTrace();
			}		
			
			resultMap.put(key, img);
			i++;
		}
		
		doUpSampling(resultMap, getDecompositionFactor());
		
		cronometer.stop();
		log.info("Discrete Wavelet Transform processing finished in " + cronometer.getTime() + "ms.");
		
		return finalResult;
	}
	
	private void doUpSampling(Map<String, String> map, int n){
			int j=0;
			Map<String, String> intermediateMap = map;
			Map<String, String> newMap = new HashMap<String, String>();
			for(int i=1; i<=n;i++){
				int length = intermediateMap.keySet().size();
				while (length>0) {
					String key1 = intermediateMap.keySet().iterator().next();
					String path1 = intermediateMap.get(key1);
					String key2 = "";
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
					length=length-2;
				}
				intermediateMap = newMap;
				newMap = new HashMap<String, String>();
			}
			finalResult =  intermediateMap.get(intermediateMap.keySet().iterator().next());
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

	public Integer getDecompositionFactor() {
		return decompositionFactor;
	}

	public void setDecompositionFactor(Integer decompositionFactor) {
		this.decompositionFactor = decompositionFactor;
	}

	public Class<?> getFusionType() {
		return fusionType;
	}

	public void setFusionType(Class<?> fusionType) {
		this.fusionType = fusionType;
	}
}
