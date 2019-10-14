package com.seven20.unit.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.seven20.picklejar.utils.DataStore;

public class DataStoreTest {

	DataStore ds = DataStore.instance();

	@Before
	public void init() {
		ds.store("key", "value");
		ds.store("Boolean", Boolean.TRUE);
		ds.store("Integer", 123);
		ds.store("Double", 123d);
		ds.store("Long", 123l);
	}

	@Test
	public void shouldGetString() {

		assertEquals("value", ds.getAsString("key"));
	}

	@Test
	public void shouldGetBool() {
		assertEquals(Boolean.TRUE, ds.getAsBoolean("Boolean"));
	}

	@Test
	public void shouldGetInteger() {
		assertEquals(123, ds.getAsInteger("Integer"), 0);
	}

	@Test
	public void shouldGetDouble() {
		assertEquals(123d, ds.getAsDouble("Double"), 0d);
	}

	@Test
	public void shouldGetLong() {
		assertEquals(123l, ds.getAsLong("Long"), 0l);
	}

	@Test
	public void shouldClearDataStore() {
		ds.clearStoredData();
		try {
			assertNull(ds.getAsBoolean("Boolean"));
		} catch (Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
		}
	}
}
