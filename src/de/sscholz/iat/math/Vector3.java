package de.sscholz.iat.math;


import de.sscholz.iat.gfx.GlUtil;

public class Vector3 extends VectorSpace<Vector3> {

	public static final Vector3 ZERO = new Vector3(0, 0, 0);
	public static final Vector3 X = new Vector3(1, 0, 0);
	public static final Vector3 Y = new Vector3(0, 1, 0);
	public static final Vector3 Z = new Vector3(0, 0, 1);
	public static final Vector3 XY = new Vector3(1, 1, 0);
	public static final Vector3 XZ = new Vector3(1, 0, 1);
	public static final Vector3 YZ = new Vector3(0, 1, 1);
	public static final Vector3 XYZ = new Vector3(1, 1, 1);
	public static final Vector3 NX = new Vector3(-1, 0, 0);
	public static final Vector3 NY = new Vector3(0, -1, 0);
	public static final Vector3 NZ = new Vector3(0, 0, -1);

	public final double x;
	public final double y;
	public final double z;

	public Vector3() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(Vector2 xy, double z) {
		this.x = xy.x;
		this.y = xy.y;
		this.z = z;
	}

	public Vector2 xy() {
		return new Vector2(x, y);
	}

	public Vector2 xz() {
		return new Vector2(x, z);
	}

	public Vector2 yz() {
		return new Vector2(y, z);
	}

	public double length() {
		return Math.sqrt(length2());
	}

	public double length2() {
		return x * x + y * y + z * z;
	}

	public double dot(Vector3 that) {
		return sub(that).length2();
	}

	public double distanceTo(Vector3 that) {
		return Math.sqrt(dot(that));
	}

	public double distanceTo2(Vector3 that) {
		return dot(that);
	}

	public Vector3 add(Vector3 that) {
		return new Vector3(x + that.x, y + that.y, z + that.z);
	}

	public Vector3 sub(Vector3 that) {
		return new Vector3(x - that.x, y - that.y, z - that.z);
	}

	public Vector3 mul(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	@Override
	public boolean equal(Vector3 that) {
		return x == that.x && y == that.y && z == that.z;
	}

	@Override
	public boolean almostEqual(Vector3 b) {
		return MathUtil.almostEqual(x, b.x) && MathUtil.almostEqual(y, b.y) && MathUtil.almostEqual(z, b.z);
	}

	public Vector3 negate() {
		return new Vector3(-x, -y, -z);
	}

	public Vector3 normalize() {
		double lsq = length2();
		if (MathUtil.almostEqual(lsq, 1.0)) {
			return this;
		}
		return div(Math.sqrt(lsq));
	}

	public double angle(Vector3 that) {
		return Math.acos(dot(that) /
				Math.sqrt(this.length2() * that.length2()));
	}

	public Vector3 cross(Vector3 v) {
		double a = y * v.z - z * v.y;
		double b = z * v.x - x * v.z;
		double c = x * v.y - y * v.x;
		return new Vector3(a, b, c);
	}

	public void gl() {
		GlUtil.getGl().glVertex3d(x, y, z);
	}

	public void glNormal() {
		GlUtil.getGl().glNormal3d(x, y, z);
	}

	public double[] data() {
		return new double[]{x, y, z};
	}

	public float[] floatData() {
		return new float[]{(float) x, (float) y, (float) z};
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public Vector3 makePerpendicularTo(Vector3 other) {
		double oldLength = length();
		return other.cross(this).cross(other).setLength(oldLength);
	}

	private Vector3 setLength(double newLength) {
		return mul(newLength / length());
	}

	public Vector3 mul(Vector3 that) {
		return new Vector3(x * that.x, y * that.y, z * that.z);
	}

	public Vector3 div(Vector3 that) {
		return new Vector3(x / that.x, y / that.y, z / that.z);
	}
}
