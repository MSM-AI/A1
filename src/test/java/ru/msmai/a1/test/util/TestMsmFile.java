package ru.msmai.a1.test.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;

import org.junit.Test;

import ru.msmai.a1.exceptions.MsmException;
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
	public void testGetFirstByCode() {
		String[] item = UtilMsm.getFirstByCode("AAAAAABABAAAA");
		assertNotNull(item);
	}
	@Test
	public void testGetParent() {
		String[] item = UtilMsm.getParent("AAAAAABABAAAA");
		assertNotNull(item);
	}

	@Test
	public void testGetRoot() {
		String[] item = UtilMsm.getRoot();
		assertNotNull(item);
	}

	@Test
	public void testSelectAll() throws MsmException {
		List<String> duplications = new ArrayList<String>();
		Map<String,String> msmbycode = UtilMsm.selectAll(duplications);
		assertNotNull(msmbycode);
		assertEquals(65468, msmbycode.size());
		assertEquals(5, duplications.size());
		assertArrayEquals(new Object[]{"AAAAAAABACAEA", "AAAAAAABACAHA", "AAAAAAABACAGA", "AAAAAAABACABA", "AAAAAAABACAAA"}
			, duplications.toArray());
		
		//UtilMsm.reindexDuplications("ALIK2.tmp");
		for(String code: duplications){
			List<String[]> children = UtilMsm.selectChildren(code);
			System.out.println("\nДубль мсм кода "+code+" у слов "+UtilMsm.selectAllWords(code)+" родитель ["+UtilMsm.getParents(code).stream().map(
					r->((String[])r)[0]+": "+((String[])r)[1]+" родитель "+Arrays.toString(UtilMsm.getParent(((String[])r)[0])))
			.collect(Collectors.joining(", "))+"]");
			System.out.println("--------------- потомки ---------------");
			children.stream().forEach((record)->{
				System.out.printf("%s: %s\n", record[0], record[1]);
			});
		}
		
		System.out.println("\n");
		  UtilMsm.getAllByCode("AAAAAAABACA").stream().forEach(r->{
				 System.out.printf("%s: %s\n", ((String[])r)[0], ((String[])r)[1]);
			 });
			System.out.println("--------------- потомки ---------------");
		 UtilMsm.selectChildren("AAAAAAABACA").stream().forEach(r->{
			 System.out.printf("%s: %s\n", ((String[])r)[0], ((String[])r)[1]);
		 });
	}


	@Test
	public void testSelectAllWords() {
		List<String> all = UtilMsm.selectAllWords("AAAAAAABACAEA");
		assertNotNull(all);
		assertEquals(2, all.size());
		assertArrayEquals(new Object[]{"ЗЕЛЕНЫЙ", "ЗЕЛЕНОГО"}, all.toArray());
	}

}
