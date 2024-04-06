package fr.codex.naturalis.drawing;

import fr.codex.naturalis.corner.Ressource;

import java.awt.*;
import java.util.Objects;

public class RessourceDrawingSequence {
    private final Graphics2D graphics2D;
    int width;
    int height;
    int cornerSize;
    public Ressource ressource;
    public RessourceDrawingSequence(Graphics2D graphics2D, int width, Ressource ressource) {
        Objects.requireNonNull(graphics2D);
        Objects.requireNonNull(ressource);
        this.graphics2D = graphics2D;
        this.ressource = ressource;
        this.width = width;
        height = width / 2;
        cornerSize = (height / 2) - (height / 10);
    }
}
