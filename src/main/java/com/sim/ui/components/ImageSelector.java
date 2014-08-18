package com.sim.ui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageSelector extends JPanel{
	private static final long serialVersionUID = -3769230932649133448L;
	
	private final static String FILE_CHOOSER_TITLE ="Select file";
	private final static String C_PATH = "C:/";
	
	private String selectedFilePath;
	private JButton browse;
	private JTextField text;
	
	public ImageSelector(int x, int y) {
		this.setBackground(Color.white);
		this.setBounds(x, y, 380, 30);
		this.setLayout(null);
		setSelectedFilePath(C_PATH);
		
		text = new JTextField(C_PATH);
		text.setEditable(false);
		text.setBounds(0, 0, 300, 30);
		this.add(text);
		
		browse = new JButton("Browse");
		browse.setBounds(300, 0, 80, 30);
		browse.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				selectImage();
			}
		});
		this.add(browse);
		
		this.setVisible(true);
	}
	
	private void selectImage(){
		JFileChooser fileChooser = new JFileChooser();
		FileFilter jpegFilter = new FileNameExtensionFilter("TIFF File","tif");
		
		fileChooser.setDialogTitle(FILE_CHOOSER_TITLE);
		fileChooser.setCurrentDirectory(new File(C_PATH));
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.addChoosableFileFilter(jpegFilter);
		
		if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
			setSelectedFilePath(fileChooser.getSelectedFile().getAbsolutePath());
			text.setText(fileChooser.getSelectedFile().getAbsolutePath());
		}
	}

	public String getSelectedFilePath() {
		return selectedFilePath;
	}

	public void setSelectedFilePath(String selectedFilePath) {
		this.selectedFilePath = selectedFilePath;
	}
}
