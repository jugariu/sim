package com.sim.ui.panel;

import javax.swing.JPanel;

import com.sim.ui.components.ImageSelector;
import com.sim.ui.components.ProcessListComponents;

public class ProcessPanel extends JPanel{
	private static final long serialVersionUID = -8123224080034010377L;

	private ProcessListComponents processListComponents;

	public ProcessPanel(ProcessListComponents processListComponents, ImageSelector firstSelector, ImageSelector secondSelector) {
		this.setBounds(0, 0, 400, 500);
		this.setLayout(null);
		this.processListComponents = processListComponents;
		
		this.add(this.processListComponents);
		this.add(firstSelector);
		this.add(secondSelector);
		
		this.setVisible(true);
	}
}
