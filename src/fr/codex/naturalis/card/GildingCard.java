package fr.codex.naturalis.card;

import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class GildingCard implements Card {
    private final Ressource type;
    private final Corner topLeft;
    private final Corner topRight;
    private final Corner bottomRight;
    private final Corner bottomLeft;
    public boolean topLeftObstructed;
    public boolean topRightObstructed;
    public boolean bottomLeftObstructed;
    public boolean bottomRightObstructed;
    private final int point;
    private final List<Ressource> cost;
    private final boolean specialPointSystem;
    private boolean isVerso;
    private Point coordinate;
    private boolean isPlaced;

    public GildingCard(Ressource type, int point, Corner topLeft, Corner topRight, Corner bottomRight, Corner bottomLeft, List<Ressource> cost) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(topLeft);
        Objects.requireNonNull(topRight);
        Objects.requireNonNull(bottomLeft);
        Objects.requireNonNull(bottomRight);
        Objects.requireNonNull(cost);

        this.type = type;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;

        topLeftObstructed = false;
        topRightObstructed = false;
        bottomLeftObstructed = false;
        bottomRightObstructed = false;

        this.cost = cost;

        if (point < 1) { point = 1;}
        if (point > 5) { point = 5;}
        this.point = point;
        this.specialPointSystem = false;

        this.isVerso = false; //for later
        this.isPlaced = false;
    }

    @Override
    public boolean topLeftObstructed() {
        return topLeftObstructed;
    }

    @Override
    public boolean topRightObstructed() {
        return topRightObstructed;
    }

    @Override
    public boolean bottomLeftObstructed() {
        return bottomLeftObstructed;
    }

    @Override
    public boolean bottomRightObstructed() {
        return bottomRightObstructed;
    }

    @Override
    public void setTopLeftObstruction(boolean status) {
        topLeftObstructed = status;
    }

    @Override
    public void setTopRightObstruction(boolean status) {
        topRightObstructed = status;
    }

    @Override
    public void setBottomLeftObstruction(boolean status) {
        bottomLeftObstructed = status;
    }

    @Override
    public void seBottomRightObstruction(boolean status) {
        bottomRightObstructed = status;
    }

    /**
     * Change the coordinates of the card without placing it.
     *
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     */
    @Override
    public void changeCoordinates(int x, int y) {
        if (!isPlaced) {
            this.coordinate = new Point(x, y);
        }
    }

    @Override
    public int getXCoordinate() {
        return coordinate.x;
    }

    @Override
    public int getYCoordinate() {
        return coordinate.y;
    }

    @Override
    public Color getColor() {
        return type.getMainColor();
    }

    @Override
    public List<Corner> getAllCorners() {
        return List.of(topLeft,topRight,bottomLeft,bottomRight);
    }

    @Override
    public boolean isVerso() {
        return isVerso;
    }

    public int getPoint() {
        return point;
    }

    /**
     * Change the coordinates of the card and place it, making it unmovable.
     *
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     */
    @Override
    public void place(int x, int y) {
        changeCoordinates(x, y);
        isPlaced = true;
        if (topLeft != null) topLeft.increase();
        if (topRight != null) topRight.increase();
        if (bottomRight != null) bottomRight.increase();
        if (bottomLeft != null) bottomLeft.increase();
    }
}
