package de.sscholz.iat.game;

import de.sscholz.iat.gfx.GlOperator;
import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.gfx.Hud;
import de.sscholz.iat.gfx.light.PositionalLight;
import de.sscholz.iat.gfx.model.Grid;
import de.sscholz.iat.gfx.model.Model;
import de.sscholz.iat.gfx.model.ModelManager;
import de.sscholz.iat.gfx.model.Nurbs;
import de.sscholz.iat.gfx.shader.CompilationFailedException;
import de.sscholz.iat.gfx.shader.Program;
import de.sscholz.iat.gfx.shader.ProgramManager;
import de.sscholz.iat.gfx.shader.ShaderManager;
import de.sscholz.iat.gfx.texture.TextureManager;
import de.sscholz.iat.input.Keyboard;
import de.sscholz.iat.input.Mouse;
import de.sscholz.iat.math.Camera;
import de.sscholz.iat.math.Color;
import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Movable;
import de.sscholz.iat.math.MovableOrientable;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.FrameRateComputer;
import de.sscholz.iat.util.Slider;
import de.sscholz.iat.util.StringUtil;
import de.sscholz.iat.util.Timer;
import de.sscholz.iat.util.log.Asserter;

import javax.media.opengl.GL;
import java.io.File;

import static java.awt.event.KeyEvent.*;

public class Scene extends GlOperator implements Asserter {

	private Camera camera = new Camera();
	private Keyboard keyboard;
	private Mouse mouse;
	private ModelManager modelManager = ModelManager.instance;
	private ShaderManager shaderManager = ShaderManager.instance;
	private ProgramManager programManager = ProgramManager.instance;
	private TextureManager textureManager = TextureManager.instance;

	private double elapsed;

	private final double moveSpeed = 14.0;
	private final double rotSpeed = MathUtil.toRadian(220);

	private Timer timer = new Timer(5.0);
	private FrameRateComputer fpsComputer = new FrameRateComputer();
	private Model sphere;
	private PositionalLight light;
	private Slider slider = new Slider(0.5);
	private Slider sliderb = new Slider(0.5);
	private Vector3 spherePos = new Vector3();
	private World world;
	private Hud hud = Hud.instance;
	private Nurbs nurbs = new Nurbs(5, 5);
	private Program defaultProgram, colorProgram, textureProgram;
	Player player;

	public Scene(Keyboard keyboard, Mouse mouse) {
		this.keyboard = keyboard;
		this.mouse = mouse;
		sliderb.setValue(.3);
	}

	public void init() {
		light = new PositionalLight();
		light.ambient = Color.WHITE;
		light.diffuse = Color.WHITE;
		light.specular = Color.WHITE;
		light.position = new Vector3(-2, 4, 4);

		camera.upVector = Camera.DEFAULT_UP_VECTOR;
		camera.position = new Vector3(0, 0, 25);
		camera.lookAt(Vector3.ZERO);

		initResources();

		initWorld();
		reset();
		GlUtil.setClearColor(Color.BLACK);
	}

	private void initResources() {
		modelManager.loadObj("sphere.obj");
		modelManager.loadObj("cube.obj");
		modelManager.loadOff("cylinder.off");
		sphere = modelManager.get("sphere.obj");
		textureManager.load("ground");
		textureManager.load("redgrid");
	}

	private void initWorld() {
		Level level = LevelFactory.instance.loadFile(new File("res/level/levela.txt"));
		world = new World(level);
		player = world.getPlayer();
	}

	private void setupFrame() {
		camera.apply();
		light.shine();
		drawCoordinateSystem();
		drawLight();
	}

	private void reset() {
		log.info("reloading");
		initWorld();
		Grid grid = new Grid(10, 8);
		grid.arrangePlane(new Vector3(-1, 0, -1), new Vector3(1, 0, -1), new Vector3(-1, 0, 1));
		grid.randomizeAxis(new Vector3(0, 1, 0), -2, 2);
		nurbs.create(grid);
		nurbs.setTransformation(Matrix.scaling(new Vector3(15, 2, 15)));
		shaderManager.reloadHeaders();
		try {
			shaderManager.loadVert("v.test", "test");
			shaderManager.loadFrag("f.test", "test");
			shaderManager.loadVert("v.simplePosition", "simplePosition");
			shaderManager.loadFrag("f.simpleDiffuseColor", "simpleDiffuseColor");
			shaderManager.loadVert("v.simpleFragCoords", "simpleFragCoords");
			shaderManager.loadFrag("f.simpleTexture", "simpleTexture");
		} catch (CompilationFailedException e) {
			log.warn("shader compilation failed.");
		}
		defaultProgram = programManager.linkProgram("test", "v.test", "f.test");
		colorProgram = programManager.linkProgram("color", "v.simplePosition", "f.simpleDiffuseColor");
		textureProgram = programManager.linkProgram("texture", "v.simpleFragCoords", "f.simpleTexture");
	}

	public void render() {
		defaultProgram.use();
		setupFrame();
		Material.MELLOW.use();
		sphere.render();
		colorProgram.use();
		world.render();
		//nurbs.render();

	}

