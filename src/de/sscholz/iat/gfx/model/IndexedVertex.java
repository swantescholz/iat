package de.sscholz.iat.gfx.model;

public class IndexedVertex {
	public int positionIndex = -1;
	public int texCoordIndex = -1;
	public int normalIndex = -1;

	public boolean hasTexCoords() {
		return texCoordIndex >= 0;
	}
}
