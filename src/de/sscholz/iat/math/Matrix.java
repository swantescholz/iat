package de.sscholz.iat.math;

import de.sscholz.iat.util.StringUtil;

public class Matrix extends VectorSpace<Matrix> {

	public static Matrix identity() {
		return new Matrix(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0);
	}

	public static Matrix zero() {
		return new Matrix(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
	}

	private static final int ROW_COUNT = 4;
	private static final int COLUMN_COUNT = 4;
	private static final int ELEMENT_COUNT = ROW_COUNT * COLUMN_COUNT;
	private final double[] m = new double[ELEMENT_COUNT];

	public Matrix() {

	}

	public Matrix(double[] m) {
		System.arraycopy(m, 0, this.m, 0, ELEMENT_COUNT);
	}

	public Matrix(
			double m00, double m01, double m02, double m03,
			double m10, double m11, double m12, double m13,
			double m20, double m21, double m22, double m23,
			double m30, double m31, double m32, double m33) {
		m[0] = m00;
		m[1] = m01;
		m[2] = m02;
		m[3] = m03;
		m[4] = m10;
		m[5] = m11;
		m[6] = m12;
		m[7] = m13;
		m[8] = m20;
		m[9] = m21;
		m[10] = m22;
		m[11] = m23;
		m[12] = m30;
		m[13] = m31;
		m[14] = m32;
		m[15] = m33;
	}

	public Matrix copy() {
		Matrix n = new Matrix();
		System.arraycopy(m, 0, n.m, 0, ELEMENT_COUNT);
		return n;
	}

	@Override
	public Matrix negate() {
		Matrix n = copy();
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
	public Matrix3 toMatrix3() {
		return new Matrix3(
				m[0],m[1],m[2],
				m[4],m[5],m[6],
				m[8],m[9],m[10]);
	}

	public Matrix add(Matrix that) {
		Matrix n = copy();
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			n.m[i] += that.m[i];
		}
		return n;
	}

	public Matrix sub(Matrix that) {
		Matrix n = copy();
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			n.m[i] -= that.m[i];
		}
		return n;
	}

