package ru.msmai.a1.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import ru.msmai.a1.util.UtilStr;

public class TestStr {

	@Test
	public void test01getUps() {
		assertEquals("A B C D E F G", UtilStr.getUps("a b C d e f g"));
	}

	@Test
	public void test07() {
		assertNull(UtilStr.getUps(null));
	}

	@Test
	public void test08() {
		assertNull(UtilStr.getUps(""));
	}

	@Test
	public void test02delDbSpace() {
		assertEquals("a b C d e f g", UtilStr.delDbSpace("a   b       C  d    e  f     g"));
	}

	@Test
	public void test09() {
		assertNull(null, UtilStr.delDbSpace(""));
	}

	@Test
	public void test10() {
		assertNull(null, UtilStr.delDbSpace(null));
	}

	@Test
	public void test03justBreak() {
		assertArrayEquals(new String[]{"a","b","C","d","e","f","g"},
				UtilStr.justBreak("a;   b:       C,  d!    e  f     g"));
	}

	@Test
	public void test11() {
		assertNull(UtilStr.justBreak(""));
	}

	@Test
	public void test12() {
		assertNull(UtilStr.justBreak(null));
	}

	@Test
	public void test04insInString() {
		assertEquals("This is a sprint!", UtilStr.insInString("This is a string!", "string", "sprint"));
	}

	@Test
	public void test13() {
		assertEquals("This is a string!", UtilStr.insInString("This is a string!", "", ""));
	}

	@Test
	public void test14() {
		assertEquals("This is a string!", UtilStr.insInString("This is a string!", null, ""));
	}

	@Test
	public void test15() {
		assertEquals("This is a string!", UtilStr.insInString("This is a string!", "", null));
	}

	@Test
	public void test16() {
		assertNull(UtilStr.insInString("", "string", "sprint"));
	}

	@Test
	public void test18() {
		assertNull(UtilStr.insInString(null, "string", "sprint"));
	}

	@Test
	public void test19() {
		assertNull(UtilStr.insInString(null, null, null));
	}

	@Test
	public void test26() {
		assertNull(UtilStr.insInString("", "", ""));
	}

	@Test
	public void test05trueCompare() {
		assertEquals("This is", UtilStr.trueCompare("This is a string!", "This is not fun"));
	}

	@Test
	public void test31() {
		assertEquals("This is a string!", UtilStr.trueCompare("This is a string!", "This is a string!"));
	}

	@Test
	public void test32() {
		assertEquals("", UtilStr.trueCompare("This is a string!", "Is it fun?"));
	}

	@Test
	public void test33() {
		assertNotNull(UtilStr.trueCompare("This is a string!", "Is it fun?"));
	}

	@Test
	public void test20() {
		assertNull(UtilStr.trueCompare("This is a string!", ""));
	}

	@Test
	public void test21() {
		assertNull(UtilStr.trueCompare("This is a string!", null));
	}

	@Test
	public void test22() {
		assertNull(UtilStr.trueCompare("", "This is not fun"));
	}

	@Test
	public void test23() {
		assertNull(UtilStr.trueCompare(null, "This is not fun"));
	}

	@Test
	public void test24() {
		assertNull(UtilStr.trueCompare(null, null));
	}

	@Test
	public void test25() {
		assertNull(UtilStr.trueCompare("", ""));
	}

	@Test
	public void test06truePercent() {
		assertEquals(4704, UtilStr.truePercent("This is a string!", "This is not fun!"));
	}
	
	@Test
	public void test27() {
		assertEquals(0, UtilStr.truePercent("This is a string!", "Is it fun?"));
	}

	@Test
	public void test28() {
		assertEquals(10000, UtilStr.truePercent("This is a string!", "This is a string!"));
	}

	@Test
	public void test29() {
		assertEquals(0, UtilStr.truePercent("", ""));
	}

	@Test
	public void test30() {
		assertEquals(0, UtilStr.truePercent(null, null));
	}
}
