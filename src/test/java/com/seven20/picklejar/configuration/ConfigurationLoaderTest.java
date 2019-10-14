package com.seven20.picklejar.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigurationLoaderTest {

	private Config config = ConfigurationLoader.getConfiguration();

	@Test
	public void shouldLoadConfiguration() {
		assertEquals("loaded", config.getString("onlyInConfiguration"));
	}

	@Test
	public void shouldLoadProperties() {
		assertEquals("loaded", config.getString("onlyInProperties"));
	}

	@Test
	public void shouldLoadAlias() {
		assertEquals("loaded", config.getString("onlyInAlias"));
	}

	@Test
	public void resolvesConflictsAcrossFiles() {
		assertEquals("a", config.getString("conflict"));
	}

	@Test
	public void loadsInEnumOrderConfigAliasProperties() {
		// Loads in order: config, alias, properties
		assertEquals("config1", config.getString("first"));
		assertEquals("alias2", config.getString("second"));
		assertEquals("properties3", config.getString("third"));
	}

	@Test
	public void resolvesConflictsWithinFiles() {
		// Loads top down
		assertEquals("y", config.getString("internalConflict"));
	}
}
