package de.sscholz.iat.gfx;

import javax.swing.*;
import java.awt.*;

public class Hud extends Container {

	public static final Hud instance = new Hud();

	public final JLabel left = new JLabel("left", SwingConstants.LEFT);
	public final JLabel right = new JLabel("right", SwingConstants.RIGHT);

	public Hud() {
		setLayout(new BorderLayout());
		left.setAlignmentX(Component.LEFT_ALIGNMENT);
		right.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(left, BorderLayout.LINE_START);
		add(right, BorderLayout.LINE_END);
		setMaximumSize(new Dimension(getMaximumSize().width, getMinimumSize().height));
	}

}
