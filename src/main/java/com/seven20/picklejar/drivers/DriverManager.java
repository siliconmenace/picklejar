package com.seven20.picklejar.drivers;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;

public class DriverManager {

	private static final Logger logger = Logger.getLogger(DriverManager.class.getName());
	private ConcurrentHashMap<Browser, Deque<WebDriver>> driverTypeMap = new ConcurrentHashMap<>();

	private DriverManager() {
	} // Cannot be instantiated outside this class

	private static class LazyHolder { // Idiom allows for threadsafe, lazy-initialization
		private static final DriverManager INSTANCE = new DriverManager();
	}

	public static DriverManager getInstance() {
		return LazyHolder.INSTANCE;
	}

	public WebDriver getDriver(Browser browser) {
		if (count(browser) > 0) {
			logger.info("Retrieving driver for browser: " + browser.name());
			return driverTypeMap.get(browser).pop();
		}
		logger.info("Creating new driver for browser: " + browser.name());
		return WebDriverFactory.getDriver(browser);
	}

	public void release(Browser browser, WebDriver driver) {
		if (driver != null & browser != null) {
			if (driverTypeMap.containsKey(browser)) {
				driverTypeMap.get(browser).push(driver);
				logger.info("Driver, " + browser.name()
						+ ", released back to driver pool.  Current size =  "
						+ driverTypeMap.get(browser).size());
			} else {
				Deque<WebDriver> deque = new LinkedBlockingDeque<>();
				deque.push(driver);
				driverTypeMap.put(browser, deque);
				logger.info("Creating new driverPool for " + browser);
			}
		}
	}

	public int count(Browser browser) {
		if (!driverTypeMap.containsKey(browser)) {
			return 0;
		}
		return driverTypeMap.get(browser).size();
	}

	public int count() {
		int sum = 0;
		for (Browser key : driverTypeMap.keySet()) {
			sum += driverTypeMap.get(key).size();
		}
		return sum;
	}

	public void kill() {
		for (Browser key : driverTypeMap.keySet()) {
			kill(key);
		}
	}

	public void kill(Browser browser) {
		Deque<WebDriver> swimLane = driverTypeMap.get(browser);
		for (WebDriver driver : swimLane) {
			driver.close();
		}
		swimLane.clear();
	}

	// private static String kill = "taskkill /im %s /f";
	// private static String[] drivers = { "chromedriver.exe", "phantomjs.exe", "IEDriverServer.exe"
	// };

	/**
	 * BFH to kill the Browser Drivers that will not shut down after the test. No danger running
	 * this regardless of the driver being used.
	 *
	 * @deprecated No longer required as the DriverManager limits the number executables through the
	 *             use of a driver pools.
	 * 	 * @throws Exception placeholder
	 */
	public static void killDrivers() throws Exception {
		// for(String driver : drivers){
		// try{
		// Runtime.getRuntime().exec(String.format(kill, driver));
		// }catch(Exception e){
		// System.out.println(e.getMessage());
		// }
		// }

	}

}
