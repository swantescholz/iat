package de.sscholz.iat.game;

import de.sscholz.iat.gfx.model.Model;
import de.sscholz.iat.gfx.model.ModelManager;
import de.sscholz.iat.gfx.model.Renderable;
import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;

public class HourGlass extends LevelElement implements Renderable {

	private static final double DEFAULT_EXTRA_TIME = 6.0;
	private static final double INVISIBLE_EXTRA_TIME = DEFAULT_EXTRA_TIME * 2.2;
	private static final double RADIUS = .22;
	private Model model = ModelManager.instance.get("cube.obj");
	private Vector2 position;
	private RedDoor levelExit;
	private boolean visible = true;
	private boolean collected = false;

	public HourGlass(Vector2 position, RedDoor levelExit) {
		this.position = position;
		this.levelExit = levelExit;
		model.setTransformation(Matrix.identity());
		model.transform(Matrix.scaling(new Vector3(new Vector2(RADIUS),.1)));
	}

	@Override
	protected void update() {
		if (!levelExit.isOpen()) {
			visible = false;
		}
		if (!collected && collidesWithBall(info.ball)) {
			double extraTime = DEFAULT_EXTRA_TIME;
			if (!visible) {
				extraTime = INVISIBLE_EXTRA_TIME;
			}
			info.worldTime.rewind(extraTime);
			visible = false;
			collected = true;
		}
	}

	private boolean collidesWithBall(Ball ball) {
		return RADIUS + ball.getRadius() > position.distanceTo(ball.getCurrentPosition());
	}

	@Override
	public boolean shouldBeRemoved() {
		return collected;
	}

	@Override
	public void render() {
		if (!isVisible()) {
			return;
		}
		Matrix oldTransformation = model.getTransformation();
		model.transform(Matrix.translation(position.to3()));
		Material.YELLOW.use();
		model.render();
		model.setTransformation(oldTransformation);
	}

	public boolean isVisible() {
		return visible;
	}
}
