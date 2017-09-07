package de.sscholz.iat.math;

public abstract class AbstractMovableOrientable implements MovableOrientable {

	public Vector3 position;
	public Vector3 direction;
	public Vector3 upVector;

	public AbstractMovableOrientable() {
		this(new Vector3(0, 0, 10), new Vector3(0, 0, -1), new Vector3(0, 1, 0));
	}

	public AbstractMovableOrientable(Vector3 pos, Vector3 dir, Vector3 up) {
		this.position = pos;
		this.direction = dir;
		this.upVector = up;
	}

	@Override
	public Vector3 getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3 newPosition) {
		this.position = newPosition;
	}

	@Override
	public Vector3 getUpVector() {
		return upVector;
	}

	@Override
	public void setUpVector(Vector3 newUpVector) {
		this.upVector = newUpVector;
	}

	@Override
	public Vector3 getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Vector3 newDirection) {
		this.direction = newDirection;
	}
}
