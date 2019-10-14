package com.seven20.picklejar.drivers;

import static com.seven20.picklejar.drivers.Browser.CHROME;
import static com.seven20.picklejar.drivers.Browser.IE32;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverManagerTest {

	private static final String CAST_EXCEPTION_MSG = ""
			+ "org.openqa.selenium.ie.InternetExplorerDriver cannot be cast to "
			+ "org.openqa.selenium.chrome.ChromeDriver";
	@Rule
	public ExpectedException exception = ExpectedException.none();
	private DriverManager instance = DriverManager.getInstance();
	private WebDriver temp;

	@SuppressWarnings("deprecation")
	@Test
	public void killDriversDoesNothing() throws Exception {
		DriverManager.killDrivers();
	}

	@Test
	public void hasPoolOfDrivers() {
		assertEquals(0, instance.count());
	}

	@Test
	public void canGetDriver() {
		temp = instance.getDriver(CHROME);
		assertNotNull(temp);
	}

	@Test
	public void gettingDriverDoesntAddItToPool() {
		temp = instance.getDriver(CHROME);
		assertEquals(0, instance.count());
	}

	@Test
	public void releasingDriverAddsItToPool() {
		catchAndRelease(CHROME);
		assertEquals(1, instance.count());
	}

	@Test
	public void releaseDriverWithNullBrowserDoesNotGetAddedToPool() {
		temp = instance.getDriver(CHROME);
		instance.release(null, temp);
		assertEquals(0, instance.count());
	}

	@Test
	public void releaseNullDriverDoesNotGetAddedToPool() {
		instance.release(CHROME, null);
		assertEquals(0, instance.count());
	}

	
	@Ignore
	@Test
	public void onlyAcceptsSupportedBrowsers() {
		//sometimes fails to start microsoft edge browser, despite it being installed
		for (Browser browser : Browser.values()) {
			catchAndRelease(browser);
		}
		assertEquals(Browser.values().length, instance.count());
	}

	@Test
	public void releaseDriverToWrongSwimLane() {
		instance.release(CHROME, instance.getDriver(IE32));
		temp = instance.getDriver(CHROME);
		exception.expect(ClassCastException.class);
		exception.expectMessage(CAST_EXCEPTION_MSG);
		@SuppressWarnings("unused")
		WebDriver mismatch = (ChromeDriver) temp;
	}

	@Test
	public void killRemovesAllDriversFromPool() {
		catchAndRelease(CHROME);
		assertEquals(1, instance.count());
		instance.kill();
		assertEquals(0, instance.count());
	}

	@Test
	public void getsNewDriverOnEachCall() {
		WebDriver chromeA = instance.getDriver(CHROME);
		WebDriver chromeB = instance.getDriver(CHROME);
		assertTrue(chromeA != chromeB);
		chromeA.close();
		chromeB.close();
	}

	@Test
	public void reusesReleasedDrivers() {
		assertEquals(0, instance.count());
		WebDriver chromeA = instance.getDriver(CHROME);
		instance.release(CHROME, chromeA);
		assertEquals(1, instance.count());
		WebDriver chromeB = instance.getDriver(CHROME);
		assertEquals(0, instance.count());
		assertEquals(chromeA, chromeB);
		chromeA.close();
	}

	@Test
	public void hasSwimLaneForEachDriverTypeInPool() {
		catchAndRelease(IE32);
		catchAndRelease(CHROME);
		assertEquals(1, instance.count(IE32));
		assertEquals(1, instance.count(CHROME));
		assertEquals(2, instance.count());
	}

	@Test
	public void killDriversInSwimLane() {
		catchAndRelease(IE32);
		catchAndRelease(CHROME);
		assertEquals(1, instance.count(IE32));
		assertEquals(1, instance.count(CHROME));
		instance.kill(CHROME);
		assertEquals(1, instance.count(IE32));
		assertEquals(0, instance.count(CHROME));
	}

	@Test
	public void canRunAnyCommandTwiceWithNoExceptions() {
		instance.count();
		instance.count();
		instance.count(CHROME);
		instance.count(CHROME);
		instance.kill();
		instance.kill();
		instance.kill(CHROME);
		instance.kill(CHROME);
		catchAndRelease(CHROME);
		catchAndRelease(CHROME);
	}

	private void catchAndRelease(Browser browser) {
		instance.release(browser, instance.getDriver(browser));
	}

	@After
	public void setup() {
		instance.kill();
		if (temp != null) {
			temp.close();
		}
	}
}
