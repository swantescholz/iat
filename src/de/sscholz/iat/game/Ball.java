package de.sscholz.iat.game;

import de.sscholz.iat.math.CollisionUtil;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Vector2;

public class Ball {

	private Vector2 currentPosition = Vector2.ZERO;
	private Vector2 lastPosition = Vector2.ZERO;
	private final double radius;

	public Ball(double radius) {
		this.radius = radius;
	}

	public void updatePosition(Vector2 newPosition) {
		lastPosition = currentPosition;
		currentPosition = newPosition;
		if (lastPosition == null) {
			lastPosition = currentPosition;
		}
	}

	public void teleportTo(Vector2 position) {
		lastPosition = position;
		currentPosition = position;
	}


	//returns ball's center when collision happened, otherwise null
	public Vector2 collidedWhereWithLine(Vector2 lineStart, Vector2 lineEnd) {
		Vector2 intersection = CollisionUtil.ballIntersectsLine(lastPosition, currentPosition, radius, lineStart, lineEnd);
		if (intersection == null) {
			return null;
		}
		return intersection.add(lastPosition.sub(intersection).setLength(MathUtil.EPSILON)); // move a bit away from wall
	}

	public Vector2 getCurrentPosition() {
		return currentPosition;
	}

	public Vector2 getLastPosition() {
		return lastPosition;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public String toString() {
		return "[" + currentPosition + ", " + lastPosition + ", " + radius + "]";
	}

}
