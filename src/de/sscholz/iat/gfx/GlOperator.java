package de.sscholz.iat.gfx;

import javax.media.opengl.GL;
import java.util.ArrayList;
import java.util.List;


public class GlOperator {

	protected GL gl;
	private static final List<GlOperator> operators = new ArrayList<>();

	public GlOperator() {
		gl = GlUtil.getGl();
		operators.add(this);
	}

	static void updateAll() {
		GL gl = GlUtil.getGl();
		for (GlOperator operator : operators) {
			operator.gl = gl;
		}
	}


}
