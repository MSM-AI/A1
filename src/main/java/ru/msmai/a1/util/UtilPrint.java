package ru.msmai.a1.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ru.msmai.a1.MsmFile;

public class UtilPrint {

	public static void printDuplications(MsmFile msmFile, List<String> element){
		for(String code: element){
			List<String[]> parents = msmFile.getParents(code);
			System.out.println("\nДубль мсм кода "
						+ code
						+ " у слов "
						+ msmFile.selectWords(code)
						+ " родитель ["+parents.stream().map(a->atoString(a)
						+ " родитель "+Arrays.toString(msmFile.getParent(a[0]))).collect(Collectors.joining(", "))+
						"]");
			UtilPrint.print(code, "потомки", msmFile.getChildren(code));
		}
	}
	
	public static void print(MsmFile msmFile, String code){
		UtilPrint.print(code, "элементы", msmFile.getElements(code));
		UtilPrint.print(code, "дети", msmFile.getChildren(code));
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
