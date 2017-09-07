package de.sscholz.iat.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Util {

	public static void wait(double seconds) {
		try {
			Thread.sleep((long) (1000 * seconds));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String getStacktrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
