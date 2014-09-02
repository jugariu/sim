package com.sim.ui.components;

import javax.swing.JScrollPane;

public class ScrollableLogArea extends JScrollPane {
	private static final long serialVersionUID = 1L;

	private static final LogArea logArea = new LogArea();
	private String loggedClass;

	public ScrollableLogArea() {
		super(logArea);
		initializeUI();
	}

	private void initializeUI() {
		this.setBounds(0, 200, 600, 300);
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.setVisible(true);
	}

	public void error(String... errorMessage) {
		if (errorMessage.length == 1) {
			logArea.error(getLoggedClass(), errorMessage[0]);
		} else {
			logArea.error(getLoggedClass(), errorMessage[0], errorMessage[1]);
		}
		this.update(getGraphics());
	}

	public void info(String infoMessage) {
		logArea.info(infoMessage);
		this.update(getGraphics());
	}

	public void appendInfo(String infoMessage) {
		logArea.appendInfo(infoMessage);
		this.update(getGraphics());
	}

	public String getLoggedClass() {
		return loggedClass;
	}

	public void setLoggedClass(String loggedClass) {
		this.loggedClass = loggedClass;
	}
}
