package de.sscholz.iat.math;

public class Plane extends VectorSpace<Plane> {

	public static final Plane X = new Plane(1, 0, 0, 0);
	public static final Plane Y = new Plane(0, 1, 0, 0);
	public static final Plane Z = new Plane(0, 0, 1, 0);
	public static final Plane XY = new Plane(1, 1, 0, 0);
	public static final Plane XZ = new Plane(1, 0, 1, 0);
	public static final Plane YZ = new Plane(0, 1, 1, 0);
	public static final Plane XYZ = new Plane(1, 1, 1, 0);

	public final Vector3 n;
	public final double d;


	public Plane() {
		n = Vector3.Y;
		d = 0.0;
	}

	public Plane(double x, double y, double z, double distance) {
		this(new Vector3(x, y, z), distance);
	}

	public Plane(Vector3 normal, double distance) {
		this.n = normal;
		this.d = distance;
	}

	@Override
	public Plane negate() {
		return new Plane(n.negate(), -d);
	}

	@Override
	public Plane add(Plane b) {
		return new Plane(n.add(b.n), d + b.d);
	}

	@Override
	public Plane sub(Plane b) {
		return new Plane(n.sub(b.n), d - b.d);
	}

	@Override
	public Plane mul(double s) {
		return new Plane(n.mul(s), d * s);
	}

	@Override
	public boolean equal(Plane that) {
		return n.equal(that.n) && d == that.d;
	}

	@Override
	public boolean almostEqual(Plane b) {
		return n.almostEqual(b.n) && MathUtil.almostEqual(d, b.d);
	}

	public Plane normalize() {
		double lsq = n.length2();
		if (MathUtil.almostEqual(lsq, 1.0)) {
			return this;
		}
		return div(Math.sqrt(lsq));
	}

	public double dot(Vector3 b) {
		return n.dot(b) + d;
	}

	public double dotNormal(Vector3 b) {
		return n.dot(b);
	}

	public double distance(Vector3 point) {
		return dot(point);
	}

	public Vector3 nearestPoint(Vector3 point) {
		return n.mul(dot(point)).negate().add(point);
	}

	public static Plane fromPoints(Vector3 a, Vector3 b, Vector3 c) {
		Vector3 n = b.sub(a).cross(c.sub(a)).normalize();
		double d = n.dot(a);
		return new Plane(n, d);
	}
}
