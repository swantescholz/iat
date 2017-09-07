package de.sscholz.iat.input;

import de.sscholz.iat.math.Vector2;

public class MouseButton {
	ButtonState state = ButtonState.UP;
	Vector2 positionOnLastPress = null;

	public boolean isDown() {
		return state == ButtonState.DOWN || state == ButtonState.PRESSED;
	}

	public boolean wasPressed() {
		if (state == ButtonState.PRESSED) {
			state = ButtonState.DOWN;
			return true;
		}
		return false;
	}

	public boolean wasReleased() {
		if (state == ButtonState.RELEASED) {
			state = ButtonState.UP;
			return true;
		}
		return false;
	}

}
