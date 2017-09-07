package de.sscholz.iat.editor;

import de.sscholz.iat.JoglApplication;
import de.sscholz.iat.game.Scene;
import de.sscholz.iat.util.log.Log;
import de.sscholz.iat.util.log.Logger;

public class Editor extends JoglApplication implements Logger {

	private final Scene scene = new Scene(keyboard, mouse);

	public static void mymain() {
		Log.DEFAULT.info("Starting Editor...");
		Editor template = new Editor();
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
