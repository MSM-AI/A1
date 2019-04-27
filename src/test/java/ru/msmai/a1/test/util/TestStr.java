package ru.msmai.a1.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ru.msmai.a1.util.UtilStr;

public class TestStr {

	@Test
	public void testGetUps01() {
		assertEquals("A B C D E F G", UtilStr.getUps("a b C d e f g"));
	}

	@Test
	public void testDelDbSpace01() {
		assertEquals("a b C d e f g", UtilStr.delDbSpace("a   b       C  d    e  f     g"));
	}

	@Test
	public void testJustBreak01() {
		assertArrayEquals(new String[]{"a","b","C","d","e","f","g"},
				UtilStr.justBreak("a;   b:       C,  d!    e  f     g"));
	}

	@Test
	public void testJustBreak02() {
		assertArrayEquals(new String[]{""}, UtilStr.justBreak(""));
	}

	@Test
	public void testInsInString01() {
		assertEquals("This is a sprint!", UtilStr.insInString("This is a string!", "string", "sprint"));
	}

	@Test
	public void testInsInString0() {
		assertEquals("This is a string!", UtilStr.insInString("This is a string!", "", ""));
	}

	@Test
	public void testInsInString03() {
		assertEquals("", UtilStr.insInString("", "string", "sprint"));
	}
}
