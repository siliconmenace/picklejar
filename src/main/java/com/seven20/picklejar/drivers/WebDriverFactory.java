package com.seven20.picklejar.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.seven20.picklejarexceptions.UnsupportedBrowserException;
import com.seven20.picklejar.utils.OperatingSystem;

import io.github.bonigarcia.wdm.Architecture;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

class WebDriverFactory {

	/**
	 * Driver files will only have an extension for Windows
	 */
	private static String driverExtension = "";

	public static WebDriver getDriver(Browser browser) {

		setExtension();
		switch (browser) {
		case CHROME:
			return chromeDriver();
		case EDGE:
			return edgeDriver();
		case IE32:
			return ieDriver(Architecture.x32);
		case IE64:
			return ieDriver(Architecture.x64);
		case PHANTOMJS:
			return phantomjsDriver();
		default:
			// In case developer modifies Browser enum
			throw new UnsupportedBrowserException(browser.name());
		}
	}

	private static void setExtension() {
		if (OperatingSystem.isWindows()) {
			driverExtension = ".exe";
		}
	}

	private static PhantomJSDriver phantomjsDriver() {
		System.setProperty("phantomjs.binary.path",
				"./src/test/resources/webdrivers/phantomjs" + driverExtension);
		return new PhantomJSDriver();
	}

	private static WebDriver chromeDriver() {
		System.setProperty("webdriver.chrome.driver",
				"./src/test/resources/webdrivers/chromedriver" + driverExtension);
		System.setProperty("webdriver.chrome.logfile",
				"./src/test/resources/webdrivers/chromedriver.log");
		return new ChromeDriver();
	}

	private static WebDriver edgeDriver() {
		System.setProperty("webdriver.edge.driver",
				"./src/test/resources/webdrivers/MicrosoftWebDriver.exe");
		EdgeDriverManager edge = EdgeDriverManager.getInstance();
		edge.setup();
		edge.setup(Architecture.x32);
		DesiredCapabilities capabilities = DesiredCapabilities.edge();
		capabilities.setBrowserName(DesiredCapabilities.edge().getBrowserName());
		return new EdgeDriver(capabilities);
	}

	private static WebDriver ieDriver(Architecture architecture) {
		System.setProperty("webdriver.ie.driver",
				"./src/test/resources/webdrivers/IEDriverServer.exe");
		InternetExplorerDriverManager manager = InternetExplorerDriverManager.getInstance();
		manager.setup();
		manager.setup(architecture);
		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setBrowserName(DesiredCapabilities.internetExplorer().getBrowserName());
		capabilities.setCapability("ignoreZoomSetting", true);
		return new InternetExplorerDriver(capabilities);
	}
}
