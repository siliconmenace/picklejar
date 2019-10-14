package com.seven20.picklejar.runners;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = { "classpath:com/seven20/picklejar/features/ScreenShot.feature" }, tags = {
		"~@dev", "~@ignore", "@screenshot, @noscreenshot" })
public class ScreenshotTestRunner extends TestRunner {

}
