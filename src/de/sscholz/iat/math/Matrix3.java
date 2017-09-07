package de.sscholz.iat.math;

import de.sscholz.iat.util.StringUtil;

public class Matrix3 extends VectorSpace<Matrix3> {

	public static Matrix3 identity() {
		return new Matrix3(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);
	}

	public static Matrix3 zero() {
		return new Matrix3(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	private static final int ROW_COUNT = 3;
	private static final int COLUMN_COUNT = 3;
	private static final int ELEMENT_COUNT = ROW_COUNT * COLUMN_COUNT;
	private final double[] m = new double[ELEMENT_COUNT];

	public Matrix3() {

	}

	public Matrix3(double[] m) {
		System.arraycopy(m, 0, this.m, 0, ELEMENT_COUNT);
	}

	public Matrix3(
			double m00, double m01, double m02,
			double m10, double m11, double m12,
			double m20, double m21, double m22) {
		m[0] = m00;
		m[1] = m01;
		m[2] = m02;
		m[3] = m10;
		m[4] = m11;
		m[5] = m12;
		m[6] = m20;
		m[7] = m21;
		m[8] = m22;
	}

	public Matrix3 copy() {
		Matrix3 n = new Matrix3();
		System.arraycopy(m, 0, n.m, 0, ELEMENT_COUNT);
		return n;
	}

	@Override
	public Matrix3 negate() {
		Matrix3 n = copy();
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			n.m[i] = -m[i];
		}
		return n;
	}


	private int getIndex(int row, int col) {
		return row * COLUMN_COUNT + col;
	}

	public double get(int row, int col) {
		return m[getIndex(row, col)];
	}

	// *********************************

	public Matrix3 add(Matrix3 that) {
		Matrix3 n = copy();
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			n.m[i] += that.m[i];
		}
		return n;
	}

	public Matrix3 sub(Matrix3 that) {
		Matrix3 n = copy();
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			n.m[i] -= that.m[i];
		}
		return n;
	}

	public Matrix3 mul(double s) {
		Matrix3 n = copy();
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			n.m[i] *= s;
		}
		return n;
	}

	@Override
	public boolean equal(Matrix3 n) {
		return m[0] == n.m[0] && m[1] == n.m[1] && m[2] == n.m[2] && m[3] == n.m[3] &&
				m[4] == n.m[4] && m[5] == n.m[5] && m[6] == n.m[6] && m[7] == n.m[7] &&
				m[8] == n.m[8];
	}

	@Override
	public boolean almostEqual(Matrix3 b) {
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			if (!MathUtil.almostEqual(m[i], b.m[i]))
				return false;
		}
		return true;
	}

	public Matrix3 mul(Matrix3 b) {
		double[] a = new double[ELEMENT_COUNT];
		for (int y = 0; y < ROW_COUNT; y++) {
			for (int x = 0; x < COLUMN_COUNT; x++) {
				double value = 0.0;
				for (int i = 0; i < ROW_COUNT; i++) {
					value += get(y, i) * b.get(i, x);
				}
				a[getIndex(y,x)] = value;
			}
		}
		return new Matrix3(a);
	}

	public Matrix3 transpose() {
		return new Matrix3(
				m[0],m[3],m[6],
				m[1],m[4],m[7],
				m[2],m[5],m[8]);
	}

	public double determinant() {
		return
				m[0]*m[4]*m[8] +
				m[1]*m[5]*m[6] +
				m[2]*m[3]*m[7] -
		        m[2]*m[4]*m[6] -
				m[1]*m[3]*m[8] -
				m[0]*m[5]*m[7];
	}

	public Matrix3 invert() {
		Matrix3 n = new Matrix3();
		double inv = 1.0 / determinant();

		n.m[0] = inv * (m[4] * m[8] - m[5] * m[7]);
		n.m[1] = -inv * (m[1] * m[8] - m[2] * m[7]);
		n.m[2] = inv * (m[1] * m[5] - m[2] * m[4]);
		n.m[3] = -inv * (m[3] * m[8] - m[5] * m[6]);
		n.m[4] = inv * (m[0] * m[8] - m[2] * m[6]);
		n.m[5] = -inv * (m[0] * m[5] - m[2] * m[3]);
		n.m[6] = inv * (m[3] * m[7] - m[4] * m[6]);
		n.m[7] = -inv * (m[0] * m[7] - m[1] * m[6]);
		n.m[8] = inv * (m[0] * m[4] - m[1] * m[3]);
		return n;
	}

	public Vector3 transform(Vector3 v) {
		double x = v.x * m[0] + v.y * m[3] + v.z * m[6];
		double y = v.x * m[1] + v.y * m[4] + v.z * m[7];
		double z = v.x * m[2] + v.y * m[5] + v.z * m[8];
		return new Vector3(x, y, z);
	}

	public Vector3 transformNormal(Vector3 v) {
		double fLength = v.length();
		if (fLength == 0.0f) return v;
		Matrix3 mTransform = invert().transpose();
		Vector3 transformed = new Vector3(
				v.x * mTransform.m[0] + v.y * mTransform.m[3] + v.z * mTransform.m[6],
				v.x * mTransform.m[1] + v.y * mTransform.m[4] + v.z * mTransform.m[7],
				v.x * mTransform.m[2] + v.y * mTransform.m[5] + v.z * mTransform.m[8]);
		return transformed.normalize().mul(fLength);
	}


	public String toString() {
		final int desiredLen = 10;
		String sep = " ";
		String s = "[" + sline(0, sep, desiredLen) + StringUtil.NEWLINE;
		s += " " + sline(3, sep, desiredLen) + StringUtil.NEWLINE;
		s += " " + sline(6, sep, desiredLen) + "]";
		return s;
	}

	private String sline(int startIndex, String sep, int desiredLen) {
		String s = ralign(m[startIndex], desiredLen);
		for (int i = 1; i < COLUMN_COUNT; i++) {
			s += sep + ralign(m[startIndex + i], desiredLen);
		}
		return s;
	}

	private String ralign(double number, int desiredLen) {
		String s = "" + number;
		while (s.length() < desiredLen) {
			s += " ";
		}
		return s;
	}

	public float[] data() {
		return new float[]{
				(float)m[0],(float)m[1],(float)m[2],
				(float)m[3],(float)m[4],(float)m[5],
				(float)m[6],(float)m[7],(float)m[8]};
	}
}
