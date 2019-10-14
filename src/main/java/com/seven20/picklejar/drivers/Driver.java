package com.seven20.picklejar.drivers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.seven20.picklejar.configuration.Config;
import com.seven20.picklejar.search.Search;
import com.seven20.picklejar.search.Until;
import com.seven20.picklejar.search.Wait;

public class Driver {

	private static final String URL_NO_CHANGE = "No change made to url for target url: %s";
	private static final String URL_CHANGE = "Loaded target url: %s";
	private static final String URL_NAVIGATION = "Navigating away from url: %s";
	private static Logger LOG = Logger.getLogger(Driver.class.getName());
	private WebDriver webDriver;
	private Config config;
	private DriverManager manager = DriverManager.getInstance();
	private double waitTimeFactor = 1;
	private Browser currentBrowser;

	public Driver(Config config) {
		this.config = config;
		this.waitTimeFactor = config.getDouble("waitTimeFactor");
		this.webDriver = initializeDriver();
	}

	// Necessary if someone kills the webdriver, the Driver instance is still up.
	// Further calls on Driver would result in null pointers
	private WebDriver driver() {
		if (webDriver == null) {
			webDriver = initializeDriver();
		}
		return webDriver;
	}

	private WebDriver initializeDriver() {
		String defaultBrowser = config.getString("defaultBrowser");
		return getDriver(Browser.get(defaultBrowser));
	}

	private WebDriver getDriver(Browser browser) {
		currentBrowser = browser;
		return manager.getDriver(browser);
	}

	/**
	 * Releases the current driver to the driver pool.
	 */
	public void close() {
		manager.release(currentBrowser, driver());
	}

	/**
	 * Kills the current driver without releasing to the pool.
	 */
	public void kill() {
		driver().close();
		webDriver = null;
	}

	/**
	 * Switches the browser currently in use by Driver.
	 * 
	 * @param browser Target browser which subsequent method calls should use
	 */
	public void switchBrowser(Browser browser) {
		close();
		webDriver = getDriver(browser);
	}

	/**
	 * Navigates to the specified url using the getUrl function. If already on the page, the driver does not refresh the page,
	 * and does not wait. 
	 * 
	 * @param url to which the driver will navigate. Must be an alias key!
	 * @return false if already on the page
	 */
	public boolean get(String url) {
		return getUrl(url , Wait.MEDIUM);
	}
	
	/**
	 * Navigates to the specified url using the getUrl function. If already on the page, the driver does not refresh the page,
	 * and does not wait.
	 * 
	 * @param url to which the driver will navigate. Must be an alias key!
	 * @param wait wait maximum time to navigate to specified url
	 * @return
	 */
	public boolean get(String url, Wait wait) {
		return getUrl(url , wait);
	}
	
	private boolean getUrl(String url, Wait wait) {
		String previousUrl = driver().getCurrentUrl();
		String targetUrl = config.getUrl(url);
		LOG.info(String.format(URL_NAVIGATION, previousUrl));
		if (isValidTarget(previousUrl, targetUrl)) {
			loadPage(previousUrl, targetUrl , wait);
			LOG.info(String.format(URL_CHANGE, targetUrl));
			return true;
		}
		LOG.info(String.format(URL_NO_CHANGE, targetUrl));
		return false;
	}
	
	private boolean isValidTarget(String previousUrl, String targetUrl) {
		return targetUrl != null && !targetUrl.equals(previousUrl);
	}

