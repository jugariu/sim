package com.sim.ui.frame;

import javax.swing.JFrame;

import com.sim.ui.panel.RulesPanel;


public class RulesFrame extends JFrame{
	private static final long serialVersionUID = -4150186682302361260L;

	public RulesFrame() {
		this.setSize(720, 320);
		this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		this.add(new RulesPanel());
		
		this.setVisible(true);

	}
}
