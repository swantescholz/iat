package de.sscholz.iat.game;

public class UpdateInformation {

	public final WorldTime worldTime;
	public final Ball ball;

	public UpdateInformation(WorldTime worldTime, Ball ball) {
		this.worldTime = worldTime;
		this.ball = ball;
	}
}
