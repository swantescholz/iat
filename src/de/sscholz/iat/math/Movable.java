package de.sscholz.iat.math;

public interface Movable {

	Vector3 getPosition();

	void setPosition(Vector3 newPosition);

	default void move(Vector3 delta) {
		setPosition(getPosition().add(delta));
	}

	default Matrix getTranslationMatrix() {
		return Matrix.translation(getPosition());
	}

}
