package ru.msmai.a1;

import static ru.msmai.a1.util.UtilMsmCode.getAvailableCodes;
import static ru.msmai.a1.util.UtilMsmCode.getParentCode;
import static ru.msmai.a1.util.UtilMsmCode.isRoot;
import static ru.msmai.a1.util.UtilMsmCode.removeParent;
import static ru.msmai.a1.util.UtilMsmCode.replaceRoot;

import java.util.ArrayList;
import java.util.Collections;
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
	
	public final String file;

	public MsmFile() {
		this("ALIK2-2.txt");
	}

	public MsmFile(String file) {
		this.file = file;
	}

	public Iterator<String> iterator() {
		return new UtilFile(file);
	}

	public Iterator<String> iterate(Predicate<String> consumer) {
		return new UtilFile(file, consumer);
	}

	public List<String[]> getDescendants(String code) {
		List<String[]> result = new ArrayList<>();
		if (code != null && !code.isEmpty()) {
			for (Iterator<String> iter = iterator(); iter.hasNext();) {
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
			for (Iterator<String> iter = iterator(); iter.hasNext();) {
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

	public String[] getElementByCode(String code) {
		AtomicReference<String[]> result = new AtomicReference<>();
		if (code != null && !code.isEmpty()) {
			iterate((String line) -> {
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

	public String[] getElementByWord(String word) {
		AtomicReference<String[]> result = new AtomicReference<>();
		if (word != null && !word.isEmpty()) {
			iterate((String line) -> {
				String[] record = UtilStr.justSplit(line);
				if (word.equals(record[1])) {
					result.set(record);
					return false;
				}
				return true;
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

	public String[] getParentByCode(String code) {
		return getElementByCode(getParentCode(code));
	}

	public List<String[]> getParents(String code) {
		return getElements(getParentCode(code));
	}

	public List<String[]> getNeighbors(String code) {
		return getChildren(getParentByCode(code)[0]);
	}
	
	public String getUsedCodes(String code) {
		return getChildren(code).stream().map(a->removeParent(a[0], code)).sorted().collect(Collectors.joining());
	}
	
	public String getNextAvailable(String code) {
		return getAvailableCodes(getUsedCodes(code)).substring(0,1);
	}
	
	public Map<String, String> selectAll(List<String> duplications) {
		Map<String, String> result = new HashMap<>(100000);
		for (Iterator<String> iter = iterator(); iter.hasNext();) {
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
		for (Iterator<String> iter = iterator(); iter.hasNext();) {
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

	public void copyPasteDescendants(String oldParentCode, String newParentCode, String file) {
		// copy & reindex
		List<String[]> descendants = getDescendants(oldParentCode);
		reindex(newParentCode, oldParentCode, descendants);
		
		Iterator<String> iter1 = iterator();
		Iterator<String[]> iter2 = descendants.iterator();
		
		UtilFile.writeByLine(file, ()->{
			if( iter1.hasNext() ){
				return iter1.next();
			}
			if( iter2.hasNext() ){
				return UtilMsmCode.toString(iter2.next());
			}
			return null;
		});
	}
	
	public void cutPasteDescendants(String newParentCode, String oldParentCode, String file) {
		// copy & reindex
		List<String[]> descendants = getDescendants(oldParentCode);
		reindex(newParentCode, oldParentCode, descendants);
		
		Iterator<String> iter1 = iterator();
		Iterator<String[]> iter2 = descendants.iterator();
		
		UtilFile.writeByLine(file, ()->{
			while( iter1.hasNext() ){
				String line = iter1.next();
				String[] record = UtilStr.justSplit(line);
				if( record[0].startsWith(oldParentCode) && !record[0].equals(oldParentCode) ){
					continue; // cut
				}
				return line;
			}
			if( iter2.hasNext() ){
				return UtilMsmCode.toString(iter2.next());
			}
			return null;
		});
	}
	
	public void sortByCode(String newFile) {
		List<String> list = UtilFile.readAll(file);
		Collections.sort(list);
		UtilFile.writeAll(newFile, list);
	}
	
	public void save(List<String[]> add, String fileName) {
		Iterator<String> iter1 = iterator();
		Iterator<String[]> iter2 = add.iterator();
		// copy
		UtilFile.writeByLine(fileName, ()->{
			if( iter1.hasNext() ){
				return iter1.next();
			}
			if( iter2.hasNext() ){ // add
				return UtilMsmCode.toString(iter2.next());
			}
			return null;
		});
	}
	
	public void delete(Map<String,String> del, String fileName) {
		Iterator<String> iter = iterator();
		// copy
		UtilFile.writeByLine(fileName, ()->{
			while( iter.hasNext() ){
				String line = iter.next();
				String[] record = UtilStr.justSplit(line);
				if( del.containsKey(record[0]) ){
					continue; // delete
				}
				return line;
			}
			return null;
		});
	}

	public String[] indexWord(String parentCode, String word){
		return new String[]{parentCode+getNextAvailable(parentCode), word};
	}
	
	public List<String[]> indexWords(String parentCode, List<String> words){
		List<String[]> result = new ArrayList<String[]>(words.size());
		String[] available = getAvailableCodes(getUsedCodes(parentCode)).split("");
		for(int i=0,max=words.size(); i<max; i++){
			result.add(new String[]{parentCode+available[i], words.get(i)});
		}
		return result;
	}
	
}
