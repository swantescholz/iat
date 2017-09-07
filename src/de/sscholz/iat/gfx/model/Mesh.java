package de.sscholz.iat.gfx.model;

import de.sscholz.iat.math.Triangle;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.NotImplementedException;
import de.sscholz.iat.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mesh implements Renderable {

	public final ArrayList<Vector3> positions = new ArrayList<>();
	public final ArrayList<Vector3> normals = new ArrayList<>();
	public final ArrayList<Vector2> texCoords = new ArrayList<>();
	public final List<IndexedTriangle> indexedTriangles = new ArrayList<>();

	private final Vbo vbo = new Vbo();

	public Mesh() {

	}

	public void createVbo() {
		List<Triangle> triangles = createTriangles();
		vbo.create(triangles);
	}

	public List<Triangle> createTriangles() {
		List<Triangle> triangles = new ArrayList<>();
		for (IndexedTriangle indexedTriangle : indexedTriangles) {
			Triangle triangle = indexedTriangle.createTriangle(positions, normals, texCoords);
			triangles.add(triangle);
		}
		return triangles;
	}

	public void render() {
		vbo.render();
	}

	public static Mesh createFromObj(String obj) {
		Mesh mesh = new Mesh();
		ArrayList<String> lines = new ArrayList<>(Arrays.asList(obj.split("\n")));
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			line = line.trim();
			lines.set(i, line);
		}
		for (String line : lines) {
			if (line.startsWith("#"))
				continue;
			mesh.readObjLine(line);
		}
		return mesh;
	}

	private void readObjLine(String line) {
		if (line.startsWith("vn")) {
			readObjNormal(line);
		} else if (line.startsWith("vt")) {
			readObjTexCoord(line);
		} else if (line.startsWith("v")) {
			readObjPosition(line);
		} else if (line.startsWith("f")) {
			readObjFace(line);
		}
	}

	private void readObjFace(String line) {
		String[] tokens = line.split(" ");
		if (line.contains("//"))
			readObjFacePositionNormal(tokens);
		else if (StringUtil.countSubstring(line, "/") / (tokens.length - 1) == 2)
			readObjFacePositionTextureNormal(tokens);
		else if (line.contains("/"))
			readObjFacePositionTexture(tokens);
		else
			readObjFacePosition(tokens);
	}

	private int readObjIndex(String str) {
		return Integer.parseInt(str) - 1;
	}

	private void readObjFacePosition(String[] tokens) {
		int index1 = readObjIndex(tokens[1]);
		for (int i = 2; i < tokens.length - 1; i++) {
			int index2 = readObjIndex(tokens[i]);
			int index3 = readObjIndex(tokens[i + 1]);
			addTriangleWithPlaneNormal(index1, index2, index3);
		}
	}

	private void readObjFacePositionTexture(String[] tokens) {
		throw new NotImplementedException();
	}

	private void readObjFacePositionTextureNormal(String[] tokens) {
		String[] subtokens1 = tokens[1].split("/");
		int index1 = readObjIndex(subtokens1[0]);
		int texCoord1 = readObjIndex(subtokens1[1]);
		int normal1 = readObjIndex(subtokens1[2]);
		for (int i = 2; i < tokens.length - 1; i++) {
			String[] subtokens2 = tokens[i].split("/");
			String[] subtokens3 = tokens[i + 1].split("/");

			int index2 = readObjIndex(subtokens2[0]);
			int index3 = readObjIndex(subtokens3[0]);
			int texCoord2 = readObjIndex(subtokens2[1]);
			int texCoord3 = readObjIndex(subtokens3[1]);
			int normal2 = readObjIndex(subtokens2[2]);
			int normal3 = readObjIndex(subtokens3[2]);
			addTriangleWithTexCoordsAndNormals(index1, index2, index3,
					texCoord1, texCoord2, texCoord3,
					normal1, normal2, normal3);
		}
	}

	private void readObjFacePositionNormal(String[] tokens) {
		String[] subtokens1 = tokens[1].split("//");
		int index1 = readObjIndex(subtokens1[0]);
		int normal1 = readObjIndex(subtokens1[1]);
		for (int i = 2; i < tokens.length - 1; i++) {
			String[] subtokens2 = tokens[i].split("//");
			String[] subtokens3 = tokens[i + 1].split("//");

			int index2 = readObjIndex(subtokens2[0]);
			int index3 = readObjIndex(subtokens3[0]);
			int normal2 = readObjIndex(subtokens2[1]);
			int normal3 = readObjIndex(subtokens3[1]);
			addTriangleWithNormals(index1, index2, index3, normal1, normal2, normal3);
		}
	}

	private void addTriangleWithNormals(int index1, int index2, int index3,
	                                    int normal1, int normal2, int normal3) {
		IndexedTriangle indexedTriangle = new IndexedTriangle();
		indexedTriangle.setPositionIndices(index1, index2, index3);
		indexedTriangle.setNormalIndices(normal1, normal2, normal3);
		indexedTriangles.add(indexedTriangle);
	}

	private void addTriangleWithTexCoordsAndNormals(int index1, int index2, int index3,
	                                                int texCoord1, int texCoord2, int texCoord3,
	                                    int normal1, int normal2, int normal3) {
		IndexedTriangle indexedTriangle = new IndexedTriangle();
		indexedTriangle.setPositionIndices(index1, index2, index3);
		indexedTriangle.setTexCoordIndices(texCoord1, texCoord2, texCoord3);
		indexedTriangle.setNormalIndices(normal1, normal2, normal3);
		indexedTriangles.add(indexedTriangle);
	}

	private void readObjPosition(String line) {
		positions.add(readObjVector3(line));
	}

	private void readObjNormal(String line) {
		normals.add(readObjVector3(line));
	}

	private void readObjTexCoord(String line) {
		texCoords.add(readObjVector2(line));
	}

	private Vector2 readObjVector2(String line) {
		String[] tokens = line.split(" ");
		double x = Double.parseDouble(tokens[1]);
		double y = Double.parseDouble(tokens[2]);
		return new Vector2(x, y);
	}

	private Vector3 readObjVector3(String line) {
		String[] tokens = line.split(" ");
		double x = Double.parseDouble(tokens[1]);
		double y = Double.parseDouble(tokens[2]);
		double z = Double.parseDouble(tokens[3]);
		return new Vector3(x, y, z);
	}

	public static Mesh createFromOff(String off) {
		Mesh mesh = new Mesh();
		String[] lines = off.split("\n");
		String info = lines[1];
		String[] tokens = info.split(" ");
		int vertexCount = Integer.parseInt(tokens[0]);
		int faceCount = Integer.parseInt(tokens[1]);
		List<String> vertexLines = new ArrayList<>();
		List<String> faceLines = new ArrayList<>();
		vertexLines.addAll(Arrays.asList(lines).subList(2, vertexCount+2));
		for (int i = 0; i < faceCount; i++) {
			faceLines.add(lines[i + vertexCount + 2]);
		}

		for (String line : vertexLines) {
			String[] coords = line.split(" ");
			mesh.readOffVertex(coords);
		}
		for (String line : faceLines) {
			String[] indexTokens = line.split(" ");
			mesh.readOffFaceTriangles(indexTokens);
		}
		return mesh;
	}

	private void readOffFaceTriangles(String[] indexTokens) {
		int indexCount = Integer.parseInt(indexTokens[0]);
		int index1 = Integer.parseInt(indexTokens[1]);
		for (int i = 1; i + 1 < indexCount; i++) {
			int index2 = Integer.parseInt(indexTokens[i + 1]);
			int index3 = Integer.parseInt(indexTokens[i + 2]);

			addTriangleWithPlaneNormal(index1, index2, index3);
		}
	}

	private void addTriangleWithPlaneNormal(int index1, int index2, int index3) {
		Vector3 a = positions.get(index1);
		Vector3 b = positions.get(index2);
		Vector3 c = positions.get(index3);
		Vector3 normal = Triangle.normal(a, b, c).negate();
		normals.add(normal);
		int normalIndex = normals.size() - 1;
		IndexedTriangle indexedTriangle = new IndexedTriangle();
		indexedTriangle.setPositionIndices(index1, index2, index3);
		indexedTriangle.setEveryNormalIndex(normalIndex);
		indexedTriangles.add(indexedTriangle);
	}

	private void readOffVertex(String[] coords) {
		double x = Double.parseDouble(coords[0]);
		double y = Double.parseDouble(coords[1]);
		double z = Double.parseDouble(coords[2]);
		positions.add(new Vector3(x, y, z));
	}

}
