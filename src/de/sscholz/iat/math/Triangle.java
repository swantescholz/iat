package de.sscholz.iat.math;

public class Triangle {

	public final Vertex a = new Vertex();
	public final Vertex b = new Vertex();
	public final Vertex c = new Vertex();

	public Triangle() {

	}

	public Triangle(Vector3 a, Vector3 b, Vector3 c) {
		this.a.position = a;
		this.b.position = b;
		this.c.position = c;
		setNormalsByPlane();
	}

	public void setNormals(Vector3 normal) {
		a.normal = normal;
		b.normal = normal;
		c.normal = normal;
	}

	public static Vector3 normal(Vector3 a, Vector3 b, Vector3 c) {
		return b.sub(a).cross(c.sub(a)).normalize();
	}

	public Vector3 getPlaneNormal() {
		return normal(a.position, b.position, c.position);
	}

	public void setNormalsByPlane() {
		setNormals(getPlaneNormal());
	}

	public boolean hasTexCoords() {
		return a.hasTexCoord() && b.hasTexCoord() && c.hasTexCoord();
	}
}