	/**
	 * Will load the target url and wait until the driver picks up a change in the url.
	 */
	private void loadPage(String previousUrl, String targetUrl ,Wait wait) {
		WebDriverWait driverWait = waitUntil(wait);
		ExpectedCondition<Boolean> finishedLoading = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return (d.getCurrentUrl() != previousUrl);
			}
		};
		driver().get(targetUrl);
		driverWait.until(finishedLoading);
	}
	
	/**
	 * Will wait until the specified element does not exist on the page using the gone function
	 * @param element the element on the page which should not exist for the next action to take place
	 */
	public void waitUntilGone(WebElement element){
		gone(element,Wait.MEDIUM);
	}
	
	/**
	 * Will wait until the specified element does not exist on the page using the gone function
	 * @param element the element on the page which should not exist for the next action to take place
	 * @param wait wait maximum time to continue search if the element exists
	 */
	public void waitUntilGone(WebElement element, Wait wait){
		gone(element,wait);
	}
	

	private void gone(WebElement element , Wait wait){
		WebDriverWait driverWait = waitUntil(wait);
		ExpectedCondition<Boolean> gone = new ExpectedCondition<Boolean>(){
			public Boolean apply(WebDriver d){
				try {
					element.isDisplayed();
			}
			catch (StaleElementReferenceException e ){
				return true;
			}
				return false;
		}};
	driverWait.until(gone);
	}
	
	/**
	 * Used to calculate the Driver wait time
	 * @param wait wait the specified time for the Web Driver
	 * @return
	 */
	private WebDriverWait waitUntil(Wait wait){
		double waitTime = (wait.val) * waitTimeFactor;
		return new WebDriverWait(driver(),(long) waitTime);
	}
	
	/**
	 * @return current screen as a byte array
	 */
	public byte[] getScreenshotAsBytes() {
		return ((TakesScreenshot) driver()).getScreenshotAs(OutputType.BYTES);
	}

	/**
	 * Finds a single element which matches the search criteria.
	 * <p>
	 * Implemented to allow access to standard selenium methods for edge cases; try to use the
	 * find() method instead!
	 * 
	 * @param by
	 * @return
	 */
	public WebElement findElement(By by) {
		return webDriver.findElement(by);
	}

	/**
	 * Finds all elements which match the search criteria.
	 * <p>
	 * Implemented to allow access to standard selenium methods for edge cases
	 * 
	 * @param by
	 * @return
	 */
	public List<WebElement> findElements(By by) {
		return webDriver.findElements(by);
	}

	/**
	 * The title of the current page.
	 * 
	 * @return The title of the current page, with leading and trailing whitespace stripped, or null
	 *         if one is not already set
	 */
	public String getTitle() {
		return driver().getTitle();
	}

	/**
	 * Get a string representing the current URL that the browser is looking at.
	 * 
	 * @return The URL of the page currently loaded in the browser
	 */
	public String getCurrentUrl() {
		return driver().getCurrentUrl();
	}

	/**
	 * Get the source of the last loaded page. If the page has been modified after loading (for
	 * example, by Javascript) there is no guarantee that the returned text is that of the modified
	 * page. Please consult the documentation of the particular driver being used to determine
	 * whether the returned text reflects the current state of the page or the text last sent by the
	 * web server. The page source returned is a representation of the underlying DOM: do not expect
	 * it to be formatted or escaped in the same way as the response sent from the web server. Think
	 * of it as an artist's impression.
	 * 
	 * @return The source of the current page
	 */
	public String getPageSource() {
		return driver().getPageSource();
	}

	/**
	 * Return a set of window handles which can be used to iterate over all open windows of this
	 * WebDriver instance by passing them to switchTo().WebDriver.Options.window()
	 * 
	 * @return A set of window handles which can be used to iterate over all open windows.
	 */
	public Set<String> getWindowHandles() {
		return driver().getWindowHandles();
	}

	/**
	 * @return Return an opaque handle to this window that uniquely identifies it within this driver
	 *         instance. This can be used to switch to this window at a later date
	 */
	public String getWindowHandle() {
		return driver().getWindowHandle();
	}

	/**
	 * @return an option interface
	 */
	public Options manage() {
		return driver().manage();
	}

	/**
	 * Send future commands to a different frame or window.
	 * 
	 * @return A TargetLocator which can be used to select a frame or window
	 */
	public TargetLocator switchTo() {
		return driver().switchTo();
	}

	/**
	 * Sends key input to element identified by locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param input keys to send
	 * @return element which receives the sent keys
	 */
	public WebElement sendKeys(String locator, String input) {
		WebElement element = find(locator);
		element.sendKeys(input);
		return element;
	}

	/**
	 * Sends key input to element identified by locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param input keys to send
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @return element which receives the sent keys
	 */
	public WebElement sendKeys(String locator, String input, Wait wait) {
		WebElement element = find(locator, wait);
		element.sendKeys(input);
		return element;
	}

	/**
	 * Sends key input to element identified by locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param input keys to send
	 * @param condition target element must satisfy before returning
	 * @return element which receives the sent keys
	 */
	public WebElement sendKeys(String locator, String input, Until condition) {
		WebElement element = find(locator, Wait.MEDIUM, condition);
		element.sendKeys(input);
		return element;
	}

	/**
	 * Sends key input to element identified by locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param input keys to send
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @param condition target element must satisfy before returning
	 * @return element which receives the sent keys
	 */
	public WebElement sendKeys(String locator, String input, Wait wait, Until condition) {
		WebElement element = find(locator, wait, condition);
		element.sendKeys(input);
		return element;
	}

	/**
	 * Clicks the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @return element which is clicked
	 */
	public WebElement click(String locator) {
		WebElement element = find(locator);
		element.click();
		return element;
	}

	/**
	 * Clicks the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @return element which is clicked
	 */
	public WebElement click(String locator, Wait wait) {
		WebElement element = find(locator, wait);
		element.click();
		return element;
	}

	/**
	 * Clicks the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param condition target element must satisfy before returning
	 * @return element which is clicked
	 */
	public WebElement click(String locator, Until condition) {
		WebElement element = find(locator, Wait.MEDIUM, condition);
		element.click();
		return element;
	}

	/**
	 * Clicks the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @param condition target element must satisfy before returning
	 * @return element which is clicked
	 */
	public WebElement click(String locator, Wait wait, Until condition) {
		WebElement element = find(locator, wait, condition);
		element.click();
		return element;
	}
	
	/**
	 * Moves the mouse to the center of the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @return element which is hovered over
	 */
	public WebElement mouseOver(String locator) {
		WebElement element = find(locator);
		Actions builder = new Actions(driver());
		Action mouseOverSetup = builder.moveToElement(element).build();
		mouseOverSetup.perform();
		return element;
	}
	
	/**
	 * Moves the mouse to the center of the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @return element which is hovered over
	 */
	public WebElement mouseOver(String locator, Wait wait) {
		WebElement element = find(locator);
		Actions builder = new Actions(driver());
		Action mouseOverSetup = builder.moveToElement(element).build();
		mouseOverSetup.perform();
		return element;
	}

	/**
	 * Moves the mouse to the center of the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param condition target element must satisfy before returning
	 * @return element which is hovered over
	 */
	public WebElement mouseOver(String locator, Until condition) {
		WebElement element = find(locator);
		Actions builder = new Actions(driver());
		Action mouseOverSetup = builder.moveToElement(element).build();
		mouseOverSetup.perform();
		return element;
	}

	/**
	 * Moves the mouse to the center of the element identified by the locator and returns the element.
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @param condition target element must satisfy before returning
	 * @return element which is hovered over
	 */
	public WebElement mouseOver(String locator, Wait wait, Until condition) {
		WebElement element = find(locator);
		Actions builder = new Actions(driver());
		Action mouseOverSetup = builder.moveToElement(element).build();
		mouseOverSetup.perform();
		return element;
	}

	/**
	 * Finds and returns the element identified by the locator. <br>
	 * Supports searches by:
	 * <ul>
	 * <li>xpath
	 * <li>tag name
	 * <li>css selectors
	 * <li>inner text
	 * <li>id
	 * <li>name
	 * <li>link text
	 * </ul>
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @return element found
	 */
	public WebElement find(String locator) {
		return new Search(lookupAlias(locator), Wait.MEDIUM, Until.PRESENT, waitTimeFactor,
				driver()).result();
	}

	/**
	 * Finds and returns the element identified by the locator. <br>
	 * Supports searches by:
	 * <ul>
	 * <li>xpath
	 * <li>tag name
	 * <li>css selectors
	 * <li>inner text
	 * <li>id
	 * <li>name
	 * <li>link text
	 * </ul>
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @return element found
	 */
	public WebElement find(String locator, Wait wait) {
		return new Search(lookupAlias(locator), wait, Until.PRESENT, waitTimeFactor, driver())
				.result();
	}

	/**
	 * Finds and returns the element identified by the locator.<br>
	 * Supports searches by:
	 * <ul>
	 * <li>xpath
	 * <li>tag name
	 * <li>css selectors
	 * <li>inner text
	 * <li>id
	 * <li>name
	 * <li>link text
	 * </ul>
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param condition target element must satisfy before returning
	 * @return element found
	 */
	public WebElement find(String locator, Until condition) {
		return new Search(lookupAlias(locator), Wait.MEDIUM, condition, waitTimeFactor, driver())
				.result();
	}

	/**
	 * Finds and returns the element identified by the locator.<br>
	 * Supports searches by:
	 * <ul>
	 * <li>xpath
	 * <li>tag name
	 * <li>css selectors
	 * <li>inner text
	 * <li>id
	 * <li>name
	 * <li>link text
	 * </ul>
	 * 
	 * @param locator unique identifier of target element. Must be an alias key!
	 * @param wait maximum time to continue search if the element is not immediately available
	 * @param condition target element must satisfy before returning
	 * @return element found
	 */
	public WebElement find(String locator, Wait wait, Until condition) {
		return new Search(lookupAlias(locator), wait, condition, waitTimeFactor, driver()).result();
	}

	private String lookupAlias(String locator) {
		return config.containsKey(locator) ? config.getString(locator) : locator;
	}

	/**
	 * Initiates a search for each locator in the list, returning one element for each.
	 * 
	 * @param locators unique identifiers for each target element. Must each be an alias key!
	 * @return elements matching each of the locators
	 */
	public List<WebElement> findEach(List<String> locators) {
		int idx = -1;
		List<WebElement> elements = new ArrayList<>();
		for (String element : locators) {
			elements.add(find(element, idx++ < 0 ? Wait.MEDIUM : Wait.NO_WAIT, Until.PRESENT));
		}
		return elements;
	}

	/**
	 * Initiates a search for each locator in the list, returning one element for each.
	 * 
	 * @param locators unique identifiers for each target element. Must each be an alias key!
	 * @param wait maximum time to continue search if the first element is not immediately available
	 * @return elements matching each of the locators
	 */
	public List<WebElement> findEach(List<String> locators, Wait wait) {
		int idx = -1;
		List<WebElement> elements = new ArrayList<>();
		for (String element : locators) {
			elements.add(find(element, idx++ < 0 ? wait : Wait.NO_WAIT, Until.PRESENT));
		}
		return elements;
	}

	/**
	 * Initiates a search for each locator in the list, returning one element for each.
	 * 
	 * @param locators unique identifiers for each target element. Must each be an alias key!
	 * @param condition each target element must satisfy before returning
	 * @return elements matching each of the locators
	 */
	public List<WebElement> findEach(List<String> locators, Until condition) {
		int idx = -1;
		List<WebElement> elements = new ArrayList<>();
		for (String element : locators) {
			elements.add(find(element, idx++ < 0 ? Wait.MEDIUM : Wait.NO_WAIT, condition));
		}
		return elements;
	}

	/**
	 * Initiates a search for each locator in the list, returning one element for each.
	 * 
	 * @param locators unique identifiers for each target element. Must each be an alias key!
	 * @param wait maximum time to continue search if the first element is not immediately available
	 * @param condition each target element must satisfy before returning
	 * @return elements matching each of the locators
	 */
	public List<WebElement> findEach(List<String> locators, Wait wait, Until condition) {
		int idx = -1;
		List<WebElement> elements = new ArrayList<>();
		for (String element : locators) {
			elements.add(find(element, idx++ < 0 ? wait : Wait.NO_WAIT, condition));
		}
		return elements;
	}
}
