package com.adelerobots.fioneg.util;

import java.io.File;
import java.io.FilenameFilter;

public class IniFileNameFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return name.endsWith(".ini");
	}

}
