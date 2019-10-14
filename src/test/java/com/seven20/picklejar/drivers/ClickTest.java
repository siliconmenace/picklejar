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

@RunWith(Parameterized.class)
public class ClickTest extends DriverTest {

	public ClickTest() throws ConfigurationException {
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
	public String expected;

	@Parameter(5)
	public Class<? extends Exception> expectedException;

	@Parameter(6)
	public String expectedExceptionMessage;

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		// @formatter:off
		return Arrays.asList(new Object[][] {
				{ "clickLinkWithId", CHROME, "click.html", "href1", "On Landing One", null, null },
				{ "clickLinkWithoutId", CHROME, "click.html", "Link to default2", "On Landing Two",
						null, null },
				{ "clickSubmitWithId", CHROME, "click.html", "input1", "On Landing One", null,
						null },
				{ "clickSubmitWithoutId", CHROME, "click.html", "//input[@value='Go to default2']",
						"On Landing Two", null, null }, });
		// @formatter:on
	}

	@Test
	public void click() {
		if (expectedException != null) {
			exception.expect(expectedException);
			exception.expectMessage(expectedExceptionMessage);
		}
		assignMocks(browser, locator);
		driver.switchBrowser(Browser.get(browser));
		load(fileName);
		driver.click(locator);
		assertEquals(expected, getPage());
	}

	private String getPage() {
		return driver.getTitle();
	}
}
