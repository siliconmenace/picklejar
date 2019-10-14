package com.seven20.unit.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.apache.commons.configuration2.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.seven20.picklejar.configuration.Config;

@RunWith(MockitoJUnitRunner.class)
public class ConfigTest {

	@Mock
	public Configuration mock;

	@Test
	public void shouldAlwaysHaveWaitTimeFactor() {
		Config config = new Config(mock);
		when(mock.getDouble("waitTimeFactor")).thenReturn(1.0);
		assertEquals(1.0, config.getDouble("waitTimeFactor"), 0.0);
	}

	@Test
	public void shouldAlwaysHaveDomain() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn("https://api.pickerjar.seven20.com/");
		assertEquals("https://api.pickerjar.seven20.com/", config.getString("domain"));
	}
	
	@Test
	public void shouldAlwaysHaveDefaultBrowser() {
		Config config = new Config(mock);
		when(mock.getString("defaultBrowser")).thenReturn("ie");
		assertEquals("ie", config.getString("defaultBrowser"));
	}

	@Test
	public void shouldContainOnlyRelativeUrlsIfOnTheRootDomain() {
		Config config = new Config(mock);
		when(mock.getString("loginPage")).thenReturn("OAuth/SSO/Login");
		assertEquals("OAuth/SSO/Login", config.getString("loginPage"));
	}
	
	@Test
	public void shouldUseFullUrlsIfNotOnTheRootDomain(){
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn("https://api.pickerjar.seven20.com/");
		when(mock.getString("loginPage")).thenReturn("https://www.google.com");
		assertEquals("https://www.google.com", config.getUrl("loginPage"));
	}

	@Test
	public void shouldConstructUrlsUsingDomainAndRelativePath() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn("https://api.pickerjar.seven20.com/");
		when(mock.getString("loginPage")).thenReturn("OAuth/SSO/Login");
		assertEquals("https://api.pickerjarseven20.com/OAuth/SSO/Login",
				config.getUrl("loginPage"));
	}

	@Test
	public void shouldReturnOnlyDomainIfRelativePathIsNotSet() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn("https://api.pickerjar.seven20.com/");
		when(mock.getString("loginPage")).thenReturn(null);
		assertEquals("https://api.pickerjarseven20.com/", config.getUrl("loginPage"));
	}

	@Test
	public void shouldReturnOnlyDomainIfRelativePathIsEmpty() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn("https://api.picklejar.seven20.com/");
		when(mock.getString("loginPage")).thenReturn("");
		assertEquals("https://api.pickerjarseven20.com/", config.getUrl("loginPage"));
	}

	@Test
	public void shouldReturnOnlyRelativePathIfDomainIsNotSet() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn(null);
		when(mock.getString("loginPage")).thenReturn("OAuth/SSO/Login");
		assertEquals("OAuth/SSO/Login", config.getUrl("loginPage"));
	}

	@Test
	public void shouldReturnOnlyRelativePathIfDomainIsEmpty() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn("");
		when(mock.getString("loginPage")).thenReturn("OAuth/SSO/Login");
		assertEquals("OAuth/SSO/Login", config.getUrl("loginPage"));
	}

	@Test
	public void shouldReturnEmptyIfDomainAndRelativePathAreNotSet() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn(null);
		when(mock.getString("loginPage")).thenReturn(null);
		assertEquals("", config.getUrl("loginPage"));
	}

	@Test
	public void shouldReturnNullIfDomainAndRelativePathAreEmpty() {
		Config config = new Config(mock);
		when(mock.getString("domain")).thenReturn("");
		when(mock.getString("loginPage")).thenReturn("");
		assertEquals("", config.getUrl("loginPage"));
	}

}
