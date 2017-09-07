package de.sscholz.iat.gfx.model;


public class Model extends RenderableObject {

	private Mesh mesh;

	public Model(Mesh mesh) {
		this.mesh = mesh;
	}

	protected void draw() {
		mesh.render();
	}

	public Model copy() {
		Model newModel = new Model(mesh);
		newModel.setTransformation(transformation);
		return newModel;
	}

}
