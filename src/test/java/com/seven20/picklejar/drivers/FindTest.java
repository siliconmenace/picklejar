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

import com.seven20.picklejar.exceptions.AmbiguousLocatorException;
import com.seven20.picklejar.search.Wait;

@RunWith(Parameterized.class)
public class FindTest extends DriverTest {

	private static final String FIND1_NOT_FOUND = "By.First[By.id: wont find1,By.name: wont find1,By.linkText: wont find1,By.partialLinkText: wont find1,By.className: wont find1]";
	private static final String TEXT_SPACE_NOT_FOUND = "By.First[By.id:  cant start with spaces,By.name:  cant start with spaces,By.linkText:  cant start with spaces,By.partialLinkText:  cant start with spaces,By.className:  cant start with spaces]";
	private static final String H2_NOT_FOUND = "By.First[By.id: h2_id,By.name: h2_id,By.linkText: h2_id,By.partialLinkText: h2_id,By.className: h2_id]";
	private static final String BY_ATTRIBUTE = "elementByAttribute.html";
	private static final String DEFAULT = "defaultElement.html";
	private static final String TEXT = "elementText.html";
	private static final String LINKS = "elementLinks.html";
	private static final String NAMES = "elementNames.html";
	private static final String CSS = "elementWithCss.html";
	private static final String TAGS = "elementTags.html";
	private static final String IN_NESTED_FRAMES = "elementInNestedFrames.html";
	private static final String IN_TWO_FRAMES = "elementInTwoFrames.html";
	private static final String IN_FRAME = "elementInFrame.html";
	private static final String IN_FORM = "elementInForm.html";
	private static final String NOT_FOUND = "no such element: Unable to locate element: {method:\"%s\"}";

