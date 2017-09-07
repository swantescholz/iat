package de.sscholz.iat.util.log;

import de.sscholz.iat.math.CanBeAlmostEqual;

public interface Asserter extends Logger {

	default void fail(String message) {
		log.throwException(new AssertionException(message));
	}

	default void assertTrue(boolean value) {
		assertTrue("Value should be TRUE.", value);
	}

	default void assertFalse(boolean value) {
		assertFalse("Value should be FALSE.", value);
	}

	default void assertTrue(String message, boolean value) {
		if (!value) {
			fail(message);
		}
	}

	default void assertFalse(String message, boolean value) {
		if (value) {
			fail(message);
		}
	}

	default <T> void assertEqual(T expected, T actual) {
		assertEqual("actual<" + actual + "> not equal expected<" + expected + ">", expected, actual);
	}

	default <T> void assertEqual(String message, T expected, T actual) {
		if (!expected.equals(actual)) {
			fail(message);
		}
	}

	default <T extends CanBeAlmostEqual<T>> void assertAlmostEqual(T expected, T actual) {
		assertAlmostEqual("actual<" + actual + "> not almost-equal to expected<" + expected + ">", expected, actual);
	}

	default <T extends CanBeAlmostEqual<T>> void assertAlmostEqual(String message, T expected, T actual) {
		if (!expected.almostEqual(actual)) {
			fail(message);
		}
	}

}
