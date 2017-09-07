package de.sscholz.iat.math;

import de.sscholz.iat.gfx.GlUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MathUtil {

	private MathUtil() {
	}

	public static final double EPSILON = 0.000001;

	public static List<Vector2> createVector2List(double... coords) {
		List<Vector2> points = new ArrayList<>(coords.length / 2);
		for (int i = 0; i < coords.length; i += 2) {
			points.add(new Vector2(coords[i], coords[i + 1]));
		}
		return points;
	}

	public static double toDegree(double radian) {
		return radian / (Math.PI * 2) * 360.0;
	}

	public static double toRadian(double degree) {
		return degree * (Math.PI * 2) / 360.0;
	}

	public static double interpolate(double a, double b, double t) {
		return (1 - t) * a + t * b;
	}

	public static int clamp(int s, int min, int max) {
		if (s < min) return min;
		if (s > max) return max;
		return s;
	}

	public static double clamp(double s, double min, double max) {
		if (s < min) return min;
		if (s > max) return max;
		return s;
	}

	public static int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1)) + min;
	}

	public static double randomDouble(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	public static boolean almostEqual(double a, double b) {
		return Math.abs(b - a) < EPSILON;

	}

	public static double clamp(double d) {
		return clamp(d, 0.0, 1.0);
	}

	public static int unsignByte(byte b) {
		return b & 0xFF;
	}

	public static double byteToDouble(byte b) {
		int i = unsignByte(b);
		return i / 255.0;
	}

	//lower left = (-.5,-.5*height/width), upper right = (.5,.5*height/width), center = (0,0)
	public static Vector2 getScreenCoordinates(Vector3 worldCoordinate, Matrix viewProjection, double ratio) {
		Vector4 p = viewProjection.transform(new Vector4(worldCoordinate, 1.0));
		double h = p.z*2.0*Math.tan(GlUtil.DEFAULT_FOV/2);
		double w = h*ratio;
		if (Math.abs(p.x) < p.w && Math.abs(p.y) < p.w && 0 < p.z && p.z < p.w) {
			return new Vector2(p.x, p.y/ratio).div(2*p.w);
		}
		return null;
	}


	public static boolean isPolygonClockwise(List<Vector2> vertices) {
		Iterator<Vector2> iterator = vertices.iterator();
		double sum = 0.0;
		Vector2 a = iterator.next();
		while(iterator.hasNext()) {
			Vector2 b = iterator.next();
			sum += (b.x-a.x)*(a.y+b.y);
			a = b;
		}
		Vector2 b = vertices.get(0);
		sum += (b.x-a.x)*(a.y+b.y);
		return sum >= 0;
	}

	public static boolean isPolygonDegenerate(List<Vector2> vertices) {
		Iterator<Vector2> ita = vertices.iterator();
		Vector2 a = ita.next();
		while(ita.hasNext()) {
			Vector2 b = ita.next();
			Iterator<Vector2> itb = vertices.iterator();
			Vector2 c = getFirstVertexAfter(b, itb);
			while(itb.hasNext()) {
				Vector2 d = itb.next();
				if (CollisionUtil.intersectLines(a,b,c,d) != null) {
					return true;
				}
				c = d;
			}
			a = b;
		}
		return false;
	}

	private static Vector2 getFirstVertexAfter(Vector2 lastVertex, Iterator<Vector2> iterator) {
		while (lastVertex != iterator.next()) {}
		if (!iterator.hasNext()) {
			return null;
		}
		return iterator.next();
	}
}
