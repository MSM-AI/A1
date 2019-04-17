package ru.msmai.a1.util;

import java.util.regex.Pattern;

public class UtilStr {

	public static final int MIN_TRUE = 0;
	public static final int MAX_TRUE = 10000;
	
	public static final String SPACE = " ";
	public static final String PUNCTUATION = " \\,\\!\\:\\;";

	public static String getUps(String str) {
		return str == null || str.isEmpty()? null: str.toUpperCase();
	}

	static private final Pattern delDbSpace = Pattern.compile("["+SPACE+"]+");
	
	public static String delDbSpace(String str) {
		return str == null || str.isEmpty()? null: delDbSpace.matcher(str).replaceAll(SPACE);
	}

	static private final Pattern justBreakReplace = Pattern.compile("["+PUNCTUATION+"]+");
	
	public static String[] justBreak(String str) {
		return str == null || str.isEmpty()? null: justBreakReplace.matcher(str).replaceAll(SPACE).split(SPACE);
	}

	public static String insInString(String str, String a, String b) {
		return str == null|| str.isEmpty()? null: a == null || b == null || b.isEmpty() || a.isEmpty()? str: str.replaceAll(a, b);
	}

	public static int trueEnds(String a, String b) {
		if( a == null || b == null || b.isEmpty() || a.isEmpty()) {
			return 0;
		} 
		byte[] c = a.getBytes(), d = b.getBytes();
		for(int i=0, min=Math.min(c.length, d.length); i<min; i++){
			if( c[i] != d[i]){
				return i;
			}
		}
		return c.length;
	}
	
	public static String trueCompare(String a, String b) {
		return a == null || b == null || b.isEmpty() || a.isEmpty()? null: a.substring(0, trueEnds(a, b)).trim();
	}
	
	public static int truePercent(String a, String b) {
		return a == null || b == null || b.isEmpty() || a.isEmpty()? MIN_TRUE: a.equals(b)? MAX_TRUE:
			Math.floorDiv(MAX_TRUE,  Math.max(a.length(), b.length()))*trueEnds(a, b);
	}

}
