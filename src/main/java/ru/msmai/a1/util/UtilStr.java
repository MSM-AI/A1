package ru.msmai.a1.util;

import java.util.regex.Pattern;

public class UtilStr {
	public static final int MIN_TRUE = 0;
	public static final int MAX_TRUE = 10000;
	public static final String SPACE = " ";
	public static final String PUNCTUATION = " \\,\\!\\:\\;";


	public static String getUps(String str) {
		return str.toUpperCase();
	}

	static private final Pattern delDbSpace = Pattern.compile("[" + SPACE + "]+");

	public static String delDbSpace(String str) {
		return delDbSpace.matcher(str).replaceAll(SPACE);
	}

	static private final Pattern justBreakReplace = Pattern.compile("[" + PUNCTUATION + "]+");

	public static String[] justBreak(String str) {
		return justBreakReplace.matcher(str).replaceAll(SPACE).split(SPACE);
	}

	public static String[] justSplit(String str) {
		return str.split(SPACE);
	}

	public static String insInString(String str, String a, String b) {
		return str.replaceAll(a, b);
	}

}
