package fr.codex.naturalis.card;

import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.drawing.CardDrawingSequence;

import java.awt.*;
import java.util.List;

public interface Card {
    public boolean topLeftObstructed();
    public boolean topRightObstructed();
    public boolean bottomLeftObstructed();
    public boolean bottomRightObstructed();
    public void setTopLeftObstruction(boolean status);
    public void setTopRightObstruction(boolean status);
    public void setBottomLeftObstruction(boolean status);
    public void setBottomRightObstruction(boolean status);
    /**
     * Change the coordinates of the card without placing it.
     *
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     */
    public void changeCoordinates(int x, int y);
    /**
     * Change the coordinates of the card and place it, making it unmovable.
     *
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     */
    public void place(int x, int y);
    /**
     * get the X coordinate of the card;
     *
     * @return the card's x coordinate;
     */
    public int getXCoordinate();
    /**
     * get the Y coordinate of the card;
     *
     * @return the card's y coordinate;
     */
    public int getYCoordinate();
    /**
     * get the card's main color based on it's ressource type.
     *
     * @return the card's main color.
     */
    public Color getColor();
    /**
     * Get a List of the card's corners in this order :
     * [topLeft, topRight, bottomLeft, bottomRight].
     *
     * @return a List of the card's corners.
     */
    public List<Corner> getAllCorners();
    /**
     * Returns the recto/verso status.
     * @return true=Verso, false=Recto;
     */
    public boolean isVerso();

    public void setVerso();

    public void setRecto();

    public void verso(CardDrawingSequence cds, int ratio, int cornerSize);
}

