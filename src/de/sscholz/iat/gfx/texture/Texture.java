package de.sscholz.iat.gfx.texture;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.gfx.shader.uniform.UniformManager;
import de.sscholz.iat.math.Matrix;

import javax.media.opengl.GL;

public class Texture {

	public static final int MAX_LAYERS = 4;

	public static void enable() {
		GlUtil.getGl().glEnable(GL.GL_TEXTURE_2D);
	}

	public static void disable() {
		GlUtil.getGl().glDisable(GL.GL_TEXTURE_2D);
	}

	private final TextureData data;
	Matrix transformation = Matrix.identity();


	public Texture(TextureData data) {
		this.data = data;
	}

	public void dispose() {
		data.dispose();
	}

	public void use() {
		use(0);
	}
	public void use(int textureLayerIndex) {
		UniformManager.instance.setTexture(this, textureLayerIndex);
	}

	public void bind() {
		data.bind();
	}

	public static void unbind() {
		TextureData.unbind();
	}

}
