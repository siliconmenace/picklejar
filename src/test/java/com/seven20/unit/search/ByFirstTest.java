package com.seven20.unit.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import com.seven20.picklejar.search.ByFirst;

@RunWith(MockitoJUnitRunner.class)
public class ByFirstTest {

	public @Mock By mockedByOne;
	public @Mock By mockedByTwo;
	public SearchContext context;
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void findElementsShouldFindNoElementsWithOneBy() {
		ByFirst first = new ByFirst(mockedByOne);
		List<WebElement> one = elements(0);
		List<WebElement> two = null;
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(0, matches.size());
	}

	@Test
	public void findElementsShouldFindOneElementsWithOneBy() {
		ByFirst first = new ByFirst(mockedByOne);
		List<WebElement> one = elements(1);
		List<WebElement> two = null;
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(1, matches.size());
	}

	@Test
	public void findElementsShouldFindTwoElementsWithOneBy() {
		ByFirst first = new ByFirst(mockedByOne);
		List<WebElement> one = elements(2);
		List<WebElement> two = null;
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(2, matches.size());
	}

	@Test
	public void findElementsShouldFindNoElementsWithTwoBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(0);
		List<WebElement> two = elements(0);
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(0, matches.size());
	}

	@Test
	public void findElementsShouldFindOneElementsWithTwoBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(1);
		List<WebElement> two = elements(0);
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(1, matches.size());
	}

	@Test
	public void findElementsShouldFindOneElementsOnSecondBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(0);
		List<WebElement> two = elements(1);
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(1, matches.size());
	}

	@Test
	public void findElementsShouldFindTwoElementsWithTwoBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(2);
		List<WebElement> two = elements(0);
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(2, matches.size());
	}

	@Test
	public void findElementsShouldFindTwoElementsOnSecondBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(0);
		List<WebElement> two = elements(2);
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(2, matches.size());
	}

	@Test
	public void findElementsShouldNotReturnMatchesAcrossAllBys() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(1);
		List<WebElement> two = elements(1);
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(1, matches.size());
	}

	@Test
	public void findElementsShouldReturnMatchesOnlyInTheFirstMatchingBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(1);
		List<WebElement> two = elements(2);
		mock(one, two);
		List<WebElement> matches = first.findElements(context);
		assertEquals(1, matches.size());
	}

	@Test
	public void findElementShouldFindNoElementsWithOneBy() {
		ByFirst first = new ByFirst(mockedByOne);
		List<WebElement> one = elements(0);
		List<WebElement> two = null;
		mock(one, two);
		exception.expect(NoSuchElementException.class);
		exception.expectMessage("No element found");
		first.findElement(context);
	}

	@Test
	public void findElementShouldFindOneElementsWithOneBy() {
		ByFirst first = new ByFirst(mockedByOne);
		List<WebElement> one = elements(1);
		List<WebElement> two = null;
		mock(one, two);
		assertNotNull(first.findElement(context));
	}

	@Test
	public void findElementShouldReturnFirstElementWithOneBy() {
		ByFirst first = new ByFirst(mockedByOne);
		List<WebElement> one = elements(2);
		List<WebElement> two = null;
		mock(one, two);
		assertNotNull(first.findElement(context));
	}

	@Test
	public void findElementShouldFindNoElementsWithTwoBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(0);
		List<WebElement> two = elements(0);
		mock(one, two);
		exception.expect(NoSuchElementException.class);
		exception.expectMessage("No element found");
		first.findElement(context);
	}

	@Test
	public void findElementShouldFindOneElementsWithTwoBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(1);
		List<WebElement> two = elements(0);
		mock(one, two);
		assertNotNull(first.findElement(context));
	}

	@Test
	public void findElementShouldFindOneElementsWithSecondBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(0);
		List<WebElement> two = elements(1);
		mock(one, two);
		assertNotNull(first.findElement(context));
	}

	@Test
	public void findElementShouldReturnFirstElementWithTwoBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(2);
		List<WebElement> two = elements(0);
		mock(one, two);
		assertNotNull(first.findElement(context));
	}

	@Test
	public void findElementShouldReturnFirstElementWithSecondBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(0);
		List<WebElement> two = elements(2);
		mock(one, two);
		assertNotNull(first.findElement(context));
	}

	@Test
	public void findElementShouldReturnOnlyFirstElementWithTwoBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		List<WebElement> one = elements(1);
		List<WebElement> two = elements(1);
		mock(one, two);
		assertNotNull(first.findElement(context));
	}

	@Test
	public void shouldReturnStringSingleBy() {
		ByFirst first = new ByFirst(mockedByOne);
		when(mockedByOne.toString()).thenReturn("ByOne");
		assertEquals("By.First[ByOne]", first.toString());
	}

	@Test
	public void shouldReturnStringForEachBy() {
		ByFirst first = new ByFirst(mockedByOne, mockedByTwo);
		when(mockedByOne.toString()).thenReturn("ByOne");
		when(mockedByTwo.toString()).thenReturn("ByTwo");
		assertEquals("By.First[ByOne,ByTwo]", first.toString());
	}

	private WebElement ele() {
		return new RemoteWebElement();
	}

	private List<WebElement> elements(int number) {
		List<WebElement> elements = new ArrayList<>();
		for (int i = 0; i < number; i++) {
			elements.add(ele());
		}
		return elements;
	}

	private void mock(List<WebElement> elementsFoundByOne, List<WebElement> elementsFoundByTwo) {
		mockMultiple(elementsFoundByOne, elementsFoundByTwo);
		mockSingles(elementsFoundByOne, elementsFoundByTwo);

	}

	private void mockMultiple(List<WebElement> elementsFoundByOne,
			List<WebElement> elementsFoundByTwo) {
		when(mockedByOne.findElements(context)).thenReturn(elementsFoundByOne);
		when(mockedByTwo.findElements(context)).thenReturn(elementsFoundByTwo);
	}

	private void mockSingles(List<WebElement> elementsFoundByOne,
			List<WebElement> elementsFoundByTwo) {
		if (elementsFoundByOne != null) {
			mockSingle(mockedByOne, elementsFoundByOne.size());
		}
		if (elementsFoundByTwo != null) {
			mockSingle(mockedByTwo, elementsFoundByTwo.size());
		}
	}

	private void mockSingle(By mocked, int elementsFound) {
		if (elementsFound == 0) {
			when(mocked.findElement(context))
					.thenThrow(new NoSuchElementException("Should never see this message"));
		} else {
			when(mocked.findElement(context)).thenReturn(ele());
		}
	}
}
