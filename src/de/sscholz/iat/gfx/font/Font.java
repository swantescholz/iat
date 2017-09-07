package de.sscholz.iat.gfx.font;

import de.sscholz.iat.gfx.texture.PixelGrid;
import de.sscholz.iat.gfx.texture.Texture;
import de.sscholz.iat.gfx.texture.TextureData;
import de.sscholz.iat.math.Color;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Font {

	final java.awt.Font font;

	public Font(int size) {
		font = new java.awt.Font("Serif", java.awt.Font.PLAIN, size);
	}

	public FontRectangle createTexture(String text, Color color) {
		FontRenderContext frc = new FontRenderContext(null, true, true);

		Rectangle2D bounds = font.getStringBounds(text, frc);
		int w = (int) bounds.getWidth();
		int h = (int) bounds.getHeight();

		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		g.setColor(Color.ZERO.toAwtColor());
		g.fillRect(0, 0, w, h);
		g.setColor(color.toAwtColor());
		g.setFont(font);
		g.drawString(text, (float) bounds.getX(), (float) -bounds.getY());
		g.dispose();

		PixelGrid grid = PixelGrid.createFromBufferedImage(image);
		TextureData data = new TextureData(grid);
		return new FontRectangle(new Texture(data), w/((double)h));
	}

}
