package fr.codex.naturalis.placing;

import fr.codex.naturalis.card.Card;

import java.awt.*;
import java.util.*;
import java.util.List;

public record PlacingCorner(int x, int y, Card card, int width, String whichCorner) {
    public PlacingCorner {
        Objects.requireNonNull(card);
        Objects.requireNonNull(whichCorner);
        if (!(whichCorner.equals("TL") || whichCorner.equals("TR") || whichCorner.equals("BL") || whichCorner.equals("BR"))) throw new IllegalArgumentException();
    }

    public void drawPlacableCorner(Graphics2D graphics2D) {
        Objects.requireNonNull(graphics2D);
        int size = (width /4) - (width /20);
        int arc = (width /5);
        graphics2D.setColor(Color.CYAN);
        graphics2D.drawRoundRect(x,y,size,size,arc,arc);
    }

    public static Map<Point,PlacingCorner> getCornersList(ArrayList<Card> placedCards, int width, int height, int diff){
        Objects.requireNonNull(placedCards);
        var cornersMap = new HashMap<Point,PlacingCorner>();
        for (Card card : placedCards) {
            int xCoord = card.getXCoordinate();
            int yCoord = card.getYCoordinate();
            if (!card.topLeftObstructed()) cornersMap.put(new Point(xCoord,yCoord),new PlacingCorner(xCoord, yCoord, card, width,"TL"));
            if (!card.topRightObstructed()) cornersMap.put(new Point(xCoord+width-diff, yCoord),new PlacingCorner(xCoord+width-diff, yCoord, card, width,"TR"));
            if (!card.bottomLeftObstructed()) cornersMap.put(new Point(xCoord,yCoord+height-diff),new PlacingCorner(xCoord,yCoord+height-diff, card, width,"BL"));
            if (!card.bottomRightObstructed()) cornersMap.put(new Point(xCoord+width-diff,yCoord+height-diff),new PlacingCorner(xCoord+width-diff,yCoord+height-diff, card, width,"BR"));
        }
        checkEverything(cornersMap, width, diff);
        return Map.copyOf(cornersMap);
    }
    private static void checkEverything(Map<Point,PlacingCorner> cornersMap, int ratio, int diff) {
        Objects.requireNonNull(cornersMap);
        var pointList = List.copyOf(cornersMap.keySet());
        for (Point point:pointList) {
            if (cornersMap.containsKey(point)) {
                switch (cornersMap.get(point).whichCorner) {
                    case "TL" -> {
                        checkForTL(cornersMap, point, ratio, diff);
                        continue;
                    }
                    case "TR" -> {
                        checkForTR(cornersMap, point, ratio, diff);
                        continue;
                    }
                    case "BL" -> {
                        checkForBL(cornersMap, point, ratio, diff);
                        continue;
                    }
                    case "BR" -> {
                        checkForBR(cornersMap, point, ratio, diff);
                        continue;
                    }
                }
            }
        }
    }
    private static void checkForTL(Map<Point,PlacingCorner> cornersMap, Point br, int ratio, int diff) {
        Objects.requireNonNull(cornersMap);
        Objects.requireNonNull(br);
        Point tl = new Point(br.x-ratio+diff,br.y-ratio/2+diff);
        Point tr = new Point(br.x, br.y-ratio/2+diff);
        Point bl = new Point(br.x-ratio+diff, br.y);
        boolean doRemove = false;
        if (cornersMap.containsKey(tl) && cornersMap.get(tl).card().bottomRightObstructed()) {
            cornersMap.remove(tl);
            doRemove = true;
        }
        if (cornersMap.containsKey(tr) && cornersMap.get(tr).card().bottomLeftObstructed()) {
            cornersMap.remove(tr);
            doRemove = true;
        }
        if (cornersMap.containsKey(bl) && cornersMap.get(bl).card().topRightObstructed()) {
            cornersMap.remove(bl);
            doRemove = true;
        }
        if (doRemove) {
            cornersMap.remove(br);
        }
    }
    private static void checkForTR(Map<Point,PlacingCorner> cornersMap, Point bl, int ratio, int diff) {
        Objects.requireNonNull(cornersMap);
        Objects.requireNonNull(bl);
        Point tl = new Point(bl.x,bl.y-ratio+diff);
        Point tr = new Point(bl.x+ratio-diff,bl.y-ratio+diff);
        Point br = new Point(bl.x+ratio-diff, bl.y);
        boolean doRemove = false;
        if (cornersMap.containsKey(tl) && cornersMap.get(tl).card().bottomRightObstructed()) {
            cornersMap.remove(tl);
            doRemove = true;
        }
        if (cornersMap.containsKey(tr) && cornersMap.get(tr).card().bottomLeftObstructed()) {
            cornersMap.remove(tr);
            doRemove = true;
        }
        if (cornersMap.containsKey(br) && cornersMap.get(br).card().topLeftObstructed()) {
            cornersMap.remove(br);
            doRemove = true;
        }
        if (doRemove) {
             cornersMap.remove(bl);
        }
    }
    private static void checkForBL(Map<Point,PlacingCorner> cornersMap, Point tr, int ratio, int diff) {
        Objects.requireNonNull(cornersMap);
        Objects.requireNonNull(tr);
        Point tl = new Point(tr.x+ratio-diff, tr.y);
        Point bl = new Point(tr.x+ratio-diff, tr.y+ratio/2-diff);
        Point br = new Point(tr.x, tr.y+ratio/2-diff);
        boolean doRemove = false;
        if (cornersMap.containsKey(tl) && cornersMap.get(tl).card().bottomRightObstructed()) {
            cornersMap.remove(tl);
            doRemove = true;
        }
        if (cornersMap.containsKey(bl) && cornersMap.get(bl).card().topRightObstructed()) {
            cornersMap.remove(bl);
            doRemove = true;
        }
        if (cornersMap.containsKey(br) && cornersMap.get(br).card().topLeftObstructed()) {
            cornersMap.remove(br);
            doRemove = true;
        }
        if (doRemove) {
            cornersMap.remove(tr);
        }
    }
    private static void checkForBR(Map<Point,PlacingCorner> cornersMap, Point tl, int ratio, int diff) {
        Objects.requireNonNull(cornersMap);
        Objects.requireNonNull(tl);
        Point tr = new Point(tl.x+ratio-diff, tl.y);
        Point bl = new Point(tl.x, tl.y+ratio/2-diff);
        Point br = new Point(tl.x+ratio-diff, tl.y+ratio/2-diff);
        boolean doRemove = false;
        if (cornersMap.containsKey(tr) && cornersMap.get(tr).card().bottomLeftObstructed()) {
            cornersMap.remove(tr);
            doRemove = true;
        }
        if (cornersMap.containsKey(bl) && cornersMap.get(bl).card().topRightObstructed()) {
            cornersMap.remove(bl);
            doRemove = true;
        }
        if (cornersMap.containsKey(br) && cornersMap.get(br).card().topLeftObstructed()) {
            cornersMap.remove(br);
            doRemove = true;
        }
        if (doRemove) {
            cornersMap.remove(tl);
        }
    }
}
