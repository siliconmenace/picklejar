package com.seven20.picklejar.runners;

import java.io.IOException;

import javax.naming.NameNotFoundException;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.seven20.picklejar.configuration.ConfigurationLoader;
import com.seven20.picklejar.RunnerArchive;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty", "html:reports/cucumber/runner",
		"rerun:target/rerun.txt" }, glue = "com/seven20/picklejar/stepdefinitions")
public class TestRunner {

	@BeforeClass
	public static void beforeClassMethods() throws NameNotFoundException, IOException {
		String srcFolder = "reports/cucumber/runner"; // Must match the plugin path above.
		String destFolder = ConfigurationLoader.getConfiguration().getString("reportArchivePath");
		RunnerArchive.archiveReport(srcFolder, destFolder);
	}
}
