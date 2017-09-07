package de.sscholz.iat.game;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.gfx.Hud;
import de.sscholz.iat.gfx.model.Model;
import de.sscholz.iat.gfx.model.ModelManager;
import de.sscholz.iat.gfx.model.Renderable;
import de.sscholz.iat.gfx.shader.uniform.UniformManager;
import de.sscholz.iat.input.Mouse;
import de.sscholz.iat.input.MouseButton;
import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Matrix;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.log.Logger;

import java.text.DecimalFormat;

import static java.awt.event.MouseEvent.BUTTON1;

public class World implements Renderable, Logger {

	private Level level;
	private Player player;
	private WorldTime worldTime = new WorldTime();
	private Vector2 aimingDirection;

	public World(Level level) {
		this.level = level;
		Model playerModel = ModelManager.instance.get("cylinder.off");
		player = new Player(playerModel);
		Hud.instance.right.setText("The Beginning of Things");
	}

	@Override
	public void render() {
		level.render();
		player.render();
		if (aimingDirection != null) {
			Model aimingModel = ModelManager.instance.get("cylinder.off");
			Material.MELLOW.use();
			double radius = .5;
			aimingModel.transform(Matrix.scaling(new Vector3(radius, .01, radius)));
			aimingModel.transform(Matrix.rotation(Vector3.X, Math.PI/2));
			aimingModel.transform(Matrix.translation(aimingDirection.to3()));
			aimingModel.render();
		}
	}

	public void update(double elapsed) {
		worldTime.advance(elapsed);
		DecimalFormat df = new DecimalFormat("#.#");
		Hud.instance.left.setText(df.format(worldTime.get()));
		player.update(elapsed);
		level.update(new UpdateInformation(worldTime, player.getBall()));
		level.adjustPlayer(player);
	}

	public void processInput(Mouse mouse) {
		aimingDirection = null;
		MouseButton button1 = mouse.getButton(BUTTON1);

		Matrix viewProjection = UniformManager.instance.getViewProjectionMatrix();
		Vector2 projectedPlayerPosition = MathUtil.getScreenCoordinates(player.getPosition().to3(), viewProjection,
				GlUtil.getAspect());
		if (projectedPlayerPosition != null) {
			if (button1.wasReleased()) {
				shootFromTo(projectedPlayerPosition, mouse.getCurrentNormalizedScreenCoordinates());
			} else if (button1.isDown()) {
				Vector2 diff = mouse.getCurrentNormalizedScreenCoordinates().sub(projectedPlayerPosition);
				aimingDirection = diff.normalize().mul(5.0).add(player.getPosition());
			}
		}
	}

	private void shootFromTo(Vector2 projectedPlayerPosition, Vector2 normalizedMousePosition) {
		Vector2 delta = normalizedMousePosition.sub(projectedPlayerPosition);
		if (delta.length() != 0) {
			delta = delta.setLength(Player.SHOOT_SPEED);
			log.debug("player shoot delta: " + delta);
			player.shoot(delta);
		}
	}

	public void reset() {
		player.stopAt(Vector2.ZERO);
		worldTime.set(0.0);
	}

	public Player getPlayer() {
		return player;
	}
}
