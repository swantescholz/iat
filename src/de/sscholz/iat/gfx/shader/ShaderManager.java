package de.sscholz.iat.gfx.shader;

import de.sscholz.iat.util.FileUtil;
import de.sscholz.iat.util.ResourceManager;

import java.io.File;

public class ShaderManager extends ResourceManager<Shader> {

	public static final ShaderManager instance = new ShaderManager(new File("res/shader"), "");

	private String headerBoth, headerVert, headerFrag;

	private ShaderManager(File defaultLocation, String defaultFilenameEnding) {
		super(defaultLocation, defaultFilenameEnding);
		reloadHeaders();
	}

	public void reloadHeaders() {
		headerBoth = FileUtil.readFile(getFile("header.txt"));
		headerVert = FileUtil.readFile(getFile("header.vert"));
		headerFrag = FileUtil.readFile(getFile("header.frag"));
	}

	public VertexShader loadVert(String simplePath) throws CompilationFailedException {
		return loadVert(simplePath, simplePath);
	}

	public FragmentShader loadFrag(String simplePath) throws CompilationFailedException {
		return loadFrag(simplePath, simplePath);
	}

	public VertexShader loadVert(String futureId, String simplePath) throws CompilationFailedException {
		VertexShader vertexShader = new VertexShader();
		String source = headerBoth + headerVert + readFile(simplePath + ".vert");
		loadShaderSource(futureId, vertexShader, source);
		return vertexShader;
	}

	public FragmentShader loadFrag(String futureId, String simplePath) throws CompilationFailedException {
		FragmentShader fragmentShader = new FragmentShader();
		String source = headerBoth + headerFrag + readFile(simplePath + ".frag");
		loadShaderSource(futureId, fragmentShader, source);
		return fragmentShader;
	}

	private void loadShaderSource(String futureId, Shader shader, String source) throws CompilationFailedException {
		shader.setSource(source);
		dispose(futureId);
		addResource(futureId, shader);
	}

	public void dispose(String shaderId) {
		if (hasResource(shaderId)) {
			getResource(shaderId).dispose();
			removeResource(shaderId);
		}
	}

	public Shader getShader(String shaderId) {
		return getResource(shaderId);
	}
}
