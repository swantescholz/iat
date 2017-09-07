package de.sscholz.iat.math;

import de.sscholz.iat.TestBase;
import de.sscholz.iat.gfx.GlUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MathUtilTest extends TestBase {


	@Test
	public void transformingToScreenCoordinatesGivesNormalizedValues() {
		Camera camera = new Camera(new Vector3(4,2,10), Vector3.NZ, Vector3.Y);
		double fov = GlUtil.DEFAULT_FOV;
		double ratio = 2.0;
		Matrix viewProjection = camera.getCameraMatrix().mul(Matrix.projection(fov, ratio, 0.01, 10000.0));
		Function<Vector3, Vector2> f = it -> MathUtil.getScreenCoordinates(it, viewProjection, ratio);
		Vector2 a = f.apply(new Vector3(0,0,30));
		Vector2 b = f.apply(new Vector3(4,2,0));
		Vector2 c = f.apply(new Vector3(0,0,0));
		Vector2 d = f.apply(new Vector3(2,4,0));
		assertAlmostEqual(Vector2.ZERO, b);
		assertTrue(c.x < 0.0 && c.y < 0.0 && c.x > -.5 && c.y > -.25);
		assertTrue(d.x < 0.0 && d.y > 0.0 && d.x > -.5 && d.y < .25);
		assertNull(a);
	}

	@Test
	public void isPolygonClockwiseWorks() {
		List<Vector2> a = Arrays.asList(Vector2.X, Vector2.NXY, Vector2.NX, Vector2.Y);
		List<Vector2> b = Arrays.asList(Vector2.X, Vector2.XY, Vector2.Y, Vector2.NX, Vector2.NY);
		List<Vector2> c = Arrays.asList(new Vector2(0,2), new Vector2(0,0), new Vector2(1,1));
		assertTrue(MathUtil.isPolygonClockwise(a));
		assertFalse(MathUtil.isPolygonClockwise(b));
		assertFalse(MathUtil.isPolygonClockwise(c));
	}

	@Test
	public void canDetectDegeneratePolygons() {
		List<Vector2> a = Arrays.asList(Vector2.X, Vector2.NXY, Vector2.NY, Vector2.Y);
		List<Vector2> b = Arrays.asList(Vector2.X, Vector2.XY, Vector2.Y, Vector2.NX, Vector2.NY);
		assertTrue(MathUtil.isPolygonDegenerate(a));
		assertFalse(MathUtil.isPolygonDegenerate(b));
	}
}
