package de.sscholz.iat.gfx.shader.uniform;

import de.sscholz.iat.gfx.light.Light;
import de.sscholz.iat.gfx.texture.Texture;
import de.sscholz.iat.math.Camera;
import de.sscholz.iat.math.Color;
import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Matrix3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// default uniforms can be set before or after glUseProgram, special uniforms only after glUseProgram
public class UniformManager {

	public static final UniformManager instance = new UniformManager();

	private final List<IUniform> defaultUniforms = new ArrayList<>();
	private final Uniform<Matrix> modelMatrix = new Uniform<>("uModelMatrix");
	private final Uniform<Matrix> viewMatrix = new Uniform<>("uViewMatrix");
	private final Uniform<Matrix> projectionMatrix = new Uniform<>("uProjectionMatrix");
	private final Uniform<Matrix> modelViewMatrix = new Uniform<>("uModelViewMatrix");
	private final Uniform<Matrix> viewProjectionMatrix = new Uniform<>("uViewProjectionMatrix");
	private final Uniform<Matrix> modelViewProjectionMatrix = new Uniform<>("uModelViewProjectionMatrix");
	private final Uniform<Matrix3> normalMatrix = new Uniform<>("uNormalMatrix");
	public final Uniform<Double> elapsedTime = new Uniform<>("uElapsedTime");
	public final Uniform<Double> timeSinceInit = new Uniform<>("uTimeSinceInit");
	public final Uniform<Camera> camera = new Uniform<>("uCamera");
	public final Uniform<Color> lightModelAmbient = new Uniform<>("uLightModelAmbient");
	public final Uniform<Material> material = new Uniform<>("uMaterial");
	public final ArrayList<Uniform<Light>> lights = new ArrayList<>();
	public final ArrayList<Uniform<Texture>> textures = new ArrayList<>();

	private UniformManager() {
		setDefaultValues();
		defaultUniforms.addAll(Arrays.asList(modelMatrix,viewMatrix,projectionMatrix,
				normalMatrix,modelViewMatrix,viewProjectionMatrix,modelViewProjectionMatrix,
				elapsedTime,timeSinceInit,camera,lightModelAmbient,material));
		defaultUniforms.addAll(lights);
		defaultUniforms.addAll(textures);
	}

	private void setDefaultValues() {
		for (int i = 0; i < Light.MAX_NUMBER; i++) {
			Uniform<Light> lightUniform = new Uniform<>("uLightSource[" + i + "]");
			lights.add(lightUniform);
		}
		for (int i = 0; i < Texture.MAX_LAYERS; i++) {
			Uniform<Texture> textureUniform = new Uniform<>("uTexture[" + i + "]");
			textures.add(textureUniform);
		}
		Matrix id = Matrix.identity();
		modelMatrix.setValue(id);
		viewMatrix.setValue(id);
		projectionMatrix.setValue(id);
		normalMatrix.setValue(Matrix3.identity());
		modelViewMatrix.setValue(id);
		viewProjectionMatrix.setValue(id);
		modelViewProjectionMatrix.setValue(id);
		elapsedTime.setValue(0.1);
		timeSinceInit.setValue(0.1);
		camera.setValue(new Camera());
		lightModelAmbient.setValue(new Color(.2,.2,.2,1.0));
	}

	public void applyAllDefaultUniformsToCurrentShader() {
		defaultUniforms.forEach(IUniform::applyToCurrentShader);
	}

	public void setModelMatrix(Matrix newModelMatrix) {
		modelMatrix.setAndApplyValue(newModelMatrix);
		modelViewMatrix.setAndApplyValue(newModelMatrix.mul(viewMatrix.getValue()));
		modelViewProjectionMatrix.setAndApplyValue(modelViewMatrix.getValue().mul(projectionMatrix.getValue()));
		normalMatrix.setAndApplyValue(newModelMatrix.toMatrix3().transpose());
	}

	public void setViewMatrix(Matrix newViewMatrix) {
		viewMatrix.setAndApplyValue(newViewMatrix);
		modelViewMatrix.setAndApplyValue(modelMatrix.getValue().mul(newViewMatrix));
		viewProjectionMatrix.setAndApplyValue(newViewMatrix.mul(projectionMatrix.getValue()));
		modelViewProjectionMatrix.setAndApplyValue(modelViewMatrix.getValue().mul(projectionMatrix.getValue()));
	}

	public void setProjectionMatrix(Matrix newProjectionMatrix) {
		projectionMatrix.setValue(newProjectionMatrix);
		viewProjectionMatrix.setValue(viewMatrix.getValue().mul(newProjectionMatrix));
		modelViewProjectionMatrix.setValue(modelViewMatrix.getValue().mul(newProjectionMatrix));
	}

	public Matrix getModelMatrix() {
		return modelMatrix.getValue();
	}

	public Matrix getViewMatrix() {
		return viewMatrix.getValue();
	}

	public Matrix getProjectionMatrix() {
		return projectionMatrix.getValue();
	}

	public Matrix getModelViewMatrix() {
		return modelViewMatrix.getValue();
	}

	public Matrix getViewProjectionMatrix() {
		return viewProjectionMatrix.getValue();
	}

	public Matrix getModelViewProjectionMatrix() {
		return modelViewProjectionMatrix.getValue();
	}

	public void setCamera(Camera camera) {
		this.camera.setAndApplyValue(camera);
		setViewMatrix(camera.getCameraMatrix());
	}

	public void applyLight(Light light) {
		int id = light.getId();
		Uniform<Light> lightUniform = lights.get(id);
		lightUniform.setAndApplyValue(light);
	}

	public void setTexture(Texture texture, int textureLayerIndex) {
		textures.get(textureLayerIndex).setAndApplyValue(texture);
	}


}
