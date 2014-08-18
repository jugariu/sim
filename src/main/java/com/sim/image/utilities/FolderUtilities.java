package com.sim.image.utilities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FolderUtilities {

	private final static String BASE_PATH = "C:/SIM";
	private final static String FILE_SEPARATOR = "/";

	public String createWorkDirectory() {
		File baseDir = new File(BASE_PATH);
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		File workingDir = new File(BASE_PATH + FILE_SEPARATOR + timeStamp);
		workingDir.mkdirs();
		
		return workingDir.getAbsolutePath();
	}
}
