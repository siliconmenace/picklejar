package com.seven20.seven20.configuration;

import static org.junit.Assert.assertEquals;

import org.apache.commons.configuration2.ex.ConversionException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigTest {

	private Config config = ConfigurationLoader.getConfiguration();

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void canFetchKeyThatDoesntExist() {
		assertEquals(null, config.getString("doesntexist"));
	}

	@Test
	public void canFetchEmptyValue() {
		assertEquals("", config.getString("empty"));
	}

	@Test
	public void cantParseIncorrectGetter() {
		exception.expect(ConversionException.class);
		exception.expectMessage(
				"Key 'isString' cannot be converted to class java.lang.Integer. Value is: 'true'.");
		assertEquals("", config.getInt("isString"));
	}

	@Test
	public void canFetchValueFromAliasFile() {
		assertEquals("test1", config.getString("test1"));
	}

	@Test
	public void multipleKeysCanHaveTheSameValue() {
		String value1 = config.getString("key1");
		String value2 = config.getString("key2");
		assertEquals(value1, value2);
	}

	@Test
	public void canFetchTilde() {
		assertEquals("value~", config.getString("tilde"));
	}

	@Test
	public void canFetchExclamation() {
		assertEquals("value!", config.getString("exclamation"));
	}

	@Test
	public void canFetchAt() {
		assertEquals("value@", config.getString("at"));
	}

	@Test
	public void canFetchPound() {
		assertEquals("value#", config.getString("pound"));
	}

	@Test
	public void canFetchDollar() {
		assertEquals("value$", config.getString("dollar"));
	}

	@Test
	public void canFetchPercent() {
		assertEquals("value%", config.getString("percent"));
	}

	@Test
	public void canFetchCarrot() {
		assertEquals("value^", config.getString("carrot"));
	}

	@Test
	public void canFetchAmpersand() {
		assertEquals("value&", config.getString("ampersand"));
	}

	@Test
	public void canFetchStar() {
		assertEquals("value*", config.getString("star"));
	}

	@Test
	public void canFetchLeftPar() {
		assertEquals("value(", config.getString("leftpar"));
	}

	@Test
	public void canFetchRightPar() {
		assertEquals("value)", config.getString("rightpar"));
	}

	@Test
	public void canFetchHyphen() {
		assertEquals("value-", config.getString("hyphen"));
	}

	@Test
	public void canFetchUnderscore() {
		assertEquals("value_", config.getString("underscore"));
	}

	@Test
	public void canFetchPlus() {
		assertEquals("value+", config.getString("plus"));
	}

	@Test
	public void canFetchEquals() {
		assertEquals("value=", config.getString("equals"));
	}

	@Test
	public void canFetchEqualsPair() {
		assertEquals("key=value", config.getString("equalsPair"));
	}

	@Test
	public void canFetchOneSingleQuote() {
		assertEquals("'value", config.getString("oneSingleQuote"));
	}

	@Test
	public void canFetchTwoSingleQuote() {
		assertEquals("'value'", config.getString("twoSingleQuote"));
	}

	@Test
	public void canFetchOneDoubleQuote() {
		assertEquals("\"value", config.getString("oneDoubleQuote"));
	}

	@Test
	public void canFetchTwoDoubleQuote() {
		assertEquals("\"value\"", config.getString("twoDoubleQuote"));
	}

	@Test
	public void canFetchOneForwardSlash() {
		assertEquals("/value", config.getString("oneForwardSlash"));
	}

	@Test
	public void canFetchTwoForwardSlash() {
		assertEquals("//value", config.getString("twoForwardSlash"));
	}

	@Test
	public void canFetchOneBackSlash() {
		assertEquals("\\value", config.getString("oneBackSlash"));
	}

	@Test
	public void cantFetchTwoBackSlash() {
		// Interprets the first slash as escaping the second
		assertEquals("\\value", config.getString("twoBackSlash"));
	}

	@Test
	public void canFetchUsingCapKey() {
		assertEquals("capskey", config.getString("CAPS"));
	}

	@Test
	public void canFetchCapValue() {
		assertEquals("CAPSVALUE", config.getString("caps"));
	}

	@Test
	public void canFetchInteger() {
		assertEquals(1, config.getInt("int"));
	}

	@Test
	public void canFetchBoolean() {
		assertEquals(true, config.getBoolean("booltrue"));
	}

	@Test
	public void canFetchBooleanUpper() {
		assertEquals(true, config.getBoolean("boolTrue"));
	}

	@Test
	public void canFetchBooleanCaps() {
		assertEquals(true, config.getBoolean("boolTRUE"));
	}

	@Test
	public void canFetchBooleanT() {
		assertEquals(true, config.getBoolean("boolT"));
	}

	@Test
	public void canFetchDouble() {
		assertEquals(1.0, config.getDouble("double"), 0.0);
	}

	@Test
	public void canFetchLeftBracket() {
		assertEquals("value[", config.getString("leftbracket"));
	}

	@Test
	public void canFetchRightBracket() {
		assertEquals("value]", config.getString("rightbracket"));
	}

	@Test
	public void canFetchPeriod() {
		assertEquals("value.", config.getString("period"));
	}

	@Test
	public void canFetchQuestion() {
		assertEquals("value?", config.getString("question"));
	}

	@Test
	public void canFetchLess() {
		assertEquals("value<", config.getString("less"));
	}

	@Test
	public void canFetchGreater() {
		assertEquals("value>", config.getString("greater"));
	}

	@Test
	public void canFetchComma() {
		assertEquals("va,lue", config.getString("comma"));
	}

	@Test
	public void canFetchDoubleDot() {
		assertEquals(1.0, config.getDouble("doubleDot"), 0.0);
	}

	@Test
	public void canFetchLong() {
		assertEquals(1L, config.getLong("long"), 0L);
	}

	@Test
	public void canFetchLongWithL() {
		// longL=1L
		exception.expect(ConversionException.class);
		exception.expectMessage("Key 'longL' cannot be converted to class java.lang.Long.");
		config.getLong("longL");
	}

	@Test
	public void canFetchXpath() {
		assertEquals("//*@id='userTable_row_certadmin'/td2", config.getString("xpath"));
	}

}
