package de.sscholz.iat.gfx.texture;

import de.sscholz.iat.util.ResourceManager;
import de.sscholz.iat.util.log.Logger;

import java.io.File;
import java.io.IOException;

public final class TextureManager extends ResourceManager<TextureData> implements Logger {


	public static final TextureManager instance = new TextureManager(new File("res/texture"), ".png");

	private TextureManager(File defaultLocation, String defaultFilenameEnding) {
		super(defaultLocation, defaultFilenameEnding);
	}

	public Texture get(String name) {
		return new Texture(getResource(name));
	}

	public Texture load(String filename) {
		File file = getFile(filename);
		try {
			PixelGrid grid = PixelGrid.createFromFile(file);
			addResource(filename, new TextureData(grid));
		} catch (IOException e) {
			log.warn("file " + file + " does not exist.");
			log.warn(e);
		}
		return get(filename);
	}

}
