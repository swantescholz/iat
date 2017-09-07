package de.sscholz.iat.util.log;

import de.sscholz.iat.util.StringUtil;

import java.time.LocalTime;
import java.util.function.Function;

public enum LogLevel {

	DEBUG(1),
	INFO(2),
	WARN(3),
	ERROR(4);

	private final int level;

	LogLevel(int level) {
		this.level = level;
	}

	public boolean allowsToLog(LogLevel applyingLog) {
		return this.level <= applyingLog.level;
	}

	public String getPrefix() {
		return getTimestamp() + " [" + toString() + "] ";
	}

	String getTimestamp() {
		LocalTime t = LocalTime.now();
		Function<Integer, String> f = number -> StringUtil.fillZeroes(number, 2);
		final String sep = ":";
		return f.apply(t.getHour()) + sep + f.apply(t.getMinute()) + sep + f.apply(t.getSecond());
	}
}
