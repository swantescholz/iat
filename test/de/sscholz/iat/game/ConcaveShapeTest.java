package de.sscholz.iat.game;

import de.sscholz.iat.TestBase;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Triangle;
import de.sscholz.iat.math.Vector2;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ConcaveShapeTest extends TestBase {

	@Test
	public void shouldUseCorrectTriangles() {
		Vector2 a = new Vector2(2, 2);
		Vector2 b = new Vector2(5, 3);
		Vector2 c = new Vector2(2, 4);
		Vector2 d = new Vector2(3, 3);
		List<Vector2> vertices = Arrays.asList(a, b, c, d);
		List<Triangle> triangles = ConcaveShape.tesselateShapeIntoTriangles(vertices);
		assertEquals(2, triangles.size());
		assertAlmostEqual(triangles.get(0).a.position.xy(), b);
		assertAlmostEqual(triangles.get(1).c.position.xy(), d);
	}

	@Test
	public void shouldHandleEvenMoreComplexShapes() {
		double[] coords = new double[]{-6.3134534,11.9639942,-4.9560609,7.6708459,-7.5130096,7.6077109,-6.2187516,3.6933698,-9.3754783,3.3776971,-10.006824,5.6505403,-12.374369,4.6403878,-11.585187,8.3653253,-8.4284603,8.3968923,-10.796005,12.8163097,-4.9876282,14.0158658};
		List<Vector2> vertices = MathUtil.createVector2List(coords);
		testTesselationWorks(vertices);
	}

	@Test
	public void offsetWorks() {
		List<Vector2> vertices = MathUtil.createVector2List(-3.314563,3.4723994,-6.1556171,2.2728432,-4.4825519,0.0947018,-2.1150069,1.7046324,-1.9256033,-1.38896,-8.8704021,-1.29426,-8.7757001,-3.882776,0.53664354,-1.641498,3.2514285,-3.69337,6.5659916,-2.178141,1.4836616,0.7260471,2.7463522,2.1781414,9.9436891,-1.7362,10.764438,1.420527,12.374369,0.1894036,10.922274,-1.04172,20.518724,-1.830901,20.865964,0.2525381,14.331539,0.8523162,15.341692,2.8094868);
		assertFalse(MathUtil.isPolygonDegenerate(vertices));
		for (int i = 0; i < vertices.size(); i++) {
			vertices.set(i, vertices.get(i).add(new Vector2(0,10)));
		}
		testTesselationWorks(vertices);
	}

	private void testTesselationWorks(List<Vector2> vertices) {
		if (MathUtil.isPolygonClockwise(vertices)) {
			Collections.reverse(vertices);
		}
		ConcaveShape.tesselateShapeIntoTriangles(vertices);
	}
}
