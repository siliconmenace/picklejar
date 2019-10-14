package com.seven20.picklejar.runners;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "classpath:com/seven20/picklejar/features" }, tags = { "~@dev", "~@ignore",
		"@pj, @datamap" })
public class TestRunner extends TestRunner {

}