	public FindTest() throws ConfigurationException {
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

	@Parameters(name = "{0} using {1} driver")
	public static Collection<Object[]> data() {
		// @formatter:off
		return Arrays.asList(new Object[][] {
				{ "h3ById", CHROME, DEFAULT, "h3_id", "h3_text", null, null, null },
				{ "failById", CHROME, DEFAULT, "h2_id", null, NoSuchElementException.class,
						notFound(H2_NOT_FOUND), null },
				{ "emptyDivById", CHROME, DEFAULT, "emptyDiv", "empty", null, null, null },
				{ "fullDivById", CHROME, DEFAULT, "fullDiv", "yum", null, null, null },
				{ "inDivById", CHROME, DEFAULT, "twinkie", "yum", null, null, null },
				{ "formById", CHROME, IN_FORM, "formID", "MaleText\nFemaleText", null, null, null },
				{ "inFormById", CHROME, IN_FORM, "abc", "input", null, null, null },
				{ "outFrameById", CHROME, IN_FRAME, "outOfFrame", "outOfScope", null, null, null },
				{ "inFrameById", CHROME, IN_FRAME, "abc", "input", null, null, null },
				{ "inSecondFrameById", CHROME, IN_TWO_FRAMES, "inFrameTwo", "inFrameTwo", null,
						null, null },
				{ "inBothFramesById", CHROME, IN_TWO_FRAMES, "inBothFrames", null,
						AmbiguousLocatorException.class, "", null },
				{ "inDefaultNestedFrameById", CHROME, IN_NESTED_FRAMES, "inDefaultContent",
						"inDefaultContent", null, null, null },
				{ "inOuterNestedFrameById", CHROME, IN_NESTED_FRAMES, "inOuterFrame",
						"inOuterFrame", null, null, null },
				// {"inInnerNestedFrameById", CHROME, "elementInNestedFrames.html", "inInnerFrame",
				// "inInnerFrame", null,null, null},

				{ "h3ByXp", CHROME, DEFAULT, "//*[@id=\"h3_id\"]", "h3_text", null, null, null },
				{ "failByXp", CHROME, DEFAULT, "//*[@id=\"h2_id\"]", null,
						NoSuchElementException.class, notFound("By.xpath: //*[@id=\"h2_id\"]"),
						null },
				{ "emptyDivByXp", CHROME, DEFAULT, "//*[@id=\"emptyDiv\"]", "empty", null, null,
						null },
				{ "fullDivByXp", CHROME, DEFAULT, "//*[@id=\"fullDiv\"]", "yum", null, null, null },
				{ "inDivByXp", CHROME, DEFAULT, "//*[@id=\"twinkie\"]", "yum", null, null, null },
				{ "formByXp", CHROME, IN_FORM, "//*[@id=\"formID\"]", "MaleText\nFemaleText", null,
						null, null },
				{ "inFormByXp", CHROME, IN_FORM, "//*[@id=\"abc\"]", "input", null, null, null },
				{ "outFrameByXp", CHROME, IN_FRAME, "//*[@id=\"outOfFrame\"]", "outOfScope", null,
						null, null },
				{ "inFrameByXp", CHROME, IN_FRAME, "//*[@id=\"abc\"]", "input", null, null, null },
				{ "inSecondFrameByXp", CHROME, IN_TWO_FRAMES, "//*[@id=\"inFrameTwo\"]",
						"inFrameTwo", null, null, null },
				{ "inBothFramesByXp", CHROME, IN_TWO_FRAMES, "//*[@id=\"inBothFrames\"]", null,
						AmbiguousLocatorException.class, "", null },
				{ "inDefaultNestedFrameByXp", CHROME, IN_NESTED_FRAMES,
						"//*[@id=\"inDefaultContent\"]", "inDefaultContent", null, null, null },
				{ "inOuterNestedFrameByXp", CHROME, IN_NESTED_FRAMES, "//*[@id=\"inOuterFrame\"]",
						"inOuterFrame", null, null, null },
				// {"inInnerNestedFrameByXp", CHROME, "elementInNestedFrames.html",
				// "//*[@id=\"inInnerFrame\"]", "inInnerFrame", null,null, null},
				{ "noIdInputByXp", CHROME, DEFAULT, "/html/body/input[2]", "noIdInput", null, null,
						null },
				{ "rootNoIdInputByXp", CHROME, DEFAULT, "./html/body/input[2]", "noIdInput", null,
						null, null },
				{ "rootRelativeNoIdInputByXp", CHROME, DEFAULT, ".//html/body/input[2]",
						"noIdInput", null, null, null },
				{ "noIdDivByXp", CHROME, DEFAULT, "/html/body/div[3]/div", "noIdDiv", null, null,
						null },
				{ "noIdInFrameByXp", CHROME, IN_FRAME, "/html/body/p[2]", null,
						AmbiguousLocatorException.class, "", null },

				{ "failByTag", CHROME, DEFAULT, "h2", null, NoSuchElementException.class,
						notFound("By.tagName: h2"), null },
				{ "h3ByTag", CHROME, TAGS, "h3", "h3", null, null, null },
				{ "pByTag", CHROME, TAGS, "p", "p", null, null, null },
				{ "aByTag", CHROME, TAGS, "a", "a", null, null, null },
				{ "inputByTag", CHROME, TAGS, "input", "input", null, null, null },
				{ "frameByTag", CHROME, TAGS, "frame", null, NoSuchElementException.class,
						notFound("By.tagName: frame"), null },
				{ "iframeByTag", CHROME, TAGS, "iframe", "iframe", null, null, null },
				{ "framesetByTag", CHROME, TAGS, "frameset", null, NoSuchElementException.class,
						notFound("By.tagName: frameset"), null },
				{ "formByTag", CHROME, TAGS, "form", "form", null, null, null },
				{ "imgByTag", CHROME, TAGS, "img", "img", null, null, null },
				{ "navByTag", CHROME, TAGS, "nav", "nav", null, null, null },
				{ "tableByTag", CHROME, TAGS, "table", "table", null, null, null },
				{ "tbodyByTag", CHROME, TAGS, "tbody", "tbody", null, null, null },
				{ "ulByTag", CHROME, TAGS, "ul", "ul", null, null, null },
				{ "liByTag", CHROME, TAGS, "li", "0", null, null, null },
				{ "ignoreCaseTag", CHROME, TAGS, "Input", "input", null, null, null },
				{ "ignoreCapsByTag", CHROME, TAGS, "INPUT", "input", null, null, null },
				{ "nameSpaceClashByTag", CHROME, TAGS, "video", "video", null, null, null },
				{ "multipleInFrameByTag", CHROME, IN_FRAME, "p", null,
						AmbiguousLocatorException.class, "", null },
				{ "inFrameByTag", CHROME, IN_FRAME, "form", "MaleText\nFemaleText", null, null,
						null },

				{ "cssTagAndAttribute", CHROME, CSS, "input[id=inputId]", "inputText", null, null,
						null },
				{ "cssTagAndMultipleAttribute", CHROME, CSS, "input[name=inputName][type=text]",
						"inputText", null, null, null },
				{ "cssMultipleTagsAndAttribute", CHROME, CSS, "form input[id=inputId]", "inputText",
						null, null, null },
				{ "cssId", CHROME, CSS, "input#inputId", "inputText", null, null, null },
				{ "cssClass", CHROME, CSS, ".inputClass", "inputText", null, null, null },
				{ "cssMultipleClass", CHROME, CSS, ".inputClass1.inputClass2", "inputText2", null,
						null, null },
				{ "cssClassAndAttribute", CHROME, CSS, "input.inputClass[name=inputName]",
						"inputText", null, null, null },
				{ "cssTagAndPartialAttributeStart", CHROME, CSS, "input[id^='inpu']", "inputText",
						null, null, null },
				{ "cssTagAndPartialAttributeEnd", CHROME, CSS, "input[id$='Id']", "inputText", null,
						null, null },
				{ "cssTagAndPartialAttributeContains", CHROME, CSS, "input[id*='put']", "inputText",
						null, null, null },
				{ "cssTagPath", CHROME, CSS, "div.divClass>form>input[id=inputId]", "inputText",
						null, null, null },
				{ "cssAdjacentTag", CHROME, CSS, "input+input", "inputText2", null, null, null },

				{ "inputByName", CHROME, NAMES, "input1", "1", null, null, null },
				{ "buttonsWithSameName", CHROME, NAMES, "subject", null,
						AmbiguousLocatorException.class, "", null },
				{ "idAndNameClash", CHROME, NAMES, "clash", "oops", null, null, null },

				{ "anchorByLink", CHROME, LINKS, "findMe", "findMe", null, null, null },
				{ "idAndLinkClash", CHROME, LINKS, "wontFind1", "1", null, null, null },
				{ "nameAndLinkClash", CHROME, LINKS, "wontFind2", "2", null, null, null },
				{ "anchorWithSameText", CHROME, LINKS, "same", null,
						AmbiguousLocatorException.class, "", null },

				{ "titleByText", CHROME, TEXT, "Elements With Text", "title", null, null, null },
				{ "pByText", CHROME, TEXT, "will be found", "will be found", null, null, null },
				{ "wontMatchOnWrapperElementsByText", CHROME, TEXT, "wont match the div",
						"wont match the div", null, null, null },
				{ "caseMattersByText", CHROME, TEXT, "Will Be Found", null,
						NoSuchElementException.class, notFound("By.Text: Will Be Found"), null },
				{ "noPartialSearchByText", CHROME, TEXT, "Will Be Foun", null,
						NoSuchElementException.class, notFound("By.Text: Will Be Foun"), null },
				{ "cantStartWithSpaceByText", CHROME, TEXT, " cant start with spaces", null,
						NoSuchElementException.class, notFound(TEXT_SPACE_NOT_FOUND), null },
				{ "idAndTextClash", CHROME, TEXT, "find wrong one", "find wrong one", null, null,
						null },
				{ "idAndTextCanBeTheSame", CHROME, TEXT, "will not be found byId",
						"will not be found byId", null, null, null },
				{ "mustBeThreeWordsOrMore", CHROME, TEXT, "wont find1", null,
						NoSuchElementException.class, notFound(FIND1_NOT_FOUND), null },

				{ "canSpecifyWaitTimesShort", CHROME, DEFAULT, "h2_id", null,
						NoSuchElementException.class, notFound(H2_NOT_FOUND), Wait.SHORT },
				{ "canSpecifyWaitTimesMedium", CHROME, DEFAULT, "h2_id", null,
						NoSuchElementException.class, notFound(H2_NOT_FOUND), Wait.MEDIUM },
				{ "canSpecifyWaitTimesLong", CHROME, DEFAULT, "h2_id", null,
						NoSuchElementException.class, notFound(H2_NOT_FOUND), Wait.LONG },
				
				{ "byAttribute", CHROME, BY_ATTRIBUTE, "//td[@id='Platinum'][3]", "link3", null, null, null },
		
		});
		// @formatter:on
	}
	// TODO add a bunch of tests for ambiguous locator returning info:
	// page, object type, etc.

	@Test
	public void find() {
		if (expectedException != null) {
			exception.expect(expectedException);
			exception.expectMessage(expectedExceptionMessage);
		}
		assignMocks(browser, locator);
		driver.switchBrowser(Browser.get(browser));
		load(fileName);
		WebElement actual = assertTimelyFind();
		assertEquals(expected, identify(actual));
	}

	private WebElement assertTimelyFind() {
		final long startTime = System.currentTimeMillis();
		WebElement actual;
		if (wait == null) {
			actual = driver.find(locator);
		} else {
			actual = driver.find(locator, wait);
		}
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

	private static String notFound(String expected) {
		return String.format(NOT_FOUND, expected);
	}
}
