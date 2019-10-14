package com.seven20.picklejar.stepdefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;

public class Hooks extends StepDefinition {

	@After
	public void ScenarioScreenShot(Scenario scenario) {
		takeScreenshot(scenario);
	}
}
