package de.sscholz.iat.game;

import de.sscholz.iat.TestBase;
import de.sscholz.iat.math.Vector2;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class BallTest extends TestBase {

	Ball ball = new Ball(1.0);

	@Test
	public void collidedWithLine() {
		Vector2 a = new Vector2(0, 0);
		Vector2 b = new Vector2(1, 0);
		ball.updatePosition(new Vector2(0, -2));
		ball.updatePosition(new Vector2(4, 14));
		assertAlmostEqual(new Vector2(.25, -1), ball.collidedWhereWithLine(a, b));
		ball.updatePosition(new Vector2(4, 5));
		assertNull(ball.collidedWhereWithLine(a, b));
	}
}
