package fr.codex.naturalis.drawing;

import fr.codex.naturalis.corner.Artifact;
import fr.codex.naturalis.corner.Corner;
import fr.codex.naturalis.corner.Ressource;

import java.awt.*;
import java.util.Objects;

public class RessourceDrawingSequence {
    private final Graphics2D graphics2D;
    int width;
    int height;
    int cornerSize;
    public RessourceDrawingSequence(Graphics2D graphics2D, int width) {
        Objects.requireNonNull(graphics2D);
        this.graphics2D = graphics2D;
        this.width = width;
        height = width / 2;
        cornerSize = (height / 2) - (height / 10);
    }
    public void drawInformationBanner() {
        int diff = cornerSize/2;
        int x = width/2 - 7*diff;
        drawAnimal(x,0); x += diff;
        drawValue(x,0,Ressource.animal); x += diff;
        drawInsect(x,0); x += diff;
        drawValue(x,0,Ressource.insect); x += diff;
        drawFungi(x,0); x += diff;
        drawValue(x,0,Ressource.fungi); x += diff;
        drawPlant(x,0); x += diff;
        drawValue(x,0,Ressource.plant); x += diff;
        drawScroll(x,0); x += diff;
        drawValue(x,0,Artifact.scroll); x += diff;
        drawInk(x,0); x += diff;
        drawValue(x,0,Artifact.ink); x += diff;
        drawFeather(x,0); x += diff;
        drawValue(x,0,Artifact.feather); x += diff;
    }
    private void drawAnimal(int x, int y) {
        graphics2D.setColor(Ressource.animal.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }
    private void drawFungi(int x, int y) {
        graphics2D.setColor(Ressource.fungi.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }
    private void drawInsect(int x, int y) {
        graphics2D.setColor(Ressource.insect.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }
    private void drawPlant(int x, int y) {
        graphics2D.setColor(Ressource.plant.getSecondaryColor());
        graphics2D.fillRect(x+cornerSize/5, y+cornerSize/5, cornerSize/2, cornerSize/2);
    }
    private void drawScroll(int x, int y) {
        graphics2D.setColor(Artifact.scroll.getMainColor());
        graphics2D.fillRect(x+cornerSize/4, y+cornerSize/4, cornerSize/2, cornerSize/2);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawChars(new char[]{'S'},0,5,x+cornerSize/4,y+cornerSize/4);
    }
    private void drawInk(int x, int y) {
        graphics2D.setColor(Artifact.ink.getMainColor());
        graphics2D.fillRect(x+cornerSize/4, y+cornerSize/4, cornerSize/2, cornerSize/2);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawChars(new char[]{'I'},0,5,x+cornerSize/4,y+cornerSize/4);
    }
    private void drawFeather(int x, int y) {
        graphics2D.setColor(Artifact.feather.getMainColor());
        graphics2D.fillRect(x+cornerSize/4, y+cornerSize/4, cornerSize/2, cornerSize/2);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawChars(new char[]{'F'},0,5,x+cornerSize/4,y+cornerSize/4);
    }
    private void drawValue(int x, int y, Corner corner) {
        graphics2D.setColor(new Color(87, 66, 11));
        graphics2D.setFont(new Font("grand", Font.BOLD,width/12));
        graphics2D.drawChars(Integer.toString(corner.getValue()).toCharArray(),0,5,x,y);
    }
}
