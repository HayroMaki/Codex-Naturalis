package fr.codex.naturalis.placing;

import fr.codex.naturalis.card.Card;

import javax.swing.text.Segment;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record PlacingCorner(int x, int y, Card card, int ratio, String whichCorner) {
    public PlacingCorner {
        Objects.requireNonNull(card);
        Objects.requireNonNull(whichCorner);
        if (!(whichCorner.equals("TL") || whichCorner.equals("TR") || whichCorner.equals("BL") || whichCorner.equals("BR"))) throw new IllegalArgumentException();
    }

    public void drawPlacableCorner(Graphics2D graphics2D) {
        Objects.requireNonNull(graphics2D);
        int size = (ratio/4) - (ratio/20);
        int arc = (ratio/5);
        graphics2D.setColor(Color.CYAN);
        graphics2D.drawRoundRect(x,y,size,size,arc,arc);
    }

    public static List<PlacingCorner> getCornersList(ArrayList<Card> placedCards, int width, int height, int diff, int ratio){
        var corners = new ArrayList<PlacingCorner>();
        for (Card card : placedCards) {
            int xCoord = card.getXCoordinate();
            int yCoord = card.getYCoordinate();
            if (card.topLeftObstructed()) corners.add(new PlacingCorner(xCoord, yCoord, card, ratio,"TL"));
            if (card.topRightObstructed()) corners.add(new PlacingCorner(xCoord+width-diff, yCoord, card, ratio,"TR"));
            if (card.bottomLeftObstructed()) corners.add(new PlacingCorner(xCoord,yCoord+height-diff, card, ratio,"BL"));
            if (card.bottomRightObstructed()) corners.add(new PlacingCorner(xCoord+width-diff,yCoord+height-diff, card, ratio,"BR"));
        }
        return List.copyOf(corners);
    }
}
