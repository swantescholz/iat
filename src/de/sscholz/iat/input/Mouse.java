package de.sscholz.iat.input;

import de.sscholz.iat.gfx.GlUtil;
import de.sscholz.iat.math.Dimension;
import de.sscholz.iat.math.Vector2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener {

	private final Map<Integer, MouseButton> buttonMap = new HashMap<>();
	private Vector2 currentPosition = Vector2.ZERO;

	@Override
	public void mousePressed(MouseEvent e) {
		MouseButton button = getButton(e.getButton());
		button.state = ButtonState.PRESSED;
		button.positionOnLastPress = getMouseLocation(e);
	}

	private Vector2 getMouseLocation(MouseEvent e) {
		return new Vector2(e.getX(), e.getY());
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		MouseButton button = getButton(e.getButton());
		button.state = ButtonState.RELEASED;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	private void checkButton(int buttonId) {
		if (!buttonMap.containsKey(buttonId)) {
			buttonMap.put(buttonId, new MouseButton());
		}
	}

	public MouseButton getButton(int buttonId) {
		checkButton(buttonId);
		return buttonMap.get(buttonId);
	}

	public boolean wasPressed(int buttonId) {
		checkButton(buttonId);
		return buttonMap.get(buttonId).wasPressed();
	}

	public boolean wasReleased(int buttonId) {
		checkButton(buttonId);
		return buttonMap.get(buttonId).wasReleased();
	}

	public boolean isDown(int buttonId) {
		checkButton(buttonId);
		return buttonMap.get(buttonId).isDown();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		currentPosition = getMouseLocation(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		currentPosition = getMouseLocation(e);
	}

	public Vector2 getCurrentNormalizedScreenCoordinates() {
		Dimension dimension = GlUtil.getDimension();
		double w = dimension.width;
		double h = dimension.height;
		double x = currentPosition.x/(w-1) - .5;
		double y = ((h-currentPosition.y)/(h-1) - .5) * (h/w);
		return new Vector2(x,y);
	}


}
