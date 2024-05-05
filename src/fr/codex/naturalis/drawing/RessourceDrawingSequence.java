package fr.codex.naturalis.drawing;

import fr.codex.naturalis.corner.Artifact;
import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;

import java.awt.*;
import java.util.List;
import java.util.Objects;

public class RessourceDrawingSequence {
    private final Graphics2D graphics2D;
    private final int width;
    int height;
    int cornerSize;
    int diff;
    public RessourceDrawingSequence(Graphics2D graphics2D, int width) {
        Objects.requireNonNull(graphics2D);
        this.graphics2D = graphics2D;
        this.width = width;
        height = width / 2;
        cornerSize = (height / 2) - (height / 10);
        diff = cornerSize/8;
    }
    public void drawInformationBanner() {
        int x = width/2 - 7*diff;
        int y = 0;
        drawBackground();
        drawAnimal(x,y); x += diff;
        drawValue(x,y,Ressource.animal); x += diff;
        drawInsect(x,y); x += diff;
        drawValue(x,y,Ressource.insect); x += diff;
        drawFungi(x,y); x += diff;
        drawValue(x,y,Ressource.fungi); x += diff;
        drawPlant(x,y); x += diff;
        drawValue(x,y,Ressource.plant); x += diff;
        drawScroll(x,y); x += diff;
        drawValue(x,y,Artifact.scroll); x += diff;
        drawInk(x,y); x += diff;
        drawValue(x,y,Artifact.ink); x += diff;
        drawFeather(x,y); x += diff;
        drawValue(x,y,Artifact.feather); x += diff;
    }
    private void drawBackground() {
        List<Point> pointList = List.of(
                new Point(width/2,2*diff+diff/2),
                new Point(width/2+diff/2,2*diff),
                new Point(width/2+8*diff-diff/2,2*diff),
                new Point(width/2+8*diff,2*diff-diff/2),
                new Point(width/2+8*diff,0),
                new Point(width/2-8*diff,0),
                new Point(width/2-8*diff,2*diff-diff/2),
                new Point(width/2-8*diff+diff/2,2*diff),
                new Point(width/2-diff/2,2*diff));
        Polygon banner = new Polygon();
        for (Point point:pointList) {banner.addPoint(point.x, point.y);}
        graphics2D.setColor(new Color(176, 159, 34));
        graphics2D.fillPolygon(banner);
    }
    private void drawAnimal(int x, int y) {
        graphics2D.setColor(Ressource.animal.getSecondaryColor());
        graphics2D.fillRect(x, y, diff, diff);
    }
    private void drawFungi(int x, int y) {
        graphics2D.setColor(Ressource.fungi.getSecondaryColor());
        graphics2D.fillRect(x, y, diff, diff);
    }
    private void drawInsect(int x, int y) {
        graphics2D.setColor(Ressource.insect.getSecondaryColor());
        graphics2D.fillRect(x, y, diff, diff);
    }
    private void drawPlant(int x, int y) {
        graphics2D.setColor(Ressource.plant.getSecondaryColor());
        graphics2D.fillRect(x, y, diff, diff);
    }
    private void drawScroll(int x, int y) {
        graphics2D.setColor(Artifact.scroll.getMainColor());
        graphics2D.fillRect(x, y, diff, diff);
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("grand", Font.BOLD,diff));
        graphics2D.drawChars(new char[]{'S'},0,1,x+diff/6,y+diff-diff/12);
    }
    private void drawInk(int x, int y) {
        graphics2D.setColor(Artifact.ink.getMainColor());
        graphics2D.fillRect(x, y, diff, diff);
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("grand", Font.BOLD,diff));
        graphics2D.drawChars(new char[]{'I'},0,1,x+diff/6,y+diff-diff/12);
    }
    private void drawFeather(int x, int y) {
        graphics2D.setColor(Artifact.feather.getMainColor());
        graphics2D.fillRect(x, y, diff, diff);
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(new Font("grand", Font.BOLD,diff));
        graphics2D.drawChars(new char[]{'F'},0,1,x+diff/6,y+diff-diff/12);
    }
    private void drawValue(int x, int y, Corner corner) {
        prepareText();
        String strPoint = Integer.toString(corner.getValue());
        char[] chrPoint = strPoint.toCharArray();
        if (strPoint.length() >= 2) {
            graphics2D.drawChars(chrPoint,0,strPoint.length(),x-diff,y+2*diff-diff/12);
        } else {
            graphics2D.drawChars(chrPoint,0,strPoint.length(),x-diff+diff/6,y+2*diff-diff/12);
        }
    }
    private void prepareText() {
        graphics2D.setFont(new Font("", Font.BOLD, diff));
        graphics2D.setColor(new Color(87, 66, 11));
    }
}
