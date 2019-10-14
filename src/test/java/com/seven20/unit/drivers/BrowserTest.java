package com.seven20.unit.drivers;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.seven20.picklejar.drivers.Browser;
import com.seven20.picklejar.exceptions.UnsupportedBrowserException;

public class BrowserTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void defaultsToIE32() {
		List<String> acceptedValues = Arrays.asList("ie", "ie32");
		assertSupported(acceptedValues, Browser.IE32);
	}

	@Test
	public void ignoresCase() {
		List<String> acceptedValues = Arrays.asList("chrome", "CHROME", "Chrome");
		assertSupported(acceptedValues, Browser.CHROME);
	}

	@Test
	public void chromeIsSupported() {
		List<String> acceptedValues = Arrays.asList("chrome", "CHROME");
		assertSupported(acceptedValues, Browser.CHROME);
	}

	@Test
	public void edgeIsSupported() {
		List<String> acceptedValues = Arrays.asList("edge", "microsoftedge");
		assertSupported(acceptedValues, Browser.EDGE);
	}

	@Test
	public void ie32IsSupported() {
		List<String> acceptedValues = Arrays.asList("ie", "ie32", "iexplore", "iexplore32",
				"internetexplorer", "internet explorer", "internetexplorer32",
				"internet explorer32", "internet explorer 32");
		assertSupported(acceptedValues, Browser.IE32);
	}

	@Test
	public void ie64IsSupported() {
		List<String> acceptedValues = Arrays.asList("ie64", "iexplore64", "internetexplorer64",
				"internet explorer64", "internet explorer 64");
		assertSupported(acceptedValues, Browser.IE64);
	}

	@Test
	public void phantomJsIsSupported() {
		List<String> acceptedValues = Arrays.asList("phantomjs", "phantom", "js", "ghost");
		assertSupported(acceptedValues, Browser.PHANTOMJS);
	}

	@Test
	public void unsupportedNamesThrow() {
		List<String> rejectedValues = Arrays.asList("abc", "123");
		assertNotSupported(rejectedValues);
	}

	private void assertSupported(List<String> values, Browser type) {
		for (String value : values) {
			Browser browser = Browser.get(value);
			assertEquals(type.name(), browser.name());
		}
	}

	private void assertNotSupported(List<String> values) {
		for (String value : values) {
			exception.expect(UnsupportedBrowserException.class);
			exception.expectMessage(value + " is not supported at this time");
			Browser.get(value);
		}
	}
}
