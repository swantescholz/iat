package de.sscholz.iat.game;

import de.sscholz.iat.gfx.model.Renderable;
import de.sscholz.iat.gfx.model.Vbo;
import de.sscholz.iat.math.CollisionUtil;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Triangle;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.log.Asserter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//expects shapes to be in Counter-Clockwise order
public class ConcaveShape implements Renderable, Asserter {

	private static final Vector3 NORMAL = Vector3.Z;

	private final Vbo vbo = new Vbo();

	public ConcaveShape(List<Vector2> vertices) {
		vbo.create(tesselateShapeIntoTriangles(vertices));
	}

	public static List<Triangle> tesselateShapeIntoTriangles(List<Vector2> vertices) {
		List<Triangle> triangles = new ArrayList<>();
		LinkedList<Vector2> leftVertices = new LinkedList<>(vertices);
		while (leftVertices.size() >= 3) {
			triangles.add(findNextNoseTriangle(leftVertices));
		}
		return triangles;
	}

	private static Triangle findNextNoseTriangle(LinkedList<Vector2> leftVertices) {
		new Asserter(){}.assertFalse(MathUtil.isPolygonDegenerate(leftVertices));
		new Asserter(){}.assertFalse(MathUtil.isPolygonClockwise(leftVertices));

		Iterator<Vector2> iterator = leftVertices.iterator();
		Vector2 a = iterator.next();
		Vector2 b = iterator.next();
		Vector2 c;
		while (true) {
			if (!iterator.hasNext()) {
				throw new IllegalArgumentException("Bad Shape, no noses found!");
			}
			c = iterator.next();
			if (c.sub(b).isRightOf(a.sub(b))) { //left angle
				if (isNoOtherPointInTriangle(leftVertices, a, b, c)) {
					leftVertices.remove(b);
					return createTriangle(a, b, c);
				}
			}
			a = b;
			b = c;
		}
	}

	private static boolean isNoOtherPointInTriangle(List<Vector2> vertices, Vector2 a, Vector2 b, Vector2 c) {
		for (Vector2 p : vertices) {
			if (p != a && p != b && p != c) {
				if (CollisionUtil.isPointInTriangle(p, a, b, c)) {
					return false;
				}
			}
		}
		return true;
	}

	private static Triangle createTriangle(Vector2 a, Vector2 b, Vector2 c) {
		Triangle triangle = new Triangle(a.to3(), b.to3(), c.to3());
		triangle.setNormals(NORMAL);
		triangle.a.texCoord = a;
		triangle.b.texCoord = b;
		triangle.c.texCoord = c;
		return triangle;
	}

	@Override
	public void render() {
		vbo.render();
	}
}