	public Matrix mul(double s) {
		Matrix n = copy();
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			n.m[i] *= s;
		}
		return n;
	}

	@Override
	public boolean equal(Matrix n) {
		return m[0] == n.m[0] && m[1] == n.m[1] && m[2] == n.m[2] && m[3] == n.m[3] &&
				m[4] == n.m[4] && m[5] == n.m[5] && m[6] == n.m[6] && m[7] == n.m[7] &&
				m[8] == n.m[8] && m[9] == n.m[9] && m[10] == n.m[10] && m[11] == n.m[11] &&
				m[12] == n.m[12] && m[13] == n.m[13] && m[14] == n.m[14] && m[15] == n.m[15];
	}

	@Override
	public boolean almostEqual(Matrix b) {
		for (int i = 0; i < ELEMENT_COUNT; i++) {
			if (!MathUtil.almostEqual(m[i], b.m[i]))
				return false;
		}
		return true;
	}

	public Matrix mul(Matrix b) {
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
		return new Matrix(a);
	}

	public Matrix transpose() {
		return new Matrix(
				m[0],m[4],m[8],m[12],
				m[1],m[5],m[9],m[13],
				m[2],m[6],m[10],m[14],
				m[3],m[7],m[11],m[15]);
	}

	public double determinant() {
		return m[0] * (m[5] * m[10] - m[6] * m[9]) -
				m[1] * (m[4] * m[10] - m[6] * m[8]) +
				m[2] * (m[4] * m[9] - m[5] * m[8]);
	}

	public Matrix invert() {
		Matrix n = new Matrix();
		double fInvDet = 1.0 / determinant();

		n.m[0] = fInvDet * (m[5] * m[10] - m[6] * m[9]);
		n.m[1] = -fInvDet * (m[1] * m[10] - m[2] * m[9]);
		n.m[2] = fInvDet * (m[1] * m[6] - m[2] * m[5]);
		n.m[3] = 0.0;
		n.m[4] = -fInvDet * (m[4] * m[10] - m[6] * m[8]);
		n.m[5] = fInvDet * (m[0] * m[10] - m[2] * m[8]);
		n.m[6] = -fInvDet * (m[0] * m[6] - m[2] * m[4]);
		n.m[7] = 0.0;
		n.m[8] = fInvDet * (m[4] * m[9] - m[5] * m[8]);
		n.m[9] = -fInvDet * (m[0] * m[9] - m[1] * m[8]);
		n.m[10] = fInvDet * (m[0] * m[5] - m[1] * m[4]);
		n.m[11] = 0.0;
		n.m[12] = -(m[12] * n.m[0] + m[13] * n.m[4] + m[14] * m[8]);
		n.m[13] = -(m[12] * n.m[1] + m[13] * n.m[5] + m[14] * m[9]);
		n.m[14] = -(m[12] * n.m[2] + m[13] * n.m[6] + m[14] * m[10]);
		n.m[15] = 1.0;
		return n;
	}

	public Vector3 transform(Vector3 v) {
		double x = v.x * m[0] + v.y * m[4] + v.z * m[8] + m[12];
		double y = v.x * m[1] + v.y * m[5] + v.z * m[9] + m[13];
		double z = v.x * m[2] + v.y * m[6] + v.z * m[10] + m[14];
		double w = v.x * m[3] + v.y * m[7] + v.z * m[11] + m[15];
		if (w != 1.0) {
			return new Vector3(x, y, z).div(w);
		}
		return new Vector3(x, y, z);
	}

	public Vector4 transform(Vector4 v) {
		double x = v.x * m[0] + v.y * m[4] + v.z * m[8] + v.w * m[12];
		double y = v.x * m[1] + v.y * m[5] + v.z * m[9] + v.w * m[13];
		double z = v.x * m[2] + v.y * m[6] + v.z * m[10] + v.w * m[14];
		double w = v.x * m[3] + v.y * m[7] + v.z * m[11] + v.w * m[15];
		return new Vector4(x, y, z, w);
	}

	public Vector3 transformNormal(Vector3 v) {
		double fLength = v.length();
		if (fLength == 0.0f) return v;
		Matrix mTransform = invert().transpose();
		Vector3 transformed = new Vector3(
				v.x * mTransform.m[0] + v.y * mTransform.m[4] + v.z * mTransform.m[8],
				v.x * mTransform.m[1] + v.y * mTransform.m[5] + v.z * mTransform.m[9],
				v.x * mTransform.m[2] + v.y * mTransform.m[6] + v.z * mTransform.m[10]);
		return transformed.normalize().mul(fLength);
	}

	public static Matrix translation(Vector3 v) {
		return new Matrix(
				1.0, 0.0, 0.0, 0.0,
				0.0, 1.0, 0.0, 0.0,
				0.0, 0.0, 1.0, 0.0,
				v.x, v.y, v.z, 1.0);
	}

	public static Matrix rotationFromAxes(Vector3 xAxis, Vector3 yAxis, Vector3 zAxis) {
		return new Matrix(
				xAxis.x, xAxis.y, xAxis.z, 0,
				yAxis.x, yAxis.y, yAxis.z, 0,
				zAxis.x, zAxis.y, zAxis.z, 0,
				0, 0, 0, 1);
	}

	public static Matrix rotation(Vector3 axis, double angle) {
		double fSin = Math.sin(-angle);
		double fCos = Math.cos(-angle);
		double fOneMinusCos = 1.0 - fCos;

		axis = axis.normalize();

		return new Matrix(
				(axis.x * axis.x) * fOneMinusCos + fCos,
				(axis.x * axis.y) * fOneMinusCos - (axis.z * fSin),
				(axis.x * axis.z) * fOneMinusCos + (axis.y * fSin),
				0.0,
				(axis.y * axis.x) * fOneMinusCos + (axis.z * fSin),
				(axis.y * axis.y) * fOneMinusCos + fCos,
				(axis.y * axis.z) * fOneMinusCos - (axis.x * fSin),
				0.0,
				(axis.z * axis.x) * fOneMinusCos - (axis.y * fSin),
				(axis.z * axis.y) * fOneMinusCos + (axis.x * fSin),
				(axis.z * axis.z) * fOneMinusCos + fCos,
				0.0,
				0.0, 0.0, 0.0, 1.0);
	}

	public static Matrix scaling(double s) {
		return Matrix.scaling(new Vector3(s, s, s));
	}

	public static Matrix scaling(Vector3 v) {
		return new Matrix(
				v.x, 0.0, 0.0, 0.0,
				0.0, v.y, 0.0, 0.0,
				0.0, 0.0, v.z, 0.0,
				0.0, 0.0, 0.0, 1.0);
	}

	public static Matrix projection(double fov, double aspectRatio, double nearPlane, double farPlane) {
		double dxInv = 1.0 / (2.0 * nearPlane * Math.tan(fov * 0.5));
		double dyInv = dxInv * aspectRatio;
		double dzInv = 1.0 / (farPlane - nearPlane);
		double x = 2.0 * nearPlane;
		double c = -(nearPlane + farPlane) * dzInv;
		double d = -nearPlane * farPlane * dzInv;
		return new Matrix(x * dxInv, 0, 0, 0, 0, x * dyInv, 0, 0, 0, 0, c, -1, 0, 0, d, 0);
	}

	public static Matrix ortho(double left, double right, double bottom, double top, double near, double far) {
		double a = 2 / (right - left);
		double b = 2 / (top - bottom);
		double c = -2 / (far - near);
		return new Matrix(
				a, 0, 0, -(right + left) / (right - left),
				0, b, 0, -(top + bottom) / (top - bottom),
				0, 0, c, -(far + near) / (far - near),
				0, 0, 0, 1);
	}

	public static Matrix camera(Vector3 pos, Vector3 dir, Vector3 up) {
		Vector3 zAxis = dir.normalize().negate();
		Vector3 xAxis = up.cross(zAxis).normalize();
		Vector3 yAxis = zAxis.cross(xAxis).normalize();

		Vector3 negPos = pos.negate();
		Matrix result = Matrix.translation(negPos);
		return result.mul(new Matrix(
				xAxis.x, yAxis.x, zAxis.x, 0.0,
				xAxis.y, yAxis.y, zAxis.y, 0.0,
				xAxis.z, yAxis.z, zAxis.z, 0.0,
				0.0, 0.0, 0.0, 1.0));
	}

	public String toString() {
		final int desiredLen = 10;
		String sep = " ";
		String s = "[" + sline(0, sep, desiredLen) + StringUtil.NEWLINE;
		s += " " + sline(4, sep, desiredLen) + StringUtil.NEWLINE;
		s += " " + sline(8, sep, desiredLen) + StringUtil.NEWLINE;
		s += " " + sline(12, sep, desiredLen) + "]";
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

	public static Matrix shear(double amount) {
		return new Matrix(
				1, 0, 0, 0,
				0, 1, 0, 0,
				amount, amount, 1, 0,
				0, 0, 0, 1
		);
	}

	public float[] data() {
		return new float[]{
				(float)m[0],(float)m[1],(float)m[2],(float)m[3],
				(float)m[4],(float)m[5],(float)m[6],(float)m[7],
				(float)m[8],(float)m[9],(float)m[10],(float)m[11],
				(float)m[12],(float)m[13],(float)m[14],(float)m[15]};
	}

}
