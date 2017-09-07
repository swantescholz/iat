package de.sscholz.iat.gfx.shader;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.util.log.Asserter;

import javax.media.opengl.GL;

public class Shader implements Asserter {

	protected final int shaderId;
	protected final GL gl;

	protected Shader(int shaderType) {
		gl = GlUtil.getGl();
		this.shaderId = GlUtil.getGl().glCreateShader(shaderType);
	}

	public void setSource(String source) throws CompilationFailedException {
		gl.glShaderSource(shaderId, 1, new String[]{source}, new int[]{source.length()}, 0);
		gl.glCompileShader(shaderId);
		checkErrors();
	}

	private void checkErrors() throws CompilationFailedException {
		int[] buffer = new int[1];
		gl.glGetShaderiv(shaderId, GL.GL_COMPILE_STATUS, buffer, 0);
		if(buffer[0] == 0) {
			gl.glGetShaderiv(shaderId, GL.GL_INFO_LOG_LENGTH, buffer, 0);

			int maxLength = buffer[0];
			byte[] errorLog = new byte[maxLength];
			gl.glGetShaderInfoLog(shaderId, maxLength, buffer, 0, errorLog, 0);

			dispose();
			log.throwException(new CompilationFailedException("Shader did not load: " + new String(errorLog)));
		}
	}

	public void dispose() {
		gl.glDeleteShader(shaderId);
	}

	public int getId() {
		return shaderId;
	}
}
