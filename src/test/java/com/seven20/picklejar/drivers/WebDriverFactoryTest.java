package com.seven20.picklejar.drivers;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.seven20.picklejar.utils.OperatingSystem;

public class WebDriverFactoryTest {

	@Test
	public void shouldReturnPhantomJS() {
		WebDriver driver = WebDriverFactory.getDriver(Browser.PHANTOMJS);
		assertEquals(PhantomJSDriver.class, driver.getClass());
		driver.close();
	}

	@Test
	public void shouldReturnChrome() {
		WebDriver driver = WebDriverFactory.getDriver(Browser.CHROME);
		assertEquals(ChromeDriver.class, driver.getClass());
		driver.close();
	}

	@Test
	public void shouldReturnIExplorex32() {
		if (OperatingSystem.isWindows()) {
			WebDriver driver = WebDriverFactory.getDriver(Browser.IE32);
			assertEquals(InternetExplorerDriver.class, driver.getClass());
			driver.close();
		}
	}

	@Test
	public void shouldReturnIExplorex64() {
		if (OperatingSystem.isWindows()) {
			WebDriver driver = WebDriverFactory.getDriver(Browser.IE64);
			assertEquals(InternetExplorerDriver.class, driver.getClass());
			driver.close();
		}
	}

	@Test
	@Ignore
	/**
	 * Can only be ran on a platform with Edge Installed.
	 */
	public void shouldReturnEdge() {
		WebDriver driver = WebDriverFactory.getDriver(Browser.EDGE);
		assertEquals(EdgeDriver.class, driver.getClass());
		driver.close();
	}

	@SuppressWarnings("deprecation")
	@AfterClass
	public static void killDrivers() throws Exception {
		DriverManager.killDrivers();
	}

}
