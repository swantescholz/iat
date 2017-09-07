package de.sscholz.iat.game;

import de.sscholz.iat.TestBase;
import de.sscholz.iat.math.Vector2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class LevelTest extends TestBase {

	List<LevelElement> levelElements = new ArrayList<>();

	private UpdateInformation createUpdateInformation(Ball ball) {
		return new UpdateInformation(new WorldTime(), ball);
	}

	@Test
	public void shouldAllowCollisionShapesToBeAdded() {
		levelElements.add(Rock.create(2, 2, 4, 4, 2, 4));
		levelElements.add(Rock.create(4, 4, 4, 2, 6, 2));
		Level level = new Level(levelElements);
		Ball ball = new Ball(1.0);
		ball.updatePosition(new Vector2(6, 0));
		ball.updatePosition(new Vector2(0, 6));
		level.update(createUpdateInformation(ball));
		assertAlmostEqual(new Vector2(5, 1), level.collidedWhereWithBall());
	}

	@Test
	public void shouldNotPassThroughRocks() {
		levelElements.add(Rock.create(-2, 4, -2, 10, -10, 4));
		Level level = new Level(levelElements);
		Player player = new Player(null);
		player.stopAt(new Vector2(0, 0));
		player.shoot(new Vector2(-3.9283072655061417, 11.971984047256814));
		level.update(createUpdateInformation(player.getBall()));
		while (!level.adjustPlayer(player) && player.getBall().getCurrentPosition().x > -10) {
			player.update(.1);
		}
		Vector2 p = player.getBall().getCurrentPosition();
		assertTrue(p.x > -3);
	}

	@Test
	public void shouldNotPassUpperRightCorner() {
		levelElements.add(Rock.create(2, 6, 2, 2, 4, 4, 6, 2, 6, 6));
		Level level = new Level(levelElements);
		Player player = new Player(null);
		player.stopAt(new Vector2(10, 10));
		player.shoot(new Vector2(-1, -1));
		level.update(createUpdateInformation(player.getBall()));
		while (!level.adjustPlayer(player) && player.getBall().getCurrentPosition().x > -10) {
			player.update(.1);
		}
		Vector2 p = player.getBall().getCurrentPosition();
		assertAlmostEqual(new Vector2(6, 6).add(Vector2.XY.mul(Math.sqrt(2) / 2)), p);
	}

}
