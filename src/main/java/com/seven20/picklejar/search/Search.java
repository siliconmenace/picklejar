package com.seven20.picklejar.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.seven20.picklejar.exceptions.AmbiguousLocatorException;

/**
 * One element found per search
 */
public class Search {

	private static Logger LOG = Logger.getLogger(Search.class.getName());
	private static final String NOT_FOUND = "no such element: Unable to locate element: {method:\"%s\"}";
	private static final String UNTIL_INFO = "Find will wait until %s condition is met or timeout limit exceeded";
	private List<WebElement> defaultMatches;
	private Map<WebElement, List<WebElement>> elementsByFrame;
	private By by;
	private String locator;
	private Wait wait;
	private Until condition;
	private double waitTimeFactor;
	private WebDriver driver;

	public Search(String locator, Wait wait, Until condition, double waitTimeFactor,
			WebDriver driver) {
		this.locator = locator;
		this.wait = wait;
		this.condition = condition;
		this.waitTimeFactor = waitTimeFactor;
		this.driver = driver;
	}

	public WebElement result() {
		defaultMatches = searchDefaultContent(locator, wait(wait), condition);
		elementsByFrame = searchAllFrames(locator);
		return validMatch(defaultMatches, elementsByFrame);
	}

	private List<WebElement> searchDefaultContent(String locator, WebDriverWait wait,
			Until condition) {
		driver.switchTo().defaultContent();
		List<WebElement> found = new ArrayList<>();
		try {
			found = wait.until(trigger(locator, condition));
		} catch (TimeoutException e) {
			LOG.info(locator + " found no elements in default content.");
		}
		return found;
	}

	/**
	 * Scans all frames for elements which match against the locator and returns a map of elements
	 * grouped by frame; empty frames are not added to the map. Driver focus is returned to default
	 * content upon completion.
	 * 
	 * @param locator <i>value</i> which exists in alias configuration
	 * @return elements grouped by frame
	 */
	private Map<WebElement, List<WebElement>> searchAllFrames(String locator) {

		// TODO frame and frameset.
		List<WebElement> frames = driver.findElements(By.tagName("iframe"));
		Map<WebElement, List<WebElement>> elementsByFrame = new HashMap<>();
		for (WebElement frame : frames) {
			driver.switchTo().frame(frame);
			List<WebElement> matchedElements = driver.findElements(By(locator));
			if (!matchedElements.isEmpty()) {
				elementsByFrame.put(frame, matchedElements);
			}
			driver.switchTo().defaultContent();
		}
		return elementsByFrame;
	}

	private WebElement validMatch(List<WebElement> defaultMatches,
			Map<WebElement, List<WebElement>> elementsByFrame) {
		if (noMatches(defaultMatches, elementsByFrame)) {
			throw new NoSuchElementException(String.format(NOT_FOUND, by.toString()));
		}
		if (extraMatches(defaultMatches, elementsByFrame)) {
			throw new AmbiguousLocatorException(locator);
		}
		return exactMatch(defaultMatches, elementsByFrame);
	}

	/**
	 * Assumes only one matched element exists across both collections.
	 */
	private WebElement exactMatch(List<WebElement> defaultMatches,
			Map<WebElement, List<WebElement>> elementsByFrame) {
		if (!defaultMatches.isEmpty()) {
			driver.switchTo().defaultContent();
			return defaultMatches.get(0);
		}
		driver.switchTo().frame(loneKey(elementsByFrame));
		return loneValue(elementsByFrame);
	}

	/**
	 * Dynamically finds the proper By based on the locator value.
	 * 
	 * @param locator <i>value</i> which exists in alias configuration
	 * @return
	 */
	private By By(String locator) {
		if (Locator.isXpath(locator)) {
			return byInfo(new ByXPath(locator));
		}
		if (Locator.isTag(locator)) {
			return byInfo(new By.ByTagName(locator));
		}
		if (Locator.isCss(locator)) {
			return byInfo(new By.ByCssSelector(locator));
		}
		if (Locator.isSentence(locator)) {
			return byInfo(new ByText(locator));
		}
			return byInfo(new ByFirst(new By.ById(locator), new By.ByName(locator),
					new By.ByLinkText(locator), new By.ByPartialLinkText(locator), new By.ByClassName(locator)));
		}

	private By byInfo(By by) {
		LOG.info(String.format("Searching by %s", by.toString()));
		this.by = by;
		return by;
	}

	/**
	 * Returns the correct trigger condition for ending a wait
	 */
	private ExpectedCondition<List<WebElement>> trigger(String locator, Until condition) {
		ExpectedCondition<List<WebElement>> trigger = null;
		switch (condition) {
		case PRESENT:
			trigger = ExpectedConditions.presenceOfAllElementsLocatedBy(By(locator));
			break;
		case VISIBLE:
			trigger = ExpectedConditions.visibilityOfAllElementsLocatedBy(By(locator));
			break;
		}
		LOG.info(String.format(UNTIL_INFO, condition.message));
		return trigger;
	}

	/**
	 * Returns a wait object with calculated maximum wait time.
	 * 
	 * @param wait
	 * @return
	 */
	private WebDriverWait wait(Wait wait) {
		double waitTime = (wait.val) * waitTimeFactor;
		LOG.info("Wait time: " + waitTime);
		return new WebDriverWait(driver, (long) waitTime);
	}

	/**
	 * Returns true if zero matches are found across or within the default content and the frames.
	 */
	private boolean noMatches(List<WebElement> defaultMatches,
			Map<WebElement, List<WebElement>> elementsByFrame) {
		if (defaultMatches.isEmpty() && elementsByFrame.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if multiple matches are found across or within the default content and the
	 * frames.
	 */
	private boolean extraMatches(List<WebElement> defaultMatches,
			Map<WebElement, List<WebElement>> elementsByFrame) {
		int matches = 0;
		matches += defaultMatches.size();
		for (WebElement frame : elementsByFrame.keySet()) {
			matches += elementsByFrame.get(frame).size();
			if (matches > 1) {
				return true;
			}
		}
		return matches > 1;
	}

	/**
	 * Assumes map has a single key and returns said key.
	 * 
	 * @param <T>
	 * @param <K>
	 * @param map
	 * @return Single key
	 */
	private <T, K> T loneKey(Map<T, K> map) {
		List<T> keys = new ArrayList<>(map.keySet());
		return keys.get(0);
	}

	/**
	 * Assumes map has only one value, value is a collection, value collection has only one value.
	 * Returns the drill-down value.
	 * 
	 * @param map
	 * @return Single value
	 */
	private <T, K> K loneValue(Map<T, List<K>> map) {
		List<List<K>> valueList = new ArrayList<>(map.values());
		return valueList.get(0).get(0);
	}
}
