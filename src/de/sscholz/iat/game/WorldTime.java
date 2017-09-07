package de.sscholz.iat.game;

public class WorldTime {

	private double time = 0.0;

	public WorldTime() {

	}

	public void advance(double delta) {
		time += delta;
	}

	public void rewind(double delta) {
		advance(-delta);
	}

	public void set(double newTime) {
		time = newTime;
	}

	public double get() {
		return time;
	}

}
