package com.seven20.picklejar.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * Searches according to the input order of the Bys, returning first match.
 */
public class ByFirst extends By {

	List<By> bys;

	public ByFirst(By... _bys) {
		this.bys = Arrays.asList(_bys);
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {

		List<WebElement> matches = new ArrayList<>();
		for (By by : bys) {
			matches = by.findElements(context);
			if (!matches.isEmpty()) {
				return matches;
			}
		}
		return matches;
	}

	@Override
	public WebElement findElement(SearchContext context) {
		WebElement match = null;
		for (By by : bys) {
			try {
				match = by.findElement(context);
			} catch (NoSuchElementException e) {
				continue;
			}
			return match;
		}
		throw new NoSuchElementException("No element found");
	}

	@Override
	public String toString() {
		String printFormat = "By.First[%s]";
		StringBuilder stringBuilder = new StringBuilder();
		for (By by : bys) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(",");
			}
			stringBuilder.append(by);
		}
		return String.format(printFormat, stringBuilder.toString());
	}

}
