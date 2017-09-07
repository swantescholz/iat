package de.sscholz.iat.math;

public class CollisionUtil {

	private static final double LINE_MIN_FACTOR = 0.0;
	private static final double LINE_MAX_FACTOR = 1.0;

	private CollisionUtil() {
	}

	public static Vector2 intersectLines(Vector2 startA, Vector2 endA, Vector2 startB, Vector2 endB) {
		Vector2 dira = endA.sub(startA);
		Vector2 dirb = endB.sub(startB);
		if (dira.isParallelTo(dirb)) {
			return null;
		}
		double num = dira.x * (startB.y - startA.y) + dira.y * (startA.x - startB.x);
		double den = dira.y * dirb.x - dira.x * dirb.y;
		double t = num / den;
		double s;
		if (dira.x == 0) {
			s = (startB.y - startA.y + t * dirb.y) / dira.y;
		} else {
			s = (startB.x - startA.x + t * dirb.x) / dira.x;
		}

		if (factorOutsideLineBounds(t) || factorOutsideLineBounds(s)) {
			return null;
		}
		return dirb.mul(t).add(startB);
	}

	public static Vector2 getClosestPointOnOtherLine(Vector2 lineStart, Vector2 lineEnd, Vector2 otherLineStart, Vector2 otherLineEnd) {
		Vector2 intersection = intersectLines(lineStart, lineEnd, otherLineStart, otherLineEnd);
		if (intersection != null) {
			return intersection;
		}
		if (otherLineStart.distanceToFiniteLine(lineStart, lineEnd) < otherLineEnd.distanceToFiniteLine(lineStart, lineEnd)) {
			return otherLineStart;
		}
		return otherLineEnd;
	}

	public static Vector2 getFirstLineCircleIntersection(Vector2 lineStart, Vector2 lineEnd, Vector2 circleCenter, double radius) {
		Vector2 d = lineEnd.sub(lineStart);
		Vector2 f = lineStart.sub(circleCenter);
		double a = d.dot(d);
		double b = 2 * f.dot(d);
		double c = f.dot(f) - radius * radius;

		double discriminant = b * b - 4 * a * c;
		if (discriminant < 0) {
			return null;
		}
		discriminant = Math.sqrt(discriminant);
		double t1 = (-b - discriminant) / (2 * a);
		double t2 = (-b + discriminant) / (2 * a);
		if (t1 >= 0 && t1 <= 1) {
			return lineStart.add(d.mul(t1));
		}
		if (t2 >= 0 && t2 <= 1) {
			return lineStart.add(d.mul(t2));
		}
		return null;
	}

	private static boolean factorOutsideLineBounds(double factor) {
		return factor < LINE_MIN_FACTOR || factor > LINE_MAX_FACTOR;
	}

	private static Vector2 getBallOffsetForLine(Vector2 lineStart, Vector2 lineEnd, Vector2 ballCenter, double ballRadius) {
		Vector2 lineNormal = lineEnd.sub(lineStart).rotate90().normalize().mul(ballRadius);
		if (lineNormal.dot(ballCenter) > 0) {
			lineNormal.negate();
		}
		return lineNormal;
	}

	public static Vector2 ballIntersectsLine(Vector2 ballStart, Vector2 ballEnd, double ballRadius,
	                                         Vector2 lineStart, Vector2 lineEnd) {
		Vector2 offset = getBallOffsetForLine(lineStart, lineEnd, ballStart, ballRadius);
		Vector2 startBallShifted = ballStart.add(offset);
		Vector2 endBallShifted = ballEnd.add(offset);
		Vector2 closestPoint = getClosestPointOnOtherLine(startBallShifted, endBallShifted, lineStart, lineEnd);
		if (closestPoint == null) {
			return null;
		} else if (closestPoint == lineStart) {
			return getFirstLineCircleIntersection(ballStart, ballEnd, lineStart, ballRadius);
		} else if (closestPoint == lineEnd) {
			return getFirstLineCircleIntersection(ballStart, ballEnd, lineEnd, ballRadius);
		}
		return closestPoint.sub(offset);
	}

	public static boolean isPointInTriangle(Vector2 p, Vector2 a, Vector2 b, Vector2 c) {
		if (p.sub(a).isRightOf(b.sub(a)))
			return false;
		if (p.sub(b).isRightOf(c.sub(b)))
			return false;
		return !p.sub(c).isRightOf(a.sub(c));
	}
}
