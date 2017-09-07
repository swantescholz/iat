package de.sscholz.iat.gfx.light;

import de.sscholz.iat.math.Movable;
import de.sscholz.iat.math.Vector3;

public class PositionalLight extends Light implements Movable {

	public Vector3 position = new Vector3();
	public double constantAttenuation = 1.0;
	public double linearAttenuation = 0.0;
	public double quadraticAttenuation = 0.0;

	public PositionalLight() {

	}

	@Override
	public Vector3 getPosition() {
		return position;
	}

	@Override
	public boolean isPositional() {
		return true;
	}

	@Override
	public void setPosition(Vector3 newPosition) {
		this.position = newPosition;
	}

	@Override
	public double getConstantAttenuation() {
		return constantAttenuation;
	}

	@Override
	public double getLinearAttenuation() {
		return linearAttenuation;
	}

	@Override
	public double getQuadraticAttenuation() {
		return quadraticAttenuation;
	}

}
