package fr.codex.naturalis.drawing;

import java.awt.*;
import java.util.Objects;

public class DeckDrawingSequence {
    private final Graphics2D graphics2D;
    int width;
    int height;
    private Color bgColor;
    private Color oLColor;
    public DeckDrawingSequence(Graphics2D graphics2D, int width, int height, Color bgColor, Color oLColor) {
        Objects.requireNonNull(graphics2D);
        this.graphics2D = graphics2D;
        this.width = width;
        this.height = height;
        this.bgColor = bgColor;
        this.oLColor = oLColor;

    }
    public void drawDeckLine(int x, int y) {
        graphics2D.setColor(oLColor);
        graphics2D.fillRect(x, y, width, height/32);
    }
    public void drawDeck(int x, int y) {
        graphics2D.setColor(bgColor);
        graphics2D.fillRect(x, y, width, height/3);
    }
}
