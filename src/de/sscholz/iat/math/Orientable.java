package de.sscholz.iat.math;

public interface Orientable {

	Vector3 DEFAULT_FRONT_DIRECTION = Vector3.NZ;
	Vector3 DEFAULT_UP_VECTOR = Vector3.Y;

	Vector3 getUpVector();

	void setUpVector(Vector3 newUpVector);

	Vector3 getDirection();

	void setDirection(Vector3 newDirection);

	default void lookInDirection(Vector3 newDirection) {
		setDirection(newDirection);
		setUpVector(getUpVector().makePerpendicularTo(newDirection));
	}

	default Vector3 getXAxis() {
		return getUpVector().cross(getDirection()).normalize();
	}

	default Vector3 getYAxis() {
		return getZAxis().cross(getXAxis()).normalize();
	}

	default Vector3 getZAxis() {
		return getDirection().normalize();
	}

	default Matrix getOrientationMatrix() {
		return Matrix.rotationFromAxes(getXAxis(), getYAxis(), getZAxis());
	}

	default void pitch(double radian) { //x
		radian *= -1; //for CCW
		Matrix rotation = Matrix.rotation(getXAxis(), radian);
		setDirection(rotation.transformNormal(getDirection()));
		setUpVector(rotation.transformNormal(getUpVector()));
	}

	default void yaw(double radian) { //y
		radian *= -1; //for CCW
		setDirection(Matrix.rotation(getUpVector(), radian).transformNormal(getDirection()));
	}

	default void yawAroundOtherUpVector(double radian, Vector3 otherUpVector) {
		radian *= -1; //for CCW
		Matrix rotation = Matrix.rotation(otherUpVector, radian);
		setDirection(rotation.transformNormal(getDirection()));
		setUpVector(rotation.transformNormal(getUpVector()));
	}

	default void roll(double radian) { //z
		radian *= -1; //for CCW
		setUpVector(Matrix.rotation(getDirection(), radian).transformNormal(getUpVector()));
	}

}
