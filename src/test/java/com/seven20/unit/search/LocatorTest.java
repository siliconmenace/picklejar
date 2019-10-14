package com.seven20.unit.search;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.seven20.picklejar.search.Locator;

@RunWith(Enclosed.class)
public class LocatorTest {

	@RunWith(Parameterized.class)
	public static class MatchTests extends LocatorTest {

		public MatchTests() throws ConfigurationException {
			super();
		}

		private enum SEARCH_TYPE {
			XP, TAG, CSS, TEXT
		}

		@Parameter(0)
		public String testName;

		@Parameter(1)
		public SEARCH_TYPE search;

		@Parameter(2)
		public String locator;

		@Parameter(3)
		public Boolean expected;

		@Parameters(name = "{0}")
		public static Collection<Object[]> data() {
			// @formatter:off
			return Arrays.asList(new Object[][] {
					{ "shouldMatchForIdXpath", SEARCH_TYPE.XP, "//*[@id=\"menu-item-7964\"", true },
					{ "shouldMatchForIdXpathDiv", SEARCH_TYPE.XP, "//*[@id=\"header\"]/div[2]",
							true },
					{ "shouldMatchForIdXpathHref", SEARCH_TYPE.XP, "//*[@id=\"menu-item-562\"]/a",
							true },
					{ "shouldMatchForIdXpathNestedDiv", SEARCH_TYPE.XP,
							"//*[@id=\"header\"]/div[2]/nav/div", true },
					{ "shouldMatchForNonIdXpathBody", SEARCH_TYPE.XP, "/html/body", true },
					{ "shouldMatchForNonIdXpathNestedForm", SEARCH_TYPE.XP,
							"/html/body/div/form/h3", true },
					{ "shouldMatchForNonIdXpathFormInput", SEARCH_TYPE.XP,
							"/html/body/div/form/p[12]/input", true },
					{ "shouldMatchForXpathContainsExpression", SEARCH_TYPE.XP,
							"//span[contains(.,'New')]", true },
					{ "shouldMatchForAnotherXpathContainsExpression", SEARCH_TYPE.XP,
							"//span[contains(.,'Save')]", true },
					{ "shouldMatchForRootLevelSearchOfH3", SEARCH_TYPE.XP, "/h3[2]", true },
					{ "shouldMatchForTreeWideSearchOfH3", SEARCH_TYPE.XP, "//h3[2]", true },
					{ "shouldMatchForParenSlash", SEARCH_TYPE.XP, "(/INPUT[@id=\"controlChooser\"])[2]", true },
					{ "shouldMatchForParenSlashSlash", SEARCH_TYPE.XP, "(//INPUT[@id=\"controlChooser\"])[2]", true },
					{ "shouldMatchForParenDotSlash", SEARCH_TYPE.XP, "(./INPUT[@id=\"controlChooser\"])[2]", true },
					{ "shouldMatchForParenDotSlashSlash", SEARCH_TYPE.XP, "(.//INPUT[@id=\"controlChooser\"])[2]", true },
					{ "shouldNotMatchForCommonIdValues1", SEARCH_TYPE.XP, "myFrame", false },
					{ "shouldNotMatchForCommonIdValues2", SEARCH_TYPE.XP, "content", false },
					{ "shouldNotMatchForCommonIdValues3", SEARCH_TYPE.XP, "header", false },
					{ "shouldNotMatchForCommonIdValues4", SEARCH_TYPE.XP, "footer", false },
					{ "shouldNotMatchForCommonIdValues5", SEARCH_TYPE.XP, "container", false },
					{ "shouldNotMatchForCommonIdValues6", SEARCH_TYPE.XP, "#menu_li_0 > a", false },
					{ "shouldMatchForATag", SEARCH_TYPE.TAG, "a", true },
					{ "shouldMatchForPTag", SEARCH_TYPE.TAG, "p", true },
					{ "shouldMatchForFrameTag", SEARCH_TYPE.TAG, "frame", true },
					{ "shouldMatchForTitleTag", SEARCH_TYPE.TAG, "title", true },
					{ "shouldMatchForDivTag", SEARCH_TYPE.TAG, "div", true },
					{ "shouldMatchForTableTag", SEARCH_TYPE.TAG, "table", true },
					{ "shouldMatchForTableTag1", SEARCH_TYPE.TAG, "#menu_li_0 > a", false },
					{ "shouldMatchForTableIgnoringCaseTag", SEARCH_TYPE.TAG, "Table", true },
					{ "shouldMatchTagAndSingleAttribute", SEARCH_TYPE.CSS, "input[id=inputId]",
							true },
					{ "shouldMatchTagAndMultipleAttribute", SEARCH_TYPE.CSS,
							"input[name=inputName][type=inputText]", true },
					{ "shouldMatchMultipleTagAndSingleAttribute", SEARCH_TYPE.CSS,
							"form input[id=inputId]", true },
					{ "shouldMatchId", SEARCH_TYPE.CSS, "input#inputId", true },
					{ "shouldMatchId1", SEARCH_TYPE.CSS, "#menu_li_0 > a", true },
					{ "shouldMatchId2", SEARCH_TYPE.CSS, "menu_li_0 > a", true },
					{ "shouldMatchId3", SEARCH_TYPE.CSS, " > a", true },
					{ "shouldMatchId4", SEARCH_TYPE.CSS, ":a", true },
					{ "shouldMatchId5", SEARCH_TYPE.CSS, ":nth-child(3)", true },
					{ "shouldMatchSingleClass", SEARCH_TYPE.CSS, "input.inputClass", true },
					{ "shouldMatchMultipleClass", SEARCH_TYPE.CSS, "input.inputClass1.inputClass2",
							true },
					{ "shouldMatchClassAndAttribute", SEARCH_TYPE.CSS,
							"input.inputClass[name=inputName]", true },
					{ "shouldMatchTagAndPartialAttributeStart", SEARCH_TYPE.CSS,
							"input[id^='inpu']", true },
					{ "shouldMatchTagAndPartialAttributeEnd", SEARCH_TYPE.CSS, "input[id$='Id']",
							true },
					{ "shouldMatchTagAndPartialAttributeContains", SEARCH_TYPE.CSS,
							"input[id*='put']", true },
					{ "shouldMatchTagPaths", SEARCH_TYPE.CSS, "div.divClass>form>input[id=inputId]",
							true },
					{ "shouldMatchAdjacentTags()", SEARCH_TYPE.CSS, "p:contains(\"text\")", true },
					{ "shouldMatchKeywordContains", SEARCH_TYPE.CSS, "input+input", true },
					{ "shouldMatchIfThreeWords", SEARCH_TYPE.TEXT, "needs three words", true },
					{ "shouldAcceptAlphanumeric", SEARCH_TYPE.TEXT, "1337 h4x m4tch35", true },
					{ "shouldMatchFiveWords", SEARCH_TYPE.TEXT, "there are five words here", true },
					{ "shouldNotMatchUnderscores", SEARCH_TYPE.TEXT, "this_wont_work", false },
					{ "shouldNotMatchJustForLongEntries", SEARCH_TYPE.TEXT,
							"doesntreallysuperduperactuallycareaboutlength", false },
					{ "shouldNotMatchTwoWords", SEARCH_TYPE.TEXT, "two words", false },
					{ "shouldNotMatchIfStartsWithSpace", SEARCH_TYPE.TEXT, " this should not match",
							false },
					{ "shouldNotMatchIfSpace", SEARCH_TYPE.TEXT, " ", false },
					{ "shouldNotMatchIfSpacesAtFront", SEARCH_TYPE.TEXT,
							"     then a bunch of words", false },
					{ "shouldNotMatchXpathIds", SEARCH_TYPE.TEXT, "//*[@id=\" long xpath id \"]/a",
							false }, });
			// @formatter:on
		}

		@Test
		public void isMatch() {
			assertEquals(expected, isMatch(search, locator));
		}

		private boolean isMatch(SEARCH_TYPE _search, String _locator) {

			switch (_search) {
			case XP:
				return Locator.isXpath(_locator);
			case TAG:
				return Locator.isTag(_locator);
			case CSS:
				return Locator.isCss(_locator);
			case TEXT:
				return Locator.isSentence(_locator);
			}
			return false;
		}
	}

}
