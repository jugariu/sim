package com.sim.ui.panel;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RulesPanel extends JPanel {
	private static final long serialVersionUID = -4127693478136032629L;

	private JLabel colorRamps;
	private Map<String, List<String>> rules;

	public RulesPanel() {
		this.setBounds(0, 0, 720, 320);
		this.setLayout(null);
		this.setBackground(Color.white);

		rules = new HashMap<String, List<String>>();
		readRules();

		colorRamps = new JLabel();
		colorRamps.setBounds(0, 0, 100, 300);
		colorRamps.setIcon(resizeIcon(new ImageIcon(ClassLoader.class .getResource("/Images/colors.jpg").getFile().substring(1)), 100, 300));
		this.add(colorRamps);
		
		final JTextArea rulesArea = new JTextArea();
		rulesArea.setBounds(260, 10, 650, 280);
		rulesArea.setEditable(false);
		this.add(rulesArea);
		
		int i=1;
		for(final String name : rules.keySet()){
			JButton button = new JButton();
			button.setBounds(105, i*25 + 5, 150, 25);
			button.setBackground(Color.white);
			button.setText(name);
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String setOfRules = "";
					for(String r : rules.get(name)){
						setOfRules = setOfRules + "\n" + r;
					}
					rulesArea.setText(setOfRules);
				}
			});
			
			this.add(button);
			
			i++;
		}

		this.setVisible(true);
	}

	private void readRules() {
		String rulesPath = ClassLoader.class.getResource("/Rules.txt").getFile().substring(1);
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(rulesPath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] aux = strLine.split("#");
				String name = aux[0];
				
				rules.put(name, Arrays.asList(aux[1].split("&&")));
			}

			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private ImageIcon resizeIcon(ImageIcon icon, int x, int y) {
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		return icon;
	}
}
