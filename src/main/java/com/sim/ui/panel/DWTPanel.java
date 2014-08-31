package com.sim.ui.panel;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sim.image.fusion.impl.AverageImageFusion;
import com.sim.image.fusion.impl.SelectMaximumImageFusion;
import com.sim.image.fusion.impl.SelectMinimumImageFusion;

public class DWTPanel extends JPanel{
	private static final long serialVersionUID = -2718702158167254046L;

	private JLabel decompositionLabel = new JLabel("Decomposition factor: ");
	private JLabel fusionLabel = new JLabel("DWT fusion type: ");
	private JTextField decompositionText = new JTextField();
	private JComboBox<String> fusionBox;
	
	private Map<String, Class<?>> processMap = new HashMap<String, Class<?>>();

	public DWTPanel() {
		this.setBounds(85, 120, 300, 100);
		this.setLayout(null);
		
		processMap.put("Average Image Fusion", AverageImageFusion.class);
		processMap.put("Select Maximum Image Fusion", SelectMaximumImageFusion.class);
		processMap.put("Select Minimum Image Fusion", SelectMinimumImageFusion.class);
		
		decompositionLabel.setBounds(0, 0, 130, 30);
		decompositionText.setBounds(130, 0, 60, 30);
		decompositionText.setText("1");
		this.add(decompositionLabel);
		this.add(decompositionText);
		
		fusionLabel.setBounds(0, 35, 100, 30);
		this.add(fusionLabel);
		
		fusionBox = new JComboBox<String>(processMap.keySet().toArray(new String[processMap.keySet().size()]));
		fusionBox.setBounds(100, 35, 180, 30);
		this.add(fusionBox);
		
		this.setVisible(false);
	}
	
	public int getDecompositionFactor(){
		return Integer.parseInt(decompositionText.getText());
	}
	
	public Class<?> getFusionType(){
		String name = fusionBox.getSelectedItem().toString();
		return processMap.get(name);
	} 
}
