package com.sim.ui.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.sim.image.filters.impl.GrayScaleFilter;
import com.sim.image.filters.impl.HighPassFilter;
import com.sim.image.filters.impl.LowPassFilter;
import com.sim.image.fusion.impl.AverageImageFusion;
import com.sim.image.fusion.impl.DiscreteWaveletTransformImageFusion;
import com.sim.image.fusion.impl.SelectMaximumImageFusion;
import com.sim.image.fusion.impl.SelectMinimumImageFusion;
import com.sim.image.lut.SevenRampsLUT;
import com.sim.image.utilities.FolderUtilities;
import com.sim.ui.components.ImageSelector;
import com.sim.ui.components.ProcessListComponents;
import com.sim.ui.components.ScrollableLogArea;
import com.sim.ui.panel.InfoPanel;
import com.sim.ui.panel.ProcessPanel;

public class MainFrame extends JFrame{
	private static final long serialVersionUID = -6218474616114970375L;

	public MainFrame() {
		this.setSize(1015, 540);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		FolderUtilities folderUtils = new FolderUtilities();
		String workingDir = folderUtils.createWorkDirectory();
		
		Map<String, Class<?>> processMap = new HashMap<String, Class<?>>();
		processMap.put("Average Image Fusion", AverageImageFusion.class);
		processMap.put("Select Maximum Image Fusion", SelectMaximumImageFusion.class);
		processMap.put("Select Minimum Image Fusion", SelectMinimumImageFusion.class);
		processMap.put("7 Ramps LUT", SevenRampsLUT.class);
		processMap.put("Gray Scale Filter", GrayScaleFilter.class);
		processMap.put("High Pass Filter", HighPassFilter.class);
		processMap.put("Low Pass Filter", LowPassFilter.class);
		processMap.put("dwt", DiscreteWaveletTransformImageFusion.class);
				
		ScrollableLogArea log = new ScrollableLogArea();

		ProcessListComponents processListComponents = new ProcessListComponents(log);
		processListComponents.setDropDownList(new ArrayList<String>(processMap.keySet()));

		ImageSelector firstSelector = new ImageSelector(10, 10);
		ImageSelector secondSelector = new ImageSelector(10, 45);
		
		this.add(new ProcessPanel(processListComponents, firstSelector, secondSelector));
		this.add(new InfoPanel(processListComponents, log, processMap, workingDir, firstSelector, secondSelector));
		
		this.setVisible(true);
	}
}
