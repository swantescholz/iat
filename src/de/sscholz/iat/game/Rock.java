package de.sscholz.iat.game;

import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.util.ClosestPointComputer;

import java.util.List;
import java.util.function.BiConsumer;

public class Rock extends LevelElement {

	private List<Vector2> vertices;
	private ConcaveShape concaveShape;

	public Rock(List<Vector2> vertices) {
		if (vertices.size() < 3) {
			throw new IllegalArgumentException("Rocks must be larger than 2 vertices: " + vertices.size());
		}
		this.vertices = vertices;
		concaveShape = new ConcaveShape(vertices);
	}

	public void forEachLine(BiConsumer<Vector2, Vector2> consumer) {
		Vector2 last = null;
		for (Vector2 current : vertices) {
			if (last != null) {
				consumer.accept(last, current);
			}
			last = current;
		}
		consumer.accept(vertices.get(vertices.size() - 1), vertices.get(0));
	}

	public Vector2 intersectsWithBall() {
		ClosestPointComputer closestIntersectionComputer = new ClosestPointComputer(info.ball.getLastPosition());
		forEachLine((lineStart, lineEnd) -> {
			Vector2 intersectionPoint = info.ball.collidedWhereWithLine(lineStart, lineEnd);
			closestIntersectionComputer.update(intersectionPoint);
		});
		return closestIntersectionComputer.getClosestPoint();
	}

	@Override
	protected void update() {

	}

	@Override
	public void render() {
		Material.FOREST.use();
		concaveShape.render();
	}

	public static Rock create(double... coords) {
		return new Rock(MathUtil.createVector2List(coords));
	}
}
