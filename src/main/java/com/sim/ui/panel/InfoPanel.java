package com.sim.ui.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sim.ui.components.ImageSelector;
import com.sim.ui.components.ProcessListComponents;
import com.sim.ui.components.ScrollableLogArea;
import com.sim.ui.frame.ImageFrame;

public class InfoPanel extends JPanel{
	private static final long serialVersionUID = 4358201489198956131L;
	
	private ProcessListComponents processListComponents;
	private JButton addComponent;
	private JButton removeComponent;
	private JButton startProcess;
	private ScrollableLogArea log;
	private Map<String, Class<?>> processMap;
	private String workingDir;
	
	private ImageSelector firstImageSelector;
	private ImageSelector secondImageSelector;

	private JLabel earth;
	private JLabel satelite;
	
	
	public InfoPanel(final ProcessListComponents processListComponents, final ScrollableLogArea log, final Map<String, Class<?>> processMap, final String workingDir, final ImageSelector firstImageSelector, final ImageSelector secondImageSelector, final DWTPanel dwtPanel) {
		this.setBounds(400, 0, 600, 500);
		this.setLayout(null);
		this.processMap = processMap;
		this.workingDir = workingDir;
		this.firstImageSelector = firstImageSelector;
		this.secondImageSelector = secondImageSelector;
		
		this.processListComponents = processListComponents;
		this.log = log;
		log.setLoggedClass(InfoPanel.class.getName());
		this.add(log);

		earth = new JLabel();
		earth.setBounds(420, 5, 170, 170);
		earth.setIcon(resizeIcon(new ImageIcon(ClassLoader.class.getResource("/Images/earth.png").getFile().substring(1)), 170, 170));
		this.add(earth);
		satelite = new JLabel();
		satelite.setBounds(320, 5, 100, 100);
		satelite.setIcon(resizeIcon(new ImageIcon(ClassLoader.class.getResource("/Images/satelit.png").getFile().substring(1)), 100, 100));
		this.add(satelite);
		
		addComponent = new JButton("Add");
		removeComponent = new JButton("Remove");
		startProcess = new JButton("Start");
		
		addComponent.setBounds(150, 10, 150, 30);
		this.add(addComponent);
		addComponent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processListComponents.addDropDown();
				log.info("Added new step.");
			}
		});
		
		removeComponent.setBounds(150, 45, 150, 30);
		this.add(removeComponent);
		removeComponent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processListComponents.removeDropDown();
				log.info("Removed step.");
			}
		});
		
		this.add(dwtPanel);
		this.setVisible(false);
		
		startProcess.setBounds(150, 80, 150, 30);
		startProcess.setForeground(Color.red);
		Font font = new Font("Broadway",Font.BOLD,16); 
		startProcess.setFont(font);
		this.add(startProcess);
		startProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(dwtPanel.getDecompositionFactor() <= 0 || dwtPanel.getDecompositionFactor() > 5) {
					log.error("Please choose a decomposition factor between 1 and 5.");
				} else {
					log.info("Start processing.");
					String firstPath = firstImageSelector.getSelectedFilePath();
					String secondPath = secondImageSelector.getSelectedFilePath();
					
					String finalResult = null;
					
					if(firstPath.equals("C:/") && secondPath.equals("C:/")){
						/**
						 * no initial images
						 */
						log.error("Please select initial image(s).");
					} else {
						/**
						 * 2 initial images
						 */
						if(!firstPath.equals("C:/") && !secondPath.equals("C:/")){
							String result = null;
							String result1 = null;
							String result2 = null;
							for(String processName : processListComponents.getProcessList()){
								try {
									//Creating instance of the process
									Class<?> processClass = processMap.get(processName);
									Constructor<?> processConstructor = processClass.getConstructor(String.class, ScrollableLogArea.class);
									Object processObject = processConstructor.newInstance(workingDir, log);
									
									//Getting the type of the process
									Field processTypeField = processObject.getClass().getField("PROCESS_TYPE");
									String processType = (String) processTypeField.get(String.class);

									//Start processing
									if(result == null){
										if(result1 == null && result2 == null){
											if(processType.equals("oneImageProcessor")){System.out.println("1");
												Method processMethod = processObject.getClass().getDeclaredMethod("processImage", String.class);
												result1 = (String) processMethod.invoke(processObject, firstImageSelector.getSelectedFilePath());
												result2 = (String) processMethod.invoke(processObject, secondImageSelector.getSelectedFilePath());
											}
											if(processType.equals("twoImageProcessor")){
												if(processName.equals("Discrete Wavelet Transform")){
													Method setDecomposition = processObject.getClass().getDeclaredMethod("setDecompositionFactor", Integer.class);
													setDecomposition.invoke(processObject, dwtPanel.getDecompositionFactor());	
													
													Method setFusion = processObject.getClass().getDeclaredMethod("setFusionType", Class.class);
													setFusion.invoke(processObject, dwtPanel.getFusionType());
												}
												Method processMethod = processObject.getClass().getDeclaredMethod("processImages", String.class, String.class);
												result = (String) processMethod.invoke(processObject, firstImageSelector.getSelectedFilePath(), secondImageSelector.getSelectedFilePath());							
											}										
										} else{
											String tempResult1 = result1;
											String tempResult2 = result2;
											if(processType.equals("oneImageProcessor")){System.out.println("2");
												Method processMethod = processObject.getClass().getDeclaredMethod("processImage", String.class);
												result1 = (String) processMethod.invoke(processObject, tempResult1);
												result2 = (String) processMethod.invoke(processObject, tempResult2);
											}
											if(processType.equals("twoImageProcessor")){
												if(processName.equals("Discrete Wavelet Transform")){
													Method setDecomposition = processObject.getClass().getDeclaredMethod("setDecompositionFactor", Integer.class);
													setDecomposition.invoke(processObject, dwtPanel.getDecompositionFactor());	
													
													Method setFusion = processObject.getClass().getDeclaredMethod("setFusionType", Class.class);
													setFusion.invoke(processObject, dwtPanel.getFusionType());
												}
												Method processMethod = processObject.getClass().getDeclaredMethod("processImages", String.class, String.class);
												result = (String) processMethod.invoke(processObject, tempResult1, tempResult2);							
											}
										}
									} else {
										String tempResult = result;
										Method processMethod = processObject.getClass().getDeclaredMethod("processImage", String.class);
										result = (String) processMethod.invoke(processObject, tempResult);
									}
									
								} catch (NoSuchMethodException e1) {
									e1.printStackTrace();
								} catch (SecurityException e1) {
									e1.printStackTrace();
								} catch (InstantiationException e1) {
									e1.printStackTrace();
								} catch (IllegalAccessException e1) {
									e1.printStackTrace();
								} catch (IllegalArgumentException e1) {
									e1.printStackTrace();
								} catch (InvocationTargetException e1) {
									e1.printStackTrace();
								} catch (NoSuchFieldException e1) {
									e1.printStackTrace();
								}
							}
							finalResult = result;
						} else {
							/**
							 * 1 initial images
							 */
							String path = (!firstPath.equals("C:/")) ? firstPath : secondPath;
							String result = null;
							for(String processName : processListComponents.getProcessList()){
								try {
									//Creating instance of the process
									Class<?> processClass = processMap.get(processName);
									Constructor<?> processConstructor = processClass.getConstructor(String.class);
									Object processObject = processConstructor.newInstance(workingDir);
									
									//Getting the type of the process
									Field processTypeField = processObject.getClass().getField("PROCESS_TYPE");
									String processType = (String) processTypeField.get(String.class);

									//Start processing
									if(result == null){
										if(processType.equals("oneImageProcessor")){
											Method processMethod = processObject.getClass().getDeclaredMethod("processImage", String.class);
											result = (String) processMethod.invoke(processObject, path);
										} else {
											log.error("Process " + processName + "requires 2 initial images.");
										}
									} else {
										String tempResult = result;
										Method processMethod = processObject.getClass().getDeclaredMethod("processImage", String.class);
										result = (String) processMethod.invoke(processObject, tempResult);
									}
									
								} catch (NoSuchMethodException e1) {
									e1.printStackTrace();
								} catch (SecurityException e1) {
									e1.printStackTrace();
								} catch (InstantiationException e1) {
									e1.printStackTrace();
								} catch (IllegalAccessException e1) {
									e1.printStackTrace();
								} catch (IllegalArgumentException e1) {
									e1.printStackTrace();
								} catch (InvocationTargetException e1) {
									e1.printStackTrace();
								} catch (NoSuchFieldException e1) {
									e1.printStackTrace();
								}
							}
							finalResult = result;
						}
					}
					new ImageFrame(finalResult);
				}
			}
		});
		
		
		
		this.setVisible(true);
	}
	
	private ImageIcon resizeIcon(ImageIcon icon, int x, int y){		
		Image img = icon.getImage() ;  
		Image newimg = img.getScaledInstance( x, y,  java.awt.Image.SCALE_SMOOTH ) ;  
		icon = new ImageIcon( newimg );
		
		return icon;
	}
}
