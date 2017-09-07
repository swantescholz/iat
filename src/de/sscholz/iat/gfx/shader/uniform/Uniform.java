package de.sscholz.iat.gfx.shader.uniform;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.gfx.light.ILight;
import de.sscholz.iat.gfx.texture.Texture;
import de.sscholz.iat.math.Camera;
import de.sscholz.iat.math.Color;
import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Matrix3;
import de.sscholz.iat.math.Quaternion;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.log.Asserter;

import javax.media.opengl.GL;

public class Uniform<T> implements Asserter, IUniform {

	private final String name;
	private T value;
	private GL gl;
	private int[] buffer = {1};
	private int currentProgram;

	public Uniform(String name) {
		this.name = name;
	}

	private int getSimpleLocation() {
		return getLocation(name);
	}

	private int getLocation(String fullName) {
		int location = gl.glGetUniformLocation(currentProgram, fullName);
		return location;
	}

	public void setValue(T newValue) {
		value = newValue;
	}

	public void setAndApplyValue(T newValue) {
		value = newValue;
		applyToCurrentShader();
	}



	// applies uniform to current shader-program
	public void applyToCurrentShader() {
		this.gl = GlUtil.getGl();
		gl.glGetIntegerv(GL.GL_CURRENT_PROGRAM, buffer, 0);
		this.currentProgram = buffer[0];
		if (value == null) {
			return;
		} else if (value instanceof Integer) {
			gl.glUniform1i(getSimpleLocation(), (Integer) value);
		} else if (value instanceof Double) {
			double data = (Double)value;
			gl.glUniform1f(getSimpleLocation(), (float) data);
		} else if (value instanceof Vector2) {
			Vector2 data = (Vector2)value;
			gl.glUniform2f(getSimpleLocation(), (float) data.x, (float) data.y);
		} else if (value instanceof Vector3) {
			uniformVector3(getSimpleLocation(), (Vector3) value);
		} else if (value instanceof Color) {
			uniformColor(getSimpleLocation(), (Color)value);
		} else if (value instanceof Quaternion) {
			Quaternion data = (Quaternion)value;
			gl.glUniform4f(getSimpleLocation(), (float) data.w, (float) data.x, (float) data.y, (float) data.z);
		} else if (value instanceof Matrix3) {
			Matrix3 matrix = (Matrix3)value;
			gl.glUniformMatrix3fv(getSimpleLocation(), 1, false, matrix.data(), 0);
		} else if (value instanceof Matrix) {
			Matrix matrix = (Matrix)value;
			gl.glUniformMatrix4fv(getSimpleLocation(), 1, false, matrix.data(), 0);
		} else if (value instanceof Camera) {
			Camera camera = (Camera)value;
			uniformVector3(getLocation(name + ".pos"), camera.getPosition());
			uniformVector3(getLocation(name + ".dir"), camera.getDirection());
			uniformVector3(getLocation(name + ".up"), camera.getUpVector());
		} else if (value instanceof Material) {
			Material material = (Material)value;
			uniformColor(getLocation(name + ".ambient"), material.ambient);
			uniformColor(getLocation(name + ".diffuse"), material.diffuse);
			uniformColor(getLocation(name + ".specular"), material.specular);
			gl.glUniform1f(getLocation(name + ".shininess"), (float) material.shininess);
		} else if (value instanceof ILight) {
			ILight light = (ILight)value;
			uniformColor(getLocation(name + ".ambient"), light.getAmbient());
			uniformColor(getLocation(name+".diffuse"), light.getDiffuse());
			uniformColor(getLocation(name+".specular"), light.getSpecular());
			uniformVector3(getLocation(name+".position"), light.getPosition());
			uniformVector3(getLocation(name+".spotDirection"), light.getSpotDirection());
			gl.glUniform1f(getLocation(name+".spotCutoff"), (float)light.getSpotCutoff());
			gl.glUniform1f(getLocation(name+".spotCosCutoff"), (float)light.getSpotCosCutoff());
			gl.glUniform1f(getLocation(name+".spotExponent"), (float)light.getSpotExponent());
			gl.glUniform1f(getLocation(name+".constantAttenuation"), (float)light.getConstantAttenuation());
			gl.glUniform1f(getLocation(name+".linearAttenuation"), (float)light.getLinearAttenuation());
			gl.glUniform1f(getLocation(name+".quadraticAttenuation"), (float)light.getQuadraticAttenuation());
			uniformBoolean(getLocation(name+".enabled"), light.isEnabled());
			uniformBoolean(getLocation(name+".positional"), light.isPositional());
		} else if (value instanceof Texture) {
			Texture texture = (Texture)value;
			int layerIndex = Integer.parseInt(name.substring(9, name.length()-1));
			gl.glUniform1i(getSimpleLocation(), layerIndex);
			gl.glActiveTexture(GL.GL_TEXTURE0 + layerIndex);
			texture.bind();
		} else {
			log.error("Uniform type '" + value.getClass().toString() + "' not supported.");
		}
	}

	private void uniformVector3(int location, Vector3 v) {
		gl.glUniform3f(location, (float) v.x, (float) v.y, (float) v.z);
	}

	private void uniformColor(int location, Color color) {
		gl.glUniform4f(location, (float) color.r, (float) color.g, (float) color.b, (float) color.a);
	}

	private void uniformBoolean(int location, boolean value) {
		gl.glUniform1i(location, value ? 1 : 0);
	}

	public T getValue() {
		return value;
	}
}
