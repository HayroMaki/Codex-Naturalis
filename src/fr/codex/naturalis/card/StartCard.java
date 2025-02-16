package fr.codex.naturalis.card;

import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;
import fr.codex.naturalis.drawing.CardDrawingSequence;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class StartCard implements Card {
    private final Ressource topLeft;
    private final Ressource topRight;
    private final Ressource bottomRight;
    private final Ressource bottomLeft;
    public boolean topLeftObstructed;
    public boolean topRightObstructed;
    public boolean bottomLeftObstructed;
    public boolean bottomRightObstructed;
    private final Color color;
    private boolean isVerso;
    private Point coordinate;
    private boolean isPlaced;

    public StartCard(Ressource topLeft, Ressource topRight, Ressource bottomLeft, Ressource bottomRight) {
        Objects.requireNonNull(topLeft);
        Objects.requireNonNull(topRight);
        Objects.requireNonNull(bottomRight);
        Objects.requireNonNull(bottomLeft);

        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomRight = bottomRight;
        this.bottomLeft = bottomLeft;

        topLeftObstructed = false;
        topRightObstructed = false;
        bottomLeftObstructed = false;
        bottomRightObstructed = false;

        this.color = new Color(228, 219, 180);

        this.isVerso = false; //for later
        this.isPlaced = false;

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StartCard startCard = (StartCard) o;
        return Objects.equals(topLeft, startCard.topLeft) && Objects.equals(topRight, startCard.topRight) && Objects.equals(bottomRight, startCard.bottomRight) && Objects.equals(bottomLeft, startCard.bottomLeft);
    }
    @Override
    public int hashCode() {
        return Objects.hash(topLeft, topRight, bottomRight, bottomLeft);
    }
    @Override
    public String toString() {
        return "\nStartCard{" +
                "topLeft=" + topLeft +
                ", topRight=" + topRight +
                ", bottomRight=" + bottomRight +
                ", bottomLeft=" + bottomLeft +
                "}";
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
    public void setBottomRightObstruction(boolean status) {
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
        return color;
    }
    @Override
    public List<Corner> getAllCorners() {
        return List.of(topLeft,topRight,bottomLeft,bottomRight);
    }
    @Override
    public boolean isVerso() {
        return isVerso;
    }

    @Override
    public void setVerso() { isVerso = true; }
    @Override
    public void setRecto() { isVerso = false; }

    @Override
    public void verso(CardDrawingSequence cds, int ratio, int cornerSize) {
        //Later
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
}
