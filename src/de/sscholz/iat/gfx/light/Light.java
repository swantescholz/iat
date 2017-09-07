package de.sscholz.iat.gfx.light;

import de.sscholz.iat.gfx.shader.uniform.UniformManager;
import de.sscholz.iat.math.Color;
import de.sscholz.iat.util.IndexPool;

public abstract class Light implements ILight {

	public static final int MAX_NUMBER = 4;

	private static final IndexPool INDEX_POOL = new IndexPool();

	static {
		for (int i = 0; i < MAX_NUMBER; i++) {
			INDEX_POOL.add(i);
		}
	}

	public Color ambient = Color.WHITE;
	public Color diffuse = Color.WHITE;
	public Color specular = Color.WHITE;
	protected int id = -1;
	private boolean enabled = false;

	protected Light() {
		setEnabled(true);
	}

	public void shine() {
		UniformManager.instance.applyLight(this);
	}




	public void setEnabled(boolean enabled) {
		IndexPool pool = INDEX_POOL;
		if (this.enabled == enabled) {
			return;
		} else if (enabled) {
			id = INDEX_POOL.acquire();
		} else {
			INDEX_POOL.release(id);
		}
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public Color getAmbient() {
		return ambient;
	}

	@Override
	public Color getDiffuse() {
		return diffuse;
	}

	@Override
	public Color getSpecular() {
		return specular;
	}
}
