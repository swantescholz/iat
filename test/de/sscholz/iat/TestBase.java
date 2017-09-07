package de.sscholz.iat;

import de.sscholz.iat.math.CanBeAlmostEqual;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.game.Ball;

import static org.junit.Assert.assertTrue;

public class TestBase {

	protected Ball ball = new Ball(1.0);

	public void assertAlmostEqual(double a, double b) {
		assertTrue(a + ", " + b, MathUtil.almostEqual(a, b));
	}

	public <T extends CanBeAlmostEqual<T>> void assertAlmostEqual(T a, T b) {
		assertTrue(a.toString() + ", " + b.toString(), a.almostEqual(b));
	}


}
