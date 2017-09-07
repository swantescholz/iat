package de.sscholz.iat.math;

public class Vertex {

	public Vector3 position = new Vector3();
	public Vector3 normal = new Vector3(0.0, 1.0, 0.0);
	public Vector2 texCoord;

	public Vertex(Vector3 position, Vector3 normal) {
		this.position = position;
		this.normal = normal;
	}

	public Vertex(Vector3 position, Vector3 normal, Vector2 texCoord) {
		this(position, normal);
		this.texCoord = texCoord;
	}

	public Vertex() {
	}

	public boolean hasTexCoord() {
		return texCoord != null;
	}
}
