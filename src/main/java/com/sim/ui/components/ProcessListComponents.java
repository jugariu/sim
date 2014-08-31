package com.sim.ui.components;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sim.ui.panel.DWTPanel;


public class ProcessListComponents extends JPanel{
	private static final long serialVersionUID = 8310366530797511046L;

	private List<String> dropDownList;
	private List<JComboBox<String>> dropDownBoxes;
	private List<JLabel> steps;
	private JLabel stepLabel;
	private ScrollableLogArea log;
	private Map<String, Class<?>> processMap;
	private Font font = new Font("Verdana",Font.BOLD,12); 
	private DWTPanel dwtPanel;
	
	public ProcessListComponents(ScrollableLogArea log, DWTPanel dwtPanel) {
		this.setBounds(10, 100, 380, 390);
		this.setLayout(null);
		this.dwtPanel = dwtPanel;
		this.log = log;
		log.setLoggedClass(ProcessListComponents.class.getName());
		
		dropDownBoxes = new ArrayList<JComboBox<String>>();
		steps = new ArrayList<JLabel>();
		
		stepLabel = new JLabel("PROCESS STEPS");
		stepLabel.setFont(font);
		stepLabel.setBounds(120, 0, 120, 40);
		this.add(stepLabel);		
		
		this.setVisible(true);
	}

	public void addDropDown(){
		this.setVisible(false);
		
		final JComboBox<String> dropDown = new JComboBox<String>(getDropDownList().toArray(new String[getDropDownList().size()]));
		dropDown.setBounds(60, (getDropDownBoxes().size()*30)+40, 300, 30);
		dropDown.setSelectedIndex(-1);

		dropDown.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				if(dropDown.getSelectedItem().equals("Discrete Wavelet Transform")){
					dwtPanel.setVisible(true);
				} else {
					dwtPanel.setVisible(false);
				}
			}
		});
		
		JLabel step = new JLabel("Step " + (getDropDownBoxes().size()+1) + ":");
		step.setBounds(0, (getSteps().size()*30)+40, 60, 30);
		step.setFont(font);
		
		getDropDownBoxes().add(dropDown);
		getSteps().add(step);
		
		this.add(dropDown);
		this.add(step);
		
		this.setVisible(true);
	}
	
	public void removeDropDown(){
		this.setVisible(false);
		
		JComboBox<String> comboBox = getDropDownBoxes().get(getDropDownBoxes().size()-1);
		getDropDownBoxes().remove(getDropDownBoxes().size()-1);
		
		JLabel step = getSteps().get(getSteps().size()-1);
		getSteps().remove(getSteps().size()-1);
		
		this.remove(comboBox);
		this.remove(step);
		
		this.setVisible(true);
	}

	public List<String> getProcessList(){
		List<String> processList = new ArrayList<String>();
		try{
			for(JComboBox<String> comboBox : getDropDownBoxes()){
				if(comboBox.getSelectedItem() == null){
					throw new NullPointerException("Please add process for \"Step "+(getDropDownBoxes().indexOf(comboBox)+1)+"\".");
				}
				processList.add((String) comboBox.getSelectedItem());
			}
		}catch (NullPointerException npe){
			log.error(npe.getMessage());
			processList = new ArrayList<String>();
		}
		
		return processList;
	}
	
	public List<String> getDropDownList() {
		return dropDownList;
	}

	public void setDropDownList(List<String> dropDownList) {
		this.dropDownList = dropDownList;
	}

	public List<JComboBox<String>> getDropDownBoxes() {
		return dropDownBoxes;
	}

	public void setDropDownBoxes(List<JComboBox<String>> dropDownBoxes) {
		this.dropDownBoxes = dropDownBoxes;
	}

	public List<JLabel> getSteps() {
		return steps;
	}

	public void setSteps(List<JLabel> steps) {
		this.steps = steps;
	}
}
