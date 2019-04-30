package ru.msmai.a1;

import static ru.msmai.a1.util.UtilMsmCode.getAvailableCodes;
import static ru.msmai.a1.util.UtilMsmCode.getParentCode;
import static ru.msmai.a1.util.UtilMsmCode.isRoot;
import static ru.msmai.a1.util.UtilMsmCode.removeParent;
import static ru.msmai.a1.util.UtilMsmCode.replaceRoot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ru.msmai.a1.util.UtilFile;
import ru.msmai.a1.util.UtilMsmCode;
import ru.msmai.a1.util.UtilStr;

public class MsmFile {
	
	private String file = "ALIK2-2.txt";

	public MsmFile() {
	}

	public MsmFile(String file) {
		this.file = file;
	}

	public Iterator<String> iterate() {
		return new UtilFile(file);
	}

	public Iterator<String> iterate(Predicate<String> consumer) {
		return new UtilFile(file, consumer);
	}

	public List<String[]> getDescendants(String code) {
		List<String[]> result = new ArrayList<>();
		if (code != null && !code.isEmpty()) {
			for (Iterator<String> iter = iterate(); iter.hasNext();) {
				String[] record = UtilStr.justSplit(iter.next());
				if (record[0].startsWith(code) && !record[0].equals(code)) {
					result.add(record);
				}
			}
		}
		return result;
	}

	public List<String[]> getChildren(String code) {
		List<String[]> result = new ArrayList<>();
		if (code != null && !code.isEmpty()) {
			int expectedCodeLength = code.length() + 1;
			for (Iterator<String> iter = iterate(); iter.hasNext();) {
				String[] record = UtilStr.justSplit(iter.next());
				if (record[0].length() == expectedCodeLength && record[0].startsWith(code)) {
					result.add(record);
				}
			}
		}
		return result;
	}

	public List<String[]> getElements(String code) {
		AtomicReference<List<String[]>> result = new AtomicReference<>(new ArrayList<>());
		if (code != null && !code.isEmpty()) {
			iterate((String line) -> {
				String[] record = UtilStr.justSplit(line);
				if (code.equals(record[0])) {
					result.get().add(record);
				}
				return true;
			});
		}
		return result.get();
	}

	public String[] getElement(String code) {
		AtomicReference<String[]> result = new AtomicReference<>();
		if (code != null && !code.isEmpty()) {
			iterate((String line) -> {
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

	public String[] getRoot() {
		AtomicReference<String[]> result = new AtomicReference<>();
		iterate((String line) -> {
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

	public String[] getParent(String code) {
		return getElement(getParentCode(code));
	}

	public List<String[]> getParents(String code) {
		return getElements(getParentCode(code));
	}

	public List<String[]> getNeighbors(String code) {
		return getChildren(getParent(code)[0]);
	}
	
	public String getUsedCodes(String code) {
		return getChildren(code).stream().map(a->removeParent(a[0], code)).sorted().collect(Collectors.joining());
	}
	
	public String getNextAvailable(String code) {
		return getAvailableCodes(getUsedCodes(code)).substring(0,1);
	}
	
	public Map<String, String> selectAll(List<String> duplications) {
		Map<String, String> result = new HashMap<>(100000);
		for (Iterator<String> iter = iterate(); iter.hasNext();) {
			String[] record = UtilStr.justSplit(iter.next());
			if (result.containsKey(record[0])) {
				duplications.add(record[0]);
			} else {
				result.put(record[0], record[1]);
			}
		}
		return result;
	}

	public List<String> selectWords(String code) {
		List<String> result = new ArrayList<>();
		for (Iterator<String> iter = iterate(); iter.hasNext();) {
			String[] record = UtilStr.justSplit(iter.next());
			if ( code.equals(record[0])) {
				result.add(record[1]);
			}
		}
		return result;
	}

	public List<String[]> reindex(String newParentCode, String oldParentCode, List<String[]> descendants) {
		String nextAvailableCode = getNextAvailable(newParentCode);
		for(String[] descendant: descendants){
			String oldChildCode = removeParent(descendant[0], oldParentCode);
			String newChildCode = replaceRoot(oldChildCode, nextAvailableCode);
			descendant[0] = newParentCode + newChildCode;
		}
		return descendants;
	}

	public void copyPasteDescendants(String newParentCode, String oldParentCode, String file) {

		List<String[]> descendants = getDescendants(oldParentCode);
		reindex(newParentCode, oldParentCode, descendants);
		
		AtomicReference<Iterator<String[]>> arefiter = new AtomicReference<>();
		AtomicReference<Boolean> areflag = new AtomicReference<>(false);
		AtomicReference<String> aline = new AtomicReference<>();
		
		Iterator<String> iter = iterate();
		
		UtilFile.writeByLine(file, ()->{
			Iterator<String[]> aiter = arefiter.get();
			if( aiter != null ){ // paste
				if( aiter.hasNext() ){
					String[] next = aiter.next();
					return next[0]+UtilMsmCode.SPACE+next[1];
				} else {
					arefiter.set(null);
					areflag.set(false);
					return aline.get();
				}
			}
			if( iter.hasNext() ){
				String line = iter.next();
				String[] record = UtilStr.justSplit(line);
				if( areflag.get() ){
					if ( !newParentCode.equals(UtilMsmCode.getParentCode(record[0]))) {
						aline.set(line);
						arefiter.set(descendants.iterator());
						String[] next = arefiter.get().next();
						return next[0]+UtilMsmCode.SPACE+next[1];
					}
				} else if ( newParentCode.equals(record[0])) {
					areflag.set(true);
				}

				return line;
			}
			return null;
		});
	}
	

	public void cutPasteDescendants(String newParentCode, String oldParentCode, String file) {

		List<String[]> descendants = getDescendants(oldParentCode);
		reindex(newParentCode, oldParentCode, descendants);
		
		AtomicReference<Iterator<String[]>> arefiter = new AtomicReference<>();
		AtomicReference<Boolean> areflag = new AtomicReference<>(false);
		AtomicReference<String> aline = new AtomicReference<>();
		
		Iterator<String> iter = iterate();
		
		UtilFile.writeByLine(file, ()->{
			Iterator<String[]> aiter = arefiter.get();
			if( aiter != null ){ // paste
				if( aiter.hasNext() ){
					String[] next = aiter.next();
					return next[0]+UtilMsmCode.SPACE+next[1];
				} else {
					arefiter.set(null);
					areflag.set(false);
					return aline.get();
				}
			}
			if( iter.hasNext() ){
				String line = iter.next();
				String[] record = UtilStr.justSplit(line);
				if( areflag.get() ){
					if ( !newParentCode.equals(UtilMsmCode.getParentCode(record[0]))) {
						aline.set(line);
						arefiter.set(descendants.iterator());
						String[] next = arefiter.get().next();
						return next[0]+UtilMsmCode.SPACE+next[1];
					}
				} else if ( newParentCode.equals(record[0])) {
					areflag.set(true);
				} else { // cut
					while( record[0].startsWith(oldParentCode) && !record[0].equals(oldParentCode) ){
						if( iter.hasNext() ){
							line = iter.next();
							record = UtilStr.justSplit(line);
						} else {
							line = null;
							break;
						}
					}
				}
				
				return line;
			}
			return null;
		});
	}
	
}
