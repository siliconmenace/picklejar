package com.seven20.picklejar.stepdefinitions;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import com.seven20.picklejar.search.Until;
import com.seven20.picklejar.search.Wait;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class FindConditionStepDef extends StepDefinition {

	private Scenario myscenario;

	@Before
	public void stepScreenShot(Scenario scenario) {
		myscenario = scenario;
	}

	@Given("^I have logged into \"([^\"]*)\" using \"([^\"]*)\"$")
	public void i_have_logged_into_using(String _url, String _id) throws Throwable {
		driver().get(_url);
		driver().sendKeys(_id, alias().getString(alias().getString(_id)));
		driver().sendKeys(_id + "_pwd", alias().getString(alias().getString(_id + "_pwd")),
				Wait.LONG);
		driver().click("SignIn_btn");
		takeScreenshot(myscenario);
	}

	@When("^I click on \"([^\"]*)\"$")
	public void i_click_on_(String r2r) throws Throwable {
		driver().click(r2r, Wait.LONG, Until.PRESENT);
		takeScreenshot(myscenario);
	}

	@And("^select \"([^\"]*)\"$")
	public void select(String _setup) throws Throwable {
		driver().mouseOver(_setup, Until.PRESENT);
		driver().click(_setup + ".Binders", Wait.LONG, Until.VISIBLE);
		takeScreenshot(myscenario);
	}

	@Then("^I must see the setup binders page$")
	public void i_must_see_the_setup_page() throws Throwable {
		driver().find("Result", Until.PRESENT);
		takeScreenshot(myscenario);
		//driver().click("SignOut");
	}
	

	@Given("^I am on the setup binders page$")
	public void i_am_on_the_setup_accounts_page() throws Throwable {
		driver().find("Result",Until.PRESENT);
	}

	@When("^I open an \"([^\"]*)\" under the Binders Page$")
	public void i_open_an_under_the_Binders_Page(String _binders_name) throws Throwable {
		driver().click(_binders_name);
	}

	@Then("^I must click on the \"([^\"]*)\"$")
	public void i_must_click_on_the_followed_by(String binders_link) throws Throwable {
		driver().click(binders_link);
		driver().sendKeys("Binders.Entity.Link.Find","SSC");
		driver().click("Binders.Entity.Link.Apply");
		driver().click("Binders.Entity.Link.CheckBox1");
		driver().click("Binders.Entity.Link.CheckBox2");
		WebElement e = driver().find("Binders.Entity.Page.Element");
		driver().click("Binders.Entity.Link.Choose");
		driver().waitUntilGone(e);
		driver().click("SignOut");
		try {
		Alert alert = driver().switchTo().alert();
		alert.accept();
		}
		catch (NoAlertPresentException ex){
		}
	}
	

}