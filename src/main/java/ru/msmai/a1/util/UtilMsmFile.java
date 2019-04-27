package ru.msmai.a1.util;

import static ru.msmai.a1.util.UtilMsmCode.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UtilMsmFile {
	
	private static final String MSMCODE_2_WORD = "ALIK2-2.txt";

	public static Iterator<String> iterate() {
		return new UtilFile(MSMCODE_2_WORD);
	}

	public static Iterator<String> iterate(Predicate<String> consumer) {
		return new UtilFile(MSMCODE_2_WORD, consumer);
	}

	public static List<String[]> getDescendants(String code) {
		List<String[]> result = new ArrayList<>();
		if (code != null && !code.isEmpty()) {
			for (Iterator<String> iter = UtilMsmFile.iterate(); iter.hasNext();) {
				String[] record = UtilStr.justSplit(iter.next());
				if (record[0].startsWith(code) && !record[0].equals(code)) {
					result.add(record);
				}
			}
		}
		return result;
	}

	public static List<String[]> getChildren(String code) {
		List<String[]> result = new ArrayList<>();
		if (code != null && !code.isEmpty()) {
			int expectedCodeLength = code.length() + 1;
			for (Iterator<String> iter = UtilMsmFile.iterate(); iter.hasNext();) {
				String[] record = UtilStr.justSplit(iter.next());
				if (record[0].length() == expectedCodeLength && record[0].startsWith(code)) {
					result.add(record);
				}
			}
		}
		return result;
	}

	public static List<String[]> getElements(String code) {
		AtomicReference<List<String[]>> result = new AtomicReference<>(new ArrayList<>());
		if (code != null && !code.isEmpty()) {
			UtilMsmFile.iterate((String line) -> {
				String[] record = UtilStr.justSplit(line);
				if (code.equals(record[0])) {
					result.get().add(record);
				}
				return true;
			});
		}
		return result.get();
	}

	public static String[] getElement(String code) {
		AtomicReference<String[]> result = new AtomicReference<>();
		if (code != null && !code.isEmpty()) {
			UtilMsmFile.iterate((String line) -> {
				String[] record = UtilStr.justSplit(line);
				if (code.equals(record[0])) {
					result.set(record);
					return false;
				} else {
					return true;
				}
			});
		}
		return result.get();
	}

	public static String[] getRoot() {
		AtomicReference<String[]> result = new AtomicReference<>();
		UtilMsmFile.iterate((String line) -> {
			String[] record = UtilStr.justSplit(line);
			if ( isRoot(record[0]) ) {
				result.set(record);
				return false;
			} else {
				return true;
			}
		});
		return result.get();
	}

	public static String[] getParent(String code) {
		return getElement(getParentCode(code));
	}

	public static List<String[]> getParents(String code) {
		return getElements(getParentCode(code));
	}

	public static List<String[]> getNeighbors(String code) {
		return getChildren(getParent(code)[0]);
	}
	
	public static String getUsedCodes(String code) {
		return getChildren(code).stream().map(a->removeParent(a[0], code)).sorted().collect(Collectors.joining());
	}
	
	public static String getNextAvailable(String code) {
		return getAvailableCodes(getUsedCodes(code)).substring(0,1);
	}
	
	public static Map<String, String> selectAll(List<String> duplications) {
		Map<String, String> result = new HashMap<>(100000);
		for (Iterator<String> iter = UtilMsmFile.iterate(); iter.hasNext();) {
			String[] record = UtilStr.justSplit(iter.next());
			if (result.containsKey(record[0])) {
				duplications.add(record[0]);
			} else {
				result.put(record[0], record[1]);
			}
		}
		return result;
	}

	public static List<String> selectWords(String code) {
		List<String> result = new ArrayList<>();
		for (Iterator<String> iter = UtilMsmFile.iterate(); iter.hasNext();) {
			String[] record = UtilStr.justSplit(iter.next());
			if ( code.equals(record[0])) {
				result.add(record[1]);
			}
		}
		return result;
	}

	public static List<String[]> reindex(String newParentCode, List<String[]> descendants, String oldParentCode) {
		String nextAvailableCode = getNextAvailable(newParentCode);
		for(String[] descendant: descendants){
			String oldChildCode = removeParent(descendant[0], oldParentCode);
			String newChildCode = replaceRoot(oldChildCode, nextAvailableCode);
			descendant[0] = newParentCode + newChildCode;
		}
		return descendants;
	}
	
}
