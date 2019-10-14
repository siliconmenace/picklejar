package com.seven20.picklejar.stepdefinitions;

import java.util.Collection;

import org.openqa.selenium.WebDriverException;

import com.seven20.picklejar.configuration.Config;
import com.seven20.picklejar.configuration.ConfigurationLoader;
import com.seven20.picklejar.drivers.AtaDriver;
import com.seven20.picklejar.utils.DataStore;

import cucumber.api.Scenario;

public class StepDefinition {

	private DataStore dataStore = DataStore.instance();
	private Config alias = ConfigurationLoader.getConfiguration();
	private static Driver driver;

	public StepDefinition() {
		alias = ConfigurationLoader.getConfiguration();
	}

	/**
	 * Initializes default driver if null
	 * 
	 * @return current initialized driver
	 */
	protected Driver driver() {
		if (driver == null) {
			driver = new Driver(ConfigurationLoader.getConfiguration());
		}
		return driver;
	}

	protected Config alias() {
		return alias;
	}

	protected DataStore dataStore() {
		return dataStore;
	}

	private void screenshot(Scenario scenario) {
		try {
			byte[] screenshot = driver().getScreenshotAsBytes();
			scenario.embed(screenshot, "image/png");
		} catch (WebDriverException e) {
			System.err.println(e.getMessage());
		} catch (ClassCastException cce) {
			cce.printStackTrace();
		}
	}

	protected void takeScreenshot(Scenario scenario) {
		Collection<String> tagNames = scenario.getSourceTagNames();
		if (!(!(scenario.isFailed()) && tagNames.toString().contains("@noscreenshot")))
			screenshot(scenario);
	}
}