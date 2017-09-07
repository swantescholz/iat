package de.sscholz.iat.math;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.gfx.texture.Pixel;

public class Color extends VectorSpace<Color> {

	public static final Color ONE = new Color(1, 1, 1);
	public static final Color ZERO = new Color(0, 0, 0, 0);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color GRAY = new Color(0.5, 0.5, 0.5);
	public static final Color WHITE = new Color(1, 1, 1);
	public static final Color RED = new Color(1, 0, 0);
	public static final Color GREEN = new Color(0, 1, 0);
	public static final Color BLUE = new Color(0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0);
	public static final Color PURPLE = new Color(1, 0, 1);
	public static final Color CYAN = new Color(0, 1, 1);
	public static final Color ORANGE = new Color(1, 0.5, 1);
	public static final Color PINK = new Color(0, 0.2667, 0.7333);
	public static final Color SKY = new Color(0.5, 0.7, 0.9);
	public static final Color MELLOW = new Color(0.9, 0.7, 0.5);
	public static final Color FOREST = new Color(0.247, 0.498, 0.373);
	public static final Color SILVER = new Color(0.784314, 0.784314, 0.784314);
	public static final Color GOLD = new Color(0.862745, 0.745098, 0.0);

	public final double r;
	public final double g;
	public final double b;
	public final double a;

	public Color() {
		this(0, 0, 0, 1);
	}

	public Color(double r, double g, double b) {
		this(r, g, b, 1);
	}

	public Color(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public static int rgbIntFromBytes(byte r, byte g, byte b) {
		return rgbIntFromInts(unsignByte(r), unsignByte(g), unsignByte(b));
	}

	public static int rgbIntFromInts(int r, int g, int b) {
		int rgb = 255;
		rgb = (rgb << 8) + r;
		rgb = (rgb << 8) + g;
		rgb = (rgb << 8) + b;
		return rgb;
	}

	public static int unsignByte(byte b) {
		return b & 0xFF;
	}

	public void gl() {
		GlUtil.getGl().glColor4d(r, g, b, a);
	}

	public Color add(Color that) {
		return new Color(r + that.r, g + that.g, b + that.b, a + that.a);
	}

	public Color sub(Color that) {
		return new Color(r - that.r, g - that.g, b - that.b, a - that.a);
	}

	public Color mul(double s) {
		return new Color(r * s, g * s, b * s, a * s);
	}

	@Override
	public boolean equal(Color that) {
		return r == that.r && g == that.g && b == that.b && a == that.a;
	}

	@Override
	public boolean almostEqual(Color b) {
		return MathUtil.almostEqual(this.r, b.r) && MathUtil.almostEqual(this.g, b.g) &&
				MathUtil.almostEqual(this.b, b.b) && MathUtil.almostEqual(this.a, b.a);
	}

	public Color negate() {
		return new Color(-r, -g, -b, -a);
	}

	public Color clamp() {
		return clamp(0.0, 1.0);
	}

	public Color clamp(double min, double max) {
		double nr = MathUtil.clamp(r, min, max);
		double ng = MathUtil.clamp(g, min, max);
		double nb = MathUtil.clamp(b, min, max);
		double na = MathUtil.clamp(a, min, max);
		return new Color(nr, ng, nb, na);
	}

	public Pixel toPixel() {
		return new Pixel(toByte(r), toByte(g), toByte(b), toByte(a));
	}

	public double[] data() {
		return new double[]{r, g, b, a};
	}

	public float[] floatData() {
		return new float[]{(float) r, (float) g, (float) b, (float) a};
	}

	public String toString() {
		return "(" + r + ", " + g + ", " + b + ", " + a + ")";
	}

	public static byte toByte(double d) {
		return (byte) MathUtil.clamp((int) (256 * d), 0, 255);
	}

	public static Color random() {
		return random(1.0);
	}

	public static Color random(double alpha) {
		final double min = 0.0;
		final double max = 1.0;
		return new Color(
				MathUtil.randomDouble(min, max),
				MathUtil.randomDouble(min, max),
				MathUtil.randomDouble(min, max),
				alpha);
	}

	public int toRgbInt() {
		return rgbIntFromInts(toInt(r), toInt(g), toInt(b));
	}

	private int toInt(double d) {
		return MathUtil.clamp((int) (d * 256.0), 0, 255);
	}

	public java.awt.Color toAwtColor() {
		return new java.awt.Color(toInt(r),toInt(g),toInt(b),toInt(a));
	}
}
