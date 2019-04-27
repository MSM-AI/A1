package ru.msmai.a1.util;

public class UtilMsmCode {
	
	public static final int MIN_TRUE = 0;
	public static final int MAX_TRUE = 10000;
	
	public static final String EMPTY = "";
	public static final String SPACE = " ";
	public static final String PUNCTUATION = " \\,\\!\\:\\;";

	public static final String MSM_CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
//	0123456789 ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz	
//	static { 
//		System.out.println(Arrays.stream("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890".split("")).sorted().collect(Collectors.joining()));
//	}
	
	public static boolean isRoot(String code) {
		return code.length() == 1 && MSM_CODES.startsWith(code);
	}

	public static String getAvailableCodes(String usedCodes) {
		return usedCodes.isEmpty()? MSM_CODES: MSM_CODES.replaceFirst(usedCodes, EMPTY);
	}
	
	public static String removeParent(String descendantCode, String parentCode) {
		return descendantCode.replaceFirst("^"+parentCode, EMPTY);
	}

	public static String replaceRoot(String code, String newRoot) {
		return newRoot+code.substring(1);
	}

	public static String getParentCode(String code) {
		return code.substring(0, code.length() - 1);
	}

	public static int trueEnds(String a, String b) {
		if (b.isEmpty() || a.isEmpty()) {
			return 0;
		}
		byte[] c = a.getBytes(), d = b.getBytes();
		int min = Math.min(c.length, d.length);
		for (int i=0; i<min; i++) {
			if (c[i] != d[i]) {
				return i;
			}
		}
		return min;
	}

	public static String trueCompare(String a, String b) {
		return a.substring(0, trueEnds(a, b)).trim();
	}

	public static int truePercent(String a, String b) {
		return b.isEmpty() || a.isEmpty() ? MIN_TRUE: a.equals(b) ? MAX_TRUE: 
			Math.floorDiv(MAX_TRUE, Math.max(a.length(), b.length())) * trueEnds(a, b);
	}
}
