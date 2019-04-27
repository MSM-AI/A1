package ru.msmai.a1.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.msmai.a1.util.UtilMsmCode.getAvailableCodes;
import static ru.msmai.a1.util.UtilMsmCode.getParentCode;
import static ru.msmai.a1.util.UtilMsmCode.removeParent;
import static ru.msmai.a1.util.UtilMsmFile.getChildren;
import static ru.msmai.a1.util.UtilMsmFile.getDescendants;
import static ru.msmai.a1.util.UtilMsmFile.getElement;
import static ru.msmai.a1.util.UtilMsmFile.getNeighbors;
import static ru.msmai.a1.util.UtilMsmFile.getNextAvailable;
import static ru.msmai.a1.util.UtilMsmFile.getParent;
import static ru.msmai.a1.util.UtilMsmFile.getRoot;
import static ru.msmai.a1.util.UtilMsmFile.getUsedCodes;
import static ru.msmai.a1.util.UtilMsmFile.iterate;
import static ru.msmai.a1.util.UtilMsmFile.reindex;
import static ru.msmai.a1.util.UtilMsmFile.selectAll;
import static ru.msmai.a1.util.UtilMsmFile.selectWords;
import static ru.msmai.a1.util.UtilPrint.atoString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import ru.msmai.a1.exceptions.MsmException;
import ru.msmai.a1.util.UtilMsmCode;
import ru.msmai.a1.util.UtilStr;

public class TestMsmFile {

	@Test
	public void testAIterator01() {
		for (Iterator<String> iter = iterate(); iter.hasNext();) {
			UtilStr.justSplit(iter.next());
		}
	}

	@Test
	public void testGetRoot01() {
		String[] item = getRoot();
		assertEquals(true, UtilMsmCode.isRoot(item[0]));
	}

	@Test
	public void testGetDescendants01() {
		List<String[]> children = getDescendants("AAAAAABABAAAA");
		assertEquals(222, children.size());
	}

	@Test
	public void testGetChildren01() {
		List<String[]> children = getChildren("AAAAAABABAAAA");
		assertEquals(7, children.size());
	}

	@Test
	public void testGetElement01() {
		String[] item = getElement("AAAAAABABAAAA");
		assertNotNull(item);
	}
	@Test
	public void testGetParent01() {
		String[] item = getParent("AAAAAABABAAAA");
		assertNotNull(item);
	}

	@Test
	public void testSelectWords01() {
		List<String> words = selectWords("AAAAAAABACAEA");
		assertNotNull(words);
		assertEquals(1, words.size());
		assertArrayEquals(new Object[]{"СИНЕТЬ"}, words.toArray());
	}

	@Test
	public void testGetNextAvailable01() {
		assertEquals("L", getNextAvailable("AAAAAABABAAAABE"));
	}

	@Test
	public void testGetNextAvailable02() {
		assertEquals("A", getNextAvailable("AAAAAABABAAAABF"));
	}

	@Test
	public void testGetUsed01() {
		assertEquals("ABCDEFGHIJK", getUsedCodes("AAAAAABABAAAABE"));
	}

	@Test
	public void testGetUsed02() {
		assertEquals("ABCDEFGHIJK", getUsedCodes("AAAAAABABAAAABE"));
	}

	@Test
	public void testGetAvailableCodes01() {
		assertEquals("LMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", getAvailableCodes(getUsedCodes("AAAAAABABAAAABE")));
	}

	@Test
	public void testSelectAll01() throws MsmException {
		List<String> duplications = new ArrayList<String>();
		Map<String,String> msmbycode = selectAll(duplications);
		assertNotNull(msmbycode);
		assertEquals(68802, msmbycode.size());
		assertEquals(2, duplications.size());
		assertArrayEquals(new Object[]{"ABABACCABA", "AABBAFBC"}, 
				duplications.toArray());
		
//		printDuplications(duplications);
		assertEquals("ABABACCABAAABBAFBC", 
				duplications.stream().collect(Collectors.joining()));

//		print("AAAAAAABACA");
	}

	@Test
	public void testReindex01() {
		String vkusCode = "AAAAAABABAAAABE";
		
		String[] vkus = getElement(vkusCode);
		assertEquals("AAAAAABABAAAABE", vkus[0]);
		
		List<String[]> vkusDescendants = getDescendants(vkusCode);
		assertEquals(38, vkusDescendants.size());
		
//		print(vkus, "потомки", vkusDescendants);
		assertEquals("ABCDEFGHIJAAABACADKKAKBKCKDKEKFKGKHKIAEAFAGAHAIAJAKALAMAEAAEAAAEABAEACAEAD", 
				vkusDescendants.stream().map(a->removeParent(a[0], vkusCode)).collect(Collectors.joining()));
		
		List<String[]> vkusNeighbors = getNeighbors(vkusCode);
		assertEquals(12, vkusNeighbors.size());

//		print(vkus, "соседи", vkusNeighbors);
		assertEquals("ABCDEFGHIJKL", 
				vkusNeighbors.stream().map(a->removeParent(a[0], getParentCode(vkusCode))).collect(Collectors.joining()));
		
		String vkusovogoCode = "AAAAAABABAAAABF";
		
		String[] vkusovogo = getElement(vkusovogoCode);
		assertEquals("AAAAAABABAAAABF", vkusovogo[0]);
		
		List<String[]> vkusovogoDescendants = getDescendants(vkusovogoCode);
		assertEquals(0, vkusovogoDescendants.size());
		
		List<String[]> vkusovogoNeighbors = getNeighbors(vkusovogoCode);
		assertEquals(12, vkusovogoNeighbors.size());
		
//		print(vkusovogoParent, "соседи", vkusovogoNeighbors);
		assertEquals("ABCDEFGHIJKL", 
				vkusovogoNeighbors.stream().map(a->removeParent(a[0], getParentCode(vkusovogoCode))).collect(Collectors.joining()));
		
		vkusovogoDescendants = reindex(vkusovogoCode, vkusDescendants, vkusCode);
		assertEquals(38, vkusovogoDescendants.size());

//		print(vkusovogoParent, "потомки", vkusovogoDescendants);
		assertEquals("AAAAAAAAAAAAABACADAAAABACADAEAFAGAHAIAEAFAGAHAIAJAKALAMAEAAEAAAEABAEACAEAD", 
				vkusovogoDescendants.stream().map(a->removeParent(a[0], vkusovogoCode)).collect(Collectors.joining()));
		
		assertEquals(vkusDescendants.stream().map(a->atoString(a)).collect(Collectors.joining()),
				vkusovogoDescendants.stream().map(a->atoString(a)).collect(Collectors.joining()));
	}
}
