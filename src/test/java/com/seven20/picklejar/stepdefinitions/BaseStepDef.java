package com.seven20.picklejar.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;

import com.seven20.picklejar.search.Until;
import com.seven20.picklejar.search.Wait;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class BaseStepDef extends StepDefinition {

	private Scenario myscenario;

	@Before
	public void stepScreenShot(Scenario scenario) {
		myscenario = scenario;
	}

	@Given("^I am on \"([^\"]*)\"$")
	public void i_am_on(String url) throws Throwable {
		assertTrue(url != null);
		driver().get(url);
		assertEquals(alias().getString(url), driver().getCurrentUrl());
		takeScreenshot(myscenario);
	};

	@Given("^I have opened a link to \"([^\"]*)\"$")
	public void i_have_opened_a_link_to(String url) throws Throwable {
		assertTrue(url != null);
		driver().manage().window().maximize();
		driver().get(url);
		assertEquals(url, driver().getTitle());
		dataStore().store("url", url);
		takeScreenshot(myscenario);
	}

	@When("^I do search for \"([^\"]*)\"$")
	public void i_do_search_for(String criteria) throws Throwable {

		// Using helper methods to shorting the boiler plate workload.
		driver().sendKeys(dataStore().getAsString("url") + "_searchBox", criteria, Wait.LONG,
				Until.VISIBLE);
		takeScreenshot(myscenario);
		driver().click(dataStore().getAsString("url") + "_searchBtn", Wait.LONG, Until.VISIBLE);
		// Long hand way of doing the above.
		// driver().findElement(By.id(ConfigurationManager.getAsString(dataStore().get("url")+"_searchBox"))).sendKeys(criteria);
		// driver().findElement(By.id(ConfigurationManager.getAsString(dataStore().get("url")+"_searchBtn"))).click();
	}

	@Then("^I should see \"([^\"]*)\"$")
	public void i_should_see(String results) throws Throwable {
		takeScreenshot(myscenario);
		String title = driver().getTitle();
		assertEquals(alias().getString(results), driver().find(title + "_xpath").getText());
	}

	@Then("^I should see \"([^\"]*)\" using \"([^\"]*)\"$")
	public void i_should_see_using(String results, String xpath) throws Throwable {
		takeScreenshot(myscenario);
		assertEquals(alias().getString(results), driver().find(xpath).getText());
	}

	@Given("^I have stored \"([^\"]*)\"$")
	public void i_have_stored(String input) throws Throwable {
		dataStore().store("1", alias().getBoolean("boolean" + input));
		dataStore().store("1", alias().getBoolean("boolean" + input));
		dataStore().store("2", alias().getDouble("double" + input));
		dataStore().store("3", alias().getInt("integer" + input));
		dataStore().store("4", alias().getLong("long" + input));
		dataStore().store("5", alias().getString("string" + input));
		takeScreenshot(myscenario);
	}

	@Then("^I should retrieve the same \"([^\"]*)\"$")
	public void i_should_retrieve_the_same(String output) throws Throwable {
		assertEquals(alias().getString("string" + output), dataStore().getAsString("5"));
		assertEquals(alias().getBoolean("boolean" + output), dataStore().getAsBoolean("1"));
		assertEquals(alias().getDouble("double" + output), dataStore().getAsDouble("2"), 0);
		assertEquals(alias().getInt("integer" + output), dataStore().getAsInteger("3"), 0);
		assertEquals(alias().getLong("long" + output), dataStore().getAsLong("4"), 0);
		takeScreenshot(myscenario);
	}

	@AfterClass
	public void teardown() {
		// driver().closeDriver();
		dataStore().clearStoredData();
	}
}
