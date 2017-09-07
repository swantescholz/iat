package de.sscholz.iat.game;

import de.sscholz.iat.math.MathUtil;
import de.sscholz.iat.math.Vector2;
import de.sscholz.iat.util.log.Asserter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LevelFactory implements Asserter {

	public static final LevelFactory instance = new LevelFactory();

	private Vector2 offset = Vector2.ZERO;
	private Vector2 scale = Vector2.XY;
	private RedDoor lastRedDoor;

	public Level loadFile(File levelFile) {
		resetScaleAndOffset();
		List<LevelElement> elements = new ArrayList<>();
		try {
			Files.lines(levelFile.toPath()).forEach(it -> {
				LevelElement element = readElementLine(it);
				if (element != null) {
					elements.add(element);
				}
			});
		} catch (IOException e) {
			log.warn(e);
			log.error("Level loading failed.");
		}

		return new Level(elements);
	}

	private void resetScaleAndOffset() {
		offset = Vector2.ZERO;
		scale = Vector2.XY;
	}

	private LevelElement readElementLine(String line) {
		String[] tokens = line.split(" ");
		switch (tokens[0].toLowerCase()) {
			case "offset":
				offset = offset.add(parseVector2(tokens[1]));
				break;
			case "scale":
				scale = scale.mul(parseVector2(tokens[1]));
				break;
			case "da": return readRedDoor(tokens);
			case "db": return readBlueDoor(tokens);
			case "h": return readHourGlass(tokens);
			case "r": return readRock(tokens);
		}
		return null;
	}

	private HourGlass readHourGlass(String[] tokens) {
		Vector2 position = parseVector2(tokens[1]);
		assertTrue(lastRedDoor != null);
		return new HourGlass(position, lastRedDoor);
	}

	private Rock readRock(String[] tokens) {
		List<Vector2> coordinates = new ArrayList<>();
		for (int i = 1; i < tokens.length; i++) {
			try {
				coordinates.add(transformCoordinate(parseVector2(tokens[i])));
			} catch (Exception e) {}
		}
		if (MathUtil.isPolygonClockwise(coordinates)) {
			Collections.reverse(coordinates); //cw -> ccw
		}
		return new Rock(coordinates);
	}

	private RedDoor readRedDoor(String[] tokens) {
		double timeLimit = Double.parseDouble(tokens[1]);
		lastRedDoor = new RedDoor(timeLimit, transformCoordinate(parseVector2(tokens[2])));
		return lastRedDoor;
	}

	private BlueDoor readBlueDoor(String[] tokens) {
		double timeLimit = Double.parseDouble(tokens[1]);
		return new BlueDoor(timeLimit, transformCoordinate(parseVector2(tokens[2])));
	}

	private Vector2 parseVector2(String twoCommaSeperatedDoubles) {
		String[] parts = twoCommaSeperatedDoubles.split(",");
		return new Vector2(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
	}

	private Vector2 transformCoordinate(Vector2 coordinate) {
		return coordinate.mul(scale).add(offset);
	}

}
