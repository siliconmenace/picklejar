package com.seven20.unit.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.seven20.picklejar.utils.OperatingSystem;

public class OperatingSystemTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	private static final String ORIGINAL = System.getProperty("os.name");

	@Test
	public void canGetAndSetOs() {
		String expected = "Windows 10";
		OperatingSystem.setOS(expected);
		assertEquals(expected, OperatingSystem.getOS());
	}

	@Test
	public void cantSetOsToNull() {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage("Operating System cannot be null!");
		OperatingSystem.setOS(null);
	}

	@Test
	public void shouldReturnTrueIfIsWindows() {
		OperatingSystem.setOS("win");
		assertEquals(true, OperatingSystem.isWindows());
	}

	@Test
	public void shouldReturnTrueIfItStartsWithWindows() {
		OperatingSystem.setOS("wins");
		assertEquals(true, OperatingSystem.isWindows());
	}

	@Test
	public void shouldReturnTrueIfIsWindows95() {
		OperatingSystem.setOS("windows95");
		assertEquals(true, OperatingSystem.isWindows());
	}

	@Test
	public void shouldIgnoreCase() {
		OperatingSystem.setOS("Win");
		assertEquals(true, OperatingSystem.isWindows());
	}

	@Test
	public void shouldReturnTrueIfIsMac() {
		OperatingSystem.setOS("mac");
		assertEquals(true, OperatingSystem.isMac());
	}

	@Test
	public void shouldReturnTrueIfIsUnixFlavorNix() {
		OperatingSystem.setOS("nix");
		assertEquals(true, OperatingSystem.isUnix());
	}

	@Test
	public void shouldReturnTrueIfIsUnixFlavorNux() {
		OperatingSystem.setOS("nux");
		assertEquals(true, OperatingSystem.isUnix());
	}

	@Test
	public void shouldReturnTrueIfIsUnixFlavorAix() {
		OperatingSystem.setOS("aix");
		assertEquals(true, OperatingSystem.isUnix());
	}

	@Test
	public void shouldReturnTrueIfIsSolaris() {
		OperatingSystem.setOS("sunos");
		assertEquals(true, OperatingSystem.isSolaris());
	}

	@Test
	public void shouldReturnTrueIfSupported() {
		List<String> supported = Arrays.asList("win", "mac", "nix", "nux", "aix", "sunos");
		for (String os : supported) {
			OperatingSystem.setOS(os);
			assertTrue(os + " should be supported!", OperatingSystem.isSupported());
		}
	}

	@Test
	public void shouldReturnFalseIfNotSupported() {
		List<String> supported = Arrays.asList("bin", "doesntExist", "");
		for (String os : supported) {
			OperatingSystem.setOS(os);
			assertFalse(os + " should not be supported!", OperatingSystem.isSupported());
		}
	}

	@Test
	public void shouldOnlyReadPropertyOnce() {
		OperatingSystem.setOS("windows");
		assertTrue(OperatingSystem.isSupported());
		System.setProperty("os.name", "thisIsNotSupported");
		assertTrue(OperatingSystem.isSupported());
	}

	@After
	public void teardown() {
		System.setProperty("os.name", ORIGINAL);
		OperatingSystem.setOS(ORIGINAL);
	}
}
