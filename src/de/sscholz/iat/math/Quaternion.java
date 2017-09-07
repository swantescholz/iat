package de.sscholz.iat.math;

public class Quaternion extends VectorSpace<Quaternion> {

	public static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);
	public static final Quaternion ONE = new Quaternion(1, 0, 0, 0);

	public final double w;
	public final double x;
	public final double y;
	public final double z;

	public Quaternion() {
		w = 0;
		x = 0;
		y = 0;
		z = 0;
	}

	public Quaternion(double w, double x, double y, double z) {
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Quaternion(double w, Vector3 v) {
		this(w, v.x, v.y, v.z);
	}

	@Override
	public Quaternion negate() {
		return new Quaternion(-w, -x, -y, -z);
	}

	public Quaternion add(Quaternion that) {
		return new Quaternion(w + that.w, x + that.x, y + that.y, z + that.z);
	}

	public Quaternion sub(Quaternion that) {
		return new Quaternion(w - that.w, x - that.x, y - that.y, z - that.z);
	}

	public Quaternion mul(double s) {
		return new Quaternion(w * s, x * s, y * s, z * s);
	}

	@Override
	public boolean equal(Quaternion that) {
		return w == that.w && x == that.x && y == that.y && z == that.z;
	}

	@Override
	public boolean almostEqual(Quaternion b) {
		return MathUtil.almostEqual(w, b.w) && MathUtil.almostEqual(x, b.x) &&
				MathUtil.almostEqual(y, b.y) && MathUtil.almostEqual(z, b.z);
	}

	public Quaternion mul(Quaternion q) {
		double a = w * q.w - x * q.x - y * q.y - z * q.z;
		double b = x * q.w + w * q.x + y * q.z - z * q.y;
		double c = y * q.w + w * q.y + z * q.x - x * q.z;
		double d = z * q.w + w * q.z + x * q.y - y * q.x;
		return new Quaternion(a, b, c, d);
	}

	public Quaternion conjugate() {
		return new Quaternion(w, -x, -y, -z);
	}

	public Quaternion normalize() {
		return div(length());
	}

	public double length() {
		return Math.sqrt(length2());
	}

	public double length2() {
		return w * w + x * x + y * y + z * z;
	}

	public Quaternion invert() {
		Quaternion inverted = conjugate();
		return inverted.div(inverted.length2());
	}

	public double dot(Quaternion q) {
		return w * q.w + x * q.x + y * q.y + z * q.z;
	}

	public double getAngle() {
		return 2.0 * Math.acos(w);
	}

	public Vector3 getAxis() {
		return new Vector3(x, y, z).normalize();
	}

	public Vector3 getVector() {
		return new Vector3(x, y, z);
	}

	public static Quaternion axisAngle(Vector3 axis, double angle) {
		return new Quaternion(Math.cos(-angle * 0.5),
				axis.normalize().mul(Math.sin(-angle * 0.5)));
	}

	public Vector3 transform(Vector3 v) {
		Quaternion vpure = new Quaternion(0, v);
		Quaternion result = mul(vpure).mul(invert());
		return result.getVector();
	}

	public Quaternion lerp(Quaternion q, double t) {
		return mul(1.0 - t).add(q.mul(t));
	}

	public Quaternion nlerp(Quaternion q, double t) {
		return lerp(q, t).normalize();
	}

	public Quaternion slerp(Quaternion q, double t) {
		final double alpha = Math.acos(dot(q));
		Quaternion tmp = q.mul(Math.sin(t * alpha));
		return mul(Math.sin((1.0 - t) * alpha)).add(tmp).div(Math.sin(alpha));
	}

	public double[] data() {
		return new double[]{x, y, z};
	}

	public String toString() {
		return "(" + w + ", " + x + ", " + y + ", " + z + ")";
	}

	public Matrix toMatrix() {
		double xx2 = 2 * x * x, yy2 = 2 * y * y, zz2 = 2 * z * z;
		double wx2 = 2 * w * x, wy2 = 2 * w * y, wz2 = 2 * w * z;
		double xy2 = 2 * x * y, xz2 = 2 * x * z, yz2 = 2 * y * z;
		double[] a = new double[16];
		a[15] = 1;
		a[3] = a[7] = a[11] = a[12] = a[13] = a[14] = 0;
		a[0] = 1 - yy2 - zz2;
		a[5] = 1 - xx2 - zz2;
		a[10] = 1 - xx2 - yy2;
		a[1] = xy2 - wz2;
		a[4] = xy2 + wz2;
		a[8] = xz2 - wy2;
		a[2] = xz2 + wy2;
		a[6] = yz2 - wx2;
		a[9] = yz2 + wx2;
		return new Matrix(a);
	}

}
