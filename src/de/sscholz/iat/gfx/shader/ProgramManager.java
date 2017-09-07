package de.sscholz.iat.gfx.shader;

import de.sscholz.iat.util.ResourceManager;
import de.sscholz.iat.util.log.Asserter;

public class ProgramManager extends ResourceManager<Program> implements Asserter {

	public static final ProgramManager instance = new ProgramManager();
	private static final ShaderManager shaders = ShaderManager.instance;

	private ProgramManager() {
		super(null, "");
	}

	public Program linkProgram(String futureId, String vertShaderId, String fragShaderId) {
		dispose(futureId);
		Shader vertShader = shaders.getShader(vertShaderId);
		Shader fragShader = shaders.getShader(fragShaderId);
		Program program = new Program();
		program.attachShader(vertShader);
		program.attachShader(fragShader);
		program.link();
		addResource(futureId, program);
		return program;
	}

	public void dispose(String programId) {
		if (hasResource(programId)) {
			getResource(programId).dispose();
			removeResource(programId);
		}
	}

	public Program getProgram(String programId) {
		return getResource(programId);
	}
}
