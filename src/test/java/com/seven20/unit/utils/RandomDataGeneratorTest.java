package com.seven20.unit.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.seven20.picklejar.utils.RandomDataGenerator;

public class RandomDataGeneratorTest {

	RandomDataGenerator rdg = RandomDataGenerator.instance();

	@Test
	public void shouldReturnRandomFirstName() {

		List<String> firstRun = new ArrayList<>();
		List<String> secondRun = new ArrayList<>();
		for (int x = 0; x < 50; x++) {
			firstRun.add(rdg.nextName());
		}
		for (int x = 0; x < 50; x++) {
			secondRun.add(rdg.nextName());
		}
		assertFalse(firstRun.equals(secondRun));

		// uncomment to demonstrate how lists are compared.
		// List<String> copy = new ArrayList<>(firstRun);
		// assertEquals(firstRun, copy); }
	}

	@Test
	public void shouldReturnRandomFullName() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 50; x++) {
			sb.append(rdg.nextName()).append(" ").append(rdg.nextName());
			assertEquals(true, sb.length() > 0);
			assertEquals(true, sb.indexOf(" ") > 0);
		}
	}

	@Test
	public void shouldReturnRandomPhoneNumber() {
		for (int i = 0; i < 50; i++) {
			String phoneNumber = rdg.nextPhoneNumber();
			assertEquals(13, phoneNumber.length());
			assertTrue(phoneNumber.contains("("));
			assertEquals(8, phoneNumber.indexOf("-"));
		}
	}

	@Test
	public void shouldReturnRandomNumber() {
		int lastNumber = Integer.MAX_VALUE;
		for (int i = 0; i < 50; i++) {
			lastNumber = rdg.nextNumber();
			assertTrue(rdg.nextNumber() != lastNumber);
		}
	}

}
