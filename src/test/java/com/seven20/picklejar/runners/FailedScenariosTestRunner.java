package com.seven20.picklejar.runners;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "@target/rerun.txt" }, plugin = {
		"html:reports/cucumber/rerunner/" }, glue = "com/seven20/picklejar/stepdefinitions")
public class FailedScenariosTestRunner {

}
