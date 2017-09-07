package de.sscholz.iat.gfx;

import de.sscholz.iat.gfx.shader.uniform.UniformManager;
import de.sscholz.iat.math.Color;
import de.sscholz.iat.math.Dimension;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.util.log.Log;

import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GlUtil {

	public static final double DEFAULT_FOV = MathUtil.toRadian(80.0);

	private static GL gl;
	private static boolean wireframed = false;
	private static final Dimension dimension = new Dimension();
	private static boolean useOrthoProjection = false;

	private GlUtil() {
	}

	public static GL getGl() {
		return gl;
	}

	private static void setGl(GL gl) {
		GlUtil.gl = gl;
		GlOperator.updateAll();
	}

	public static int[] toArray(int i) {
		int[] tmp = new int[1];
		tmp[0] = i;
		return tmp;
	}

	public static void toggleWireframeMode() {
		if (wireframed) {
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL);
		} else {
			gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);
		}
		wireframed = !wireframed;
	}

	public static void toggleShadeModel() {
		int model = getInteger(GL.GL_SHADE_MODEL);
		int newModel = GL.GL_FLAT;
		if (model == GL.GL_FLAT) {
			newModel = GL.GL_SMOOTH;
		}
		gl.glShadeModel(newModel);
	}

	public static void toggleEnabled(int field) {
		if (gl.glIsEnabled(field)) {
			gl.glDisable(field);
		} else {
			gl.glEnable(field);
		}
	}

	private static int getInteger(int field) {
		IntBuffer buffer = IntBuffer.allocate(1);
		gl.glGetIntegerv(field, buffer);
		return buffer.get();
	}

	public static void setClearColor(Color bgColor) {
		gl.glClearColor((float) bgColor.r, (float) bgColor.g, (float) bgColor.b, (float) bgColor.a);
	}

	public static void initGl(GL gl) {
		setGl(gl);
		setClearColor(Color.SKY);
		gl.glClearDepth(1.0f);

		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);

		gl.glCullFace(GL.GL_BACK);
		gl.glFrontFace(GL.GL_CCW);
	}


	public static Dimension getDimension() {
		return dimension;
	}

	public static double getAspect() {
		return (double) dimension.width / (double) dimension.height;
	}

	private static void setDimension(int width, int height) {
		dimension.width = width;
		dimension.height = height;
	}

	public static void reshapeGlView(GL gl, int x, int y, int width, int height) {
		setGl(gl);
		Log.DEFAULT.debug("reshaping: " + x + ", " + y + ", " + width + ", " + height);
		gl.glViewport(0, 0, width, height);
		setDimension(width, height);
		updateProjectionMatrix();
	}

	private static void updateProjectionMatrix() {
		if (useOrthoProjection) {
			double top = 20.0;
			double right = top * getAspect();
			double far = top;
			Matrix ortho = Matrix.ortho(-right, right, -top, top, -far, far);
			UniformManager.instance.setProjectionMatrix(ortho);
		} else {
			Matrix projectionMatrix = Matrix.projection(DEFAULT_FOV, getAspect(), 0.01, 10000.0);
			UniformManager.instance.setProjectionMatrix(projectionMatrix);
		}
	}

	public static void toggleProjectionMode() {
		useOrthoProjection = !useOrthoProjection;
		updateProjectionMatrix();
	}

	public static void initFrame(GL gl) {
		setGl(gl);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}

	public static Dimension getViewportDimension() {
		int[] data = new int[4];
		gl.glGetIntegerv(GL.GL_VIEWPORT, data, 0);
		return new Dimension(data[2], data[3]);
	}

	public static void saveScreenshot(File file) {
		BufferedImage image = readScreenshot();
		try {
			file.mkdirs();
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage readScreenshot() {
		Dimension viewportDimension = getViewportDimension();
		final int bytesPerPixel = 3;
		Log.DEFAULT.debug("screenshot dimension: " + viewportDimension);
		ByteBuffer buffer = ByteBuffer.allocate(viewportDimension.size() * bytesPerPixel);
		gl.glPixelStorei(GL.GL_PACK_ALIGNMENT, 1);
		gl.glReadBuffer(GL.GL_FRONT);
		gl.glReadPixels(0, 0, viewportDimension.width, viewportDimension.height,
				GL.GL_RGB, GL.GL_UNSIGNED_BYTE, buffer);
		BufferedImage image = new BufferedImage(viewportDimension.width,
				viewportDimension.height, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < viewportDimension.height; y++) {
			for (int x = 0; x < viewportDimension.width; x++) {
				int index = ((viewportDimension.height - y - 1) * viewportDimension.width + x) * bytesPerPixel;
				byte red = buffer.get(index);
				byte green = buffer.get(index + 1);
				byte blue = buffer.get(index + 2);
				int rgb = Color.rgbIntFromBytes(red, green, blue);
				image.setRGB(x, y, rgb);
			}
		}
		return image;
	}

	public static void pushEnabled() {
		gl.glPushAttrib(GL.GL_ENABLE_BIT);
	}

	public static void popEnabled() {
		gl.glPopAttrib();
	}

}
