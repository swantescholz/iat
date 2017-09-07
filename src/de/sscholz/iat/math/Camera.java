package de.sscholz.iat.math;


import de.sscholz.iat.gfx.shader.uniform.UniformManager;

public class Camera extends AbstractMovableOrientable {

	public Camera() {
		this(new Vector3(0, 0, 10), new Vector3(0, 0, -1), new Vector3(0, 1, 0));
	}

	public Camera(Vector3 pos, Vector3 dir, Vector3 up) {
		super(pos, dir, up);
	}

	public Matrix getCameraMatrix() {
		return Matrix.camera(position, direction, upVector);
	}

	public void apply() {
		UniformManager.instance.setCamera(this);
	}
}
