package ru.msmai.a1.util;

import static ru.msmai.a1.util.UtilMsmFile.getChildren;
import static ru.msmai.a1.util.UtilMsmFile.getElements;
import static ru.msmai.a1.util.UtilMsmFile.getParent;
import static ru.msmai.a1.util.UtilMsmFile.getParents;
import static ru.msmai.a1.util.UtilMsmFile.selectWords;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UtilPrint {

	public static void printDuplications(List<String> element){
		for(String code: element){
			List<String[]> parents = getParents(code);
			System.out.println("\nДубль мсм кода "
						+ code
						+ " у слов "
						+ selectWords(code)
						+ " родитель ["+parents.stream().map(a->atoString(a)
						+ " родитель "+Arrays.toString(getParent(a[0]))).collect(Collectors.joining(", "))+
						"]");
			UtilPrint.print(code, "потомки", getChildren(code));
		}
	}
	
	public static void print(String code){
		UtilPrint.print(code, "элементы", getElements(code));
		UtilPrint.print(code, "дети", getChildren(code));
	}

	public static void print(String[] element, String header, List<String[]> elements) {
		print(atoString(element), header, elements);
	}

	public static void print(String str, String header, List<String[]> elements) {
		System.out.println(("\n------ "+header+" ----------------").substring(0, 25)+str);
		elements.stream().map(a->atoString(a)).forEach(System.out::println);
	}

	public static String atoString(String[] element) {
		return element[0]+": "+element[1];
	}

	public static void print(String[] element) {
		System.out.println(atoString(element));
	}
}
