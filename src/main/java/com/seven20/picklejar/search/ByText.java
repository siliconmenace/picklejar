package com.seven20.picklejar.search;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Searches for inner text which matches against the locator.
 */
public class ByText extends By {

	private final String text;
	private final ByFirst bys;
	private static final String XPATH_EXACT = "//*[text()='%s']";
	// Use exact to prevent ambiguous results for searches in divs/forms/etc.
	@SuppressWarnings("unused")
	private static final String XPATH_CONTAINS = "//*[text()[contains(.,'%s')]]";

	public ByText(String _text) {
		this.text = _text;
		this.bys = initializeBys(_text);
	}

	private ByFirst initializeBys(String _text) {
		By first = new By.ByXPath(String.format(XPATH_EXACT, _text));
		By second = new By.ByPartialLinkText(_text);
		return new ByFirst(first, second);
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		return bys.findElements(context);
	}

	@Override
	public WebElement findElement(SearchContext context) {
		return bys.findElement(context);
	}

	@Override
	public String toString() {
		return "By.Text: " + text;
	}
}
