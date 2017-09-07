package de.sscholz.iat.game;

import de.sscholz.iat.gfx.model.Model;
import de.sscholz.iat.gfx.model.Renderable;
import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;

public class Player implements Renderable {

	public static final double SHOOT_SPEED = 12.6;

	private Ball ball = new Ball(1.0);
	private Vector2 direction = Vector2.ZERO;
	private Model playerModel;
	private boolean stopped = true;

	public Player(Model playerModel) {
		this.playerModel = playerModel;
	}

	public void render() {
		playerModel.setTransformation(Matrix.rotation(Vector3.X, Math.PI/2));
		playerModel.transform(Matrix.scaling(new Vector3(1, 1, 0.2)));
		playerModel.transform(Matrix.scaling(ball.getRadius()));
		playerModel.transform(Matrix.translation(ball.getCurrentPosition().to3()));
		Material.GOLD.use();
		playerModel.render();
	}

	public void update(double dt) {
		ball.updatePosition(ball.getCurrentPosition().add(direction.mul(dt)));
	}

	public void shoot(Vector2 shootDirection) {
		if (isStopped()) {
			direction = shootDirection;
			stopped = false;
		}
	}

	public void stopAt(Vector2 newPosition) {
		ball.teleportTo(newPosition);
		direction = Vector2.ZERO;
		stopped = true;
	}

	public boolean isStopped() {
		return stopped;
	}

	public Ball getBall() {
		return ball;
	}

	public Vector2 getPosition() {
		return ball.getCurrentPosition();
	}
}
