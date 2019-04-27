package ru.msmai.a1.test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.msmai.a1.util.UtilMsmCode.getAvailableCodes;

import org.junit.Test;

import ru.msmai.a1.util.UtilMsmCode;

public class TestMsmCode {

	@Test
	public void testTrueCompare01() {
		assertEquals("This is", UtilMsmCode.trueCompare("This is a string!", "This is not fun"));
	}

	@Test
	public void testTrueCompare02() {
		assertEquals("This is a string!", UtilMsmCode.trueCompare("This is a string!", "This is a string!"));
	}

	@Test
	public void testTrueCompare03() {
		assertEquals("", UtilMsmCode.trueCompare("This is a string!", "Is it fun?"));
	}

	@Test
	public void testTrueCompare04() {
		assertNotNull(UtilMsmCode.trueCompare("This is a string!", "Is it fun?"));
	}

	@Test
	public void testTrueCompare05() {
		assertEquals("", UtilMsmCode.trueCompare("This is a string!", ""));
	}

	@Test
	public void testTrueCompare06() {
		assertEquals("", UtilMsmCode.trueCompare("", "This is not fun"));
	}

	@Test
	public void testTrueCompare07() {
		assertEquals("", UtilMsmCode.trueCompare("", ""));
	}

	@Test
	public void testTruePercent01() {
		assertEquals(4704, UtilMsmCode.truePercent("This is a string!", "This is not fun!"));
	}
	
	@Test
	public void testTruePercent02() {
		assertEquals(0, UtilMsmCode.truePercent("This is a string!", "Is it fun?"));
	}

	@Test
	public void testTruePercent03() {
		assertEquals(10000, UtilMsmCode.truePercent("This is a string!", "This is a string!"));
	}

	@Test
	public void testTruePercent04() {
		assertEquals(0, UtilMsmCode.truePercent("", ""));
	}

	@Test
	public void testReplaceRoot01() {
		assertEquals("CBCDEFG", UtilMsmCode.replaceRoot("ABCDEFG", "C"));
	}

	@Test
	public void testRemoveParent01() {
		assertEquals("DEFG", UtilMsmCode.removeParent("ABCDEFG", "ABC"));
	}
	@Test
	public void testRemoveParent02() {
		assertEquals("ABCDEFG", UtilMsmCode.removeParent("ABCDEFG", "BCD"));
	}

	@Test
	public void testGetAvailableCodes01() {
		assertEquals("LMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", getAvailableCodes("ABCDEFGHIJK"));
	}

}
