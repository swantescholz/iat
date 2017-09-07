package de.sscholz.iat.gfx.model;

import de.sscholz.iat.util.FileUtil;
import de.sscholz.iat.util.ResourceManager;

import java.io.File;
import java.util.Arrays;

public final class ModelManager extends ResourceManager<Mesh> {

	public static final ModelManager instance = new ModelManager(new File("res/model"), "");

	private ModelManager(File defaultLocation, String defaultFilenameEnding) {
		super(defaultLocation, defaultFilenameEnding);
	}

	public Model get(String name) {
		for (String ending : Arrays.asList("", ".obj", ".off")) {
			Mesh mesh = getResource(name + ending);
			if (mesh != null) {
				return new Model(mesh);
			}
		}
		return null;
	}

	public void loadOff(String filename) {
		Mesh mesh = Mesh.createFromOff(FileUtil.readFile(getFile(filename)));
		addMesh(filename, mesh);
	}

	public void loadObj(String filename) {
		Mesh mesh = Mesh.createFromObj(FileUtil.readFile(getFile(filename)));
		addMesh(filename, mesh);
	}

	private void addMesh(String filename, Mesh mesh) {
		mesh.createVbo();
		addResource(filename, mesh);
	}

}
