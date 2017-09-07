package de.sscholz.iat.gfx.font;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.gfx.shader.Program;
import de.sscholz.iat.gfx.shader.ProgramManager;
import de.sscholz.iat.gfx.texture.Texture;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;

import javax.media.opengl.GL;

public class FontRectangle {

	public Vector2 position = Vector2.ZERO;
	public double angle = 0.0;
	public double size = 1.0;
	public final Texture fontTexture;
	public final double wDivH;

	public FontRectangle(Texture fontTexture, double wDivH) {
		this.fontTexture = fontTexture;
		this.wDivH = wDivH;
	}

	public void render() {
		GL gl = GlUtil.getGl();
		Program.push();
		ProgramManager.instance.getProgram("texture").use();
		fontTexture.use();
		double f = size*.5;
		double z = 0;
		Matrix m = Matrix.rotation(Vector3.Z, angle).mul(Matrix.translation(position.to3()));
		Vector3 a = m.transform(new Vector3(-f*wDivH, -f, z));
		Vector3 b = m.transform(new Vector3( f*wDivH, -f, z));
		Vector3 c = m.transform(new Vector3( f*wDivH, f, z));
		Vector3 d = m.transform(new Vector3(-f*wDivH, f, z));
		gl.glBegin(GL.GL_QUADS);
		gl.glTexCoord2i(0, 0);
		a.gl();
		gl.glTexCoord2i(1,0);
		b.gl();
		gl.glTexCoord2i(1,1);
		c.gl();
		gl.glTexCoord2i(0,1);
		d.gl();
		gl.glEnd();
		Program.pop();
	}
}
