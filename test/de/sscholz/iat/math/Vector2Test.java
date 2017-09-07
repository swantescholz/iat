package de.sscholz.iat.math;

import de.sscholz.iat.TestBase;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;


public class Vector2Test extends TestBase {

	@Test
	public void distanceToLineWorks() {
		assertEquals(1.0, new Vector2(5, -1).distanceToInfiniteLine(Vector2.NX, Vector2.X));
		assertAlmostEqual(Math.sqrt(2), new Vector2(0, 1).distanceToInfiniteLine(new Vector2(-1, 2), new Vector2(-2, 1)));
	}

	@Test
	public void distanceToFiniteLineWorks() {
		assertAlmostEqual(Math.sqrt(2), new Vector2(0, 1).distanceToFiniteLine(new Vector2(1, 2), new Vector2(3, 3)));
		assertAlmostEqual(3, new Vector2(6, 3).distanceToFiniteLine(new Vector2(1, 2), new Vector2(3, 3)));
	}

}
