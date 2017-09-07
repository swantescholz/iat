package de.sscholz.iat.game;

import de.sscholz.iat.gfx.model.Renderable;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.util.ClosestPointComputer;

import java.util.List;

public class Level implements Renderable {

	private List<LevelElement> levelElements;
	UpdateInformation info;

	public Level(List<LevelElement> levelElements) {
		this.levelElements = levelElements;
	}

	public Vector2 collidedWhereWithBall() {
		ClosestPointComputer closestPointComputer = new ClosestPointComputer(info.ball.getLastPosition());
		levelElements.forEach(element -> {
			Vector2 intersectionPoint = element.intersectsWithBall();
			closestPointComputer.update(intersectionPoint);
		});
		return closestPointComputer.getClosestPoint();
	}

	@Override
	public void render() {
		levelElements.forEach(LevelElement::render);
	}

	public boolean adjustPlayer(Player player) {
		Vector2 contactPointCenter = collidedWhereWithBall();
		if (contactPointCenter != null) {
			player.stopAt(contactPointCenter);
			return true;
		}
		return false;
	}

	public void update(UpdateInformation info) {
		this.info = info;
		levelElements.forEach(it -> it.doUpdate(info));
	}

}
