package de.sscholz.iat.gfx.model;

import de.sscholz.iat.gfx.shader.uniform.UniformManager;
import de.sscholz.iat.math.Matrix;

public abstract class RenderableObject extends Transformable implements Renderable {

	public void render() {
		UniformManager uniforms = UniformManager.instance;
		Matrix oldModelMatrix = uniforms.getModelMatrix();
		uniforms.setModelMatrix(oldModelMatrix.mul(transformation));
		draw();
		uniforms.setModelMatrix(oldModelMatrix);
	}

	protected abstract void draw();

}
