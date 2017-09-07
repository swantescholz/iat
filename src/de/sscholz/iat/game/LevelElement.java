package de.sscholz.iat.game;

import de.sscholz.iat.gfx.model.Renderable;
import de.sscholz.iat.math.Vector2;

public abstract class LevelElement implements Renderable {

	protected UpdateInformation info;

	public Vector2 intersectsWithBall() {
		return null;
	}

	public boolean shouldBeRemoved() {
		return false;
	}

	protected abstract void update();

	public void doUpdate(UpdateInformation info) {
		this.info = info;
		update();
	}

}
