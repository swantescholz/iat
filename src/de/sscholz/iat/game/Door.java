package de.sscholz.iat.game;

import de.sscholz.iat.gfx.font.Font;
import de.sscholz.iat.gfx.font.FontRectangle;
import de.sscholz.iat.gfx.model.Model;
import de.sscholz.iat.gfx.model.ModelManager;
import de.sscholz.iat.math.Color;
import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.ClosestPointComputer;

import java.text.DecimalFormat;

public abstract class Door extends LevelElement {

	protected static final double DOOR_HEIGHT = 3.0;
	protected static final double DOOR_WIDTH = 0.1;
	protected final double timeLimit;
	protected final Model model;
	protected final Vector2 position;
	protected boolean open = true;
	protected final Font font = new Font(24);

	public Door(double timeLimit, Vector2 position) {
		this.timeLimit = timeLimit;
		this.position = position;
		model = ModelManager.instance.get("cube.obj");
		model.setTransformation(Matrix.scaling(new Vector3(DOOR_WIDTH,DOOR_HEIGHT,.1).mul(.5)));
	}

	boolean isOpen() {
		return open;
	}
	abstract Material getClosedMaterial();

	Material getOpenMaterial() {
		return Material.GRAY.interpolateLinear(getClosedMaterial(), .3);
	}

	@Override
	public void update() {

	}

	@Override
	public void render() {
		Matrix oldTransformation = model.getTransformation();
		if (isOpen()) {
			model.transform(Matrix.rotation(Vector3.Z, Math.PI/2));
			getOpenMaterial().use();
		} else {
			getClosedMaterial().use();
		}
		model.transform(Matrix.translation(position.to3()));
		model.render();
		model.setTransformation(oldTransformation);
		Color textColor = getClosedMaterial().diffuse;
		DecimalFormat df = new DecimalFormat("#.#");
		String text = df.format(timeLimit);
		FontRectangle fontRectangle = font.createTexture(text, textColor);
		fontRectangle.position = position;
		fontRectangle.size = 1;
		fontRectangle.render();
	}


	@Override
	public Vector2 intersectsWithBall() {
		if (isOpen()) {
			return null;
		}
		return intersectsWithBallWhenClosed(info.ball);
	}

	protected Vector2 intersectsWithBallWhenClosed(Ball ball) {
		ClosestPointComputer closestPointComputer = new ClosestPointComputer(ball.getLastPosition());
		Vector2 up = new Vector2(0,DOOR_HEIGHT/2);
		Vector2 right = new Vector2(DOOR_WIDTH/2,0);
		Vector2 a = position.add(up).sub(right);
		Vector2 b = position.sub(up).sub(right);
		Vector2 c = position.sub(up).add(right);
		Vector2 d = position.add(up).add(right);
		closestPointComputer.update(ball.collidedWhereWithLine(a,b));
		closestPointComputer.update(ball.collidedWhereWithLine(b,c));
		closestPointComputer.update(ball.collidedWhereWithLine(c,d));
		closestPointComputer.update(ball.collidedWhereWithLine(d,a));
		return closestPointComputer.getClosestPoint();
	}

	protected boolean ballAwayFromDoor(Ball ball) {
		double distance = ball.getCurrentPosition().distanceTo(position);
		return distance*.95 > ball.getRadius() + getBoundingCircleRadius();
	}

	protected double getBoundingCircleRadius() {
		return Math.sqrt(DOOR_HEIGHT*DOOR_HEIGHT + DOOR_WIDTH*DOOR_WIDTH);
	}
}
