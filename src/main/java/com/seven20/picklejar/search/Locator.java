package com.seven20.picklejar.search;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Locator {

	private static final List<String> SUPPORTED_TAGS = Arrays.asList("a", "abbr", "acronym",
			"address", "applet", "area", "article", "aside", "audio", "b", "base", "basefont",
			"bdi", "bdo", "big", "blockquote", "body", "br", "button", "canvas", "caption",
			"center", "cite", "code", "col", "colgroup", "datalist", "dd", "del", "details", "dfn",
			"dialog", "dir", "div", "dl", "dt", "em", "embed", "fieldest", "figcaption", "figure",
			"font", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6",
			"head", "hr", "html", "i", "iframe", "img", "input", "ins", "isindex", "kbd", "keygen",
			"label", "legend", "li", "link", "main", "mark", "map", "menu", "menuitem", "meta",
			"meter", "nav", "nobr", "noframes", "noscript", "object", "ol", "optgroup", "option",
			"output", "p", "param", "pre", "progress", "q", "rp", "rt", "ruby", "s", "samp",
			"script", "select", "section", "small", "source", "span", "strike", "strong", "style",
			"sub", "summary", "sup", "table", "tbody", "td", "textarea", "tfoot", "th", "thead",
			"time", "title", "tr", "track", "tt", "u", "ul", "var", "video", "wbr");

	/**
	 * Determines if the given locator is in xpath format.
	 * 
	 * @param locator <i>value</i> which exists in alias configuration
	 * @return true if starts with / or //.
	 */
	public static boolean isXpath(String locator) {
		return locator.startsWith("/") || locator.startsWith("./") 
				|| locator.startsWith("(/") || locator.startsWith("(./");
	}

	/**
	 * Determines if the given locator is an html tag name.
	 * 
	 * @param locator <i>value</i> which exists in alias configuration
	 * @return true if supported html tag
	 */
	public static boolean isTag(String locator) {
		return SUPPORTED_TAGS.contains(locator.toLowerCase());
	}

	/**
	 * Determines if the given locator is a CSS selector.
	 * 
	 * @param locator <i>value</i> which exists in alias configuration
	 * @return true if contains correct CSS selector syntax: "[attribute=value]"
	 */
	public static boolean isCss(String locator) {
		return isSearchForAttribute(locator) || isSearchForContains(locator);
	}

	private static boolean isSearchForAttribute(String locator) {
		Pattern p = Pattern.compile("[\\[\\]=+\\.#>:]");
		Matcher m = p.matcher(locator);
		return m.find();
	}

	private static boolean isSearchForContains(String locator) {
		Pattern p = Pattern.compile("contains");
		Matcher m = p.matcher(locator);
		return m.find();
	}

	/**
	 * Determines if the given locator is a sentence of three or more words.
	 * 
	 * @param locator <i>value</i> which exists in alias configuration
	 * @return true if contains three or more words
	 */
	public static boolean isSentence(String locator) {
		return locator.matches("(\\w+)\\s(\\w+)\\s(\\w+).*");
	}

}
