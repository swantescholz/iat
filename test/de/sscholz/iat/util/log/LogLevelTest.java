package de.sscholz.iat.util.log;

import de.sscholz.iat.TestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogLevelTest extends TestBase {

	@Test
	public void timeStampShouldBe8CharsLong() {
		assertEquals(8, LogLevel.DEBUG.getTimestamp().length());
	}

	@Test
	public void infoShouldAllowError() {
		assertTrue(LogLevel.INFO.allowsToLog(LogLevel.ERROR));
	}

}
