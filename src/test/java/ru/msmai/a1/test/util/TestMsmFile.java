package ru.msmai.a1.test.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.msmai.a1.util.UtilMsmCode.getAvailableCodes;
import static ru.msmai.a1.util.UtilMsmCode.getParentCode;
import static ru.msmai.a1.util.UtilMsmCode.removeParent;
import static ru.msmai.a1.util.UtilPrint.atoString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;

import ru.msmai.a1.MsmFile;
import ru.msmai.a1.exceptions.MsmException;
import ru.msmai.a1.util.UtilMsmCode;
import ru.msmai.a1.util.UtilStr;

public class TestMsmFile {

	static MsmFile msmFile;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		msmFile = new MsmFile();
	}

	@Test
	public void testAIterator01() {
		for (Iterator<String> iter = msmFile.iterate(); iter.hasNext();) {
			UtilStr.justSplit(iter.next());
		}
	}

	@Test
	public void testGetRoot01() {
		String[] item = msmFile.getRoot();
		assertEquals(true, UtilMsmCode.isRoot(item[0]));
	}

	@Test
	public void testGetDescendants01() {
		List<String[]> children = msmFile.getDescendants("AAAAAABABAAAA");
		assertEquals(222, children.size());
	}

	@Test
	public void testGetChildren01() {
		List<String[]> children = msmFile.getChildren("AAAAAABABAAAA");
		assertEquals(7, children.size());
	}

	@Test
	public void testGetElement01() {
		String[] item = msmFile.getElement("AAAAAABABAAAA");
		assertNotNull(item);
	}
	@Test
	public void testGetParent01() {
		String[] item = msmFile.getParent("AAAAAABABAAAA");
		assertNotNull(item);
	}

	@Test
	public void testSelectWords01() {
		List<String> words = msmFile.selectWords("AAAAAAABACAEA");
		assertNotNull(words);
		assertEquals(1, words.size());
		assertArrayEquals(new Object[]{"СИНЕТЬ"}, words.toArray());
	}

	@Test
	public void testGetNextAvailable01() {
		assertEquals("L", msmFile.getNextAvailable("AAAAAABABAAAABE"));
	}

	@Test
	public void testGetNextAvailable02() {
		assertEquals("A", msmFile.getNextAvailable("AAAAAABABAAAABF"));
	}

	@Test
	public void testGetUsed01() {
		assertEquals("ABCDEFGHIJK", msmFile.getUsedCodes("AAAAAABABAAAABE"));
	}

	@Test
	public void testGetUsed02() {
		assertEquals("ABCDEFGHIJK", msmFile.getUsedCodes("AAAAAABABAAAABE"));
	}

	@Test
	public void testGetAvailableCodes01() {
		assertEquals("LMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", getAvailableCodes(msmFile.getUsedCodes("AAAAAABABAAAABE")));
	}

	@Test
	public void testSelectAll01() throws MsmException {
		List<String> duplications = new ArrayList<String>();
		Map<String,String> msmbycode = msmFile.selectAll(duplications);
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
		
		String[] vkus = msmFile.getElement(vkusCode);
		assertEquals("AAAAAABABAAAABE", vkus[0]);
		
		List<String[]> vkusDescendants = msmFile.getDescendants(vkusCode);
		assertEquals(38, vkusDescendants.size());
		
//		UtilPrint.print(vkus, "потомки", vkusDescendants);
		assertEquals("ABCDEFGHIJAAABACADKKAKBKCKDKEKFKGKHKIAEAFAGAHAIAJAKALAMAEAAEAAAEABAEACAEAD", 
				vkusDescendants.stream().map(a->removeParent(a[0], vkusCode)).collect(Collectors.joining()));
		
		List<String[]> vkusNeighbors = msmFile.getNeighbors(vkusCode);
		assertEquals(12, vkusNeighbors.size());

//		UtilPrint.print(vkus, "соседи", vkusNeighbors);
		assertEquals("ABCDEFGHIJKL", 
				vkusNeighbors.stream().map(a->removeParent(a[0], getParentCode(vkusCode))).collect(Collectors.joining()));
		
		String vkusovogoCode = "AAAAAABABAAAABF";
		
		String[] vkusovogo = msmFile.getElement(vkusovogoCode);
		assertEquals("AAAAAABABAAAABF", vkusovogo[0]);
		
		List<String[]> vkusovogoDescendants = msmFile.getDescendants(vkusovogoCode);
		assertEquals(0, vkusovogoDescendants.size());
		
		List<String[]> vkusovogoNeighbors = msmFile.getNeighbors(vkusovogoCode);
		assertEquals(12, vkusovogoNeighbors.size());
		
//		UtilPrint.print(vkusovog, "соседи", vkusovogoNeighbors);
		assertEquals("ABCDEFGHIJKL", 
				vkusovogoNeighbors.stream().map(a->removeParent(a[0], getParentCode(vkusovogoCode))).collect(Collectors.joining()));
		
		vkusovogoDescendants = msmFile.reindex(vkusovogoCode, vkusCode, vkusDescendants);
		assertEquals(38, vkusovogoDescendants.size());

//		UtilPrint.print(vkusovogo, "потомки", vkusovogoDescendants);
		assertEquals("AAAAAAAAAAAAABACADAAAABACADAEAFAGAHAIAEAFAGAHAIAJAKALAMAEAAEAAAEABAEACAEAD", 
				vkusovogoDescendants.stream().map(a->removeParent(a[0], vkusovogoCode)).collect(Collectors.joining()));
		
		assertEquals(vkusDescendants.stream().map(a->atoString(a)).collect(Collectors.joining()),
				vkusovogoDescendants.stream().map(a->atoString(a)).collect(Collectors.joining()));
	}
	
	@Test
	public void testCopyPasteDescendants01() {
		String vkusCode = "AAAAAABABAAAABE";
		String vkusovogoCode = "AAAAAABABAAAABF";
		msmFile.copyPasteDescendants(vkusovogoCode, vkusCode, "ALIK2-3.tmp");
	}
	
	@Test
	public void testCutPasteDescendants01() {
		String vkusCode = "AAAAAABABAAAABE";
		String vkusovogoCode = "AAAAAABABAAAABF";
		msmFile.cutPasteDescendants(vkusovogoCode, vkusCode, "ALIK2-4.tmp");
	}
}
