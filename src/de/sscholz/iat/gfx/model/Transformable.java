package de.sscholz.iat.gfx.model;

import de.sscholz.iat.math.Matrix;

public class Transformable {

	protected Matrix transformation = Matrix.identity();

	public Matrix getTransformation() {
		return transformation;
	}

	public void setTransformation(Matrix newTransformation) {
		this.transformation = newTransformation;
	}

	public void transform(Matrix newTransformation) {
		this.transformation = this.transformation.mul(newTransformation);
	}

}
