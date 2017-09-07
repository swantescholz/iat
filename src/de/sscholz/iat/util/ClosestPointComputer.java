package de.sscholz.iat.util;

import de.sscholz.iat.math.Vector2;

public class ClosestPointComputer {

	private final Vector2 center;
	private Vector2 closestPoint = null;

	public ClosestPointComputer(Vector2 center) {
		this.center = center;
	}

	public void update(Vector2 point) {
		if (point == null) {
			return;
		}
		if (closestPoint == null || center.distanceTo2(point) < center.distanceTo2(closestPoint)) {
			closestPoint = point;
		}
	}

	public Vector2 getClosestPoint() {
		return closestPoint;
	}
}
