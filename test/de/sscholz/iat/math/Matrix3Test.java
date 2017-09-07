package de.sscholz.iat.math;

import de.sscholz.iat.TestBase;
import org.junit.Test;

public class Matrix3Test extends TestBase {

	@Test
	public void invertWorks() {
		Matrix3 a = Matrix.rotation(new Vector3(4,1,1),2).toMatrix3();
		Matrix3 b = a.invert().invert();
		assertAlmostEqual(a,b);
	}

}
