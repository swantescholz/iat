package de.sscholz.iat.gfx.shader;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.gfx.shader.uniform.UniformManager;

import javax.media.opengl.GL;
import java.util.Stack;

public class Program {

	private final GL gl;
	private final int programId;
	private static final Stack<Program> stack = new Stack<>();
	private static Program currentlyUsedProgram;

	public Program() {
		gl = GlUtil.getGl();
		programId = gl.glCreateProgram();
	}

	public void attachShader(Shader shader) {
		gl.glAttachShader(programId, shader.getId());
	}

	public void link() {
		gl.glLinkProgram(programId);
	}

	public void use() {
		currentlyUsedProgram = this;
		gl.glUseProgram(programId);
		UniformManager.instance.applyAllDefaultUniformsToCurrentShader();
	}

	public static void useNone() {
		GlUtil.getGl().glUseProgram(0);
		currentlyUsedProgram = null;
	}

	public static void push() {
		if (currentlyUsedProgram != null) {
			stack.push(currentlyUsedProgram);
		}
	}

	public static void pop() {
		if (!stack.empty()) {
			stack.pop().use();
		}
	}

	public void dispose() {
		gl.glDeleteProgram(programId);
	}
}
