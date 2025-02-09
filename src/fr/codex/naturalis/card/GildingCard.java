package fr.codex.naturalis.card;

import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;
import fr.codex.naturalis.drawing.CardDrawingSequence;

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

    public GildingCard(Ressource type, int point, Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight, List<Ressource> cost) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GildingCard that = (GildingCard) o;
        return point == that.point && specialPointSystem == that.specialPointSystem && Objects.equals(type, that.type) && Objects.equals(topLeft, that.topLeft) && Objects.equals(topRight, that.topRight) && Objects.equals(bottomRight, that.bottomRight) && Objects.equals(bottomLeft, that.bottomLeft) && Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, topLeft, topRight, bottomRight, bottomLeft, point, cost, specialPointSystem);
    }

    @Override
    public String toString() {
        var str = new StringBuilder("Card :\n");
        str.append(topLeft).append(" | ").append(topRight).append("\n");
        str.append("- - - - ").append(point).append("pts").append(" - - - -\n");
        str.append(bottomLeft).append(" | ").append(bottomRight).append("\n");
        str.append(cost).append("\n");
        return str.toString();
    }

    @Override
    public boolean topLeftObstructed() {
        return (topLeftObstructed || topLeft.equals(Corner.invisible));
    }
    @Override
    public boolean topRightObstructed() {
        return (topRightObstructed || topRight.equals(Corner.invisible));
    }
    @Override
    public boolean bottomLeftObstructed() {
        return (bottomLeftObstructed || bottomLeft.equals(Corner.invisible));
    }
    @Override
    public boolean bottomRightObstructed() {
        return (bottomRightObstructed || bottomRight.equals(Corner.invisible));
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
    public void setBottomRightObstruction(boolean status) {
        bottomRightObstructed = status;
    }
    /**
     * Change the coordinates of the card without placing it.
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
        if (!isVerso) {
            return List.of(topLeft,topRight,bottomLeft,bottomRight);
        }
        return List.of(Corner.empty,Corner.empty,Corner.empty,Corner.empty);
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
        topLeft.increase();
        topRight.increase();
        bottomRight.increase();
        bottomLeft.increase();
    }
    @Override
    public void setVerso() { isVerso = true; }
    @Override
    public void setRecto() { isVerso = false; }

    public Ressource getType() {
        return type;
    }

    public void verso(CardDrawingSequence cds, int ratio, int cornerSize) {
        cds.drawRessource(type, getXCoordinate()+ratio/2-cornerSize/4, getYCoordinate()+ratio/4-cornerSize/4);
    }
}
