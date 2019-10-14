package com.seven20.picklejar.drivers;

import java.util.Arrays;
import java.util.List;

import com.seven20.picklejar.exceptions.UnsupportedBrowserException;

public enum Browser {
	CHROME("chrome"), EDGE("edge", "microsoftedge"), IE32("ie", "ie32", "iexplore", "iexplore32",
			"internetexplorer", "internet explorer", "internetexplorer32", "internet explorer32",
			"internet explorer 32"), IE64("ie64", "iexplore64", "internetexplorer64",
					"internet explorer64",
					"internet explorer 64"), PHANTOMJS("phantomjs", "phantom", "js", "ghost");

	private List<String> names;

	private Browser(String... names) {
		this.names = Arrays.asList(names);
	}

	public static Browser get(String name) {
		for (Browser b : Browser.values()) {
			if (b.names.contains(name.toLowerCase())) {
				return b;
			}
		}
		throw new UnsupportedBrowserException(name);
	}
}