	private void drawLight() {
		defaultProgram.use();
		Material.GOLD.use();
		Model lightBall = modelManager.get("sphere.obj");
		lightBall.setTransformation(Matrix.scaling(.33).mul(Matrix.translation(light.position)));
		lightBall.render();
	}

	public void update() {
		fpsComputer.update();
		elapsed = fpsComputer.getElapsed();
		Timer.updateAll(elapsed);
		if (timer.isExpired()) {
			log.debug("FPS: " + fpsComputer.getAverageFps());
			timer.reset();
		}
		processInput();

		Hud.instance.right.setText(sliderb.getValue()+"");
		//moveExponentiallyTo(light, player.getPosition());
		moveExponentiallyTo(camera, player.getPosition());
		//camera.setPosition(new Vector3(player.getPosition(), camera.getPosition().z));
		spherePos = spherePos.approachExponentially(light.getPosition(), slider.map(.01, .99) / 4, elapsed);
		Matrix m = Matrix.scaling(slider.map(.2, 2)).mul(Matrix.translation(spherePos));
		sphere.setTransformation(m);
		world.update(elapsed);
	}

	public void moveExponentiallyTo(Movable object, Vector2 destination) {
		final double base = sliderb.map(0, 1);
		Vector3 p = object.getPosition();
		object.setPosition(p.xy().approachExponentially(destination, base, elapsed).to3(p.z));
	}

	private void processInput() {
		updateSlider();
		moveOrientableObject(camera, VK_UP, VK_DOWN, VK_LEFT, VK_RIGHT, VK_C, VK_X);
		moveObject(light, VK_D, VK_A, VK_E, VK_Q, VK_S, VK_W);
		if (keyboard.wasPressed(VK_T))
			GlUtil.toggleWireframeMode();
		if (keyboard.wasPressed(VK_Y))
			GlUtil.toggleProjectionMode();
		if (keyboard.wasPressed(VK_U))
			GlUtil.toggleShadeModel();
		if (keyboard.wasPressed(VK_I))
			GlUtil.toggleEnabled(GL.GL_LIGHTING);
		if (keyboard.wasPressed(VK_O))
			GlUtil.toggleEnabled(GL.GL_AUTO_NORMAL);
		if (keyboard.wasPressed(VK_P))
			saveScreenshot();
		if (keyboard.wasPressed(VK_R))
			reset();
		world.processInput(mouse);
	}

	private void moveOrientableObject(MovableOrientable object, int xp, int xn, int yp, int yn, int zp, int zn) {
		if (keyboard.isDown(zp))
			object.advance(moveSpeed * elapsed);
		if (keyboard.isDown(zn))
			object.advance(-moveSpeed * elapsed);
		if (keyboard.isDown(yp))
			object.yawAroundOtherUpVector(-rotSpeed * elapsed, Vector3.Y);
		if (keyboard.isDown(yn))
			object.yawAroundOtherUpVector(rotSpeed * elapsed, Vector3.Y);
		if (keyboard.isDown(xp))
			object.pitch(rotSpeed * elapsed);
		if (keyboard.isDown(xn))
			object.pitch(-rotSpeed * elapsed);
	}

	private void moveObject(Movable object, int xp, int xn, int yp, int yn, int zp, int zn) {
		Vector3 delta = Vector3.ZERO;
		int[] keys = new int[]{xp, xn, yp, yn, zp, zn};
		Vector3[] dirs = new Vector3[]{Vector3.X, Vector3.NX, Vector3.Y, Vector3.NY, Vector3.Z, Vector3.NZ};
		for (int i = 0; i < keys.length; i++) {
			if (keyboard.isDown(keys[i])) {
				delta = delta.add(dirs[i]);
			}
		}
		object.move(delta.mul(elapsed * moveSpeed));
	}

	private void saveScreenshot() {
		File file = new File("screenshots", StringUtil.currentTimestamp() + ".jpg");
		GlUtil.saveScreenshot(file);
		log.info("saved screenshot " + file);
	}


	private void drawCoordinateSystem() {
		colorProgram.use();
		final double axisLength = 20.0;
		Material.RED.use();
		drawLine(Vector3.NX.mul(axisLength), Vector3.X.mul(axisLength));
		Material.GREEN.use();
		drawLine(Vector3.NY.mul(axisLength), Vector3.Y.mul(axisLength));
		Material.BLUE.use();
		drawLine(Vector3.NZ.mul(axisLength), Vector3.Z.mul(axisLength));
	}

	private void drawLine(Vector3 a, Vector3 b) {
		gl.glBegin(GL.GL_LINES);
		a.gl();
		b.gl();
		gl.glEnd();
	}

	private void updateSlider() {
		if (keyboard.isDown(VK_F))
			slider.progress(-elapsed);
		if (keyboard.isDown(VK_G))
			slider.progress(elapsed);
		if (keyboard.isDown(VK_V))
			sliderb.progress(-elapsed);
		if (keyboard.isDown(VK_B))
			sliderb.progress(elapsed);
	}


}
