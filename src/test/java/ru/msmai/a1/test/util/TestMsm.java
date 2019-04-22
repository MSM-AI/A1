package ru.msmai.a1.test.util;

import static org.junit.Assert.*;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import ru.msmai.a1.util.UtilMsm;
import ru.msmai.a1.util.UtilStr;

public class TestMsm {
	@Test
	public void testAIterator() {
		for (Iterator<String> iter = UtilMsm.iterate(); iter.hasNext();) {
			UtilStr.justSplit(iter.next());
		}
	}

	@Test
	public void testAllDescendants() {
		List<String[]> children = UtilMsm.selectDescendants("AAAAAABABAAAA");
		assertEquals(223, children.size());
	}

	@Test
	public void testAllChildren() {
		List<String[]> children = UtilMsm.selectChildren("AAAAAABABAAAA");
		assertEquals(7, children.size());
	}

	@Test
	public void testGetByCode() {
		String[] item = UtilMsm.getByCode("AAAAAABABAAAA");
		assertNotNull(item);
	}

	@Test
	public void testGetParent() {
		String[] item = UtilMsm.getParentCode("AAAAAABABAAAA");
		assertNotNull(item);
	}

	@Test
	public void testGetRoot() {
		String[] item = UtilMsm.getRoot();
		assertNotNull(item);
	}
}
