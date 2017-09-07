package de.sscholz.iat.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class Keyboard implements KeyListener {
	private final Map<Integer, ButtonState> keyMap = new HashMap<>();

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyMap.put(e.getKeyCode(), ButtonState.PRESSED);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyMap.put(e.getKeyCode(), ButtonState.RELEASED);
	}

	public boolean wasPressed(int keycode) {
		if (!keyMap.containsKey(keycode)) {
			return false;
		}
		ButtonState state = keyMap.get(keycode);
		if (state == ButtonState.PRESSED) {
			keyMap.put(keycode, ButtonState.DOWN);
			return true;
		}
		return false;
	}

	public boolean isDown(int keycode) {
		if (!keyMap.containsKey(keycode)) {
			return false;
		}
		ButtonState state = keyMap.get(keycode);
		return state == ButtonState.DOWN || state == ButtonState.PRESSED;
	}


}
