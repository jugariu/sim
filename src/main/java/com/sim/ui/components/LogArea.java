package com.sim.ui.components;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class LogArea extends JTextPane {
	private static final long serialVersionUID = 1L;

	public LogArea() {
		this.setVisible(true);
	}

	protected void info(String message) {
		this.setEditable(true);
		append(Color.blue, "\n[INFO]  ");
		append(Color.blue, message);
		this.setEditable(false);
	}

	protected void error(String className, String message) {
		this.setEditable(true);
		append(Color.red, "\n[ERROR]  ");
		append(Color.red, className + ": ");
		append(Color.red, message);
		this.setEditable(false);
	}

	protected void error(String className, String message, String exceptionMessage) {
		this.setEditable(true);
		append(Color.red, "\n[ERROR]  ");
		append(Color.red, className + ": ");
		append(Color.red, message);
		append(Color.red, " "+ exceptionMessage);
		this.setEditable(false);
	}

	private void append(Color c, String msg) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
				StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = this.getDocument().getLength();
		this.setCaretPosition(len);
		this.setCharacterAttributes(aset, false);
		this.replaceSelection(msg);
	}
}
