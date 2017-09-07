package de.sscholz.iat.math;

import de.sscholz.iat.gfx.GlUtil;

public class Vector2 extends VectorSpace<Vector2> {

	public static final Vector2 X = new Vector2(1, 0);
	public static final Vector2 Y = new Vector2(0, 1);
	public static final Vector2 XY = new Vector2(1, 1);
	public static final Vector2 NX = new Vector2(-1, 0);
	public static final Vector2 NY = new Vector2(0, -1);
	public static final Vector2 NXY = new Vector2(-1, -1);
	public static final Vector2 ZERO = new Vector2(0, 0);

	public final double x;
	public final double y;

	public Vector2() {
		x = 0.0;
		y = 0.0;
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(double xAndY) {
		this(xAndY, xAndY);
	}

	public double length() {
		return Math.sqrt(x * x + y * y);
	}

	public double length2() {
		return x * x + y * y;
	}

	public double distanceTo(Vector2 that) {
		return Math.sqrt(distanceTo2(that));
	}

	public double distanceTo2(Vector2 that) {
		double dx = that.x - this.x;
		double dy = that.y - this.y;
		return dx * dx + dy * dy;
	}

	public Vector2 add(Vector2 that) {
		return new Vector2(x + that.x, y + that.y);
	}

	public Vector2 sub(Vector2 that) {
		return new Vector2(x - that.x, y - that.y);
	}

	public Vector2 mul(double s) {
		return new Vector2(x * s, y * s);
	}

	@Override
	public boolean equal(Vector2 that) {
		return x == that.x && y == that.y;
	}

	@Override
	public boolean almostEqual(Vector2 b) {
		return MathUtil.almostEqual(x, b.x) && MathUtil.almostEqual(y, b.y);
	}

	public Vector2 negate() {
		return new Vector2(-x, -y);
	}

	public Vector2 normalize() {
		double lsq = length2();
		if (MathUtil.almostEqual(lsq, 1.0)) {
			return this;
		}
		return div(Math.sqrt(lsq));
	}

	public void gl() {
		GlUtil.getGl().glVertex2d(x, y);
	}

	public void glTex() {
		GlUtil.getGl().glTexCoord2d(x, y);
	}

	public double[] data() {
		return new double[]{x, y};
	}

	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public double dot(Vector2 other) {
		return x * other.x + y * other.y;
	}

	public double cross(Vector2 other) {
		return x * other.y - y * other.x;
	}

	public boolean isParallelTo(Vector2 other) {
		return cross(other) == 0.0;
	}

	public Vector2 rotate90() {
		return new Vector2(-y, x);
	}

	public boolean isRightOf(Vector2 other) {
		return other.rotate90().dot(this) < 0.0;
	}

	public Vector2 negateY() {
		return new Vector2(x, -y);
	}

	public Vector2 setLength(double newLength) {
		return normalize().mul(newLength);
	}

	public double distanceToInfiniteLine(Vector2 lineStart, Vector2 lineEnd) {
		Vector2 normal = lineEnd.sub(lineStart).rotate90().normalize();
		return Math.abs(normal.dot(sub(lineStart)));
	}

	public double distanceToFiniteLine(Vector2 lineStart, Vector2 lineEnd) {
		Vector2 d = lineEnd.sub(lineStart);
		if (d.dot(sub(lineStart)) < 0) {
			return distanceTo(lineStart);
		}
		if (d.negate().dot(sub(lineEnd)) < 0) {
			return distanceTo(lineEnd);
		}
		return distanceToInfiniteLine(lineStart, lineEnd);
	}

	public Vector3 to3() {
		return to3(0.0);
	}

	public Vector3 to3(double z) {
		return new Vector3(x, y, z);
	}

	public Vector2 mul(Vector2 that) {
		return new Vector2(x * that.x, y * that.y);
	}

	public Vector2 div(Vector2 that) {
		return new Vector2(x / that.x, y / that.y);
	}
}
