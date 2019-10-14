package com.seven20.picklejar.drivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.seven20.picklejar.drivers.Browser;
import com.seven20.picklejar.search.Until;
import com.seven20.picklejar.search.Wait;

@RunWith(Parameterized.class)
public class FindConditionsTest extends DriverTest {

	private static final String FIND_CONDITIONS = "FindCondition.html";
	private static final String NOT_VISIBLE = "no such element: Unable to locate element: {method:\"%s\"}";
	private static final String BY_FIRST = "By.First[By.id: %1$s,By.name: %1$s,By.linkText: %1$s,By.partialLinkText: %1$s,By.className: %1$s]";

	public FindConditionsTest() throws ConfigurationException {
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

	@Parameter(7)
	public Wait wait;

	@Parameter(8)
	public Until until;

	@Parameters(name = "{0} using {1} driver")
	public static Collection<Object[]> data() {
		// @formatter:off
		return Arrays.asList(new Object[][] {

				{ "find-WithoutWaitAndCondition", CHROME, FIND_CONDITIONS, "find", "find", null,
						null, null, null },
				{ "findWithWait-WithoutWaitAndCondition", CHROME, FIND_CONDITIONS, "findWithWait",
						"findWithWait", null, null, null, null },
				{ "findWithCondition-WithoutWaitAndCondition", CHROME, FIND_CONDITIONS,
						"findWithCondition", "findWithCondition", null, null, null, null },

				{ "find-WithWait", CHROME, FIND_CONDITIONS, "find", "find", null, null, Wait.SHORT,
						null },
				{ "findWithWait-WithWait", CHROME, FIND_CONDITIONS, "findWithWait", "findWithWait",
						null, null, Wait.MEDIUM, null },
				{ "findWithCondition-WithWait", CHROME, FIND_CONDITIONS, "findWithCondition",
						"findWithCondition", null, null, Wait.MEDIUM, null },

				{ "find-WithCondition", CHROME, FIND_CONDITIONS, "find", "find", null, null, null,
						Until.VISIBLE },
				{ "findWithWait-WithCondition", CHROME, FIND_CONDITIONS, "findWithWait",
						"findWithWait", null, null, null, Until.VISIBLE },
				{ "findWithCondition-WithCondition", CHROME, FIND_CONDITIONS, "findWithCondition",
						"findWithCondition", null, null, null, Until.VISIBLE },

				{ "find-WithWaitandCondition", CHROME, FIND_CONDITIONS, "find", "find", null, null,
						Wait.SHORT, Until.VISIBLE },
				{ "findWithWait-WithWaitandCondition", CHROME, FIND_CONDITIONS, "findWithWait",
						"findWithWait", null, null, Wait.MEDIUM, Until.VISIBLE },
				{ "findWithCondition-WithWaitandCondition", CHROME, FIND_CONDITIONS,
						"findWithCondition", "findWithCondition", null, null, Wait.MEDIUM,
						Until.VISIBLE },

				{ "find-ShortPRESENT", CHROME, FIND_CONDITIONS, "findWithWaitAndCondition",
						"findWithWaitAndCondition", NoSuchElementException.class,
						notPresent(String.format(BY_FIRST, "findWithWaitAndCondition")), Wait.SHORT,
						Until.PRESENT },
				{ "find-MediumPRESENT", CHROME, FIND_CONDITIONS, "findWithWaitAndCondition",
						"findWithWaitAndCondition", NoSuchElementException.class,
						notPresent(String.format(BY_FIRST, "findWithWaitAndCondition")),
						Wait.MEDIUM, Until.PRESENT },
				{ "find-LongPRESENT", CHROME, FIND_CONDITIONS, "findWithWaitAndCondition",
						"findWithWaitAndCondition", NoSuchElementException.class,
						notPresent(String.format(BY_FIRST, "findWithWaitAndCondition")), Wait.LONG,
						Until.PRESENT }, });
	}

	@Test
	public void findUsingClick() {
		if (expectedException != null) {
			exception.expect(expectedException);
			exception.expectMessage(expectedExceptionMessage);
		}
		assignMocks(browser, locator);
		driver.switchBrowser(Browser.get(browser));
		load(fileName);
		driver.click("//*[@id=\"drop-nav\"]/li[2]/a", Wait.LONG);
		WebElement actual = assertTimelyFind();
		assertEquals(expected, identify(actual));
	}
	
	@Test
	public void findUsingMouseOver(){
		if (expectedException != null) {
			exception.expect(expectedException);
			exception.expectMessage(expectedExceptionMessage);
		}
		assignMocks(browser, locator);
		driver.switchBrowser(Browser.get(browser));
		load(fileName);
		driver.mouseOver("//*[@id=\"drop-nav\"]/li[2]/a", Wait.LONG);
		WebElement actual = assertTimelyFind();
		assertEquals(expected, identify(actual));
	}

	private WebElement assertTimelyFind() {
		final long startTime = System.currentTimeMillis();
		if (wait == null) {
			wait = Wait.MEDIUM;
		}
		if (until == null) {
			until = Until.PRESENT;
		}
		WebElement actual = driver.find(locator, wait, until);

		final long endTime = System.currentTimeMillis();
		final long maxWaitTime = getMaxWaitTimeInSeconds();
		final long minWaitTime = getMinWaitTimeInSeconds(maxWaitTime);
		final long executionTime = getExecutionTimeInSeconds(startTime, endTime);
		String format = "Expected runtime between %s and %s but was actually %s";
		String errMsg = String.format(format, minWaitTime, maxWaitTime, executionTime);
		assertTrue(errMsg, isWithinTimeConstraints(executionTime, maxWaitTime, minWaitTime));
		return actual;
	}

	private long getMaxWaitTimeInSeconds() {
		if (wait == null) {
			return (long) ((Wait.MEDIUM.val) * waitTimeFactor);
		}
		return (long) ((wait.val) * waitTimeFactor);
	}

	private long getMinWaitTimeInSeconds(long _maxWaitTime) {
		long min = 0;
		if (expectedException != null) {
			min = _maxWaitTime - 1L;
		}
		return min;
	}

	private long getExecutionTimeInSeconds(final long startTime, final long endTime) {
		return (endTime - startTime) / 1000L;
	}

	private boolean isWithinTimeConstraints(long executionTime, long maxWaitTime,
			long minWaitTime) {
		return executionTime >= minWaitTime && executionTime <= maxWaitTime;
	}

	/**
	 * Cycles through element getters until finding a not null value. Used to confirm that the
	 * <i>correct</i> element is found on page.
	 * 
	 * @param matchedElement
	 * @return
	 */
	private String identify(WebElement matchedElement) {
		String identifier = matchedElement.getText();
		if (identifier == null || identifier.isEmpty()) {
			identifier = matchedElement.getAttribute("value");
		}
		if (identifier == null || identifier.isEmpty()) {
			identifier = matchedElement.getTagName();
		}
		return identifier;
	}

	private static Object notPresent(String expected) {
		return String.format(NOT_VISIBLE, expected);
	}
}
