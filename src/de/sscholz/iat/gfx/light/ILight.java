package de.sscholz.iat.gfx.light;

import de.sscholz.iat.math.Color;
import de.sscholz.iat.math.Vector3;

public interface ILight {

	int getId();
	Color getAmbient();
	Color getDiffuse();
	Color getSpecular();
	Vector3 getPosition();
	boolean isEnabled();
	boolean isPositional();
	default double getConstantAttenuation() {
		return 1.0;
	}

	default double getLinearAttenuation() {
		return 0.0;
	}

	default double getQuadraticAttenuation() {
		return 0.0;
	}

	default double getSpotCutoff() {
		return 180.0;
	}

	default double getSpotCosCutoff() {
		return Math.cos(getSpotCutoff());
	}

	default double getSpotExponent() {
		return 0.0;
	}

	default Vector3 getSpotDirection() {
		return new Vector3(0,0,-1);
	}
}
