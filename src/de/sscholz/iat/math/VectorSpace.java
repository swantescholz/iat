package de.sscholz.iat.math;

public abstract class VectorSpace<T extends VectorSpace<T>> implements CanBeAlmostEqual<T> {

	public abstract T negate();

	public abstract T add(T b);

	public abstract T sub(T b);

	public abstract T mul(double b);

	public T div(double b) {
		return mul(1.0 / b);
	}

	public T interpolateLinear(T b, double t) {
		return mul(1.0 - t).add(b.mul(t));
	}

	public static <U extends VectorSpace<U>> U interpolateHermite(U valueA, U tangentA, U valueB, U tangentB, double t) {
		U a = valueA.sub(valueB).mul(2.0).add(tangentA).add(tangentB).mul(t * t * t);
		U b = valueB.sub(valueA).mul(3.0).sub(tangentA.mul(2.0)).sub(tangentB).mul(t * t);
		return a.add(b).add(tangentA.mul(t)).add(valueA);
	}

	public static <U extends VectorSpace<U>> U interpolateBilinear(U a, U b, U c, U d, double x, double y) {
		U p = a.interpolateLinear(b, x);
		U q = c.interpolateLinear(d, x);
		return p.interpolateLinear(q, y);
	}

	public T approachExponentially(T destination, double base, double elapsed) {
		return interpolateLinear(destination, 1.0 - Math.pow(base, elapsed));
	}

	public abstract boolean equal(T that);
}
