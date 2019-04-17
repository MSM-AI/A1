package ru.msmai.a1.util;

public class UtilMsm {

	public static String anyMsmFunction(String a, String b) {
		return a == null || b == null || b.isEmpty() || a.isEmpty()? null: a+b;
	}

}
