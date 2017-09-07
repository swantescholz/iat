package de.sscholz.iat.game;

import de.sscholz.iat.JoglApplication;
import de.sscholz.iat.util.log.Log;
import de.sscholz.iat.util.log.Logger;

public class Game extends JoglApplication implements Logger {

	private final Scene scene = new Scene(keyboard, mouse);

	public static void mymain() {
		Log.DEFAULT.info("Starting...");
		Game template = new Game();
		template.setVisible(true);
	}

	@Override
	public void setUp() {
		scene.init();
		log.info(canvas.getWidth() + ", " + canvas.getHeight() + " canvas");
	}

	@Override
	public void draw() {
		scene.update();
		scene.render();
	}

}
