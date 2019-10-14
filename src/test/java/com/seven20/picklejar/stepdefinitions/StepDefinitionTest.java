package com.seven20.picklejar.stepdefinitions;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.seven20.picklejar.configuration.Config;
import com.seven20.picklejar.drivers.Driver;
import com.seven20.picklejar.utils.DataStore;

import cucumber.api.Scenario;

public class StepDefinitionTest {

	StepDefinition stepdef = new StepDefinition();

	@Test
	public void canReturnDriver() {
		assertNotNull(stepdef.driver());
	}

	@Test
	public void alwaysReturnsTheSameDriver() {
		Driver = stepdef.driver();
		Driver b = stepdef.driver();
		assertEquals(a, b);
	}

	@Test
	public void canReturnConfig() {
		assertNotNull(stepdef.alias());
	}

	@Test
	public void alwaysReturnsTheSameConfig() {
		Config a = stepdef.alias();
		Config b = stepdef.alias();
		assertEquals(a, b);
	}

	@Test
	public void canReturnDataStore() {
		assertNotNull(stepdef.dataStore());
	}

	@Test
	public void alwaysReturnsTheSameDataStore() {
		DataStore a = stepdef.dataStore();
		DataStore b = stepdef.dataStore();
		assertEquals(a, b);
	}

	@Test
	public void canTakeScreenshot() {
		Driver temp = stepdef.driver();
		Scenario scenario = new Scenario() {
			String name;

			@Override
			public void write(String arg0) {
			}

			@Override
			public boolean isFailed() {
				return false;
			}

			@Override
			public String getStatus() {
				return null;
			}

			@Override
			public Collection<String> getSourceTagNames() {
				return new ArrayList<String>();
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getId() {
				return null;
			}

			@Override
			public void embed(byte[] screenshot, String name) {
				this.name = name;
			}
		};
		stepdef.takeScreenshot(scenario);
		assertEquals("image/png", scenario.getName());
		temp.kill();
	}
}
