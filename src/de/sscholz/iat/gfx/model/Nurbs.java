package de.sscholz.iat.gfx.model;

import de.sscholz.iat.math.Triangle;
import de.sscholz.iat.math.Vector3;
import de.sscholz.iat.util.Array2;
import de.sscholz.iat.util.log.Logger;

import java.util.List;

public class Nurbs extends RenderableObject implements Logger {

	private final int wSubDivCount;
	private final int hSubDivCount;
	private Array2<ControlPoint> controlPoints;
	private Grid grid;
	private final Vbo vbo = new Vbo();

	private class ControlPoint {
		Vector3 position = new Vector3();
		Vector3 tangentW = new Vector3();
		Vector3 tangentH = new Vector3();
	}

	public Nurbs(int wSubDivCount, int hSubDivCount) {
		this.wSubDivCount = wSubDivCount;
		this.hSubDivCount = hSubDivCount;
	}

	public void create(Grid controlPointGrid) {
		dispose();
		int nodew = controlPointGrid.getWidth();
		int nodeh = controlPointGrid.getHeight();
		controlPoints = new Array2<>(nodew, nodeh);
		for (int y = 0; y < nodeh; y++) {
			for (int x = 0; x < nodew; x++) {
				ControlPoint point = new ControlPoint();
				point.position = controlPointGrid.getNode(x, y).position;
				controlPoints.set(x, y, point);
			}
		}
		computeTangentsCatmull(0.5);
		createGrid();
		fillVbo();
	}

	@Override
	protected void draw() {
		vbo.render();
	}

	public void dispose() {
		vbo.dispose();
	}

	private void fillVbo() {
		List<Triangle> triangles = grid.createTriangles(true);
		log.debug("NURBS creation, #triangles: " + triangles.size());
		vbo.create(triangles);
	}

	private void createGrid() {
		final int gridWidth = computeGridWidth();
		final int gridHeight = computeGridHeight();
		grid = new Grid(gridWidth, gridHeight);
		fillGrid();
	}

	private int computeGridHeight() {
		return (controlPoints.getHeight() - 3) * (hSubDivCount + 1) + 1;
	}

	private int computeGridWidth() {
		return (controlPoints.getWidth() - 3) * (wSubDivCount + 1) + 1;
	}

	private void fillGrid() {
		for (int y = 1; y < controlPoints.getHeight() - 2; y++) {
			for (int x = 1; x < controlPoints.getWidth() - 2; x++) {
				fillGridQuad(x, y);
			}
		}
	}

	private void fillGridQuad(int x, int y) {
		ControlPoint uplt = controlPoints.get(x, y);
		ControlPoint uprt = controlPoints.get(x + 1, y);
		ControlPoint dnlt = controlPoints.get(x, y + 1);
		ControlPoint dnrt = controlPoints.get(x + 1, y + 1);
		int gridxa = (x - 1) * (wSubDivCount + 1);
		int gridxb = x * (wSubDivCount + 1);
		int gridya = (y - 1) * (hSubDivCount + 1);
		int gridyb = y * (hSubDivCount + 1);
		if (x + 3 == controlPoints.getWidth()) {
			gridxb += 1;
		}
		if (y + 3 == controlPoints.getHeight()) {
			gridyb += 1;
		}
		for (int gridx = gridxa; gridx < gridxb; gridx++) {
			for (int gridy = gridya; gridy < gridyb; gridy++) {
				double fx = (double) (gridx - gridxa) / (wSubDivCount + 1);
				double fy = (double) (gridy - gridya) / (hSubDivCount + 1);
				Vector3 gridPoint = interpolate(uplt, uprt, dnlt, dnrt, fx, fy);
				grid.getNode(gridx, gridy).position = gridPoint;
			}
		}
	}

	private Vector3 interpolate(ControlPoint uplt, ControlPoint uprt, ControlPoint dnlt, ControlPoint dnrt, double fx, double fy) {
		Vector3 posupm = Vector3.interpolateHermite(uplt.position, uplt.tangentW,
				uprt.position, uprt.tangentW, fx);
		Vector3 posdnm = Vector3.interpolateHermite(dnlt.position, dnlt.tangentW,
				dnrt.position, dnrt.tangentW, fx);
		Vector3 tangentupm = uplt.tangentH.interpolateLinear(uprt.tangentH, fx);
		Vector3 tangentdnm = dnlt.tangentH.interpolateLinear(dnrt.tangentH, fx);
		return Vector3.interpolateHermite(posupm, tangentupm, posdnm, tangentdnm, fy);
	}

	private void computeTangentsCatmull(final double factor) {
		for (int y = 1; y < controlPoints.getHeight() - 1; y++) {
			for (int x = 1; x < controlPoints.getWidth() - 1; x++) {
				ControlPoint point = controlPoints.get(x, y);
				ControlPoint north = controlPoints.get(x, y - 1);
				ControlPoint east = controlPoints.get(x + 1, y);
				ControlPoint south = controlPoints.get(x, y + 1);
				ControlPoint west = controlPoints.get(x - 1, y);
				point.tangentW = east.position.sub(west.position).mul(factor);
				point.tangentH = south.position.sub(north.position).mul(factor);
			}
		}
	}


}
