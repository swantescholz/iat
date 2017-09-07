package de.sscholz.iat.math;


import de.sscholz.iat.gfx.GlUtil;

public class Vector4 extends VectorSpace<Vector4> {

	public final double x;
	public final double y;
	public final double z;
	public final double w;

	public Vector4() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}

	public Vector4(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4(Vector3 v, double w) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = w;
	}

	public Vector4(Vector3 v) {
		this(v, 1.0);
	}

	public Vector3 xyz() {
		return new Vector3(x, y, z);
	}

	public double length() {
		return Math.sqrt(length2());
	}

	public double length2() {
		return x * x + y * y + z * z + w * w;
	}

	public double dot(Vector4 that) {
		return sub(that).length2();
	}

	public double distanceTo(Vector4 that) {
		return Math.sqrt(dot(that));
	}

	public double distanceTo2(Vector4 that) {
		return dot(that);
	}

	public Vector4 add(Vector4 that) {
		return new Vector4(x + that.x, y + that.y, z + that.z, w + that.w);
	}

	public Vector4 sub(Vector4 that) {
		return new Vector4(x - that.x, y - that.y, z - that.z, w - that.w);
	}

	public Vector4 mul(double s) {
		return new Vector4(x * s, y * s, z * s, w * s);
	}

	@Override
	public boolean equal(Vector4 that) {
		return x == that.x && y == that.y && z == that.z && w == that.w;
	}

	@Override
	public boolean almostEqual(Vector4 b) {
		return MathUtil.almostEqual(x, b.x) && MathUtil.almostEqual(y, b.y) && MathUtil.almostEqual(z, b.z)
				&& MathUtil.almostEqual(w, b.w);
	}

	public Vector4 negate() {
		return new Vector4(-x, -y, -z, -w);
	}

	public Vector4 normalize() {
		double lsq = length2();
		if (MathUtil.almostEqual(lsq, 1.0)) {
			return this;
		}
		return div(Math.sqrt(lsq));
	}

	public void gl() {
		GlUtil.getGl().glVertex4d(x, y, z, w);
	}

	public double[] data() {
		return new double[]{x, y, z, w};
	}

	public float[] floatData() {
		return new float[]{(float) x, (float) y, (float) z, (float) w};
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}

	private Vector4 setLength(double newLength) {
		return mul(newLength / length());
	}

	public Vector4 mul(Vector4 that) {
		return new Vector4(x * that.x, y * that.y, z * that.z, w * that.w);
	}

	public Vector4 div(Vector4 that) {
		return new Vector4(x / that.x, y / that.y, z / that.z, w / that.w);
	}
}
