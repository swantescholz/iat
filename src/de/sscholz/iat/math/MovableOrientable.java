package de.sscholz.iat.math;

public interface MovableOrientable extends Movable, Orientable {

	default void lookAt(Vector3 to) {
		lookInDirection(to.sub(getPosition()));
	}

	default void moveLocal(Vector3 v) {
		Vector3 delta = getXAxis().mul(v.x).
				add(getYAxis().mul(v.y)).
				add(getZAxis().mul(v.z));
		move(delta);
	}

	default void advance(double zmove) {
		moveLocal(new Vector3(0, 0, zmove));
	}

}
