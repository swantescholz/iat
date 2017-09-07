package de.sscholz.iat.util.log;

import de.sscholz.iat.util.StringUtil;
import de.sscholz.iat.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Log {

	public static final Log DEFAULT = new Log();
	private static final boolean APPEND = false;

	static {
		DEFAULT.addListener(System.out, LogLevel.INFO);
		try {
			DEFAULT.addOutputFile(new File("log.txt"), LogLevel.DEBUG);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private final List<Listener> listeners = new ArrayList<>();

	private class Listener {
		public final OutputStream stream;
		public final LogLevel logLevel;

		public Listener(OutputStream stream, LogLevel logLevel) {
			this.stream = stream;
			this.logLevel = logLevel;
		}
	}

	private synchronized void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void addListener(OutputStream stream, LogLevel logLevel) {
		addListener(new Listener(stream, logLevel));
	}

	public void addOutputFile(File outputFile, LogLevel logLevel) throws FileNotFoundException {
		OutputStream stream = new FileOutputStream(outputFile, APPEND);
		addListener(stream, logLevel);
	}

	private synchronized void log(String message, LogLevel logLevel) {
		byte[] bytes = (logLevel.getPrefix() + message + StringUtil.NEWLINE).getBytes();
		listeners.forEach(it -> {
			if (!it.logLevel.allowsToLog(logLevel)) {
				return;
			}
			try {
				it.stream.write(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	public void debug(String message) {
		log(message, LogLevel.DEBUG);
	}

	public void info(String message) {
		log(message, LogLevel.INFO);
	}

	public void warn(String message) {
		log(message, LogLevel.WARN);
	}

	public void warn(Exception e) {
		log(Util.getStacktrace(e), LogLevel.WARN);
	}

	public void error(String message) {
		log(message, LogLevel.ERROR);
		System.exit(-1);
	}

	public void throwException(RuntimeException e) {
		logException(e);
		throw e;
	}

	public <T extends Exception> void throwException(T e) throws T {
		logException(e);
		throw e;
	}

	private void logException(Exception e) {
		log("failed with exception: " + Util.getStacktrace(e), LogLevel.ERROR);
	}
}
