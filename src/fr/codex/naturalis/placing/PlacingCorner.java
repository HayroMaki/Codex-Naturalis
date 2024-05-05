package fr.codex.naturalis.placing;

import fr.codex.naturalis.card.Card;

import javax.swing.text.Segment;
import java.awt.*;
import java.util.*;
import java.util.List;

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

    public static Map<Point,PlacingCorner> getCornersList(ArrayList<Card> placedCards, int width, int height, int diff, int ratio){
        var cornersMap = new HashMap<Point,PlacingCorner>();
        for (Card card : placedCards) {
            int xCoord = card.getXCoordinate();
            int yCoord = card.getYCoordinate();
            if (!card.topLeftObstructed()) cornersMap.put(new Point(xCoord,yCoord),new PlacingCorner(xCoord, yCoord, card, ratio,"TL"));
            if (!card.topRightObstructed()) cornersMap.put(new Point(xCoord+width-diff, yCoord),new PlacingCorner(xCoord+width-diff, yCoord, card, ratio,"TR"));
            if (!card.bottomLeftObstructed()) cornersMap.put(new Point(xCoord,yCoord+height-diff),new PlacingCorner(xCoord,yCoord+height-diff, card, ratio,"BL"));
            if (!card.bottomRightObstructed()) cornersMap.put(new Point(xCoord+width-diff,yCoord+height-diff),new PlacingCorner(xCoord+width-diff,yCoord+height-diff, card, ratio,"BR"));
        }
        // go through the Map and check for each corner, depending on "whichCorner", the 3 other corners;
        return Map.copyOf(cornersMap);
    }
}
