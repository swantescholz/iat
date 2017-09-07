package de.sscholz.iat.game;

import de.sscholz.iat.math.Material;
import de.sscholz.iat.math.Vector2;

public class BlueDoor extends Door {

	public BlueDoor(double timeLimit, Vector2 position) {
		super(timeLimit, position);
	}

	@Override
	public void update() {
		double worldTime = info.worldTime.get();
		if (open) {
			if (worldTime < timeLimit && ballAwayFromDoor(info.ball)) {
				open = false;
			}
		} else {
			open = worldTime >= timeLimit;
		}
	}

	@Override
	Material getClosedMaterial() {
		return Material.BLUE;
	}

}
