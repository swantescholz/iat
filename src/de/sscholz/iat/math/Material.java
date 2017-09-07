package de.sscholz.iat.math;

import de.sscholz.iat.gfx.shader.uniform.UniformManager;

public class Material extends VectorSpace<Material> {

	public static final Material BLACK = new Material(Color.BLACK);
	public static final Material GRAY = new Material(Color.GRAY);
	public static final Material WHITE = new Material(Color.WHITE);
	public static final Material RED = new Material(Color.RED);
	public static final Material GREEN = new Material(Color.GREEN);
	public static final Material BLUE = new Material(Color.BLUE);
	public static final Material YELLOW = new Material(Color.YELLOW);
	public static final Material PURPLE = new Material(Color.PURPLE);
	public static final Material CYAN = new Material(Color.CYAN);
	public static final Material ORANGE = new Material(Color.ORANGE);
	public static final Material PINK = new Material(Color.PINK);
	public static final Material SKY = new Material(Color.SKY);
	public static final Material MELLOW = new Material(Color.MELLOW);
	public static final Material FOREST = new Material(Color.FOREST);
	public static final Material SILVER = new Material(Color.SILVER, 96.0);
	public static final Material GOLD = new Material(Color.GOLD, 96.0);
	public static final Material BRASS = new Material(new Color(0.33, 0.22, 0.03),
			new Color(0.78, 0.57, 0.11), new Color(0.99, .91, 0.81), 5.0);

	public final Color ambient;
	public final Color diffuse;
	public final Color specular;
	public final double shininess;

	public Material() {
		ambient = Color.WHITE;
		diffuse = Color.WHITE;
		specular = Color.WHITE;
		shininess = 64.0;
	}

	public Material(Color color) {
		this(color, color, color);
	}

	public Material(Color color, double shininess) {
		this(color, color, color, shininess);
	}

	public Material(Color ambient, Color diffuse, Color specular) {
		this(ambient, diffuse, specular, 64.0);
	}

	public Material(Color ambient, Color diffuse, Color specular, double shininess) {
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
		this.shininess = shininess;
	}

	@Override
	public Material negate() {
		return new Material(ambient.negate(), diffuse.negate(), specular.negate(), -shininess);
	}

	public Material add(Material b) {
		return new Material(
				ambient.add(b.ambient),
				diffuse.add(b.diffuse),
				specular.add(b.specular),
				shininess + b.shininess
		);
	}

	public Material sub(Material b) {
		return new Material(
				ambient.sub(b.ambient),
				diffuse.sub(b.diffuse),
				specular.sub(b.specular),
				shininess - b.shininess
		);
	}

	public Material mul(double s) {
		return new Material(
				ambient.mul(s),
				diffuse.mul(s),
				specular.mul(s),
				shininess * s
		);
	}

	@Override
	public boolean equal(Material that) {
		return ambient.equal(that.ambient) && diffuse.equal(that.diffuse) &&
				specular.equal(that.specular) && shininess == that.shininess;
	}

	@Override
	public boolean almostEqual(Material b) {
		return ambient.almostEqual(b.ambient) && diffuse.almostEqual(b.diffuse) &&
				specular.almostEqual(b.specular) && MathUtil.almostEqual(shininess, b.shininess);
	}

	public void use() {
		UniformManager.instance.material.setAndApplyValue(this);
	}

	public String toString() {
		return "[" + ambient + ", " + diffuse + ", " + specular + ", " + shininess + "]";
	}

}
