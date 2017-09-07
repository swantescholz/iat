package de.sscholz.iat.game;

import de.sscholz.iat.TestBase;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Vector2;
import org.junit.Test;

import java.util.List;

public class RockTest extends TestBase {

	List<Vector2> vertices = MathUtil.createVector2List(2, 6, 2, 2, 4, 4, 6, 2, 6, 6);
	Rock rock = new Rock(vertices);

	@Test
	public void canHandleBallCollision() {
		ball.updatePosition(new Vector2(2, 8));
		ball.updatePosition(new Vector2(10, 0));
		rock.doUpdate(new UpdateInformation(null, ball));
		Vector2 p = rock.intersectsWithBall();
		assertAlmostEqual(new Vector2(3, 7), p);
	}

}
