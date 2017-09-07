package de.sscholz.iat.gfx.model;

import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Triangle;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.Array2;
import de.sscholz.iat.util.expression.Expression;
import de.sscholz.iat.util.expression.Parser;
import de.sscholz.iat.util.expression.SetVariable;
import de.sscholz.iat.util.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class Grid implements Logger {

	public void renderControlNodes(RenderableObject object) {
		Matrix oldTransformation = object.getTransformation();
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Vector3 p = getNode(x, y).position;
				object.setTransformation(oldTransformation);
				object.transform(Matrix.translation(p));
				object.render();
			}
		}
		object.setTransformation(oldTransformation);
	}

	public class Node {
		public Vector3 position = new Vector3();
		public Vector3 normal = new Vector3();
	}

	private final Array2<Node> data;
	private final int width;
	private final int height;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		data = new Array2<>(width, height);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				data.set(x, y, new Node());
			}
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void arrangePlane(Vector3 upperLeft, Vector3 upperRight, Vector3 lowerLeft) {
		Vector3 lowerRight = lowerLeft.add(upperRight).sub(upperLeft);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Node node = data.get(x, y);
				Vector2 fxy = getFactors(x, y);
				node.position = Vector3.interpolateBilinear(upperLeft, upperRight, lowerLeft, lowerRight,
						fxy.x, fxy.y);
			}
		}
	}

	public void randomizeAxis(Vector3 axis, double minAmount, double maxAmount) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Node node = getNode(x, y);
				node.position = node.position.add(axis.mul(MathUtil.randomDouble(minAmount, maxAmount)));
			}
		}
	}

	private Vector2 getFactors(int x, int y) {
		return new Vector2(((double) x) / (width - 1), ((double) y) / (height - 1));
	}

	public List<Triangle> createTriangles(boolean smoothNormals) {
		computeSmoothNormals();
		List<Triangle> triangles = new ArrayList<>();
		for (int y = 0; y < height - 1; y++) {
			addTrianglesOfRow(triangles, y, smoothNormals);
		}
		return triangles;
	}

	private void computeSmoothNormals() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Node node = getNode(x, y);
				Vector3 up = null, dn = null, rt = null, lt = null;
				if (y > 0)
					up = getNode(x, y - 1).position.sub(node.position);
				if (x + 1 < width)
					rt = getNode(x + 1, y).position.sub(node.position);
				if (y + 1 < height)
					dn = getNode(x, y + 1).position.sub(node.position);
				if (x > 0)
					lt = getNode(x - 1, y).position.sub(node.position);
				node.normal = Vector3.ZERO;
				if (up != null) {
					if (rt != null) node.normal = node.normal.add(rt.cross(up));
					if (lt != null) node.normal = node.normal.add(up.cross(lt));
				}
				if (dn != null) {
					if (rt != null) node.normal = node.normal.add(dn.cross(rt));
					if (lt != null) node.normal = node.normal.add(lt.cross(dn));
				}
				node.normal = node.normal.normalize();
			}
		}

	}


	public Node getNode(int x, int y) {
		return data.get(x, y);
	}

	private void addTrianglesOfRow(List<Triangle> triangles, int y, boolean smoothNormals) {
		for (int x = 0; x < width - 1; x++) {
			addTriangle(triangles, y, x, y + 1, x, y, x + 1, smoothNormals);
			addTriangle(triangles, y + 1, x, y + 1, x + 1, y, x + 1, smoothNormals);
		}
	}

	private void addTriangle(List<Triangle> triangles, int ya, int xa,
	                         int yb, int xb,
	                         int yc, int xc, boolean smoothNormals) {
		Triangle triangle = new Triangle();
		triangle.a.position = getNode(xa, ya).position;
		triangle.b.position = getNode(xb, yb).position;
		triangle.c.position = getNode(xc, yc).position;
		if (smoothNormals) {
			triangle.a.normal = getNode(xa, ya).normal;
			triangle.b.normal = getNode(xb, yb).normal;
			triangle.c.normal = getNode(xc, yc).normal;
		} else {
			triangle.setNormalsByPlane();
		}
		triangles.add(triangle);
	}

	public void applyYExpression(String string) {
		try {
			Expression expr = new Parser().parse(string);

			for (Node node : data) {
				Vector3 p = node.position;
				expr.accept(new SetVariable("x", p.x));
				expr.accept(new SetVariable("z", p.z));
				double y = expr.evaluate();
				node.position = new Vector3(p.x, y, p.z);
			}

		} catch (Exception e) {
			log.warn(e);
		}

	}
}
