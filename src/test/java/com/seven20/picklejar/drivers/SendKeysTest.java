package com.seven20.picklejar.drivers;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebElement;

@RunWith(Parameterized.class)
public class SendKeysTest extends DriverTest {

	public SendKeysTest() throws ConfigurationException {
		super();
	}

	@Parameter(0)
	public String testName;

	@Parameter(1)
	public String browser;

	@Parameter(2)
	public String fileName;

	@Parameter(3)
	public String locator;

	@Parameter(4)
	public String keys;

	@Parameter(5)
	public String expected;

	@Parameter(6)
	public Class<? extends Exception> expectedException;

	@Parameter(7)
	public String expectedExceptionMessage;

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		// @formatter:off
		return Arrays.asList(new Object[][] { { "enterNameInForm", CHROME, "sendKeys.html",
				"input2", "myName", "fillmyName", null, null }, });
		// @formatter:on
	}

	@Test
	public void sendKeys() {
		if (expectedException != null) {
			exception.expect(expectedException);
			exception.expectMessage(expectedExceptionMessage);
		}
		assignMocks(browser, locator, keys);
		driver.switchBrowser(Browser.get(browser));
		load(fileName);
		WebElement sent = driver.sendKeys(locator, keys);
		assertEquals(expected, (sent.getAttribute("value")));
	}
}
