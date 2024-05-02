package fr.codex.naturalis.card;

import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class RessourceCard implements Card {
    private final Ressource type;
    private final Corner topLeft;
    private final Corner topRight;
    private final Corner bottomLeft;
    private final Corner bottomRight;
    public boolean topLeftObstructed;
    public boolean topRightObstructed;
    public boolean bottomLeftObstructed;
    public boolean bottomRightObstructed;
    private final int point;
    private boolean isVerso;
    private Point coordinate;
    private boolean isPlaced;


    public RessourceCard(Ressource type, Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight, int point) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(topLeft);
        Objects.requireNonNull(topRight);
        Objects.requireNonNull(bottomLeft);
        Objects.requireNonNull(bottomRight);
        this.type = type;

        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;

        topLeftObstructed = false;
        topRightObstructed = false;
        bottomLeftObstructed = false;
        bottomRightObstructed = false;

        if (point < 0) { point = 0;}
        if (point > 5) { point = 5;}
        this.point = point;

        this.isVerso = false; //for later
        this.isPlaced = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RessourceCard that = (RessourceCard) o;
        return point == that.point && Objects.equals(type, that.type) && Objects.equals(topLeft, that.topLeft) && Objects.equals(topRight, that.topRight) && Objects.equals(bottomLeft, that.bottomLeft) && Objects.equals(bottomRight, that.bottomRight);
    }
    @Override
    public int hashCode() {
        return Objects.hash(type, topLeft, topRight, bottomLeft, bottomRight, point);
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
    @Override
    public String toString() {
        var str = new StringBuilder("Card :\n");
        str.append(topLeft).append(" | ").append(topRight).append("\n");
        str.append("- - - - ").append(point).append("pts").append(" - - - -\n");
        str.append(bottomLeft).append(" | ").append(bottomRight).append("\n");
        return str.toString();
    }
    @Override
    public void changeCoordinates(int x, int y) {
        if (!isPlaced) {
            this.coordinate = new Point(x, y);
        }
    }
    @Override
    public void place(int x, int y) {
        changeCoordinates(x, y);
        isPlaced = true;
        if (topLeft != null) topLeft.increase();
        if (topRight != null) topRight.increase();
        if (bottomLeft != null) bottomLeft.increase();
        if (bottomRight != null) bottomRight.increase();
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
    public Color getSecondaryColor() {
        return type.getSecondaryColor();
    }
    public Color getTertiaryColor() {
        return type.getTertiaryColor();
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
}
