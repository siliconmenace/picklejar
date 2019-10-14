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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.seven20.picklejar.drivers.Browser;
import com.seven20.picklejar.exceptions.AmbiguousLocatorException;

@RunWith(Parameterized.class)
public class PartialLinkTextTest extends DriverTest {

	private static final String LINKTEXT_CONDITIONS = "linkText.html";
	private static final String BYCLASS_CONDITIONS = "byClass.html";
	private static final String NOT_PRESENT = " matches multiple elements";
	private static final String NOT_PRESENT1 ="no such element: Unable to locate element" ;

	public PartialLinkTextTest() throws ConfigurationException {
		super();
	}

	@Parameter(0)
	public String testName;

	@Parameter(1)
	public String browser;

	@Parameter(2)
	public String fileName;

	@Parameter(3)
	public String present;

	@Parameter(4)
	public String expected;

	@Parameter(5)
	public Class<? extends Exception> expectedException;

	@Parameter(6)
	public String expectedExceptionMessage;

	@Parameters(name = "{0} using {1} driver")
	public static Collection<Object[]> data() {
		// @formatter:off
		return Arrays.asList(new Object[][] {

				{ "linktext-singleword", CHROME, LINKTEXT_CONDITIONS, "Google", "Google", null,
						null },
				{ "linktext-multiword1", CHROME, LINKTEXT_CONDITIONS, "Google for websearch",
						"Google for websearch", null, null },
				{ "linktext-multiword2", CHROME, LINKTEXT_CONDITIONS, "Google for images",
						"Google for images", null, null },
				{ "linktext-numbers", CHROME, LINKTEXT_CONDITIONS, "123", "123",
						AmbiguousLocatorException.class, notPresent("matches multiple elements") },
				{ "linktext-numbersandwords", CHROME, LINKTEXT_CONDITIONS, "123Facebook123",
						"123Facebook123", null, null },
				{ "linktext-numbersand words2", CHROME, LINKTEXT_CONDITIONS, "Facebook123",
						"Facebook123", null, null },

				{ "partiallinktext-numbersandwords", CHROME, LINKTEXT_CONDITIONS, "e456",
						"phase456", null, null },
				{ "partiallinktext-numbersandwords2", CHROME, LINKTEXT_CONDITIONS, "@insta",
						"@insta4567", null, null },
				{ "partiallinktext-numbersandwords3", CHROME, LINKTEXT_CONDITIONS, "@1F2s3",
						"@1F2s34", null, null },
				{ "partiallinktext-numbersspecialcharactersandwords", CHROME, LINKTEXT_CONDITIONS,
						"789!@(Google789!", "789!@(Google789!@ for websearch", null, null },
				{ "partiallinktext-specialcharactersandwords", CHROME, LINKTEXT_CONDITIONS,
						"*connec", "Facebook to *connect* to @friends@", null, null },
				{ "partiallinktext-numbersandwords4", CHROME, LINKTEXT_CONDITIONS, "book123",
						"book123", AmbiguousLocatorException.class,
						notPresent("matches multiple elements") },
				{ "partiallinktext-partofsingleword", CHROME, LINKTEXT_CONDITIONS, "Goog", "Goog",
						AmbiguousLocatorException.class, notPresent("matches multiple elements") },
				{ "partiallinktext-partofsentence", CHROME, LINKTEXT_CONDITIONS, "Google for",
						"Google for", AmbiguousLocatorException.class,
						notPresent("matches multiple elements") },
				{ "partiallinktext-specialcharacterpartofsentence", CHROME, LINKTEXT_CONDITIONS,
						"@friends@", "@friends@", AmbiguousLocatorException.class,
						notPresent("matches multiple elements") },
				{ "partiallinktext-specialcharacter", CHROME, LINKTEXT_CONDITIONS, "*", "*",
						AmbiguousLocatorException.class,
						notPresent("matches multiple elements") }, 
				{ "class-present", CHROME, BYCLASS_CONDITIONS, "byclass", "Testing", null,
							null },
				{ "class-not present", CHROME, BYCLASS_CONDITIONS, "bdd",
						"bdd", NoSuchElementException.class,notPresent1("Unable to find the element")},
		});
	}

	@Test
	public void find() {
		if (expectedException != null) {
			exception.expect(expectedException);
			exception.expectMessage(expectedExceptionMessage);
		}
		assignMocks(browser, present);
		driver.switchBrowser(Browser.get(browser));
		load(fileName);
		WebElement actual = driver.find(present);
		assertEquals(expected, identify(actual));
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
		return String.format(NOT_PRESENT, "id", expected);
	}
	
	private static Object notPresent1(String expected) {
		return String.format(NOT_PRESENT1, "id", expected);
	}
}
