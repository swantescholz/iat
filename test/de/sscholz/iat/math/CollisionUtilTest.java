package de.sscholz.iat.math;

import de.sscholz.iat.TestBase;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CollisionUtilTest extends TestBase {

	Vector2 a = new Vector2(0.0, 0.0);
	Vector2 b = new Vector2(1.0, 0.0);
	Vector2 c = new Vector2(0.5, -1.0);
	Vector2 d = new Vector2(1.0, 1.0);

	@Test
	public void lineIntersectsLineProperly() {
		Vector2 p = CollisionUtil.intersectLines(a, b, c, d);
		assertAlmostEqual(p, new Vector2(0.75, 0.0));
		assertNull(CollisionUtil.intersectLines(a, d, b, c));
		assertNull(CollisionUtil.intersectLines(a, b, a, b));
		assertNull(CollisionUtil.intersectLines(a, b, new Vector2(2, -1), new Vector2(2, 1)));
	}

	@Test
	public void lineIntersectsMovingCircleCorrectly() {
		Vector2 p = CollisionUtil.ballIntersectsLine(c, d, 0.25, a, b);
		assertAlmostEqual(new Vector2(0.6875, -0.25), p);
	}

	@Test
	public void shouldKnowAPointInTriangle() {
		assertTrue(CollisionUtil.isPointInTriangle(new Vector2(.5, .2), a, b, d));
		assertFalse(CollisionUtil.isPointInTriangle(new Vector2(.5, -.2), a, b, d));
		assertFalse(CollisionUtil.isPointInTriangle(new Vector2(-.5, .2), a, b, d));
		assertFalse(CollisionUtil.isPointInTriangle(new Vector2(2.5, .2), a, b, d));
	}

	@Test
	public void circleShouldIntersectCorrectly() {
		Vector2 la = new Vector2(8, 8);
		Vector2 lb = new Vector2(8, 4);
		Vector2 c = new Vector2(7, 6);
		assertNull(CollisionUtil.getFirstLineCircleIntersection(la, lb, c, .8));
		assertAlmostEqual(new Vector2(8, 6 + Math.sqrt(3)), CollisionUtil.getFirstLineCircleIntersection(la, lb, c, 2));
	}

	@Test
	public void ballShouldHitCornersCorrectly() {
		assertNull(CollisionUtil.ballIntersectsLine(new Vector2(-2, -2), new Vector2(-1, 2), 1.0, a, b));
		Vector2 p = CollisionUtil.ballIntersectsLine(new Vector2(-2, -2), new Vector2(0, 0), 1.0, a, b);
		assertAlmostEqual(Vector2.NXY.mul(Math.sqrt(2) / 2), p);
	}

}
