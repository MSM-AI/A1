package ru.msmai.a1.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class UtilMsm {
	private static final String WORD_2_MSMCODE = "ALIK.txt";
	private static final String MSMCODE_2_WORD = "ALIK2.txt";

	public static Iterator<String> iterate() {
		return new UtilFile(MSMCODE_2_WORD);
	}

	public static Iterator<String> iterate(Predicate<String> consumer) {
		return new UtilFile(MSMCODE_2_WORD, consumer);
	}

	public static List<String[]> selectDescendants(String parentCode) {
		List<String[]> result = new ArrayList<>();
		if (parentCode != null && !parentCode.isEmpty()) {
			for (Iterator<String> iter = UtilMsm.iterate(); iter.hasNext();) {
				String[] record = UtilStr.justSplit(iter.next());
				if (record[0].startsWith(parentCode)) {
					result.add(record);
				}
			}
		}
		return result;
	}

	public static List<String[]> selectChildren(String parentCode) {
		List<String[]> result = new ArrayList<>();
		if (parentCode != null && !parentCode.isEmpty()) {
			int expectedCodeLength = parentCode.length()+1;
			for (Iterator<String> iter = UtilMsm.iterate(); iter.hasNext();) {
				String[] record = UtilStr.justSplit(iter.next());
				if (record[0].length() == expectedCodeLength && record[0].startsWith(parentCode)) {
					result.add(record);
				}
			}
		}
		return result;
	}

	public static String[] getByCode(String code) {
		AtomicReference<String[]> result = new AtomicReference<>();
		if (code != null && !code.isEmpty()) {
			UtilMsm.iterate((String line) -> {
				String[] record = UtilStr.justSplit(line);
				if (code.equals(record[0])) {
					result.set(record);
					return false;
				}
				return true;
			});
		}
		return result.get();
	}

	public static String[] getRoot() {
		AtomicReference<String[]> result = new AtomicReference<>();
		UtilMsm.iterate((String line) -> {
			result.set(UtilStr.justSplit(line));
			return false;
		});
		return result.get();
	}

	public static String[] getParentCode(String code) {
		return getByCode(UtilStr.getParentMsmCode(code));
	}
}
