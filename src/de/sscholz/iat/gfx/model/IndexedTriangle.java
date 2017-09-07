package de.sscholz.iat.gfx.model;

import de.sscholz.iat.math.Triangle;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;

import java.util.ArrayList;

class IndexedTriangle {
	public IndexedVertex a = new IndexedVertex();
	public IndexedVertex b = new IndexedVertex();
	public IndexedVertex c = new IndexedVertex();

	public Triangle createTriangle(ArrayList<Vector3> positions, ArrayList<Vector3> normals, ArrayList<Vector2> texCoords) {
		Triangle triangle = new Triangle();
		triangle.a.position = positions.get(a.positionIndex);
		triangle.b.position = positions.get(b.positionIndex);
		triangle.c.position = positions.get(c.positionIndex);
		triangle.a.normal = normals.get(a.normalIndex);
		triangle.b.normal = normals.get(b.normalIndex);
		triangle.c.normal = normals.get(c.normalIndex);
		if (a.hasTexCoords() && b.hasTexCoords() && c.hasTexCoords()) {
			triangle.a.texCoord = texCoords.get(a.texCoordIndex);
			triangle.b.texCoord = texCoords.get(b.texCoordIndex);
			triangle.c.texCoord = texCoords.get(c.texCoordIndex);
		}
		return triangle;
	}

	public Vector3 computeNormal(ArrayList<Vector3> positions) {
		Vector3 va = positions.get(a.positionIndex);
		Vector3 vb = positions.get(b.positionIndex);
		Vector3 vc = positions.get(c.positionIndex);
		return Triangle.normal(va, vb, vc);
	}

	public void setEveryNormalIndex(int normalIndex) {
		a.normalIndex = normalIndex;
		b.normalIndex = normalIndex;
		c.normalIndex = normalIndex;
	}

	public void setPositionIndices(int index1, int index2, int index3) {
		a.positionIndex = index1;
		b.positionIndex = index2;
		c.positionIndex = index3;
	}

	public void setNormalIndices(int normal1, int normal2, int normal3) {
		a.normalIndex = normal1;
		b.normalIndex = normal2;
		c.normalIndex = normal3;
	}

	public void setTexCoordIndices(int texCoord1, int texCoord2, int texCoord3) {
		a.texCoordIndex = texCoord1;
		b.texCoordIndex = texCoord2;
		c.texCoordIndex = texCoord3;
	}
}
